// Auteur         : Fadi Feghali
// Code permanent : FEGF07069109
// Professeur     : M. Ammar Hamad
// Objectif       : implémentation en C d’un validateur de Sudoku

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <stdbool.h>
#include <math.h>
#include <string.h>


/**
 * Struct qui stocke les données à transmettre aux threads
 */
typedef struct {
	char *matrice;	
} parametres;


// Flag : 1 pour invalide et 0 pour valide
int codeRetour = 0;


/**
 * Fonction qui demande à l'utilisateur le fichier source qui contient les sudokus à valider
 * @return pointeur vers le fichier en question
 */
FILE * validerSource() {
	char intro [] = "Entrez le chemin du fichier qui contient la ou les grille(s) de Sudoku à valider : ";
	printf("%s", intro);
	char sourceFichier[100] = {0};
	scanf("%s", sourceFichier);
	FILE *fp = fopen(sourceFichier, "r");
	while (fp == NULL) {
		fprintf(stderr, "Le fichier fourni n'existe pas, veuillez réessayer\n");
		printf("%s", intro);
		scanf("%s", sourceFichier);
		fp = fopen(sourceFichier, "r");
	}

	return fp;
}


/**
 * Fonctions qui compte le nombre de Sudokus dans le fichier fourni par l'utilisateur
 * @param le fichier en question
 * @return le nombre de Sudokus
 */
int compteurMatrices(FILE *fp) {
	int nombreMatrices = 0;
	char ligne[100];
	char copieLigne[100];
	
	while (fgets(ligne, sizeof(ligne), fp)) {
		if (ligne[0] == '\n' && copieLigne[0] != '\n') 
			nombreMatrices += 1;
		strcpy(copieLigne, ligne);
	}
	
	rewind(fp);
	
	if (ligne[0] != '\n')
		nombreMatrices += 1;
	
	return nombreMatrices;
}


/**
 * Fonction qui sert à extraire le contenu du fichier et de le convertir en tableau de char
 * @param le fichier en question
 * @return un tableau de char qui contient toutes les matrices
 */
char * contenuFichier(FILE *fp) {
	char *contenu  = malloc(1000 * sizeof(char));
	char ligne[100];
	
	while (fgets(ligne, sizeof(ligne), fp)) {
		if (ligne[0] == '\n') {
			strcat(contenu, "*");
		} else { 
			strcat(contenu, ligne);
		}
	}
	
	strcat(contenu, "\n");
	fclose (fp);
	
	return contenu;
}


/**
 * Fonction qui sert à extraire les matrices une à la fois, et les stocker dans un tableau de matrices
 * @param le tableau de char contenant toutes les grilles de Sudoku
 * @return un tableau contenant les différentes grilles de Sudoku
 */
char ** extraireMatrice(char * contenu, int nbrMatrices) { 
	int nbrCaracteres = strlen(contenu);
	char **tableauMatrices = malloc (nbrCaracteres * sizeof(char));
	char *p1, *p2, *temp;
	
	p1 = strtok_r(contenu, "*", &temp);
	tableauMatrices[0] = p1;

	for (int i = 0; i < nbrMatrices; i++) {
		p2 = strtok_r(NULL, "*", &temp);
		if (p2 != NULL)
			tableauMatrices[i + 1] = p2;
	}
	
	return tableauMatrices;
}


/**
 * Fonction qui calcule le nombre de ligne dans une matrice
 * @param la matrice à traiter
 * @return le nombre de ligne
 */
double getLigne(char *matrice) {
	double nbrLigne = 0;
	for (int i = 0; i < strlen(matrice); i++) {
		if (matrice[i] == '\n')
			nbrLigne += 1;
	}

	return nbrLigne;
}


/**
 * Fonction qui calcule le nombre de colonne dans une matrice
 * @param la matrice à traiter 
 * @return le nombre de colonne
 */
double getColonne(char *matrice) {
	double nbrColonne = 0;
	double nbrColonneCopie = 0;
	
	for (int i = 0; i < strlen(matrice); i++) {
		if (matrice[i] == ' ' && matrice[i] != '\n')
			nbrColonne += 1;
		if (matrice[i] == '\n') {
			if (nbrColonne < nbrColonneCopie)
				nbrColonne = nbrColonneCopie;
			nbrColonneCopie = nbrColonne;
			nbrColonne = 0;	
		}
	}
	
	return nbrColonneCopie + 1;
}


/**
 * Fonction qui sert à trouver si un doublon est présent dans un tableau d'entiers
 * @param tableau d'entiers
 * @return le doublon trouvé
 */
int TrouverDoublon(int *tabEntier) { 
	int doublon = 0;
	for(int i = 0; i < 9; i++) 
		for(int j = i + 1; j < 9; j++) 
			if(tabEntier[i] == tabEntier[j]) 
				doublon = tabEntier[i];
	return doublon;			
} 


/**
 * Comparateur utilisé par le qsort dans la fonction verifChiffres
 * @param entier #1
 * @param entier #2
 * @return la différence entre les deux entiers
 */
int compareChiffres(const void * a, const void * b) {
	return (*(int*)a - *(int*)b);
}


/**
 * Fonction qui vérifie que les lignes, colonnes ou les sous-matrices contiennent les chiffres de 1 à 9
 * @param tabCarac  : Un tableau de char contenant les chiffres d'une certaine ligne, colonne ou sous-matrice
 * @param numErreur : Le numéro de ligne, colonne ou de la sous-grille non-confrome
 * @param direction : savoir s'il sagit d'une ligne, colonne ou d'une sous-grille
 */
void verifChiffres(char *tabCarac, double numErreur, const char *direction) {
	int tabEntiers [9];

	for (int i = 0; i < 9; i++) {
		tabEntiers[i] = tabCarac[i] - '0';
	}
	
	qsort(tabEntiers, 9, sizeof(int), compareChiffres);
	
	for (int i = 0; i < 9; i++) {
		if (tabEntiers[i] != i + 1) {
			fprintf(stderr, "\nIl y a un doublon dans %s %0.0f -> %d", direction, numErreur, TrouverDoublon(tabEntiers));
			codeRetour = 1;
			break;
		}
	}
}


/**
 * Fonction qui sert à valider qu'une ligne x contient les chiffres de 1 à 9
 * @param transmettre le paramètre matrice au thread
 * @return fonction générique qui retourne pointeur de type inconnu
 */
void *validerLigne(void* param) {
	
	parametres *params = (parametres*) param;
	char *matrice = params->matrice;

	char *tableauChiffres = malloc(9 * sizeof(char));
	int cpt = 0;
	int cptChiffres = 0;

	for (int i = 0; i < strlen(matrice); i++) {
		if (matrice[i] == '\n') {
			verifChiffres(tableauChiffres, ceil(cptChiffres/9), "la ligne");
			cpt = -1;
		}
		
		if (matrice[i] != ' ') {
			tableauChiffres[cpt] = matrice[i];
			cpt++;
			if (matrice[i] != '\n')
				cptChiffres++;
		}
	}
	pthread_exit(NULL);
}

/**
 * Fonction qui sert à valider qu'une colonne x contient les chiffres de 1 à 9
 * @param transmettre le paramètre matrice au thread
 * @return fonction générique qui retourne pointeur de type inconnu
 */
void *validerColonne(void* param) {
	parametres *params = (parametres*) param;
	char *matrice = params->matrice;
		
	char *tableauChiffres = malloc(9 * sizeof(char));
	int cpt = 0;
	int sautColonne = 0;
	
	for (int i = 0; i < 9; i++) {
		for (int j = sautColonne; cpt < 9; j+= 18) {
			tableauChiffres[cpt] = matrice[j];
			cpt++;
		}
		verifChiffres(tableauChiffres, i + 1, "la colonne");
		sautColonne += 2;
		cpt = 0;
	}
	pthread_exit(NULL);
}

/**
 * Fonction qui sert à valider qu'une grille 3x3 contient les chiffres de 1 à 9
 * @param transmettre le paramètre matrice au thread
 * @return fonction générique qui retourne pointeur de type inconnu
 */
void *valider3x3(void* param) {
	parametres *params = (parametres*) param;
	char *matrice = params->matrice;
		
	char *tableauChiffres = malloc(9 * sizeof(char));
	int cpt = 0;
	int pointeur3x3 = 0;
	
	for (int i = 0; i < 9; i++) {
		if (i == 3) {
			pointeur3x3 = 54;
		} else if (i == 6) {
			pointeur3x3 = 108;
		}	
		for (int j = pointeur3x3; cpt < 9; j+= 18) {
			tableauChiffres[cpt] = matrice[j];
			cpt++;
			if (cpt % 3 == 0 && cpt != 1) {
				pointeur3x3 += 2;	
				j = -18 + pointeur3x3;
			}
		}
		verifChiffres(tableauChiffres, i + 1, "la sous-grille");
		cpt = 0;
	}
	pthread_exit(NULL);
}

/**
 * Fonction qui détecte si un Sudoku contient des caractères invalides (non-entier, caractères spéciaux...)
 * @param matrice    : La matrice à vérifier 
 * @param nbrLigne   : Le nombre de ligne de cette matrice 
 * @param nbrColonne : Le nombre de colonne de cette matrice 
 * @return           : Un Booléen
 */
bool caractereInvalide(char *matrice, double nbrLigne, double nbrColonne) {
	int cpt = 0;
	int cpt1 = 0;
	bool invalide = false;
	for (int i = 0; i < strlen(matrice); i++) {
		if (matrice[i] != ' ' && matrice[i] != '\n') {
			cpt++;
			cpt1++;
		}
		if ((matrice[i] < '1' || matrice[i] > '9') && matrice[i] != ' ' && matrice[i] != '\n') {
			double ligneInvalide = ceil(cpt/nbrLigne);
			double colonneInvalide = nbrColonne - (ceil(cpt/nbrLigne) * nbrColonne - cpt);
			fprintf(stderr, "\nLa case (%0.0f,%0.0f), contient un caractère spécial non admis", ligneInvalide, colonneInvalide);
			invalide = true;
			codeRetour = 1;
		}
		if (matrice[i] == '\n') {
			if (cpt1 < nbrColonne) {
				fprintf(stderr, "\n%0.0f chiffre(s) manque(ent) dans la ligne %0.0f", (nbrColonne - cpt1), ceil(cpt/nbrLigne));
				invalide = true;
				codeRetour = 1;
			} else {
				cpt1 = 0;
			}
		}
	}
	return invalide;
}


/**
 * Fonction main qui vérifie la la conformité de chaque grille de Sudoku du fichier source
.* Chaque thread est assigné à une tâche pour déterminer la validité d'une région particulière de Sudoku. Le résultat sera envoyé au thread parent
.* Pour ce TP j'ai décidé d'utiliser un seul code de retour pour les 3 threads, si codeRetour = 1 -> sudoku invalide.
 */
int main(int argc, char *argv[]) {

	FILE *fp = validerSource();
	int nbrMatrices = compteurMatrices(fp);
	
	////////////////////////////////////////
	pthread_t threads[nbrMatrices * 3];  //
	//////////////////////////////////////
	int nbrThread = 0;                 //
	////////////////////////////////////
	
	char *contenu = contenuFichier(fp);
	char **tableauMatrices = extraireMatrice(contenu, nbrMatrices);
	
	printf("\n------------------ Matrice 1 ------------------\n\n");
	
	// Valider un Sudoku, un à la suite de l'autre
	for (int i = 0; i < nbrMatrices; i++) {
		printf("%s", tableauMatrices[i]);
		
		double nbrLigne = getLigne(tableauMatrices[i]);
		double nbrColonne = getColonne(tableauMatrices[i]);
		
		if (nbrLigne == 9 && nbrColonne == 9) {
			if (!caractereInvalide(tableauMatrices[i], nbrLigne, nbrColonne)) {
				parametres *data = (parametres *) malloc(sizeof(parametres));	
				data->matrice = tableauMatrices[i];	
			//------------------------- Thread 1 --------------------------------//	
				pthread_create(&threads[nbrThread++], NULL, validerLigne, data);
			//------------------------- Thread 2 --------------------------------//	
				pthread_create(&threads[nbrThread++], NULL, validerColonne, data);
			//------------------------- Thread 3 --------------------------------//	
				pthread_create(&threads[nbrThread++], NULL, valider3x3, data);
			}
		} else {
			fprintf(stderr, "\nLa taille de la grille de Sudoku devrait être 9x9");
			codeRetour = 1;
		}

		// Attendre la fin de tous les threads
		for (int i = 0; i < nbrThread; i++) {
			pthread_join(threads[i], NULL);
		}
		
		if (codeRetour == 1) {
			printf("\n\nDésolé! Votre Sudoku est invalide!\n\n");
		} else {
			printf("\n\nBravo! Votre Sudoku est valide!\n\n");
		}
		if (i != nbrMatrices - 1)
			printf("------------------ Matrice %d ------------------\n\n", i + 2);
		codeRetour = 0;
	}

	free(contenu);
	free(tableauMatrices);

	return 0;
}
