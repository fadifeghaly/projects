import sqlite3


class cliniquesDB:
    def __init__(self):
        self.connection = None

    def get_connection(self):
        if self.connection is None:
            self.connection = sqlite3.connect('db/cliniques.db')
        return self.connection

    def disconnect(self):
        if self.connection is not None:
            self.connection.close()

    def add_cliniques(self, nom, adresse, telephone, etatService, ville):
        connection = self.get_connection()
        connection.execute(
            'INSERT INTO Cliniques (Nom, Adresse, codePostal, Téléphone, ÉtatService, Ville) VALUES (?,?,?,?,?,?)',
            (nom,
             adresse,
             codePostal,
             telephone,
             etatService,
             ville))
        connection.commit()

    def get_all_cliniques(self):
        cursor = self.get_connection().cursor()
        cursor.execute("SELECT * FROM Cliniques")
        cliniques = cursor.fetchall()
        if cliniques is None:
            return None
        else:
            return cliniques

    def get_cliniques_by_region(self, ville):
        cursor = self.get_connection().cursor()
        query = "SELECT * FROM Cliniques WHERE Ville LIKE ? ORDER BY Nom"
        cursor.execute(query, ('%' + ville + '%',))
        cliniques = cursor.fetchall()
        if cliniques is None:
            return None
        else:
            return cliniques

    def get_doctors(self,nom_clinique):
        cursor = self.get_connection().cursor()
        query = "SELECT * FROM Doctors WHERE clinique_nom LIKE ? ORDER BY Nom"
        cursor.execute(query, ('%' + nom_clinique + '%',))
        doctors = cursor.fetchall()
        if doctors is None:
            return None
        else:
            return doctors

    def get_tel_adress(self,nom_clinique):
        cursor = self.get_connection().cursor()
        query = "SELECT Adresse,Téléphone FROM Cliniques WHERE Nom LIKE ?"
        cursor.execute(query, ('%' + nom_clinique + '%',))
        data = cursor.fetchone()
        if data is None:
            return None
        else:
            return data
