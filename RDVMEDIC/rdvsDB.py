import sqlite3


class rdvsDB:
    def __init__(self):
        self.connection = None

    def get_connection(self):
        if self.connection is None:
            self.connection = sqlite3.connect('db/rdvs.db')
        return self.connection

    def disconnect(self):
        if self.connection is not None:
            self.connection.close()

    def add_rdv(
            self,
            prenom_client,
            nom_client,
            courriel,
            nom_clinique,
            tel_clinique,
            addr_clinique,
            date_rdv,
            heure_rdv,
            num_confirmation,
            nom_doctor):
        connection = self.get_connection()
        connection.execute(
            'INSERT INTO RendezVous (Prénom, Nom, Courriel, Nom_clinique, Tel_clinique,'
            'Adresse_clinique, Date_rdv, Heure_rdv, Confirmation,nom_doctor) values (?,?,?,?,?,?,?,?,?,?)',
            (prenom_client,
             nom_client,
             courriel,
             nom_clinique,
             tel_clinique,
             addr_clinique,
             date_rdv,
             heure_rdv,
             num_confirmation,
             nom_doctor))
        connection.commit()

    def update_rdv_email(self, courriel, num_confirmation):
        connection = self.get_connection()
        connection.execute(
            ("UPDATE RendezVous SET Courriel=? WHERE Confirmation=?"),
            (courriel,
             num_confirmation,
             ))
        connection.commit()

    def get_rdv_by_confirmation(self, num_confirmation):
        cursor = self.get_connection().cursor()
        cursor.execute(("SELECT * FROM RendezVous WHERE Confirmation=?"),
                       (num_confirmation,))
        rdv = cursor.fetchone()
        if rdv is None:
            return None
        else:
            return rdv

    def get_rdv_by_email(self, courriel):
        cursor = self.get_connection().cursor()
        cursor.execute(("SELECT * FROM RendezVous WHERE Courriel=?"),
                       (courriel,))
        rdv = cursor.fetchall()
        if rdv is None:
            return None
        else:
            return rdv

    def get_rdv_by_Id(self, id):
        cursor = self.get_connection().cursor()
        cursor.execute(("SELECT * FROM RendezVous WHERE Id=?"),
                       (id,))
        rdv = cursor.fetchone()
        if rdv is None:
            return None
        else:
            return rdv

    def rm_rdv_by_confirmation(self, num_confirmation):
        connection = self.get_connection()
        connection.execute(("DELETE FROM RendezVous WHERE Confirmation=?"),
                           (num_confirmation,))
        connection.commit()

    def get_all_rdv_by_clinique(self, nom_clinique):
        cursor = self.get_connection().cursor()
        cursor.execute(("SELECT * FROM RendezVous WHERE Nom_clinique=?"),
                       (nom_clinique,))
        rdvs = cursor.fetchall()
        if rdvs is None:
            return None
        else:
            return rdvs

    def get_email_addresses_by_date(self, date):
        cursor = self.get_connection().cursor()
        cursor.execute(("SELECT Courriel FROM RendezVous WHERE Date_rdv=?"),
                       (date,))
        emails = cursor.fetchall()
        if emails is None:
            return None
        else:
            return emails

    def get_available_appoint_by_clinique(
            self, nom_clinique, nom_docteur, date_rdv):
        cursor = self.get_connection().cursor()
        cursor.execute(
            ("SELECT Heure_rdv FROM RendezVous WHERE Nom_clinique=? AND nom_doctor=? AND Date_rdv=?"),
            (nom_clinique,
             nom_docteur,
             date_rdv))
        hrs = cursor.fetchall()
        if hrs is None:
            return None
        else:
            return hrs
