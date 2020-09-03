/*! \file tp1.cpp
* Travail pratique #1 : Recherche dans un espace d'états / Rétablir l'électricité
* Auteur : Fadi Feghali - FEGF07069109
*/

#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
#include <vector>
#include <math.h>
#include <iterator>
#include <list>
#include <algorithm>

using namespace std;

#define interrupteurON 1
#define interrupteurOFF 0
#define myPair pair<char, pair<double, double>>
#define maisonVisitee 'M'
#define interrupteurBriseSignale 'B'
#define condRepare 'R'

enum codeRetour {echoue, reussi};
enum reparation {necessaire, pasNecessaire};
vector <codeRetour> vectCodeRetour;
vector <reparation> vectReparation;

class noeud {
public:
	pair<double, double> pos, parent;
	double coutF, coutG, coutH;
	char direction;
};

class Matrice {
public:
	string contenu;
	double nbrLigne, nbrColonne;
	vector<noeud> listeSourcesElectricite;
	vector<noeud> equipeEntretien;
	vector<noeud> listeMaisons;	
};

class ASTAR {
public:
	vector<noeud> listeOuverte, listeFermee;
	vector<myPair> listeActions;
	stringstream listeFinale;
};

/*!
* Fonction pour lire le nom du fichier de la ligne de commande
* @param argc nombre d'arguments
* @param argv les arguments 
* @return le nom du fichier à taiter
*/
string lectureNomFichier(int argc, char *argv[]) {
	
	string nomFichier;
	if(argc == 1) {
		cerr << "Fichier d'entrée non fourni\n";
		exit(1);
	} else if(argc == 2) {
		nomFichier = argv[1];
	} else if(argc == 3) {
		nomFichier = argv[2];
	} else {
		cerr<< "Le nombre d'arguments est invalide\n";
		exit(1);
	}
	
	return nomFichier;
}

/*!
* Fonction pour lire le contenu du fichier fourni par l'utilisateur
* @param nomFichier
* @return le contenu du fichier
*/
string lectureContenuFichier(string nomFichier) {
	
	ifstream ifs;
	ifs.open(nomFichier);
	if(ifs.fail()){
		cout << "Fichier d'entrée n'existe pas\n";
		exit(1);
	}
	stringstream ss;
	ss << ifs.rdbuf();
	string contenuFichier = ss.str(); 
	ifs.close();
	
	return contenuFichier;
}

/*!
* Fonction qui trouve le nombre de ligne de la grille 
* @param contenuFichier
* @return le nombre de lignes
*/
double getNbrLigne(string contenuFichier) {
	
	double nbrLigne = 1;
	
	for(int i = 0; i < contenuFichier.length(); i++) {
		if(contenuFichier.at(i) == '\n') 
			nbrLigne++;
	}

	return nbrLigne;
}

/*!
* fonction qui trouve le nombre de colonne de la grille
* @param contenuFichier
* @return le nombre de colonnes
*/
double getNbrColonne(string contenuFichier) {
	
	double nbrColonne;
	
	for(int i = 0; i < contenuFichier.length(); i++) {
		nbrColonne++;
		if(contenuFichier.at(i) == '\n') {
			nbrColonne -= 1;
			break;
		}
	}

	return nbrColonne;
}

/*!
* fonction pour convertir une position (x,y) en indice 
* @param m la matrice 
* @param c un noeud
* @return l'indice du noeud
*/
double posToIndex(Matrice *m, noeud c) {
	
	return((c.pos.first - 1) * m->nbrColonne + c.pos.second + (c.pos.first - 2));
}


/*!
* fonction qui trouve les positions des noeuds cibles : équipe d'entretien, interrupteurs, source d'électricité
* @param m la matrice 
* @param carac le caractère qui représente le noeud cible 
* @param liste vector initialement vide
*/
void trouveCible(Matrice *m, char carac, vector<noeud> *liste) {
	
	noeud cible;
	double posX, posY;
	int cpt = 0;
	
	string contenuFichier = m->contenu;
	
	for(int i = 0; i < contenuFichier.length(); i++) {
		if(contenuFichier.at(i) != '\n')
			cpt++;
		if(contenuFichier.at(i) == 'I')
			m->contenu.at(i) = 'i';	
		if(contenuFichier.at(i) == carac) {
			posX = ceil(cpt/m->nbrColonne);
			posY = m->nbrColonne - (ceil(cpt/m->nbrColonne) * m->nbrColonne - cpt);
			cible.pos.first = posX;
			cible.pos.second = posY;
			liste->push_back(cible);
		}
	}
}

/*!
* Fonction pour trouver le chemin autorisé du courant (peut uniquement traverser des objets conducteurs)
* @param m la matrice
* @param c un noeud 
* @param dest noeud destionation = maison
* @return vrai ou faux
*/
bool deplacementPossibleCourant(Matrice *m, noeud c, noeud dest) {
	
	double indice = posToIndex(m, c);
	double indicePos = posToIndex(m, dest);
	if (m->contenu.at(indice) == 'M' && indice != indicePos)
		return false;	
	return	(m->contenu.at(indice) != '#' && 
		(1 <= c.pos.first <= m->nbrLigne) && 
			(1 <= c.pos.second <= m->nbrColonne) &&
				m->contenu.at(indice) != ' ' &&
					m->contenu.at(indice) != 'm' &&
						m->contenu.at(indice) != 'S');
}

/*!
* Fonction pour trouver le chemin autorisé de l'équipe d'entretien (en évitant les obstacles)
* @param m la matrice
* @param c un noeud
* @return vrai ou faux
*/
bool deplacementPossibleEquipe(Matrice *m, noeud c) {
	
	double indice = posToIndex(m, c);
	return	(m->contenu.at(indice) != '#' && 
		(1 <= c.pos.first <= m->nbrLigne) && 
			(1 <= c.pos.second <= m->nbrColonne));
}

/*!
* deplacement possible vers le nord?
* @param m la matrice 
* @param c un noeud
* @param dest la destination
* @param flag 1 pour courant et 0 pour équipe d'entretien
* @return noeud voisins
*/
noeud deplacementNord(Matrice *m, noeud c, noeud dest, int flag) {
	
	noeud cNord;
	cNord.pos.first = c.pos.first - 1;
	cNord.pos.second = c.pos.second;
	cNord.direction = 'N';
	
	if (flag == 1) {
		if(deplacementPossibleCourant(m, cNord, dest))
			return cNord;
	} else {
		if(deplacementPossibleEquipe(m, cNord))
			return cNord;	
	}
	return c;
}

/*!
* deplacement possible vers le sud?
* @param m la matrice 
* @param c un noeud
* @param dest la destination
* @param flag 1 pour courant et 0 pour équipe d'entretien
* @return noeud voisins
*/
noeud deplacementSud(Matrice *m, noeud c, noeud dest, int flag) {
	
	noeud cSud;
	cSud.pos.first = c.pos.first + 1;
	cSud.pos.second = c.pos.second;
	cSud.direction = 'S';
	
	if (flag == 1) {
		if(deplacementPossibleCourant(m, cSud, dest))
			return cSud;
	} else {
		if(deplacementPossibleEquipe(m, cSud))
			return cSud;	
	}
	return c;
}

/*!
* deplacement possible vers l'ouest?
* @param m la matrice 
* @param c un noeud
* @param dest la destination
* @param flag 1 pour courant et 0 pour équipe d'entretien
* @return noeud voisins
*/
noeud deplacementOuest(Matrice *m, noeud c, noeud dest, int flag) {
	
	noeud cOuest;
	cOuest.pos.first = c.pos.first;
	cOuest.pos.second = c.pos.second - 1;
	cOuest.direction = 'W';
	
	if (flag == 1) {
		if(deplacementPossibleCourant(m, cOuest, dest))
			return cOuest;
	} else {
		if(deplacementPossibleEquipe(m, cOuest))
			return cOuest;	
	}
	return c;
}

/*!
* deplacement possible vers l'est?
* @param m la matrice 
* @param c un noeud
* @param dest la destination
* @param flag 1 pour courant et 0 pour équipe d'entretien
* @return noeud voisins
*/
noeud deplacementEst(Matrice *m, noeud c, noeud dest, int flag) {
	
	noeud cEst;
	cEst.pos.first = c.pos.first;
	cEst.pos.second = c.pos.second + 1;
	cEst.direction = 'E';
	
	if (flag == 1) {
		if(deplacementPossibleCourant(m, cEst, dest))
			return cEst;
	} else {
		if(deplacementPossibleEquipe(m, cEst))
			return cEst;	
	}
	return c;
}

/*!
* imprime R quand un conducteur est réparé
* @param a ASTAR de la matrice
*/
void reparerConducteur(ASTAR *a) {
	
	a->listeFinale << condRepare << " ";
}

/*!
* imprime 1 quand un interrupteur est ouvert
* @param a ASTAR de la matrice
*/
void allumerInterrupteur(ASTAR *a) {
	
	a->listeFinale << interrupteurON << " ";
}

/*!
* imprime 0 quand un interrupteur est fermé
* @param a ASTAR de la matrice
*/
void eteindreInterrupteur(ASTAR *a) {
	
	a->listeFinale << interrupteurOFF << " ";
}

/*!
* Fonction heuristique pour quantifier la qualité des noeuds : On calcule la distane entre le point étudié et le dernier point qu'on
* a jugé comme bon et on calcule aussi la distance entre le point étudié et le point de destination. La somme des ces deux distances donne la 
* qualité du noeud. Heurisitique utilisée : la distance euclidienne
* @param c un noeud
* @param dest la destination 
* @return la distance
*/
double fheuristique(noeud c, noeud dest) {
	
	return sqrt((c.pos.first - dest.pos.first) * (c.pos.first - dest.pos.first) + 
		(c.pos.second - dest.pos.second) * (c.pos.second - dest.pos.second));
}

/*!
* Heuristique #2 -> carré de la distance euclidienne (pour tester seulement) car l'heuristique #1 est meilleure pour minimiser le nombre d'actions
double fheuristique(noeud c, noeud dest) {
	
	return ((c.pos.first - dest.pos.first) * (c.pos.first - dest.pos.first) + 
		(c.pos.second - dest.pos.second) * (c.pos.second - dest.pos.second));
}  
*/

/*!
* Fonction qui détermine si deux noeuds sont identiques en comparant leur positions
* @param a premier noeud
* @param b deuxième noeud 
* @return vrai ou faux
*/
bool sontIdentiques(noeud a, noeud b) {
	
	return(a.pos == b.pos);
}

/*!
* Fonction qui détermine si un noeud est présent dans une liste donnée
* @param c un noeud
* @param vect la liste
* @return vrai ou faux
*/
bool estDansListe(noeud c, vector<noeud> vect) {
	
	for(int i = 0; i < vect.size(); i++) {
		if(vect.at(i).pos == c.pos)
			return true;
	}
	return false;
}

/*!
* Fonction qui ajuste la qualité d'un noeud dans la liste ouverte s'il a une moins bonne qualité
* @param a ASTAR de la matrice
* @param c un noeud
* @param nouveauCout le nouveau coût (la distance)
*/
void ajusteQualite(ASTAR *a, noeud c, double nouveauCout) {
	
	for(int i = 0; i < a->listeOuverte.size(); i++) {
		if(a->listeOuverte.at(i).pos == c.pos) {
			if(a->listeOuverte.at(i).coutF > nouveauCout)
				a->listeOuverte.at(i).coutF = nouveauCout;
		}
	}
}

/*!
* Fonction qui trouve le noeud qui a la meilleure qualité dans toute la liste ouverte.
* @param a ASTAR de la matrice
* @return le noeud avec la meilleur qualité
*/
noeud trouveMeilleurQualite(ASTAR *a) {
	
	noeud meilleur = a->listeOuverte.at(0);
	for(int i = 0; i < a->listeOuverte.size(); i++) {
		if(a->listeOuverte.at(i).coutF <= meilleur.coutF)
			meilleur = a->listeOuverte.at(i);
	}
	
	return meilleur;
}

/*!
* Fonction qui retourne le coût G du parrent d'un noeud 
* @param a ASTAR de la matrice
* @param c un noeud
* @return le coût du parent (h + f)
*/
double coutParent(ASTAR *a, noeud c) {
	
	for(int i = 0; i < a->listeFermee.size(); i++) {
		if(a->listeFermee.at(i).pos == c.pos) {
			return a->listeFermee.at(i).coutG;
		}
	}
	return 0;
}

/*!
* Fonction qui sert à repérer les noeuds voisins et les ajoute à la liste ouverte
* @param m la matrice
* @param a ASTAR de la matrice
* @param c un noeud
* @param dest la destination
* @param flag utile pour la fonction deplacementPossibleCourant et deplacementPossibleEquipe
*/
void trouveVoisins(Matrice *m, ASTAR *a, noeud c, noeud dest, int flag) {
	
	vector<noeud> temp;
	noeud voisinNord = deplacementNord(m, c , dest, flag); 
	noeud voisinSud = deplacementSud(m, c, dest, flag); 
	noeud voisinOuest = deplacementOuest(m, c, dest, flag); 
	noeud voisinEst = deplacementEst(m, c, dest, flag); 
	temp.push_back(voisinNord); 
	temp.push_back(voisinSud); 
	temp.push_back(voisinOuest); 
	temp.push_back(voisinEst);
	
	for(int i = 0; i < temp.size(); i++) {
		if(!estDansListe(temp.at(i), a->listeFermee) && !sontIdentiques(temp.at(i), c)) {
			temp.at(i).coutG = coutParent(a, c) + fheuristique(temp.at(i), c);
			temp.at(i).coutH = fheuristique(temp.at(i), dest);
			temp.at(i).coutF = temp.at(i).coutG + temp.at(i).coutH;
			temp.at(i).parent = c.pos;
			
			if(estDansListe(temp.at(i), a->listeOuverte)) {
				ajusteQualite(a, temp.at(i), temp.at(i).coutF);
			} else {
				a->listeOuverte.push_back(temp.at(i));
			}
		}
	}
}

/*!
* Fonction pour ajouter un noeud à la liste fermée et le supprimer de la liste ouverte
* @param a ASTAR de la matrice
* @param c un noeud
*/
void ajouteListeFermee(ASTAR *a, noeud c) {
	
	a->listeFermee.push_back(c);
	for(int i = 0; i < a->listeOuverte.size(); i++) {
		if(a->listeOuverte.at(i).pos == c.pos)
			a->listeOuverte.erase(a->listeOuverte.begin()+i);
	}
}

/*!
* Fonction qui sert à trouver un noeud dans la liste fermée en utilisant ses coordonnées
* @param a ASTAR de la matrice
* @param precedant les coordonées
* @return le noeud recherché
*/
noeud trouveNoeud(ASTAR *a, pair<double, double> precedant) {
	
	noeud n;
	for(int i = 0; i < a->listeFermee.size(); i++) {
		if(a->listeFermee.at(i).pos == precedant) {
			n = a->listeFermee.at(i);
			break;
		}
	}	
	return n;	
}

/*!
* Fonction qui sert à trouver le chemin une fois la destination a été atteinte. On remonte les noeuds de parent en parent -> chaque noeud
* est ajouté en tête de la liste en utilisant push_front
* @param a ASTAR de la matrice	
* @param m la matrice
* @param depart le noeud de départ
* @param destination le noeud destination
* @param flag 1 trouver le chemin de la position de l'équipe d'entretien vers la destination, 2 trouver le chemin de la/les source(s) vers la/les maison(s)
*/
void trouverChemin(ASTAR *a, Matrice *m, noeud depart, noeud destination, int flag) {
	
	list<pair<double, double>> chemin;
	pair<double, double> precedant = destination.pos;
	bool trouve = false;
	vector<myPair> temp;
	myPair reparationNecessaire;

	while (precedant != depart.pos) {
		chemin.push_front(precedant);
		noeud temp = trouveNoeud(a, precedant);
		precedant = temp.parent;
	}

	vector<noeud> vectN;
	noeud n;

	if (flag == 1) {
		for (auto c : chemin) {
			if (m->contenu.at(posToIndex(m, trouveNoeud(a, c))) == 'b' || 
			m->contenu.at(posToIndex(m, trouveNoeud(a, c))) == 'j') {
				trouve = true;
				n = trouveNoeud(a, c);
				vectN.push_back(n);
			}
			a->listeFinale << m->contenu.at(posToIndex(m, trouveNoeud(a, c)));
			reparationNecessaire.first = m->contenu.at(posToIndex(m, trouveNoeud(a, c)));
			reparationNecessaire.second = trouveNoeud(a, c).pos;
			temp.push_back(reparationNecessaire);
		}
		if (trouve) {
			for (int i  = 0; i < vectN.size(); i++) 
				m->contenu.at(posToIndex(m, vectN.at(i))) = interrupteurBriseSignale;
			a->listeActions.insert(end(a->listeActions), begin(temp), end(temp));
		}
	} else {
		for (auto c : chemin) 
			a->listeFinale << trouveNoeud(a, c).direction << " ";
	}
	temp.clear();	
}

/*!
* Fonction qui fait appel à la fonction trouverChemin pour trouver le chemin optimal de la source vers la destination
* @param m la matrice
* @param a ASTAR de la matrice
* @param depart le noeud de depart
* @param destination le noeud destination
* @param flag utile pour distinguer entre deplacementPossibleCourant ou deplacementPossibleEquipe
*/
void deplacementCourantOUequipe(Matrice *m, ASTAR *a, noeud depart, noeud destination, int flag) {
	
	noeud courant = depart;
	a->listeOuverte.push_back(courant);
	ajouteListeFermee(a, courant);
	trouveVoisins(m, a, courant, destination, flag);
	
	while(courant.pos != destination.pos && !(a->listeOuverte.empty())) {
		courant = trouveMeilleurQualite(a);
		ajouteListeFermee(a, courant);
		trouveVoisins(m, a, courant, destination, flag);
	}	
	
	if (courant.pos == destination.pos) {
		trouverChemin(a, m, depart, destination, flag);	
	} else if (flag != 1) {
		codeRetour cr = echoue;
		vectCodeRetour.push_back(cr);	
	}
	
	a->listeOuverte.clear();
	a->listeFermee.clear();
}

/*!
* Fonction utile pour la fonction "sort" qui sert à trier un vector de pair
* @param a première paire
* @param b deuxième paire
* @return vrai ou faux
*/
bool tierVect(pair<int, noeud> a, pair<int, noeud> b);

/*!
* Fonction qui sert à trier les sources par rapport à leur distance de l'équipe d'entretien
* @param m la matrice m
* @param a ASTAR de la matrice
* @return les sources triées dans un vector
*/
vector<int> trierSources(Matrice *m, ASTAR *a) {
	
	vector<pair<int, noeud>> vect;
	vector<int> sourcesTriees;
	pair<int, noeud> p;
	string temp = a->listeFinale.str();
	a->listeFinale.str(string());
	a->listeFinale.clear();
	
	for(int i = 0; i < m->listeSourcesElectricite.size(); i++) {
		deplacementCourantOUequipe(m, a, m->equipeEntretien.at(0), m->listeSourcesElectricite.at(i), 2);
		p.first = a->listeFinale.str().length();
		p.second = m->listeSourcesElectricite.at(i);
		vect.push_back(p);
		a->listeFinale.str(string());
		a->listeFinale.clear();
	}
	
	a->listeFinale.str() = temp;
	
	sort(vect.begin(), vect.end(), tierVect);
	
	for(int i = 0; i < vect.size(); i++)	
		sourcesTriees.push_back(vect.at(i).first);
		
	return sourcesTriees;
}

/*!
* Fonction qui compare le nombre d'actions de plusieurs chemins menant à la même destination, et et qui choisit le meilleur = moins d'actions
* @param m la matrice
* @param a ASTAR de la matrice
* @param vect le chemin en forme de string
* @return le chemin qui contient le moins d'actions
*/
string cheminOptimal(Matrice *m, ASTAR *a, vector<string> vect) {
	
	pair<int, string> min;
	vector<pair<int, string>> temp;
	pair<int, string> chemin;
	int index;
	
	vector<int> sourcesTriees = trierSources(m, a);
	
	for (int i = 0; i < vect.size(); i++) {
		chemin.first = vect.at(i).size();
		for (int j = 0; j < vect.at(i).size(); j++) {
			chemin.second = vect.at(i);
			if (vect.at(i).at(j) == 'c')
				chemin.first -= 1;
			if (vect.at(i).at(j) == 'b' || vect.at(i).at(j) == 'i' || vect.at(i).at(j) == 'j')
				chemin.first += 1;
		}
		temp.push_back(chemin);	
	}
	
	
	for (int i = 0; i < temp.size(); i++) {
		if (temp.at(i).first != 0 && temp.at(i).first != 1)
			temp.at(i).first += sourcesTriees.at(i);
	}
	
	for (int i = 0; i < temp.size(); i++) {
		if (temp.at(i).first != 0) {
			min = temp.at(i);
			index = i;	
			break;
		}
	}
	
	for (int i = index + 1; i < temp.size(); i++) {
		if (temp.at(i).first < min.first && temp.at(i).first != 0)
			min = temp.at(i);	
	}
	
	return min.second;
}

/*!
* Fonction qui cherche la présence d'un interrupteur OFF dans le chemin donné
* @param chemin le chemin donné
* @return vrai ou faux
*/
bool contientInterrupteurOFF(string chemin) {
	
	for (int i = 0; i < chemin.length(); i++) {
		if (chemin.at(i) == 'j')
			return true;
	}
	return false;
}

/*!
* Fonction qui decide si une réparation est nécessaire dans un chemin donné, 
* si oui ajoute necessaire dans le vector vectReparation, sinon ajoute pasNecessaire
* @param chemin le chemin donné
*/
void reparationNecessaire(string chemin) {
	
	bool reseauSain = true;
	
	for (int i = 0; i < chemin.size(); i++) {
		if ((chemin.at(i) == 'b' || chemin.at(i) == 'B'))
			reseauSain = false;
	}
	
	reparation r;
	
	if (contientInterrupteurOFF(chemin))
		reseauSain = false;
	
	if (reseauSain && !chemin.empty()) {
		r = pasNecessaire;
		vectReparation.push_back(r);
	} else {
		r = necessaire;
		vectReparation.push_back(r);
	}
}

/*!
* Fonction qui trouve le chemin optimal des sources vers les maisons et décide si une réparation de courant est nécessaire ou pas
* @param m la matrice
* @param a ASTAR de la matrice
*/
vector<myPair> filtreActions(Matrice *m, ASTAR *a) {
	
	vector<string> cheminsPossibles;
	vector<myPair> vect;
	string chemin;
	int k = 0;
	
	for(int i = 0; i < m->listeMaisons.size(); i++) {
		for(int j = 0; j < m->listeSourcesElectricite.size(); j++) {
			m->contenu.at(posToIndex(m, m->listeMaisons.at(i))) = maisonVisitee;
			deplacementCourantOUequipe(m, a, m->listeSourcesElectricite.at(j), m->listeMaisons.at(i), 1);
			cheminsPossibles.push_back(a->listeFinale.str());
			a->listeFinale.str(string());
			a->listeFinale.clear();
		}
		
		chemin = cheminOptimal(m, a, cheminsPossibles);
		
		if (m->listeSourcesElectricite.size() > 1) {
			for (int j = 0; j < chemin.length() && k < a->listeActions.size(); j++) {
				if (chemin.at(j) == a->listeActions.at(k).first) {
					vect.push_back(a->listeActions.at(k));
				} else {
					vect.clear();
					j = -1;
				}
				k++;
			}
		}
		cheminsPossibles.clear();
		reparationNecessaire(chemin);
	}
	
	bool reparationNecessaire = false;
	
	for (int i = 0; i < vectReparation.size(); i++) {
		if (vectReparation.at(i) != pasNecessaire) 
			reparationNecessaire = true;
	}
	
	if (!reparationNecessaire) {
		cout << "Aucune réparation n'est nécessaire\n";
		exit(0);
	}
	
	return vect;
}

/*!
* Fonction qui filtre les chemins, en enlevant les noeuds inutiles et en gardant les noeuds qui feront parti de la solution finale
* @param vect vector qui contient des paires : caratère du noeud + ses coordonnées dans la grille
*/
vector<myPair> trouveActions(vector<myPair> vect) {
	
	vector<myPair> vectModifie;
	int cpt1, cpt2;
	bool interrupteurNecessaire = true;
	
	for (int i = 0; i < vect.size(); i++) {
		if (vect.at(i).first != 'i' && vect.at(i).first != 'b' && vect.at(i).first != 'j') {
			vect.erase(vect.begin() + i);
			i--;
		}
	}

	for (int i = 0; i < vect.size() - 1; i++) {
		if (vect.at(i).first == 'i' && vect.at(i + 1).first != 'b') {
			vect.erase(vect.begin() + i);
			i--;
		}
	}
	
	for (int i = 0; i < vect.size(); i++) {
		vectModifie.push_back(vect.at(i));
		if (i < vect.size() - 1 && vect.at(i).first == 'j' && vect.at(i + 1).first != 'j') {
			vectModifie.erase(vectModifie.begin() + i);
			cpt1 = i;
			i++;
			while (i < vect.size() && (vect.at(i).first != 'i')) {
				vectModifie.push_back(vect.at(i));
				i++;
			}
			vectModifie.push_back(vect.at(cpt1));
			if (i != vect.size())
				i = cpt1;
		}
	}
	
	for (int i = 0; i < vectModifie.size(); i++) {
		if (vectModifie.at(i).first == 'i' && i != vectModifie.size())
			interrupteurNecessaire = false;
	}
			
	if (vectModifie.back().first == 'i' && !interrupteurNecessaire)
		vectModifie.erase(vectModifie.begin() + vectModifie.size() - 1);		

	return vectModifie;
}

/*!
* Fonction qui sert à detecter les interrupteurs communs pour un seul bris
* @param vect vector qui contient des paires : caratère du noeud + ses coordonnées dans la grille
*/
vector<myPair> condBrisesInterCommuns(vector<myPair> vect) {
	
	vector<myPair> vectModifie;
	int cpt1, cpt2;
	
	for (int i = 0; i < vect.size(); i++) {
		vectModifie.push_back(vect.at(i));
		for (int j = i + 1; j < vect.size() ; j++ ) {
			if (vect.at(i) == vect.at(j)) {
				cpt1 = i;
				i++;
				while (i < vect.size() && vect.at(i).first == 'b') {
					vectModifie.push_back(vect.at(i));
					i++;
				}
				i = cpt1;
				cpt2 = j;
				j++;
				while (j < vect.size() && vect.at(j).first == 'b') {
					vectModifie.push_back(vect.at(j));
					vect.erase(vect.begin() + j);
					j++;
				}
				j = cpt2;
				vect.erase(vect.begin() + j);
				vect.erase(vect.begin() + i);
			}
		}
	}
	
	return vectModifie;
}

/*!
* Fonction qui sert à detecter la présence de plusieurs sources dans le monde et appelle différentes fonctions en conséquence
* @param vect vector qui contient des paires : caratère du noeud + ses coordonnées dans la grille
* @param m la matrice
* @param a ASTAR de la matrice
*/
void uneOuPlusieursSource(vector<myPair> vect, Matrice *m, ASTAR *a) {
	
	if (m->listeSourcesElectricite.size() > 1 && vect.size() != 0) {
		a->listeActions = condBrisesInterCommuns(trouveActions(vect));
	} else if (a->listeActions.size() != 0) {
		a->listeActions = condBrisesInterCommuns(trouveActions(a->listeActions));
	} else {
		cout << "IMPOSSIBLE\n";
		exit(1);
	}
}

/*!
* Fonction qui sert à imprimer le résultat final (la série d'actions à faire)
* @param a ASTAR de la matrice
*/
void cheminPossible(ASTAR *a) {
	
	cout << a->listeFinale.str();
}

/*!
* Fonction qui imprime IMPOSSIBLE en cas d'absence de solution
*/
void validation() {
	for (int i = 0; i < vectCodeRetour.size(); i++) {
		if (vectCodeRetour.at(i) == echoue) {
			cout << "IMPOSSIBLE\n";
			exit(1);
		}
	}
}

/*!
* Fonction qui traite les actions et imprime la séquence d'actions permettant de rétablir l'électricité pour toutes les maisons
* @param m la matrice
* @param a ASTAR de la matrice
*/
void imprimeResultat1(Matrice *m, ASTAR *a) {
	
	noeud posEquipeEntretien = m->equipeEntretien.at(0);
	
	bool reparationNecessaire, presenceInterrupteurON, presenceInterrupteurOFF = false;
	
	for (int i = 0; i < a->listeActions.size(); i++) {
		if (a->listeActions.at(i).first == 'b') 
			reparationNecessaire = true;
		if (a->listeActions.at(i).first == 'j')
			presenceInterrupteurOFF = true;	
		if (a->listeActions.at(i).first == 'i')
			presenceInterrupteurON = true;	
	}
	
	if (reparationNecessaire && 
		!presenceInterrupteurON && 
	!presenceInterrupteurOFF) {
		cout << "IMPOSSIBLE\n";
		exit(1);
	}
	
	if (!reparationNecessaire && presenceInterrupteurOFF) {
		for (int i = 0; i < a->listeActions.size(); i++) {
			if (a->listeActions.at(i).first == 'j') {
				noeud n;
				n.pos = a->listeActions.at(i).second;
				deplacementCourantOUequipe(m, a, posEquipeEntretien, n, 2);
				posEquipeEntretien.pos = n.pos;
				allumerInterrupteur(a);
			}
		}
		validation();
		cheminPossible(a);	
		cout << '\n';
		exit(0);
	}
}

/*!
* Fonction qui traite les actions et imprime la séquence d'actions permettant de rétablir l'électricité pour toutes les maisons
* @param m la matrice
* @param a ASTAR de la matrice
*/
void imprimeResultat2(Matrice *m, ASTAR *a) {

	noeud posEquipeEntretien = m->equipeEntretien.at(0);
	int cpt1;
	bool presenceInterrupteur = false;
	
	for (int i = 0; i < a->listeActions.size(); i++) {
		noeud n;
		if (a->listeActions.at(i).first == 'i') {
			n.pos = a->listeActions.at(i).second;
			deplacementCourantOUequipe(m, a, posEquipeEntretien, n, 2);
			posEquipeEntretien.pos = n.pos;
			eteindreInterrupteur(a);
		} else if (a->listeActions.at(i).first == 'b') {
			n.pos = a->listeActions.at(i).second;
			deplacementCourantOUequipe(m, a, posEquipeEntretien, n, 2);
			posEquipeEntretien.pos = n.pos;
			reparerConducteur(a);
			if (i == a->listeActions.size() - 1 ||
				a->listeActions.at(i + 1).first == 'i' ||
			a->listeActions.at(i + 1).first == 'j') {
				cpt1 = i + 1;
				while (i > 0 && 
					a->listeActions.at(i).first != 'i' && 
						a->listeActions.at(i).first != 'j')
							i--;

				if (i == 0 && a->listeActions.back().first != 'b') {
					n.pos = a->listeActions.at(i + 1).second;
				} else {
					n.pos = a->listeActions.at(i).second;
				}
				if (posEquipeEntretien.pos != n.pos) {
					deplacementCourantOUequipe(m, a, posEquipeEntretien, n, 2);
					posEquipeEntretien.pos = n.pos;
					allumerInterrupteur(a);
				}
				i = cpt1 - 1;
				while (i >= 0) {
					if (a->listeActions.at(i).first == 'i')
						presenceInterrupteur = true;
					i--;
				}
				i = cpt1 - 1;
			}
		} else if (a->listeActions.at(i).first == 'b' && 
		a->listeActions.at(i + 1).first == 'i') {
			while (i > 0 && a->listeActions.at(i).first != 'i')
				i--;
			n.pos = a->listeActions.at(i).second;
			if (posEquipeEntretien.pos != n.pos) {
				deplacementCourantOUequipe(m, a, posEquipeEntretien, n, 2);
				posEquipeEntretien.pos = n.pos;
				allumerInterrupteur(a);
			}
		} else if (a->listeActions.at(i).first == 'j') {
			n.pos = a->listeActions.at(i).second;
			if (posEquipeEntretien.pos != n.pos) {
				deplacementCourantOUequipe(m, a, posEquipeEntretien, n, 2);
				posEquipeEntretien.pos = n.pos;
				allumerInterrupteur(a);
			}
		}
	}
	validation();
	cheminPossible(a);	
	cout << '\n';
}

/*!
* Fonction utile pour la fonction "sort" qui sert à trier un vector de pair
* @param a première paire
* @param b deuxième paire
* @return vrai ou faux
*/
bool tierVect(pair<int, noeud> a, pair<int, noeud> b) { 
	return (a.first < b.first); 
} 

/*!
* Fonction qui sert à trier les maisons et les sources d'électricité en fonction de la distance de l'équipe d'entretien;
* du plus proche au plus loin
* @param m la matrice
* @param a ASTAR de la matrice
*/
vector<noeud> trierSelonDistance(Matrice *m, ASTAR *a) {
	
	vector<pair<int, noeud>> vect;
	vector<noeud> maisonsTriees;
	pair<int, noeud> p;
	
	for(int i = 0; i < m->listeMaisons.size(); i++) {
		deplacementCourantOUequipe(m, a, m->equipeEntretien.at(0), m->listeMaisons.at(i), 2);
		p.first = a->listeFinale.str().length();
		p.second = m->listeMaisons.at(i);
		vect.push_back(p);
		a->listeFinale.str(string());
		a->listeFinale.clear();
	}
	
	sort(vect.begin(), vect.end(), tierVect);
	
	for(int i = 0; i < vect.size(); i++)	
		maisonsTriees.push_back(vect.at(i).second);
		
	return maisonsTriees;
}

/*!
* La fonction principale
* @param argc nombre d'arguments
* @param argv les arguments
* @return 0
*/
int main(int argc, char *argv[]) {
	
	ASTAR a;
	Matrice m;
	string nomFichier = lectureNomFichier(argc, argv);
	m.contenu = lectureContenuFichier(nomFichier);
	m.nbrLigne = getNbrLigne(m.contenu);
	m.nbrColonne = getNbrColonne(m.contenu);
	
	trouveCible(&m, 'S', &m.listeSourcesElectricite);
	trouveCible(&m, 'm', &m.listeMaisons);	
	trouveCible(&m, '*', &m.equipeEntretien);	
	
	vector<noeud> temp = trierSelonDistance(&m, &a);
	m.listeMaisons = m.listeSourcesElectricite;
	m.listeSourcesElectricite = trierSelonDistance(&m, &a);
	m.listeMaisons = temp;
	
	vector<myPair> vect = filtreActions(&m, &a);
	uneOuPlusieursSource(vect, &m, &a);
	imprimeResultat1(&m, &a);
	imprimeResultat2(&m, &a);
	
	return 0;
}
