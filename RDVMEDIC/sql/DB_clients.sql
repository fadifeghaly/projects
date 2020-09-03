create table Clients (
	Id INTEGER PRIMARY KEY AUTOINCREMENT,
	Prénom varchar(100),
	Nom varchar(100),
	Téléphone varchar(100),
	Courriel varchar(100),
	Adresse varchar(100),
	CodePostal varchar(100),
	Salt varchar(32),
	Hash varchar(128)
);