create table if not exists Doctors (
	id INTEGER primary key autoincrement,
	clinique_nom varchar(100),
	nom varchar(100),
	departement varchar(100),
	statut varchar(50),
	FOREIGN KEY(clinique_nom) REFERENCES Cliniques(Nom)
);

