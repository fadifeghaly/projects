create table if not exists RendezVous (
	Id INTEGER PRIMARY KEY AUTOINCREMENT,
	Prénom varchar(100),
	Nom varchar(100),
	Courriel varchar(100),
	Nom_clinique varchar(100),
	Tel_clinique varchar(100),
	Adresse_clinique varchar(100),
	Date_rdv varchar(100),
	Heure_rdv varchar(100),
	Confirmation varchar(100)
);

ALTER TABLE RendezVous ADD nom_doctor varchar(100);