package ca.uqam.info.inf600g.data;

import ca.uqam.info.inf600g.model.Question;
import ca.uqam.info.inf600g.model.Quiz;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuizzesFactory {

    protected Map<String, Quiz> quizzesDB;

    public QuizzesFactory() {
        this.quizzesDB = new HashMap<>();
        initializeGermany();
        initializeHistory();
        initializePolitics();
        initializeOlympics();
        initializeCanada();
        initializeUSA();
        initializeUK();
    }

    public void initializeGermany() {
        Question q1 = new Question(
                "Laquelle de ces marques de voiture n’est pas d’origine allemande ?",
                "Porsche",
                "Audi",
                "Saab",
                3);
        Question q2 = new Question(
                "Combien d’habitants compte l’Allemagne ?",
                "45,5 millions",
                "82,3 millions",
                "97 millions",
                2);
        Question q3 = new Question(
                "Dans quelle ville du Sud de l’Allemagne ont eu lieu les Jeux Olympiques d’Été en 1972 ?",
                "Stuttgart",
                "Nuremberg",
                "Munich",
                3);

        quizzesDB.put("Allemagne", new Quiz(
                "Allemagne",
                "Allemagne",
                "Quiz sur l'Allemagne",
                Stream.of(q1, q2, q3).collect(Collectors.toSet())));
    }

    public void initializeHistory() {
        Question q1 = new Question(
                "Quel est l’événement qui a déclenché officiellement la Seconde Guerre mondiale ?",
                "L’invasion de la Pologne par les troupes allemandes",
                "L’assassinat à Sarajevo de l'héritier du trône d'Autriche",
                "L’annexion de l’Autriche par l’Allemagne",
                1);
        Question q2 = new Question(
                "Quels sont les principaux pays qui formaient les forces de l’Axe ?",
                "Allemagne, URSS, Italie",
                "Allemagne, Italie, Japon",
                "Allemagne, URSS, Chine",
                2);
        Question q3 = new Question(
                "Combien de Canadiens ont péri lors de la Deuxième Guerre mondiale ?",
                "45 000",
                "85 000",
                "1 000 000",
                1);

        quizzesDB.put("Histoire", new Quiz(
                "Histoire",
                "Histoire",
                "Quiz sur l'histoire",
                Stream.of(q1, q2, q3).collect(Collectors.toSet())));
    }

    public void initializePolitics() {
        Question q1 = new Question(
                "Isabel Martínez de Perón devient le 1er juillet 1974 la première femme présidente de " +
                        "l'histoire. De quel pays prend-elle la tête suite au décès de son mari, alors président ?",
                "L'Equateur",
                "L'Argentine",
                "La République dominicaine",
                2);
        Question q2 = new Question(
                "Au 1er mai 2014, combien de femmes occupent-elles le poste de chef d'état " +
                        "(et pas de gouvernement) en Afrique ?",
                "Douze",
                "Trois",
                "Aucune",
                2);
        Question q3 = new Question(
                "Ce fut l’un des plus gros scandales politiques anglais du début des années 1960. Quel est son nom ?",
                "L’affaire Kennedy",
                "L’affaire Profumo",
                "L’affaire DSK.",
                2);

        quizzesDB.put("Politique", new Quiz(
                "Politique",
                "Politique",
                "Quiz sur la politique",
                Stream.of(q1, q2, q3).collect(Collectors.toSet())));
    }

    public void initializeOlympics() {
        Question q1 = new Question(
                "Organisés à Londres en 1908, où les JO auraient-ils dû avoir lieu cette année-là ?",
                "À Paris",
                "À Olympie",
                "À Rome",
                3);
        Question q2 = new Question(
                "Quel sport, né en Ecosse, fait son entrée aux Jeux Olympiques en 1924 ?",
                "Le curling",
                "Le lancer de tronc",
                "Le rugby à VII",
                1);
        Question q3 = new Question(
                "En quelle année les premiers Jeux Paralympiques ont-ils été organisés ?",
                "1948",
                "1960",
                "1996",
                2);

        quizzesDB.put("Olympiques", new Quiz(
                "Olympiques",
                "olympiques",
                "Quiz sur les jeux olympiques",
                Stream.of(q1, q2, q3).collect(Collectors.toSet())));
    }

    public void initializeCanada() {
        Question q1 = new Question(
                "Quelle est la capitale du Canada ?",
                "Ottawa",
                "Québec",
                "Montréal",
                1);
        Question q2 = new Question(
                "Combien d'inuits voit-on ?",
                "2",
                "0",
                "1",
                3);
        Question q3 = new Question(
                "De quelle couleur est la bande médiane du drapeau du Canada?",
                "Rouge",
                "Blanche",
                "Noire",
                2);
        Question q4 = new Question(
                "Est-ce que le policier pointe vers sa gauche ou sa droite ?",
                "Gauche",
                "Droite",
                "",
                1);
        Question q5 = new Question(
                "Quel sport voit-on ?",
                "Hockey",
                "Football",
                "Tennis",
                1);
        Question q6 = new Question(
                "Est-ce que l'océan Arctique borde le Canada ?",
                "Non",
                "Oui",
                "",
                2);
        Question q7 = new Question(
                "Quel pays est de l'autre côté de la baie de Baffin ?",
                "L'Irlande",
                "Le Groenland",
                "Le Mexique",
                2);
        Question q8 = new Question(
                "Qu'est-ce que le castor tient ?",
                "Une feuille d'arbre",
                "Une branche d'arbre",
                "Rien",
                2);

        quizzesDB.put("canada", new Quiz(
                "canada",
                "canada",
                "Quiz sur le Canada",
                Stream.of(q1, q2, q3, q4, q5, q6, q7, q8).collect(Collectors.toSet())));
    }

    public void initializeUSA() {
        Question q1 = new Question(
                "Quel type d'oiseau voit-on ?",
                "Aigrette bleue",
                "Pygargue à tête blanche",
                "Aigle royal",
                2);
        Question q2 = new Question(
                "L'océan Pacifique borde-t-il les États-Unis ?",
                "Oui",
                "Non",
                "",
                1);
        Question q3 = new Question(
                "Quelles couleurs voit-on sur le drapeau des États-Unis ?",
                "Rouge, Blanc, Noir, Bleu",
                "Rouge, Blanc, Bleu",
                "Rouge, Jaune, Noir",
                1);
        Question q4 = new Question(
                "Combien de cowboys voit-on ?",
                "0",
                "2",
                "1",
                3);
        Question q5 = new Question(
                "De quelle couleur est la casquette du joueur de baseball ?",
                "Rouge",
                "Noire",
                "Bleue",
                3);
        Question q6 = new Question(
                "Quelle statue célèbre voit-on ?",
                "Statue de Christophe Colomb",
                "Statue de la liberté",
                "Statue du Christ des Ozarks",
                2);
        Question q7 = new Question(
                "Combien de rayures diagonales y a-t-il sur le verre de boisson gazeuse ?",
                "5",
                "4",
                "8",
                3);
        Question q8 = new Question(
                "Quelle est la capitale des États-Unis ?",
                "Washington",
                "New York",
                "Chicago",
                1);

        quizzesDB.put("etats_unis", new Quiz(
                "etats_unis",
                "etats_unis",
                "Quiz sur les États-Unis",
                Stream.of(q1, q2, q3, q4, q5, q6, q7, q8).collect(Collectors.toSet())));
    }

    public void initializeUK() {
        Question q1 = new Question(
                "Quelle est la race du chien que vous avez observez ?",
                "Boxer",
                "Bulldog",
                "Carlin",
                2);
        Question q2 = new Question(
                "Le chien porte-t-il un collier ?",
                "Oui",
                "Non",
                "",
                2);
        Question q3 = new Question(
                "Pouvez-vous nommer l'instrument de musique montré ?",
                "Flûte traversière",
                "Violon",
                "Cornemuse",
                3);
        Question q4 = new Question(
                "Quel numéro se trouve à l'avant du bus ?",
                "44",
                "54",
                "34",
                1);
        Question q5 = new Question(
                "Combien de ballons de football voit-on ?",
                "0",
                "1",
                "2",
                2);
        Question q6 = new Question(
                "De quelle couleur est la ligne verticale sur le drapeau ?",
                "Rouge",
                "Verte",
                "Blanche",
                1);
        Question q7 = new Question(
                "L'heure sur l'horloge indique-t-elle minuit ?",
                "Non",
                "Oui",
                "",
                1);
        Question q8 = new Question(
                "Quel bâtiment célèbre voit-on ?",
                "La tour de Londres",
                "Big ben",
                "London Eye",
                2);

        quizzesDB.put("royaume_uni", new Quiz(
                "royaume_uni",
                "royaume_uni",
                "Quiz sur le Royaume-Uni",
                Stream.of(q1, q2, q3, q4, q5, q6, q7, q8).collect(Collectors.toSet())));
    }

}
