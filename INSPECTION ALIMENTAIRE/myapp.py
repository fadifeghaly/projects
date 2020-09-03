import locale
import datetime
import subprocess
from apscheduler.scheduler import Scheduler
import urllib.request
from bs4 import BeautifulSoup
import flask_sqlalchemy
from send_email import send_email
from post_twitter import post_to_twitter
import yaml
from flask_restful import Resource, Api
from json2xml import json2xml
import csv
from flask import Flask, render_template, request
from flask import make_response, json, Response


# App config
app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///contrevenants.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = True
app.config['JSON_AS_ASCII'] = False
db = flask_sqlalchemy.SQLAlchemy(app)
api = Api(app)


# Set local time
locale.setlocale(locale.LC_ALL, 'fr_FR')


# Extraire la liste des adresses courriels du fichier YAML
def get_email():
    with open(r'yaml/liste_courriels.yaml') as file:
        liste_courriels = yaml.load(file, Loader=yaml.FullLoader)
        liste = []
        for item, courriel in liste_courriels.items():
            liste.append(courriel)
    return liste


# Classe contrevenant
class Contrevenant(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    proprietaire = db.Column(db.String(100), nullable=False)
    categorie = db.Column(db.String(100), nullable=False)
    etablissement = db.Column(db.String(100), nullable=False)
    adresse = db.Column(db.String(100), nullable=False)
    ville = db.Column(db.String(100), nullable=False)
    description = db.Column(db.String(1000), nullable=False)
    date_infraction = db.Column(db.String(100), nullable=False)
    date_jugement = db.Column(db.String(100), nullable=False)
    montant = db.Column(db.String(100), nullable=False)

    def __repr__(self):
        return '<%r>' % self.id


# Extraire les données du site de la ville de montréal
def scrape_data():
    # Importation à minuit
    query = "DELETE FROM Contrevenant"
    db.engine.execute(query)

    req = urllib.request.urlopen(
        'http://donnees.ville.montreal.qc.ca/dataset/a5c1f0b9-261f-4247'
        '-99d8-f28da5000688/resource/92719d9b-8bf2-4dfd-b8e0-1021ffcaee2f'
        '/download/inspection-aliments-contrevenants.xml')

    xml = BeautifulSoup(req, features="xml")
    i = 1
    for item in xml.findAll('contrevenant'):
        nouveau_contrevenant = Contrevenant(
            id=i,
            proprietaire=(item.find('proprietaire').text.strip()).replace(
                "'", "''"),
            categorie=item.find('categorie').text.strip().replace("'", "''"),
            etablissement=item.find('etablissement').text.strip().replace(
                "'", "''"),
            adresse=item.find('adresse').text.strip().replace("'", "''"),
            ville=item.find('ville').text.strip().replace("'", "''"),
            description=item.find('description').text.strip().replace("'",
                                                                      "''"),
            date_infraction=datetime.datetime.strptime
            (item.find('date_infraction').text.strip(), '%d %B %Y').date(),
            date_jugement=datetime.datetime.strptime
            (item.find('date_jugement').text.strip(), '%d %B %Y').date(),
            montant=item.find('montant').text.strip())

        db.session.add(nouveau_contrevenant)
        db.session.commit()
        i += 1

    # Envoi de la liste des nouveaux contrevenants par courriel + post twitter
    today = datetime.date.today()
    # Trouver les nouveaux contrevenants et envoyer la liste par courriel +
    # twitter
    query = "SELECT DISTINCT etablissement FROM Contrevenant WHERE " \
            "date_infraction == '{param_1}'". \
        format(param_1=today)
    tasks = db.engine.execute(query)
    liste_contrevenants = ''
    for task in tasks:
        liste_contrevenants += ('- ' + task[0] + "\n\n")
    liste_courriels = get_email()
    if len(liste_contrevenants) != 0:
        for courriel in liste_courriels:
            send_email(liste_contrevenants, courriel)
            post_to_twitter(liste_contrevenants)


# 1ere importation, si la base de donnée est vide -> scrape data
query = "SELECT COUNT(*) FROM Contrevenant"
result = db.engine.execute(query).fetchall()
for row in result:
    result = row[0]
if result == 0:
    scrape_data()


# Démarrer le "scheduler"
sched = Scheduler()
sched.start()
# scrape_data à minuit : 23h59
sched.add_cron_job(scrape_data, day_of_week='mon-sun', hour=23, minute=59)


# Page principale
@app.route('/')
def index():
    return render_template('index.html')


# Documentation API
@app.route('/doc')
def get_doc():
    return render_template('doc.html')


# Tableau de bord
@app.route('/dashboard', methods=['GET', 'POST'])
def dashboard():
    if request.method == 'POST':
        req = request.get_json()
        etablissement = req['etablissement'].replace("'", "''")
        query = 'SELECT * FROM Contrevenant WHERE etablissement LIKE "{' \
                'param_1}"'. \
            format(param_1=etablissement)
        tasks = db.engine.execute(query)
        data = [{"Propriétaire": each[1].replace("''", "'"),
                 "Catégorie": each[2].replace("''", "'"),
                 "Adresse": each[4].replace("''", "'"),
                 "Ville": each[5].replace("''", "'"),
                 "Description": each[6].replace("''", "'"),
                 "Date infraction": each[7].replace("''", "'"),
                 "Date jugement": each[8].replace("''", "'"),
                 "Montant": each[9].replace("''", "'")} for each in tasks]
        return json.dumps(data, indent=4, sort_keys=False, encoding='utf8')
    else:
        query = 'SELECT DISTINCT etablissement FROM Contrevenant'
        tasks = db.engine.execute(query)
        return render_template('dashboard.html', tasks=tasks)


# Recherche par nom d'établissement, propriétaire ou rue
@app.route('/recherche', methods=['GET', 'POST'])
def search():
    if request.method == 'POST':
        mot_cle = request.form['search'].replace("'", "''")
        query = 'SELECT * FROM contrevenant WHERE etablissement LIKE "%{' \
                'param}%" OR proprietaire LIKE "%{param}%" OR adresse LIKE ' \
                '"%{param}%" ORDER BY date_infraction DESC'. \
            format(param=mot_cle)
        tasks = db.engine.execute(query)
        if mot_cle and len(tasks.fetchall()) != 0:
            tasks = db.engine.execute(query)
            return render_template('recherche.html', tasks=tasks)
        else:
            return render_template('aucunResultat.html', tasks=tasks)


# Formulaire de recherche rapide permettant de saisir deux dates
@app.route('/contrevenants', methods=["POST"])
def get_contrevenant():
    req = request.get_json()
    du = req['date1']
    au = req['date2']
    query = "SELECT etablissement, COUNT(*) FROM Contrevenant WHERE " \
            "date_infraction BETWEEN '{param_1}' AND '{param_2}' GROUP " \
            "BY etablissement ORDER BY COUNT(*) DESC". \
        format(param_1=du, param_2=au)
    tasks = db.engine.execute(query)
    data = [{"Établissement": each[0].replace(
        "''", "'"), "Nombre de contraventions": each[1]} for each in
        tasks]
    return json.dumps(data, indent=4, sort_keys=False, encoding='utf8')


@app.route('/api/contrevenants', methods=["GET"])
def get_contrevenant_api():
    du = request.args.get('du')
    au = request.args.get('au')
    query = 'SELECT * FROM Contrevenant WHERE date_infraction BETWEEN ' \
            '"{param_1}" AND "{param_2}"'. \
        format(param_1=du, param_2=au)
    tasks = db.engine.execute(query)
    data = [{"Propriétaire": each[1].replace("''", "'"),
             "Categorie": each[2].replace("''", "'"),
             "Établissement": each[3].replace("''", "'"),
             "Adresse": each[4].replace("''", "'"),
             "Ville": each[5].replace("''", "'"),
             "Description": each[6].replace("''", "'"),
             "Date infraction": each[7].replace("''", "'"),
             "Date jugement": each[8].replace("''", "'"),
             "Montant": each[9].replace("''", "'")} for each in tasks]
    json_string = json.dumps(data, indent=4, sort_keys=False,
                             encoding='utf8')
    response = Response(
        json_string,
        content_type="application/json; charset=utf-8")
    if len(data) != 0:
        return response
    else:
        return render_template('aucunResultat.html')


# service REST pour obtenir les données en Json, XML ou CSV
class Etablissements(Resource):
    def get(self, format):
        query = "SELECT etablissement, COUNT(*) FROM Contrevenant GROUP BY " \
                "etablissement ORDER BY COUNT(*) DESC"
        tasks = db.engine.execute(query)
        data = [{"Établissement": each[0].replace(
            "''", "'"), "Nombre de contraventions": each[1]} for each in
            tasks]
        json_string = json.dumps(data, indent=4, sort_keys=False,
                                 encoding='utf8')
        response = Response(
            json_string,
            content_type="application/json; charset=utf-8")
        if format == 'json':
            return response
        elif format == 'xml':
            out = open("out.xml", "w")
            out.write(
                json2xml.Json2xml(data, wrapper="all", pretty=True).to_xml())
            headers = {'Content-Type': 'text/html'}
            return make_response(render_template('xml.html'), 200, headers)
        elif format == 'csv':
            contrevenant_parsed = json.loads(json_string)
            contrevenant_csv = open('out.csv', 'w')
            csvwriter = csv.writer(contrevenant_csv)
            count = 0
            for contrevenant in contrevenant_parsed:
                if count == 0:
                    header = contrevenant.keys()
                    csvwriter.writerow(header)
                    count += 1
                csvwriter.writerow(contrevenant.values())
            contrevenant_csv.close()
            headers = {'Content-Type': 'text/html'}
            return make_response(render_template('csv.html'), 200, headers)
        else:
            headers = {'Content-Type': 'text/html'}
            return make_response(render_template('404.html'), 404, headers)


api.add_resource(Etablissements, '/api/infractions/<format>')


# Page d'erreur 404
@app.errorhandler(404)
def page_not_found(e):
    return render_template('404.html'), 404

# Page d'erreur 404
@app.errorhandler(405)
def page_not_found(e):
    return render_template('405.html'), 405


if __name__ == "__main__":
    app.run(debug=True)
