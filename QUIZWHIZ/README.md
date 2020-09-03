# INF600G - Conception de Logiciels Adaptés

| Cours | Sigle | Université|
| ------ | ------ | ------ |
| Conception de logiciels adaptés | INF600G | UQÀM |
    
📱 Le projet utilise un émulateur de type tablette (Nexus 7 API 29) avec une résolution minimale de `1200` par `1920` pixels.

##  Description du produit réalisé

_Décrivez ici en quelques lignes votre réalisation_

Le code source de l'application mobile (client) est dans le repertoire `app`
Le code source de la partie arrière (serveur) est dans le repertoire `serveur`.

## Exécution du projet

- Pour exécuter le projet, vous pouvez simplement tapper la commande `make` dans le terminal à partir du répertoire `Serveur` :

```sh
fadifeghali@Fadis-MacBook-Pro Serveur % make
```	
ou 

```sh
fadifeghali@Fadis-MacBook-Pro Serveur % mvn clean package jetty:run-war
```

- Pour lancer l'application mobile dans un émulateur Android, Il suffit d'importer le projet dans Android Studio, puis de le lancer en cliquant sur la flèche verte.

### Utilisation de la partie arrière

Les ressource de partie arrière sont exposées à l'aide de Swagger, et disponible à l'adresse suivante :

- [http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui)

Les ressources statiques utilisées par l'application sont stockées dans le repertoire `static`, et disponible à l'addresse suivante :

- [http://localhost:8080/static](http://localhost:8080/static)
