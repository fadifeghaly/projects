import sqlite3


class clientsDB:
    def __init__(self):
        self.connection = None

    def get_connection(self):
        if self.connection is None:
            self.connection = sqlite3.connect('db/clients.db')
        return self.connection

    def disconnect(self):
        if self.connection is not None:
            self.connection.close()

    def add_user(
            self,
            prenom,
            nom,
            telephone,
            courriel,
            adresse,
            codePostal,
            salt,
            hash_pass):
        connection = self.get_connection()
        connection.execute(
            'INSERT INTO Clients (Prénom, Nom, Téléphone, Courriel, Adresse, CodePostal, Salt, Hash) VALUES (?,?,?,?,?,?,?,?)',
            (prenom,
             nom,
             telephone,
             courriel,
             adresse,
             codePostal,
             salt,
             hash_pass))
        connection.commit()

    def get_password(self, courriel):
        cursor = self.get_connection().cursor()
        cursor.execute(("SELECT Salt, Hash FROM Clients WHERE Courriel=?"),
                       (courriel,))
        user = cursor.fetchone()
        if user is None:
            return None
        else:
            return user[0], user[1]

    def get_user_info(self, courriel):
        cursor = self.get_connection().cursor()
        cursor.execute(("SELECT * FROM Clients WHERE Courriel=?"),
                       (courriel,))
        user = cursor.fetchone()
        if user is None:
            return None
        else:
            return user

    def get_user_id(self, courriel):
        cursor = self.get_connection().cursor()
        cursor.execute(("SELECT Id FROM Clients WHERE Courriel=?"),
                       (courriel,))
        Id = cursor.fetchone()
        if Id is None:
            return None
        else:
            return Id[0]

    def get_user_tel_num(self, courriel):
        cursor = self.get_connection().cursor()
        cursor.execute(("SELECT Téléphone FROM Clients WHERE Courriel=?"),
                       (courriel,))
        tel = cursor.fetchone()
        if tel is None:
            return None
        else:
            return tel[0]

    def get_user_postal_code(self, courriel):
        cursor = self.get_connection().cursor()
        cursor.execute(("SELECT codePostal FROM Clients WHERE Courriel=?"),
                       (courriel,))
        pc = cursor.fetchone()
        if pc is None:
            return None
        else:
            return pc[0]

    def update_user_info(self, Id, telephone, courriel):
        connection = self.get_connection()
        connection.execute(
            ("UPDATE Clients SET Téléphone=?, Courriel=? WHERE Id=?"),
            (telephone, courriel, Id,))
        connection.commit()

    def get_user_full_name(self, courriel):
        cursor = self.get_connection().cursor()
        cursor.execute(("SELECT Prénom, Nom FROM Clients WHERE Courriel=?"),
                       (courriel,))
        user = cursor.fetchone()
        if user is None:
            return None
        else:
            pair = (user[0], user[1])
            return " ".join(pair)

    def get_user_email(self, id_session):
        cursor = self.get_connection().cursor()
        cursor.execute(("SELECT Courriel FROM Sessions WHERE Id_session=?"),
                       (id_session,))
        courriel = cursor.fetchone()
        if courriel is None:
            return None
        else:
            return courriel[0]

    def delete_user(self, courriel):
        connection = self.get_connection()
        connection.execute(("DELETE FROM Clients WHERE Courriel=?"),
                           (courriel,))
        connection.commit()

    def save_session(self, id_session, courriel):
        connection = self.get_connection()
        full_name = self.get_user_full_name(courriel)
        connection.execute(
            ("INSERT INTO Sessions(Id_session, Nom, Courriel) "
             " VALUES (?, ?, ?)"), (id_session, full_name, courriel))
        connection.commit()

    def delete_session(self, id_session):
        connection = self.get_connection()
        connection.execute(("DELETE FROM Sessions WHERE Id_session=?"),
                           (id_session,))
        connection.commit()

    def update_session_email(self, courriel, id_session):
        connection = self.get_connection()
        connection.execute(("UPDATE Sessions SET Courriel=? WHERE Id_session=?"),
                           (courriel, id_session,))
        connection.commit()

    def get_session(self, id_session):
        cursor = self.get_connection().cursor()
        cursor.execute(("SELECT Nom FROM Sessions WHERE Id_session=?"),
                       (id_session,))
        data = cursor.fetchone()
        if data is None:
            return None
        else:
            return data[0]

    def get_session_email(self, id_session):
        cursor = self.get_connection().cursor()
        cursor.execute(("SELECT Courriel FROM Sessions WHERE Id_session=?"),
                       (id_session,))
        data = cursor.fetchone()
        if data is None:
            return None
        else:
            return data[0]

    def get_nom_prenom(self, mail):
        cursor = self.get_connection().cursor()
        cursor.execute(("SELECT Prénom,Nom FROM Clients WHERE Courriel=?"),
                       (mail,))
        data = cursor.fetchone()
        if data is None:
            return None
        else:
            return data

