#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>
#include <math.h>
#include <algorithm>

#define joueurPierresNoires 1;
#define joueurPierresBlanches 2;

using namespace std;

/**
 * enumération des couleurs 
.* neutre : case qui contient pas de pierre
.* noire : case qui contient une pierre noire
.* blanche : case qui contient une pierre blanche
 */
enum Couleur {
	neutre,
	noire,
	blanche,
};

/**
 * Structure des positions adjacentes possibles
 */
struct {
	int x, y;
} directions[] = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

/**
 * structure Noeud 
 */
struct Noeud {
	public:
	pair <double, double> pos;
	bool occupe, utilise;
	int valeur, flag;
};

/**
 * structure d'une configuration de connect5
 */
struct Connect5 {
	public:
	int * grille;
	double nbrLigne, nbrColonne;
	vector <Noeud> configCourante;
	vector <vector<Noeud> > configsPossibles;
	int tempsReflexion, nbrPierresNoires, nbrPierresBlanches;
	vector <Noeud> listePierresNoires, listePierresBlanches;
};

/**
 * Message d'introduction affichant les détails de l'étudiant
 */
void msgIntro() {
	cout << "Nom de l'étudiant : Fadi Feghali" << endl;
	cout << "Code permanent    : FEGF07069109" << endl;
	cout << "Adresse courriel  : feghali.fadi@courrier.uqam.ca" << endl;
	cout << "-------------------------------------------------" << endl;
	cout << "PRET" << endl;
}

/**
 * Fonction qui lit le nombre  de ligne et le nombre de colonne du jeu connect5
 * @return une paire contenant les deux valeurs lus
 */
pair <double, double> lireNbrLigneColonne() {
	pair <int, int> dimension;
	double nbrLigne = 0, nbrColonne = 0;
	cin >> nbrLigne >> nbrColonne >> std::ws;

	if (cin.fail() || nbrLigne == 0 || nbrColonne == 0) {
		cout << "Erreur taille grille!\n";
		exit(1);
	}

	dimension.first = nbrLigne;
	dimension.second = nbrColonne;

	return dimension;
}

/**
 * Fonction qui lit la configuration d'un jeu connect5
 * @param le jeu connect5 : c5
 */
void lectureConfiguration(Connect5 * c5) {
	int nbligne = (int) c5->nbrLigne;
	int nbcol = (int) c5->nbrColonne;

	for (int l = 0; l < c5->nbrLigne; l++) {
		string ligne;
		getline(cin, ligne);
		ligne += '\n';
		stringstream sstr(ligne);
		for (int c = 0; c < nbcol; c++) {
			char ca = 0;
			sstr >> ca;
			switch (ca) {
			case '0':
				c5->grille[l * nbcol + c] = 0;
				break;
			case 'N':
				c5->grille[l * nbcol + c] = 1;
				break;
			case 'B':
				c5->grille[l * nbcol + c] = 2;
				break;
			}
		}
	}
}

/**
 * Fonction qui lit le nombre de pierres noires dans une configuration d'un jeu connect5
 * @param le jeu connect5 : c5
 * @return le nombre de pierres noires
 */
int getNbrPierresNoires(Connect5 * c5) {
	int nbrPierresNoires = 0;
	int dimension = (int)(c5->nbrLigne * c5->nbrColonne);
	for (int i = 0; i < dimension; i++) {
		if (c5->grille[i] == 1)
			nbrPierresNoires++;
	}
	return nbrPierresNoires;
}

/**
 * Fonction qui lit le nombre de pierres blanches dans une configuration d'un jeu connect5
 * @param le jeu connect5 : c5
 * @return le nombre de pierres blanches
 */
int getNbrPierresBlanches(Connect5 * c5) {
	int nbrPierresBlanches = 0;
	int dimension = (int)(c5->nbrLigne * c5->nbrColonne);
	for (int i = 0; i < dimension; i++) {
		if (c5->grille[i] == 2)
			nbrPierresBlanches++;
	}
	return nbrPierresBlanches;
}

/**
 * Fonction qui décide le tour d'un joueur : joueur 1 ou 2
 * @param le jeu connect5 : c5
 * @return le numéro du joueur courant
 */
int tourJoueur(Connect5 * c5) {
	if (c5->nbrPierresNoires > c5->nbrPierresBlanches)
		return joueurPierresBlanches;
	if (c5->nbrPierresNoires == c5->nbrPierresBlanches == 0)	
		return joueurPierresNoires;
	return joueurPierresNoires;
}

/**
 * Foncton qui lit le temps de réflexion 
 * @return le temps de réflexion
 */
int lectureTempsReflexion() {
	int tempReflexion;
	cin >> tempReflexion;
	return tempReflexion;
}

/**
 * Fonction qui construit un vecteur de Noeuds contentant toutes les cases vides 
.* les pierres noires, et blanches avec leur positions (x, y) dans la grille 
 * @param le jeu connect5 : c5
 * @param la configuration courante du jeu : configCourante
 * @param vecteur de pierres noires : PN 
 * @param vecteur de pierres blanches : PB 
 */
void trouverPierres(Connect5 * c5, vector <Noeud> * configCourante, vector <Noeud> * PN, vector <Noeud> * PB) {
	Noeud pierreRecherchee;
	double posX, posY;
	int cpt = 0;
	int dimension = (int)(c5->nbrLigne * c5->nbrColonne);

	for (int i = 0; i < dimension; i++) {
		if (c5->grille[i] != '\n')
			cpt++;
		posX = ceil(cpt / c5->nbrColonne);
		posY = c5->nbrColonne - (ceil(cpt / c5->nbrColonne) * c5->nbrColonne - cpt);
		if (c5->grille[i] == 1 || c5->grille[i] == 2) {
			pierreRecherchee.pos.first = posX;
			pierreRecherchee.pos.second = posY;
			pierreRecherchee.occupe = true;
			if (c5->grille[i] == 1) {
				pierreRecherchee.valeur = 1;
				PN->push_back(pierreRecherchee);
				configCourante->push_back(pierreRecherchee);
			} else {
				pierreRecherchee.valeur = 2;
				PB->push_back(pierreRecherchee);
				configCourante->push_back(pierreRecherchee);
			}
		} else {
			pierreRecherchee.valeur = 0;
			pierreRecherchee.pos.first = posX;
			pierreRecherchee.pos.second = posY;
			pierreRecherchee.occupe = false;
			configCourante->push_back(pierreRecherchee);
		}
	}
}

/**
 * Fonction qui convertir un vecteur de Noeud en une matrice 2D
 * @param le jeu connect5 : c5
 * @param Une configuration donnée : config
 * @return la matrice 2D formée de 0, 1 ou 2
 */
int ** convertTo2DMatrix(Connect5 * c5, vector<Noeud> config) {
	int ** mat = new int * [(int) c5->nbrLigne];

	for (int i = 0; i < c5->nbrLigne; i++) {
		mat[i] = new int[(int) c5->nbrColonne];
	}

	for (int i = 0; i < (int) c5->nbrLigne; i++) {
		for (int j = 0; j < (int) c5->nbrColonne; j++) {
			if ((int) c5->nbrLigne == (int) c5->nbrColonne) {
				mat[i][j] = config.at(i * (int)(c5->nbrLigne) + j).valeur;
			} else {
				mat[i][j] = config.at(i * (int)(c5->nbrColonne) + j).valeur;
			}
		}
	}
	return mat;
}

/**
 * Fonction qui vérifie si les voisins d'un point donné sont de la même couleur
.* Regarde à droite
 * @param la matrice 2D : mat
 * @param le numéro du joueur 1 ou 2 : joueur
 * @param l'indice : i (d'une case)
 * @param l'indice : j (d'une case)
 * @param le nombre de pierres à vérifier : n (1, 2, 3, 4 ou 5)
 * @return vrai si le nombre de pierres est trouvé, faux sinon
 */
bool lookRight(int ** mat, int joueur, int i, int j, int n) {
	for (int k = 1; k < n; k++) {
		if (mat[i][j + k] != joueur)
			return false;
	}
	return true;
}

/**
 * Fonction qui vérifie si les voisins d'un point donné sont de la même couleur 
.* Regarde la verticale descendante
 * @param la matrice 2D : mat
 * @param le numéro du joueur 1 ou 2 : joueur
 * @param l'indice : i (d'une case)
 * @param l'indice : j (d'une case)
 * @param le nombre de pierres à vérifier : n (1, 2, 3, 4 ou 5)
 * @return vrai si le nombre de pierres est trouvé, faux sinon
 */
bool lookDown(int ** mat, int joueur, int i, int j, int n) {
	for (int k = 1; k < n; k++) {
		if (mat[i + k][j] != joueur)
			return false;
	}
	return true;
}

/**
 * Fonction qui vérifie si les voisins d'un point donné sont de la même couleur
.* Regarde la diagonale droite descendante
 * @param la matrice 2D : mat
 * @param le numéro du joueur 1 ou 2 : joueur
 * @param l'indice : i (d'une case)
 * @param l'indice : j (d'une case)
 * @param le nombre de pierres à vérifier : n (1, 2, 3, 4 ou 5)
 * @return vrai si le nombre de pierres est trouvé, faux sinon
 */
bool lookDownRight(int ** mat, int joueur, int i, int j, int n) {
	for (int k = 1; k < n; k++) {
		if (mat[i + k][j + k] != joueur)
			return false;
	}
	return true;
}

/**
 * Fonction qui vérifie si les voisins d'un point donné sont de la même couleur
.* Regarde la diagonale gauche descendante
 * @param la matrice 2D : mat
 * @param le numéro du joueur 1 ou 2 : joueur
 * @param l'indice : i (d'une case)
 * @param l'indice : j (d'une case)
 * @param le nombre de pierres à vérifier : n (1, 2, 3, 4 ou 5)
 * @return vrai si le nombre de pierres est trouvé, faux sinon
 */
bool lookDownLeft(int ** mat, int joueur, int i, int j, int n) {
	for (int k = 1; k < n; k++) {
		if (mat[i + k][j - k] != joueur)
			return false;
	}
	return true;
}

/**
 * Fonction qui vérifie si un nombre de pierres est inclu dans un plus grand nombre de pierres
.* par exemple si un groupe de 3 pierres est inclu dans un groupes de 4 pierres, seulement le groupe de 
.* 4 pierres sera considéré
.* Regarde à droite
 * @param le jeu connect5 : c5
 * @param la matrice 2D : mat
 * @param le numéro du joueur : joueur
 * @param l'indice : i (d'une case)
 * @param l'indice : j (d'une case)
 * @param les autres groupes à exclure de la recherche  : autresGroupes
 * @return vrai si la condition est respectée, faux sinon
 */
bool incluAutresGroupesR(Connect5 * c5, int ** mat, int joueur, int i, int j, vector <int> autresGroupes) {

	if (autresGroupes.empty()) return false;

	for (int k = 0; k < autresGroupes.size(); k++) {
		if (i < (int) c5->nbrLigne && j + autresGroupes.at(k) - 1 < c5->nbrColonne) {
			if (lookRight(mat, joueur, i, j, autresGroupes.at(k)))
				return true;
		}
	}
	return false;
}

/**
 * Fonction qui vérifie si un nombre de pierres est inclu dans un plus grand nombre de pierres
.* par exemple si un groupe de 3 pierres est inclu dans un groupes de 4 pierres, seulement le groupe de 
.* 4 pierres sera considéré
.* Regarde la verticale descendante
 * @param le jeu connect5 : c5
 * @param la matrice 2D : mat
 * @param le numéro du joueur : joueur
 * @param l'indice : i (d'une case)
 * @param l'indice : j (d'une case)
 * @param les autres groupes à exclure de la recherche  : autresGroupes
 * @return vrai si la condition est respectée, faux sinon
 */
bool incluAutresGroupesD(Connect5 * c5, int ** mat, int joueur, int i, int j, vector <int> autresGroupes) {

	if (autresGroupes.empty()) return false;

	for (int k = 0; k < autresGroupes.size(); k++) {
		if (i + autresGroupes.at(k) - 1 < c5->nbrLigne && j < (int) c5->nbrColonne) {
			if (lookDown(mat, joueur, i, j, autresGroupes.at(k)))
				return true;
		}
	}
	return false;

}

/**
 * Fonction qui vérifie si un nombre de pierres est inclu dans un plus grand nombre de pierres
.* par exemple si un groupe de 3 pierres est inclu dans un groupes de 4 pierres, seulement le groupe de 
.* 4 pierres sera considéré
.* Regarde la diagonale droite descendante
 * @param le jeu connect5 : c5
 * @param la matrice 2D : mat
 * @param le numéro du joueur : joueur
 * @param l'indice : i (d'une case)
 * @param l'indice : j (d'une case)
 * @param les autres groupes à exclure de la recherche  : autresGroupes
 * @return vrai si la condition est respectée, faux sinon
 */
bool incluAutresGroupesDR(Connect5 * c5, int ** mat, int joueur, int i, int j, vector <int> autresGroupes) {

	if (autresGroupes.empty()) return false;

	for (int k = 0; k < autresGroupes.size(); k++) {
		if (i + autresGroupes.at(k) - 1 < c5->nbrLigne && j + autresGroupes.at(k) - 1 < c5->nbrColonne) {
			if (lookDownRight(mat, joueur, i, j, autresGroupes.at(k)))
				return true;
		}
	}
	return false;

}

/**
 * Fonction qui vérifie si un nombre de pierres est inclu dans un plus grand nombre de pierres
.* par exemple si un groupe de 3 pierres est inclu dans un groupes de 4 pierres, seulement le groupe de 
.* 4 pierres sera considéré
.* Regarde la diagonale gauche descendante
 * @param le jeu connect5 : c5
 * @param la matrice 2D : mat
 * @param le numéro du joueur : joueur
 * @param l'indice : i (d'une case)
 * @param l'indice : j (d'une case)
 * @param les autres groupes à exclure de la recherche  : autresGroupes
 * @return vrai si la condition est respectée, faux sinon
 */
bool incluAutresGroupesDL(Connect5 * c5, int ** mat, int joueur, int i, int j, vector <int> autresGroupes) {

	if (autresGroupes.empty()) return false;

	for (int k = 0; k < autresGroupes.size(); k++) {
		if (i + autresGroupes.at(k) - 1 < c5->nbrLigne && j - (autresGroupes.at(k) - 1) >= 0) {
			if (lookDownLeft(mat, joueur, i, j, autresGroupes.at(k)))
				return true;
		}
	}
	return false;

}

/**
 * Fonction qui vérifie si un groupe de pierres (2, 3, 4 et 5) d'aligner existe dans une configuration du jeu connect5
 * @param le jeu connect5 : c5
 * @param une configuration du jeu : config
 * @param le numéro du joueur : joueur
 * @param le nombre de pierres à vérifier : nbrPierres
 * @return le nombre de groupe de pierres trouvé
 */
int trouveGroupe(Connect5 * c5, vector<Noeud> config, int joueur, int nbrPierres) {
	vector <int> autresGroupes;
	autresGroupes.push_back(1);
	autresGroupes.push_back(2);
	autresGroupes.push_back(3);
	autresGroupes.push_back(4);
	autresGroupes.push_back(5);
	while (!autresGroupes.empty() && autresGroupes.at(0) <= nbrPierres)
		autresGroupes.erase(autresGroupes.begin());

	int nbrGroupe = 0;

	int ** mat = convertTo2DMatrix(c5, config);

	for (int i = 0; i < (int) c5->nbrLigne; i++) {
		for (int j = 0; j < (int) c5->nbrColonne; j++) {
			int couleur = mat[i][j];
			if (couleur != joueur)
				continue;
			if (j + (nbrPierres - 1) < c5->nbrColonne && lookRight(mat, joueur, i, j, nbrPierres))
				if (!incluAutresGroupesR(c5, mat, joueur, i, j, autresGroupes)) {
					nbrGroupe++;
					if (j > 0 && nbrGroupe != 0 && mat[i][j - 1] == joueur)
						nbrGroupe--;
				}
			if (i + (nbrPierres - 1) < c5->nbrLigne) {

				if (lookDown(mat, joueur, i, j, nbrPierres)) {
					if (!incluAutresGroupesD(c5, mat, joueur, i, j, autresGroupes)) {
						nbrGroupe++;
						if (i > 0 && nbrGroupe != 0 && mat[i - 1][j] == joueur)
							nbrGroupe--;
					}
				}
				if (j + (nbrPierres - 1) < c5->nbrColonne && lookDownRight(mat, joueur, i, j, nbrPierres)) {
					if (!incluAutresGroupesDR(c5, mat, joueur, i, j, autresGroupes)) {
						nbrGroupe++;
						if (i > 0 && j > 0 && nbrGroupe != 0 && mat[i - 1][j - 1] == joueur)
							nbrGroupe--;
					}
				}
				if (j - (nbrPierres - 1) >= 0 && lookDownLeft(mat, joueur, i, j, nbrPierres)) {
					if (!incluAutresGroupesDL(c5, mat, joueur, i, j, autresGroupes)) {
						nbrGroupe++;
						if (i > 0 && j < c5->nbrColonne - 1 && nbrGroupe != 0 && mat[i - 1][j + 1] == joueur)
							nbrGroupe--;
					}
				}
			}
		}
	}
	return nbrGroupe;
}

/**
 * Fonction qui vérifie si un groupe de pierres (1) existe dans une configuration du jeu connect5
 * @param le jeu connect5 : c5
 * @param une configuration du jeu : config
 * @param le numéro du joueur : joueur
 * @param le nombre de pierres à vérifier : nbrPierres
 * @return le nombre de groupe de pierres trouvé
 */
int trouveGroupe1(Connect5 * c5, vector<Noeud> config, int joueur) {
	int flag, nbrGroupe = 0;

	int ** mat = convertTo2DMatrix(c5, config);
	int ** tempMat = new int * [(int) c5->nbrLigne + 2];

	for (int i = 0; i < c5->nbrLigne + 2; i++)
		tempMat[i] = new int[(int) c5->nbrColonne + 2];
	
	for (int i = 0; i < (int) c5->nbrLigne; i++)
		for (int j = 0; j < (int) c5->nbrColonne; j++)
			tempMat[i + 1][j + 1] = mat[i][j];

	for (int i = 1; i < (int) c5->nbrLigne + 1; i++) {
		flag = 8;
		for (int j = 1; j < (int) c5->nbrColonne + 1; j++) {
			int couleur = tempMat[i][j];
			if (couleur != joueur)
				continue;
				
			for (int k = 0; k < 8; k++) {
				if (tempMat[i + directions[k].x][j + directions[k].y] == joueur)
					flag--;
			}
			
			if (flag == 8)
				nbrGroupe++;
		}
	}
	return nbrGroupe;
}

/**
 * Fonction qui calcule le nombre de cases vides dans une configuration
 * @param le jeu connect5 : c5
 * @return le nombre de cases vides 
 */
int getCasesLibres(Connect5 * c5) {
	int nbrCasesVides = 0;
	for (int i = 0; i < c5->configCourante.size(); i++) {
		if (!c5->configCourante.at(i).occupe)
			nbrCasesVides++;
	}
	return nbrCasesVides;
}

/**
 * Fonction qui retourne toutes les actions possibles d'un joueur à un état spécifique
.* Autrement dit, elle place la pierre du joueur courant dans une des cases vides et retourne 
.* toutes les grilles à évaluer
 * @param le jeu connect5 : c5
 * @param le nombre de cases vides : nbrCasesVides
 * @param le numéro du joueur : joueurCourant
 */
void getSuccesseurs(Connect5 * c5, int nbrCasesVides, int joueurCourant) {
	vector<Noeud> temp = c5->configCourante;
	while (nbrCasesVides > 0) {
		vector<Noeud> gen = temp;
		for (int i = 0; i < temp.size(); i++) {
			if (!temp.at(i).occupe && !temp.at(i).utilise) {
				gen.at(i).valeur = joueurCourant;
				gen.at(i).flag = 1;
				temp.at(i).utilise = true;
				c5->configsPossibles.push_back(gen);
				break;
			}
		}
		nbrCasesVides--;
	}
}

/**
 * Fonction qui calcule le score d'un joueur dans une configuration donnée
 * @param les groupes de 1, 2, 3, 4 et 5 pierres noires : tabGroupesNoires
 * @param les groupes de 1, 2, 3, 4 et 5 pierres blanches : tabGroupesBlanches
 * @param le numéro du joueur : joueur
 * @return le score du joueur 
 */
int calculScore(vector<int> tabGroupesNoires, vector<int> tabGroupesBlanches, int joueur) {
	int s1, s2, s3, s4, s5;
	if (joueur == 1) {
		s1 = tabGroupesNoires.at(0) - tabGroupesBlanches.at(0);
		s2 = 10 * (tabGroupesNoires.at(1) - tabGroupesBlanches.at(1));
		s3 = 100 * (tabGroupesNoires.at(2) - tabGroupesBlanches.at(2));
		s4 = 1000 * (tabGroupesNoires.at(3) - tabGroupesBlanches.at(3));
		s5 = 10000 * (tabGroupesNoires.at(4) - tabGroupesBlanches.at(4));
	} else {
		s1 = tabGroupesBlanches.at(0) - tabGroupesNoires.at(0);
		s2 = 10 * (tabGroupesBlanches.at(1) - tabGroupesNoires.at(1));
		s3 = 100 * (tabGroupesBlanches.at(2) - tabGroupesNoires.at(2));
		s4 = 1000 * (tabGroupesBlanches.at(3) - tabGroupesNoires.at(3));
		s5 = 10000 * (tabGroupesBlanches.at(4) - tabGroupesNoires.at(4));
	}
	return s1 + s2 + s3 + s4 + s5;
}

/**
 * Fonction d'évaluation qui permet à chaque joueur de prendre la meilleur 
.* décision d'attaque ou de défense
 * @param le jeu connect5 : c5
 * @param le numéro du joueur : joueurCourant
 * @return un vecteur qui contient le score de chaque successeur(grille à évaluer) avec son 
.* indice dans configsPossibles 
 */
vector<pair<int, int> > fonctionEvaluation(Connect5 * c5, int joueurCourant) {
	vector<int> tabGroupesNoires;
	vector<int> tabGroupesBlanches;
	int groupePierresNoires, groupePierresBlanches, score;
	vector<pair<int, int> > res;
	pair<int, int> scoreConfig;

	for (int k = 0; k < c5->configsPossibles.size(); k++) {
		for (int i = 0; i < 5; i++) {
			if (i == 0) {
				groupePierresNoires = trouveGroupe1(c5, c5->configsPossibles.at(k), noire);
				tabGroupesNoires.push_back(groupePierresNoires);
			} else {
				groupePierresNoires = trouveGroupe(c5, c5->configsPossibles.at(k), noire, i + 1);
				tabGroupesNoires.push_back(groupePierresNoires);
			}
		}
	
		for (int i = 0; i < 5; i++) {
			if (i == 0) {
				groupePierresBlanches = trouveGroupe1(c5, c5->configsPossibles.at(k), blanche);
				tabGroupesBlanches.push_back(groupePierresBlanches);
			} else {
				groupePierresBlanches = trouveGroupe(c5, c5->configsPossibles.at(k), blanche, i + 1);
				tabGroupesBlanches.push_back(groupePierresBlanches);
			}
		}
		score = calculScore(tabGroupesNoires, tabGroupesBlanches, joueurCourant);
		tabGroupesNoires.clear();
		tabGroupesBlanches.clear();
		res.push_back(make_pair(score, k));
	}
	return res;
}

/**
 * Fonction qui sert à trier les grilles à évaluer selon leur score déterminé 
.* par la fonction d'évaluation
 * @param paire score/indice d'une grille à évaluer : a
 * @param paire score/indice d'une grille à évaluer : b
 * @return vrai si la grille 1 a un meilleur score que la grille 2
 */
bool sortbyFirst(const pair<int, int> &a, const pair<int, int> &b) { 
	return (a.first > b.first); 
} 

/**
 * Fonction qui choisi le meilleur successeur entre toutes les grilles déjà évaluées
 * @param le vecteur qui contient le score des sucesseurs avec leurs indices : res
 * @return retourne le score du meilleur successeur avec son indice
 */
pair<int, int> choisirMeilleurSuccesseurs(vector<pair<int, int> > res) {
	sort(res.begin(), res.end(), sortbyFirst);
	return res.at(0);
}

/**
 * Fonction qui retourne la position (x,y) de la pierre à placer
 * @param le jeu connect5 : c5
 * @param l'indice de la grille choisie du vecteur configsPossibles : index
 */
pair <double, double> prochainePosBot(Connect5 * c5, int index) {
	pair <double, double> pos;
	vector<Noeud> meilleurOption = c5->configsPossibles.at(index);
	
	for (int i = 0; i < meilleurOption.size(); i++) {
		if (meilleurOption.at(i).flag == 1) {
			pos = meilleurOption.at(i).pos;
			break;
		}
	}
	return pos;
}

/**
 * Fonction qui applique l'idée derière l'algorithme minimax 
 * @param le jeu connect5 : c5
 * @return l'indice de la grille choisie à partir de toutes les grilles déjà évaluées
 */
int minimax(Connect5 * c5) {
	int joueurAdversaire;
	int joueurCourant = tourJoueur(c5);
	if (joueurCourant == 1) joueurAdversaire = 2;
	if (joueurCourant == 2) joueurAdversaire = 1;
	
	getSuccesseurs(c5, getCasesLibres(c5), joueurCourant);
	vector<pair<int, int> > res1 = fonctionEvaluation(c5, joueurCourant);
	int scoreJoueurCourant = choisirMeilleurSuccesseurs(res1).first;
	int indiceGrilleCourant = choisirMeilleurSuccesseurs(res1).second;
	c5->configsPossibles.clear();
	getSuccesseurs(c5, getCasesLibres(c5), joueurAdversaire);
	vector<pair<int, int> > res2 = fonctionEvaluation(c5, joueurAdversaire);
	int scoreAdversaire = choisirMeilleurSuccesseurs(res2).first;
	int indiceGrilleAdversaire = choisirMeilleurSuccesseurs(res2).second;

	if (scoreAdversaire >= scoreJoueurCourant)
		return indiceGrilleAdversaire;

	return indiceGrilleCourant;	
}

/**
 * Fonction qui réinitialise le jeu pour le prochain tour
 * @param le jeu connect5 : c5
 */
void resetConnect5(Connect5 * c5) {
	delete[] c5->grille;
	c5->configCourante.clear();
	c5->configsPossibles.clear();
	c5->listePierresNoires.clear();
	c5->listePierresBlanches.clear();
}

/**
 * Fonction main
 */
int main(int argc, char * argv[]) {
	msgIntro();
	while (true) {
		Connect5 c5;
		pair <double, double> dimension = lireNbrLigneColonne();
		c5.nbrLigne = dimension.first;
		c5.nbrColonne = dimension.second;
		c5.grille = new int[(int)(c5.nbrLigne * c5.nbrColonne)];
		lectureConfiguration(&c5);		
		c5.tempsReflexion = lectureTempsReflexion();
		c5.nbrPierresNoires = getNbrPierresNoires(&c5);
		c5.nbrPierresBlanches = getNbrPierresBlanches(&c5);
		trouverPierres(&c5, &c5.configCourante, &c5.listePierresNoires, &c5.listePierresBlanches);
		int indexMeilleurGrille = minimax(&c5);
		pair<double, double> posBot = prochainePosBot(&c5, indexMeilleurGrille);
		cout << posBot.first - 1 << " " << posBot.second - 1 << endl;	
		cout.flush();
		resetConnect5(&c5);
	}
}
