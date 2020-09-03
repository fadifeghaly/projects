create table Contrevenant (
	id INTEGER primary key,
	proprietaire varchar(100),
	categorie varchar(100),
	etablissement varchar(100),
	adresse varchar(100),
	ville varchar(100),
	description varchar(1000),
	date_infraction varchar(100),
	date_jugement varchar(100),
	montant varchar(50)
);