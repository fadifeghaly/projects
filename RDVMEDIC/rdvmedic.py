from flask import Flask, render_template, request, g, redirect, session
from flask import make_response, json, Response, flash
from functools import wraps
from clientsDB import clientsDB
from cliniquesDB import cliniquesDB
from rdvsDB import rdvsDB
from notifications import send_comment, send_confirmation_email, send_sms
import urllib.request
import hashlib
import uuid
import sqlite3
import string
import random
import locale
import datetime
from datetime import datetime, date
from apscheduler.scheduler import Scheduler


# App config
app = Flask(__name__)
app.secret_key = 'securitaireAUBOUTTE'
app.config['SESSION_TYPE'] = 'filesystem'


# Captcha keys
RECAPTCHA_PUBLIC_KEY = '6LehRrAZAAAAAD3Jj7Hz7UckLFM0TyVGFdvEn9Ma'
RECAPTCHA_PRIVATE_KEY = '6LehRrAZAAAAAJXIN_gMVh7AIbBSic8gb6rX5jiY'

# Démarrer le "scheduler"
sched = Scheduler()
sched.start()


# DB clients
def get_clients_db():
    db = getattr(g, '_database', None)
    if db is None:
        g._database = clientsDB()
    return g._database


# DB cliniques
def get_cliniques_db():
    db = getattr(g, '_database', None)
    if db is None:
        g._database = cliniquesDB()
    return g._database


# DB rdvs
def get_rdvs_db():
    db = getattr(g, '_database', None)
    if db is None:
        g._database = rdvsDB()
    return g._database


# Page principale
@app.route('/')
def start_page():
    full_name = 'Espace membre'
    if "id" in session:
        full_name = get_clients_db().get_session(session["id"])
    return render_template('index.html', user=full_name)


# Créer un compte
@app.route('/inscription', methods=['POST'])
def inscription():
    prenom = request.form['prenom']
    nom = request.form['nomFamille']
    telephone = request.form['tel']
    courriel = request.form['courriel']
    adresse = request.form['adresse']
    codePostal = request.form['codePostal']
    mdp = request.form['mdp']
    salt = uuid.uuid4().hex
    hash_pass = hashlib.sha512(str(mdp + salt).encode("utf-8")).hexdigest()
    response = request.form.get('g-recaptcha-response')
    if (im_not_a_bot(response, RECAPTCHA_PRIVATE_KEY)):
        get_clients_db().add_user(
            prenom,
            nom,
            telephone,
            courriel,
            adresse,
            codePostal,
            salt,
            hash_pass)
        return render_template('inscription.html')
    else:
        return render_template('echecCaptcha.html')


# Véfification du captcha client
def im_not_a_bot(response, secretkey):
    gc = 'https://www.google.com/recaptcha/api/siteverify?' + \
        'secret=' + str(secretkey) + '&response=' + str(response)
    jsonobj = json.loads(urllib.request.urlopen(gc).read())
    if jsonobj['success']:
        return True
    else:
        return False


# Signin
@app.route('/authentification', methods=['POST'])
def connexion():
    req = request.get_json()
    courriel = req['courriel']
    mdp = req['mdp']
    user = get_clients_db().get_password(courriel)
    if user is not None:
        ## Authentification client ##
        salt = user[0]
        hash_pass = hashlib.sha512(str(mdp + salt).encode("utf-8")).hexdigest()
        if hash_pass == user[1]:
            id_session = uuid.uuid4().hex
            get_clients_db().save_session(id_session, courriel)
            session["id"] = id_session
            headers = {'Content-Type': 'text/html'}
            return '', 200
        else:
            return 'Mot de passe incorrect!', 404
    else:
        return 'Utilisateur inexistant!', 404


# voir les détails du profil
@app.route('/profil')
def profil():
    id_session = session["id"]
    courriel = get_clients_db().get_user_email(id_session)
    user_info = get_clients_db().get_user_info(courriel)
    g._database = rdvsDB()
    rdvs = get_rdvs_db().get_rdv_by_email(courriel)
    last_rdv = []
    next_rdv = []
    for rdv in rdvs:
        if datetime.strptime(
                rdv[7],
                "%Y-%m-%d").date() < datetime.today().date():
            last_rdv.append(rdv)
        else:
            next_rdv.append(rdv)
    return render_template(
        'monProfil.html',
        infos=user_info,
        last_rdv=last_rdv,
        next_rdv=next_rdv)


# Modifier les informations du compte
@app.route('/modifier_profil')
def modify_profil():
    return render_template('modifierProfil.html')


# Modifier les informations du compte
@app.route('/confirmer_modification', methods=['POST'])
def confirm_modification():
    id_session = session["id"]
    ancien_courriel = get_clients_db().get_user_email(id_session)
    user_id = get_clients_db().get_user_id(ancien_courriel)
    nouveau_courriel = request.form['courriel']
    telephone = request.form['tel']
    update = get_clients_db().update_user_info(
        user_id, telephone, nouveau_courriel)
    user_info = get_clients_db().get_user_info(nouveau_courriel)
    g._database = rdvsDB()
    rdv = None
    # if get_rdvs_db().get_rdv_by_email(ancien_courriel) is not None:
    #     num_confirmation = get_rdvs_db().get_rdv_by_email(ancien_courriel)[9]
    #     get_rdvs_db().update_rdv_email(nouveau_courriel, num_confirmation)
    #     rdv = get_rdvs_db().get_rdv_by_email(nouveau_courriel)
    g._database = clientsDB()
    get_clients_db().update_session_email(nouveau_courriel, id_session)
    return render_template('monProfil.html', infos=user_info, rdv=rdv)


# Supprimer un compte
@app.route('/supprimer_compte')
def delete_user():
    id_session = session["id"]
    courriel = get_clients_db().get_user_email(id_session)
    deconnexion()
    get_clients_db().delete_user(courriel)
    return render_template('aurevoir.html')


# Signout
@app.route('/deconnexion')
def deconnexion():
    id_session = session["id"]
    session.pop('id', None)
    get_clients_db().delete_session(id_session)
    return redirect('/')


# Voir les cliniques par région
@app.route('/cliniques', methods=['POST'])
def recherche():
    region = request.form['search']
    cliniques = get_cliniques_db().get_cliniques_by_region(region)
    try:
        g._database = clientsDB()
        get_clients_db().get_session(session["id"])
        id_session = session["id"]
        courriel = get_clients_db().get_user_email(id_session)
        postal_code = get_clients_db().get_user_postal_code(courriel)
    except BaseException:
        postal_code = None
    if region and len(cliniques) != 0:
        return render_template(
            'recherche.html',
            cliniques=cliniques,
            pc=postal_code)
    else:
        return render_template('aucunResultat.html')


# Consulter un rdv
@app.route('/consulter_rdv')
def consulter():
    if "id" in session:
        return render_template('consulterRdv.html')
    else:
        return render_template(
            'index.html',
            login_required=True,
            user='Espace membre')


# Voir les détails d'un rdv
@app.route('/details_rdv', methods=['POST'])
def get_rdv():
    if "id" in session:
        confirmation = None
        if request.method == 'POST':
            confirmation = request.form['search']
            rdv = get_rdvs_db().get_rdv_by_confirmation(confirmation)
            session_id = session["id"]
            g._database = clientsDB()
            mail = get_clients_db().get_session_email(session_id)

            if rdv is not None:
                if rdv[3] != mail:
                    return render_template('consulterRdv.html',
                                           msg='Aucun rendez-vous trouvé!')
                else:
                    cancel = ""
                    if datetime.strptime(
                            rdv[7], "%Y-%m-%d").date() >= datetime.today().date():
                        cancel = "OK"
                    return render_template(
                        'consulterRdv.html',
                        rdv=rdv,
                        confirmation=confirmation,
                        cancel=cancel)
            else:
                return render_template('consulterRdv.html',
                                       msg='Aucun rendez-vous trouvé!')
    else:
        return redirect('/')


# Annuler un rdv
@app.route('/annuler_rdv/<num_conf>', methods=['POST'])
def annuler_rdv(num_conf):
    try:
        get_clients_db().get_session(session["id"])
    except BaseException:
        return render_template(
            'consulterRdv.html',
            msg='Connectez-vous afin de réaliser cette action')
    g._database = rdvsDB()
    session_id = session["id"]
    g._database = clientsDB()
    mail = get_clients_db().get_session_email(session_id)
    user_info = get_clients_db().get_user_info(mail)
    g._database = rdvsDB()
    rdv = get_rdvs_db().rm_rdv_by_confirmation(num_conf)  # rdv to be removed
    rdvs = rdvs = get_rdvs_db().get_rdv_by_email(mail)  # all rdvs
    msg = 'Votre rendez-vous (n° confirmation ' + \
        num_conf + ') est bien annulé.\nMerci'
    subj = 'RDV-MEDIC - Annulation de RDV'
    send_confirmation_email(mail, subj, msg)
    alert = 'Votre rendez-vous ' + num_conf + ' est annulé!'
    last_rdv = []
    next_rdv = []
    for rdv in rdvs:
        if datetime.strptime(
                rdv[7],
                "%Y-%m-%d").date() < datetime.today().date():
            last_rdv.append(rdv)
        else:
            next_rdv.append(rdv)
    return render_template(
        'monProfil.html',
        infos=user_info,
        last_rdv=last_rdv,
        next_rdv=next_rdv,
        alert=alert)

# Annuler un rdv (dans consulter_rdv)
@app.route('/cancel_rdv/<num_conf>', methods=['POST'])
def cancel_rdv(num_conf):
    try:
        get_clients_db().get_session(session["id"])
    except BaseException:
        return render_template(
            'consulterRdv.html',
            msg='Connectez-vous afin de réaliser cette action')
    g._database = rdvsDB()
    session_id = session["id"]
    g._database = clientsDB()
    mail = get_clients_db().get_session_email(session_id)
    user_info = get_clients_db().get_user_info(mail)
    g._database = rdvsDB()
    rdv = get_rdvs_db().rm_rdv_by_confirmation(num_conf)  # rdv to be removed
    msg = 'Votre rendez-vous (n° confirmation ' + \
        num_conf + ') est bien annulé.\nMerci'
    subj = 'RDV-MEDIC - Annulation de RDV'
    send_confirmation_email(mail, subj, msg)
    alert = 'Votre rendez-vous ' + num_conf + ' est annulé!'
    return render_template('consulterRdv.html', alert=alert)

# Contactez-nous
@app.route('/contactez-nous')
def contact():
    return render_template('contactezNous.html')


# Laisser un commentaire
@app.route('/envoie_msg', methods=['POST'])
def msg():
    nom = request.form['nomFamille']
    prenom = request.form['prenom']
    courriel = request.form['courriel']
    commentaire = request.form['commentaire']
    send_comment(nom, prenom, courriel, commentaire)
    return render_template('commentaire.html')


# Envoie du numéro de confirmation par courriel
@app.route('/confirmation_courriel/<id>')
def confirmation_courriel(id):
    id_session = session["id"]
    courriel = get_clients_db().get_user_email(id_session)
    user_info = get_clients_db().get_user_info(courriel)
    g._database = rdvsDB()
    rdv = get_rdvs_db().get_rdv_by_Id(id)  # rdv to send email
    rdvs = get_rdvs_db().get_rdv_by_email(courriel)  # all rdvs
    msg = 'Votre rendez-vous (n° confirmation ' + rdv[9] + ') avec Dr. ' + rdv[10] + \
        ' (' + rdv[4] + ') ' + 'à ' + rdv[8] + 'h le ' + rdv[7] + ' est confirmé.\nMerci.'
    subj = 'RDV-MEDIC - Confirmation de RDV'
    send_confirmation_email(courriel, subj, msg)
    last_rdv = []
    next_rdv = []
    for rdv in rdvs:
        if datetime.strptime(
                rdv[7],
                "%Y-%m-%d").date() < datetime.today().date():
            last_rdv.append(rdv)
        else:
            next_rdv.append(rdv)
    alert = 'Courriel envoyé 👍'
    return render_template(
        'monProfil.html',
        infos=user_info,
        last_rdv=last_rdv,
        next_rdv=next_rdv,
        alert=alert)


# Envoie du numéro de confirmation par SMS
@app.route('/confirmation_sms/<id>')
def confirmation_sms(id):
    id_session = session["id"]
    courriel = get_clients_db().get_user_email(id_session)
    user_info = get_clients_db().get_user_info(courriel)
    num_tel = get_clients_db().get_user_tel_num(courriel)
    g._database = rdvsDB()
    rdv = get_rdvs_db().get_rdv_by_Id(id)  # rdv to send sms
    rdvs = get_rdvs_db().get_rdv_by_email(courriel)  # all rdvs
    msg = ''.join('Votre rendez-vous (n° de confirmation ' +
                  rdv[9] +
                  ') avec Dr. ' +
                  rdv[10] +
                  ' (' +
                  rdv[4] +
                  ') ' +
                  'à ' +
                  rdv[8] +
                  'h le ' +
                  rdv[7] +
                  ' est confirmé.\nMerci.').encode('utf-8').strip()
    send_sms(num_tel, msg)
    alert = 'SMS envoyé 👍'
    last_rdv = []
    next_rdv = []
    for rdv in rdvs:
        if datetime.strptime(
                rdv[7],
                "%Y-%m-%d").date() < datetime.today().date():
            last_rdv.append(rdv)
        else:
            next_rdv.append(rdv)
    return render_template(
        'monProfil.html',
        infos=user_info,
        last_rdv=last_rdv,
        next_rdv=next_rdv,
        alert=alert)


# Afficher les médecins d'une clinique
@app.route('/prendre_rdv/<nom_clinique>')
def show_doctors(nom_clinique):
    if "id" in session:
        doctors = get_cliniques_db().get_doctors(nom_clinique)
        msg = ''
        return render_template(
            'reservation.html',
            nom_clinique=nom_clinique,
            doctors=doctors,
            msg=msg)
    else:
        return redirect('/authentification')

# Réserver
@app.route('/booking/<nom_clinique>/<doctor>', methods=['POST'])
def book_rdv(nom_clinique, doctor):
    if "id" in session:
        if request.method == 'POST':
            date = request.form['date']
            hour = request.form['hour']
            g._database = cliniquesDB()
            doctors = get_cliniques_db().get_doctors(nom_clinique)
            session_id = session["id"]
            g._database = clientsDB()
            mail = get_clients_db().get_session_email(session_id)
            prenomNom = get_clients_db().get_nom_prenom(mail)
            prenom = prenomNom[0]
            nom = prenomNom[1]
            g._database = cliniquesDB()
            telAdress = get_cliniques_db().get_tel_adress(nom_clinique)
            tel = telAdress[1]
            adress = telAdress[0]
            confirmation = ''.join(
                random.choice(
                    string.ascii_uppercase +
                    string.digits) for _ in range(6))
            g._database = rdvsDB()
            get_rdvs_db().add_rdv(
                prenom,
                nom,
                mail,
                nom_clinique,
                tel,
                adress,
                date,
                hour,
                confirmation,
                doctor)
            msg = 'Bonjour ' + prenom + ' ' + nom + '\n\nVotre rendez-vous (n° de confirmation ' + confirmation + ') avec Dr. ' + \
                doctor + ' (' + nom_clinique + ') ' + 'à ' + hour + 'h le ' + date + ' est confirmé.\n\nMerci\n\nRDV-Medic'
            subj = 'RDV-Medic - Confirmation de RDV'
            send_confirmation_email(mail, subj, msg)
            return render_template(
                'confirmation_rdv.html',
                nom_clinique=nom_clinique,
                doctor=doctor,
                hour=hour,
                date=date)
    else:
        return redirect('/authentification')


def send_reminder():
    with app.app_context():
        today = datetime.date.today()
        tomorrow = today + datetime.timedelta(days=1)
        liste_courriels = get_rdvs_db().get_email_addresses_by_date(tomorrow)
        for courriel in liste_courriels:
            courriel = str(courriel[0])
            rdv = get_clients_db().get_rdv_by_email(courriel)
            num_conf = rdv[0][9]
            medecin = rdv[0][10]
            clinique = rdv[0][4]
            date = rdv[0][7]
            heure = str(rdv[0][8])
            msg = 'Ceci est un rappel pour votre rendez-vous (n° de confirmation ' + num_conf + ') avec Dr. ' + \
                medecin + ' (' + clinique + ') ' + 'à ' + heure + 'h le ' + date + '\nMerci'
            subj = 'RDV-Medic - Rappel'
            send_confirmation_email(courriel, subj, msg)


# Envoyer des rappels tous les jours à 16h30 pour les rdvs du jour d'après
sched.add_cron_job(send_reminder, day_of_week='mon-fri', hour=16, minute=30)

# Disponibilités des tous les médecins d'une clinique donnée
@app.route('/disponibilites', methods=['POST'])
def get_availabilities():
    req = request.get_json()
    clinique = req['clinique']
    docteur = req['docteur']
    jour = req['jour']
    hrs = get_rdvs_db().get_available_appoint_by_clinique(clinique, docteur, jour)
    data = [{"Heure": each[0]} for each in hrs]
    return json.dumps(data)


# Page d'erreur 404
@app.errorhandler(404)
def page_inexistante(e):
    return render_template('404.html'), 404

# Page d'erreur 405
@app.errorhandler(405)
def page_inexistante(e):
    return render_template('405.html'), 405


# Main
if __name__ == "__main__":
    app.run(debug=True)
