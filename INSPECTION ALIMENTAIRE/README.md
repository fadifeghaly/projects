# Projet de session - H2020 
<br>

| Cours | Sigle | Université
| ------ | ------ | ------ |
|  Programmation Web avancée | INF5190 | UQÀM

## Auteur

| Prénom et nom de famille | Code permanent |
| ------ | ------ |
| Fadi Feghali | FEGF07069109 |

<br>

❗**Installer virtualenv sur votre machine**

OSX : avec pip ou pip3 
```sh
$ pip install virtualenv
```

Linux : 
```sh
$ sudo apt install virtualenv
```
<br>

**Exécutez l'application à partir de votre terminal pour tester directement quelques fonctionnalités à partir de ce fichier**

```sh
$ make
$ source venv/bin/activate
$ python3 myapp.py
```

⚠️ Lorsque vous exécutez l'application si vous obtenez une erreur par rapport au format 'fr_FR'; utilisez les deux commandes suivantes : 

```sh
$ sudo locale-gen fr_FR
$ sudo locale-gen fr_FR.UTF-8
```

## Fonctionnalités développées

| Nom | Status | Test |
| ------ | ------ | ------ |
DOC | Complétée | <http://localhost:5000/doc> |
A1 | Complétée | non disponible |
A2 | Complétée | http://localhost:5000/dashboard |
A3 | Complétée | non disponible |
A4 | Complétée | <http://localhost:5000/api/contrevenants?du=2018-01-01?au=2019-01-01> |
A5 | Complétée | <http://localhost:5000/dashboard> |
A6 | Complétée | <http://localhost:5000/dashboard> |
B1 | Complétée | [Gmail](https://accounts.google.com/signin/v2/identifier?continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&service=mail&sacu=1&rip=1&flowName=GlifWebSignIn&flowEntry=ServiceLogin) |
B2 | Complétée | <https://twitter.com/Garry62983794> |
C1 | Complétée | <http://localhost:5000/api/infractions/json> |
C2 | Complétée | <http://localhost:5000/api/infractions/xml> |
C3 | Complétée | <http://localhost:5000/api/infractions/csv> |
F1 | Complétée | <https://fegf07069109-projet-session.herokuapp.com> |