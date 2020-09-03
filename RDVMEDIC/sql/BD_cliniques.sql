create table if not exists Cliniques (
	Id INTEGER primary key AUTOINCREMENT,
	Nom varchar(100),
	Adresse varchar(100),
	Ville varchar(100),
	CodePostal varchar(100),
	Téléphone varchar(100),
	ÉtatService varchar(100)
);
