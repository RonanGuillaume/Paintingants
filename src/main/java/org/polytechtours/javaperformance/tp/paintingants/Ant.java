package org.polytechtours.javaperformance.tp.paintingants;
// package PaintingAnts_v3;
// version : 4.0

import java.awt.Color;
import java.util.Random;

public class Ant {
  // Tableau des incrémentations à effectuer sur la position des fourmis
  // en fonction de la direction du deplacement
  static private int[][] incDirection = new int[8][2];
  // le generateur aléatoire (Random est thread safe donc on la partage)
  private static Random randomGenerator = new Random();
  // couleur déposé par la fourmi
  private Color colorDeposed;
  private float luminanceFollowingColor;
  // objet graphique sur lequel les fourmis peuvent peindre
  private MyCanvas painting;
  // Coordonées de la fourmi
  private int x, y;
  // Proba d'aller a gauche, en face, a droite, de suivre la couleur
  private float[] proba = new float[4];
  // Numéro de la direction dans laquelle la fourmi regarde
  private int direction;
  // Taille de la trace de phéromones déposée par la fourmi
  private int size;
  // Pas d'incrémentation des directions suivant le nombre de directions
  // allouées à la fourmies
  private int decalDir;
  // l'applet
  private PaintingAnts applies;
  // seuil de luminance pour la détection de la couleur recherchée
  private float luminanceLimit;
  // nombre de déplacements de la fourmi
  private long nbDeplacements;

  /*************************************************************************************************
  */
  public Ant(Color pCouleurDeposee, Color pCouleurSuivie, float pProbaTD, float pProbaG, float pProbaD,
             float pProbaSuivre, MyCanvas pPainting, char pTypeDeplacement, float pInit_x, float pInit_y, int pInitDirection,
             int pTaille, float pSeuilLuminance, PaintingAnts pApplis) {

    colorDeposed = pCouleurDeposee;
    luminanceFollowingColor = 0.2426f * pCouleurDeposee.getRed() + 0.7152f * pCouleurDeposee.getGreen()
        + 0.0722f * pCouleurDeposee.getBlue();
    painting = pPainting;
    applies = pApplis;

    // direction de départ
    direction = pInitDirection;

    // taille du trait
    size = pTaille;

    // initialisation des probas
    proba[0] = pProbaG; // proba d'aller à gauche
    proba[1] = pProbaTD; // proba d'aller tout droit
    proba[2] = pProbaD; // proba d'aller à droite
    proba[3] = pProbaSuivre; // proba de suivre la couleur

    // nombre de directions pouvant être prises : 2 types de déplacement
    // possibles
    if (pTypeDeplacement == 'd') {
      decalDir = 2;
    } else {
      decalDir = 1;
    }

    // initialisation du tableau des directions
    Ant.incDirection[0][0] = 0;
    Ant.incDirection[0][1] = -1;
    Ant.incDirection[1][0] = 1;
    Ant.incDirection[1][1] = -1;
    Ant.incDirection[2][0] = 1;
    Ant.incDirection[2][1] = 0;
    Ant.incDirection[3][0] = 1;
    Ant.incDirection[3][1] = 1;
    Ant.incDirection[4][0] = 0;
    Ant.incDirection[4][1] = 1;
    Ant.incDirection[5][0] = -1;
    Ant.incDirection[5][1] = 1;
    Ant.incDirection[6][0] = -1;
    Ant.incDirection[6][1] = 0;
    Ant.incDirection[7][0] = -1;
    Ant.incDirection[7][1] = -1;

    luminanceLimit = pSeuilLuminance;
    nbDeplacements = 0;
  }

  /*************************************************************************************************
   * Titre : void move() Description : Fonction de deplacement de la fourmi
   *
   */
  public void move() {
    float tirage, prob1, prob2, prob3, total;
    int[] dir = new int[3];
    int i, j;
    Color lCouleur;

    nbDeplacements++;

    dir[0] = 0;
    dir[1] = 0;
    dir[2] = 0;

    // le tableau dir contient 0 si la direction concernée ne contient pas la
    // couleur
    // à suivre, et 1 sinon (dir[0]=gauche, dir[1]=tt_droit, dir[2]=droite)
    i = modulo(x + Ant.incDirection[modulo(direction - decalDir, 8)][0], painting.getLargeur());
    j = modulo(y + Ant.incDirection[modulo(direction - decalDir, 8)][1], painting.getHauteur());
    if (applies.mBaseImage != null) {
      lCouleur = new Color(applies.mBaseImage.getRGB(i, j));
    } else {
      lCouleur = new Color(painting.getCouleur(i, j).getRGB());
    }
    if (testCouleur(lCouleur)) {
      dir[0] = 1;
    }

    i = modulo(x + Ant.incDirection[direction][0], painting.getLargeur());
    j = modulo(y + Ant.incDirection[direction][1], painting.getHauteur());
    if (applies.mBaseImage != null) {
      lCouleur = new Color(applies.mBaseImage.getRGB(i, j));
    } else {
      lCouleur = new Color(painting.getCouleur(i, j).getRGB());
    }
    if (testCouleur(lCouleur)) {
      dir[1] = 1;
    }
    i = modulo(x + Ant.incDirection[modulo(direction + decalDir, 8)][0], painting.getLargeur());
    j = modulo(y + Ant.incDirection[modulo(direction + decalDir, 8)][1], painting.getHauteur());
    if (applies.mBaseImage != null) {
      lCouleur = new Color(applies.mBaseImage.getRGB(i, j));
    } else {
      lCouleur = new Color(painting.getCouleur(i, j).getRGB());
    }
    if (testCouleur(lCouleur)) {
      dir[2] = 1;
    }

    // tirage d'un nombre aléatoire permettant de savoir si la fourmi va suivre
    // ou non la couleur
    tirage = randomGenerator.nextFloat();// Math.random();

    // la fourmi suit la couleur
    if (((tirage <= proba[3]) && ((dir[0] + dir[1] + dir[2]) > 0)) || ((dir[0] + dir[1] + dir[2]) == 3)) {
      prob1 = (dir[0]) * proba[0];
      prob2 = (dir[1]) * proba[1];
      prob3 = (dir[2]) * proba[2];
    }
    // la fourmi ne suit pas la couleur
    else {
      prob1 = (1 - dir[0]) * proba[0];
      prob2 = (1 - dir[1]) * proba[1];
      prob3 = (1 - dir[2]) * proba[2];
    }
    total = prob1 + prob2 + prob3;
    prob1 = prob1 / total;
    prob2 = prob2 / total + prob1;
    prob3 = prob3 / total + prob2;

    // incrémentation de la direction de la fourmi selon la direction choisie
    tirage = randomGenerator.nextFloat();// Math.random();
    if (tirage < prob1) {
      direction = modulo(direction - decalDir, 8);
    } else {
      if (tirage < prob2) {
        /* rien, on va tout droit */
      } else {
        direction = modulo(direction + decalDir, 8);
      }
    }

    x += Ant.incDirection[direction][0];
    y += Ant.incDirection[direction][1];

    x = modulo(x, painting.getLargeur());
    y = modulo(y, painting.getHauteur());

    // coloration de la nouvelle position de la fourmi
    painting.setCouleur(x, y, colorDeposed, size);

    applies.IncrementFpsCounter();
  }

  /*************************************************************************************************
  */
  public long getNbDeplacements() {
    return nbDeplacements;
  }
  /****************************************************************************/

  /*************************************************************************************************
  */
  public int getX() {
    return x;
  }

  /*************************************************************************************************
  */
  public int getY() {
    return y;
  }

  /*************************************************************************************************
   * Titre : modulo Description : Fcontion de modulo permettant au fourmi de
   * reapparaitre de l autre coté du Canvas lorsque qu'elle sorte de ce dernier
   *
   * @param x
   *          valeur
   *
   * @return int
   */
  private int modulo(int x, int m) {
    return (x + m) % m;
  }

  /*************************************************************************************************
   * Titre : boolean testCouleur() Description : fonction testant l'égalité
   * d'une couleur avec la couleur suivie
   *
   */
  private boolean testCouleur(Color pCouleur) {
    boolean lReponse = false;
    float lLuminance;

    /* on calcule la luminance */
    lLuminance = 0.2426f * pCouleur.getRed() + 0.7152f * pCouleur.getGreen() + 0.0722f * pCouleur.getBlue();

    /* test */
    if (Math.abs(luminanceFollowingColor - lLuminance) < luminanceLimit) {
      lReponse = true;
      // System.out.print(x);
    }

    return lReponse;
  }
}
