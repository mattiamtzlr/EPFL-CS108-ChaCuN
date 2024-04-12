  Le projet ChaCuN     function orgHighlight() { document .querySelectorAll('pre.src') .forEach((el) => { hljs.highlightElement(el); }); } addEventListener('DOMContentLoaded', orgHighlight, false); addEventListener('load', orgHighlight, false);

# Le projet ChaCuN

CS-108

## 1. Introduction

Le projet de cette année consiste à réaliser une version électronique du jeu de société _Chasseurs et cueilleurs_, dérivé du célèbre [_Carcassonne_](https://fr.wikipedia.org/wiki/Carcassonne%20(jeu)). Notre version, qui diffère légèrement de l'originale, se nomme _Chasseurs et cueilleurs au Néolithique_, abrégé _ChaCuN_.

### 1.1. Principe

ChaCuN est conçu pour 2 à 5 joueurs, dont le but est de construire progressivement un paysage préhistorique en plaçant côte à côte des tuiles carrées. Les différentes parties du paysage ainsi construit — forêts, rivières, etc. — peuvent être occupées par des chasseurs, cueilleurs ou pêcheurs, dans le but d'obtenir des points.

Les règles exactes seront progressivement expliquées durant les premières étapes du projet, mais le principe général est le suivant. Une partie débute avec la tuile de départ, visible ci-dessous, qui est placée au centre de la surface de jeu.

![board_00-initial.png](i/board_00-initial.png)

Figure 1 : La tuile de départ, constituant le paysage initial

À leur tour, les joueurs tirent une nouvelle tuile au hasard, qu'ils placent sur la surface de jeu — éventuellement après l'avoir tournée — de manière à ce qu'elle soit voisine d'au moins une tuile déjà posée, et que les bords des tuiles qui se touchent forment un paysage continu.

Par exemple, si la tuile tirée par le premier joueur est celle visible ci-dessous (à droite), il peut la placer à l'est (droite) de la tuile originale, de manière à ce que la forêt du côté ouest (gauche) de cette tuile continue la forêt du côté est (droite) de la tuile de départ.

![board_00-two-tiles.png](i/board_00-two-tiles.png)

Figure 2 : La tuile de départ accompagnée d'une seconde tuile

Cette nouvelle tuile aurait pu être placée de nombreuses autres manières ; par exemple, à l'ouest de la tuile de départ, afin de connecter les deux rivières ; ou encore, au nord de la tuile de départ, après avoir été tournée d'un demi-tour ; et ainsi de suite.

Normalement, chaque joueur ne peut placer qu'une seule tuile durant son tour. Toutefois, si la tuile qu'il pose ferme au moins une forêt contenant un menhir, alors il a le droit de placer une seconde tuile, tirée du tas des « tuiles menhir », distinct du tas normal. Les tuiles menhir sont généralement de plus grande valeur que les autres, et certaines possèdent même un pouvoir spécial, comme nous le verrons ultérieurement.

Après avoir placé une tuile, un joueur peut éventuellement l'occuper au moyen de l'un des 5 pions ou de l'une des 3 huttes qu'il possède. Suivant où ils sont placés, ces pions et huttes jouent différents rôles. Par exemple, un pion placé dans une forêt joue le rôle d'un cueilleur.

Les occupants — pions et huttes — permettent aux joueurs de remporter des points à différents moments de la partie. Par exemple, lorsqu'une forêt est totalement fermée, le ou les joueurs possédant le plus de cueilleurs dans cette forêt remportent un nombre de points qui dépend de la taille de la forêt. Une fois les points comptabilisés, tous les cueilleurs présents dans une forêt fermée sont retournés à leurs propriétaires, qui peuvent les réutiliser ultérieurement pour occuper d'autres tuiles.

En plus des forêts, les autres éléments du paysage qu'il est possible d'occuper sont :

*   les rivières, qui peuvent être occupées par des pions jouant le rôle de pêcheurs,
*   les prés, qui peuvent être occupés par des pions jouant le rôle de chasseurs,
*   les réseaux hydrographiques — constitués de rivières reliées entre elles par des lacs — qui peuvent être occupés par des huttes de pêcheurs.

Les rivières fonctionnent comme les forêts, dans le sens où dès que l'une d'entre elles est terminée aux deux bouts — soit par un lac, soit par un autre élément de paysage — le ou les joueurs y possédant le plus grand nombre de pêcheurs remportent des points, et tous les occupants de la rivière sont retournés à leur propriétaire.

Les prés et les réseaux hydrographiques, par contre, ne rapportent des points à leurs chasseurs et pêcheurs respectifs qu'au moment où la partie se termine, c.-à-d. que la dernière tuile a été posée. Les chasseurs et huttes de pêcheurs posés restent donc à leur place jusqu'à la fin de la partie et ne peuvent pas être réutilisés.

### 1.2. Programme

L'image ci-dessous montre ce à quoi devrait ressembler l'interface graphique de ChaCuN jeu à la fin du projet. On y voit une partie en cours entre cinq joueurs, et les différents éléments de l'interface graphique ont été numérotés de 1 à 5 pour faciliter la description qui suit.

[![chacun-gui.jpg](i/chacun-gui.jpg)](i/chacun-gui.jpg)

Figure 3 : Une partie de ChaCuN (cliquer pour agrandir)

Sur la gauche de l'interface se trouve le plateau de jeu (1) sur lequel les tuiles sont placées. Les cases colorées en violet sont celles sur lesquelles la prochaine tuile pourrait être déposée, et elles sont coloriées de la couleur correspondant à la personne qui doit poser la tuile (Pauline).

Sur la droite de l'interface se trouvent différentes informations. Tout en haut (2), les joueurs, le nombre de points qu'ils ont déjà acquis et les pions et huttes qui leur restent en main sont visibles. Le joueur courant est entouré.

Des messages donnant des informations sur le déroulement de la partie sont affichés au centre (3). Ils permettent par exemple de savoir qu'un joueur a gagné un certain nombre de points à un moment donné, et pourquoi.

Les deux tas de tuiles sont visibles au-dessous (4), celui des tuiles normales à gauche, celui des tuiles menhir à droite. Le nombre affiché sur chacun des tas est celui des cartes restantes.

Finalement, en bas, la tuile à poser est affichée en grand (5).

## 2. Organisation

Le projet se fait par groupes de 2 personnes au maximum. La formation de ces groupes est libre et peut changer au cours du semestre, pour peu que les directives concernant le plagiat soient respectées. En particulier, si deux personnes ayant travaillé en commun se séparent, elles doivent se partager le code et ne peuvent chacune l'emporter en totalité de leur côté.

La mise en œuvre du projet est découpée en 12 étapes hebdomadaires, regroupées en trois parties :

1.  les étapes 1 à 6,
2.  les étapes 7 à 11,
3.  l'étape 12, qui est optionnelle.

La première partie est très guidée, afin de vous permettre de bien démarrer, tandis que la seconde l'est moins, la troisième étant (presque) totalement libre.

## 3. Notation

Un total de 500 points est attribué durant le semestre, répartis ainsi :

*   projet : 300 points,
*   examen intermédiaire : 75 points,
*   examen final : 125 points.

Les 300 points attribués au projet sont répartis de la manière suivante :

*   rendus testés : 90 points (18 points par rendu),
*   rendu intermédiaire : 80 points,
*   rendu final : 110 points,
*   test final : 20 points.

Un rendu testé est un rendu qui est évalué automatiquement au moyen de tests. Il y a cinq rendus de ce type au cours du semestre, un pour chacune des étapes 2 à 6. Le nombre de points obtenus à un rendu testé est proportionnel au nombre de tests passés avec succès.

Le rendu intermédiaire, qui concerne les étapes 1 à 6, et le rendu final, qui concerne les étapes 7 à 11, sont quant à eux évalués par lecture de votre code, et les points attribués en fonction de la qualité du programme. L'efficacité, la concision et l'élégance du code sont prises en compte dans cette évaluation.

Le test final consiste en un test non automatisé — contrairement au test des étapes 2 à 6 — du bon fonctionnement du projet terminé.

  Mise en place     function orgHighlight() { document .querySelectorAll('pre.src') .forEach((el) => { hljs.highlightElement(el); }); } addEventListener('DOMContentLoaded', orgHighlight, false); addEventListener('load', orgHighlight, false);

# Mise en place

ChaCuN – étape 1

## 1. Introduction

Cette première étape du projet ChaCuN a pour but de définir un certain nombre de classes représentant les principaux éléments du jeu : tuiles, animaux, pions, huttes, etc.

Comme toutes les descriptions d'étapes, celle-ci commence par une introduction aux concepts nécessaires à sa réalisation (§[2](#sec/concepts)), suivie d'une présentation de leur mise en œuvre en Java (§[3](#sec/implementation)).

Si vous travaillez en groupe, vous êtes toutefois priés de lire, avant de continuer, les guides [_Travailler en groupe_](../g/group-work.html) et [_Synchroniser son travail_](../g/sync.html), qui vous aideront à bien vous organiser.

## 2. Concepts

### 2.1. Tuiles

Une **tuile** (_tile_ en anglais) est une petite carte carrée sur laquelle figurent différentes sortes de terrains : prés, forêts, rivières ou lacs. Comme expliqué dans l'introduction, les tuiles sont progressivement assemblées au cours de la partie pour former un paysage de plus en plus grand.

ChaCuN comporte un total de 95 tuiles, de 3 différentes sortes : une tuile de départ, 78 tuiles « normales » et 16 tuiles « menhir ». Chacune de ces tuiles est identifiée par un nombre compris entre 0 et 94. Par exemple, la tuile de départ est identifiée par le nombre 56[1](#fn.1).

### 2.2. Zones

Chaque tuile est composée d'une ou de plusieurs **zones** (_zones_), qui sont des régions connexes d'un même type. Chaque zone peut être :

1.  un **pré** (_meadow_), qui peut contenir des animaux, ou
2.  une **forêt** (_forest_), qui peut contenir des menhirs ou des groupes de champignons, ou
3.  une **rivière** (_river_), qui peut contenir des poissons, ou
4.  un **lac** (_lake_), qui peut également contenir des poissons.

Les lacs ont la particularité de se trouver à l'intérieur des tuiles, c.-à-d. qu'ils ne sont jamais en contact avec l'un des bords. Les autres types de zones, par contre, sont toujours en contact avec au moins un bord de la tuile.

Même si les lacs ne sont jamais en contact avec un bord de tuile, ils sont toujours connectés à au moins une rivière qui est, elle, en contact avec l'un d'entre eux.

Le concept de zone peut être illustré au moyen de la tuile de départ, qui en comporte cinq au total, dont les limites sont mises en évidence en rouge sur l'image ci-dessous.

![zones_56;128.png](i/zones_56;128.png)

Figure 1 : Les cinq zones de la tuile 56 (tuile de départ)

En partant de la zone touchant le bord nord et en se déplaçant ensuite dans le sens des aiguilles d'une montre, ces cinq zones sont :

1.  un pré qui touche le bord nord et constitue également le second pré entourant la rivière du bord ouest,
2.  une forêt, qui touche les bords est et sud,
3.  un petit pré dénué d'animaux, qui constitue le premier pré entourant la rivière du bord ouest,
4.  une rivière qui touche le bord ouest,
5.  un lac connecté à cette rivière.

Les zones d'une tuile sont numérotées à partir de 0 en parcourant les bords de la même manière que ci-dessus — c.-à-d. dans le sens nord, est, sud, ouest. Comme aucune tuile ne comporte plus de 8 zones distinctes en contact avec un bord, ces zones sont numérotées de 0 à 7, et les numéros 8 et 9 sont réservés aux éventuels lacs — ce qui suffit car aucune tuile ne comporte plus de deux lacs. Lorsqu'une tuile comporte deux lacs, le numéro 8 est attribué à celui connecté à la rivière ayant le plus petit numéro.

Ainsi, le pré de la tuile de départ contenant l'animal porte le numéro 0, la forêt le numéro 1, le petit pré le numéro 2, la rivière le numéro 3, et le lac le numéro 8.

Un identifiant unique est attribué à chaque zone de chaque tuile, obtenu en multipliant par 10 l'identifiant de la tuile et en y ajoutant le numéro de la zone. Ainsi, la forêt de la tuile de départ possède l'identifiant 561.

La manière dont cet identifiant unique est construit permet de déterminer — par une simple division entière — le numéro de la tuile à laquelle appartient une zone, ce qui est souvent utile.

#### 2.2.1. Prés

Un pré peut contenir un ou plusieurs animaux, chacun d'entre eux pouvant être :

*   un **mammouth** (_mammoth_), ou
*   un **auroch** (_aurochs_)[2](#fn.2), ou
*   un **cerf** (_deer_), ou
*   un **smilodon**, aussi appelé « tigre à dents de scie » (_saber-toothed tiger_).

Comme nous le verrons ultérieurement, ces animaux rapportent des points aux joueurs ayant le plus grand nombre de chasseurs dans le pré.

Les quatre types d'animaux existants sont visibles sur les tuiles ci-dessous où se trouvent respectivement, de gauche à droite, un mammouth, un auroch, deux cerfs (un mâle couché, une femelle debout) et un smilodon.

![board_01-animals.png](i/board_01-animals.png)

Figure 2 : Animaux des prés : mammouths, aurochs, cerfs et smilodons

Les animaux d'un pré sont numérotés 0 ou 1, car aucun pré ne possède plus de deux animaux. Lorsque deux animaux de type différent sont présents dans un même pré, ils sont numérotés en fonction de leur type, en commençant par les mammouths, puis les aurochs, les cerfs et enfin les smilodons. Si deux animaux d'un même type occupent un pré — comme sur la troisième tuile de la figure [2](#org818b555) — ils sont numérotés arbitrairement.

Un identifiant unique est attribué à chaque animal de chaque pré, obtenu en multipliant par 10 l'identifiant du pré auquel il appartient et en y ajoutant le numéro de l'animal — 0 ou 1. Ainsi, l'auroch de la tuile initiale est identifié par le numéro 5600.

Une fois encore, la manière dont cet identifiant est construit permet d'obtenir facilement l'identifiant du pré auquel appartient un animal dont on connaît l'identifiant. Bien entendu, il est ensuite possible d'obtenir l'identifiant de la tuile sur laquelle se trouve l'animal au moyen de l'identifiant de son pré.

#### 2.2.2. Forêts

Une forêt peut contenir un **menhir** (_menhir_) ou un **groupe de champignons** (_mushroom group_), mais jamais les deux à la fois. La présence d'un menhir dans une forêt permet au joueur qui la termine de jouer une seconde tuile durant son tour, tandis que la présence d'un groupe de champignons rapporte des points supplémentaires aux cueilleurs majoritaires de la forêt.

Les trois types de forêts existants sont visibles sur les tuiles ci-dessous qui, de gauche à droite, comportent respectivement une forêt vide, une forêt contenant des menhirs, et une forêt contenant un groupe de champignons.

![board_01-forests.png](i/board_01-forests.png)

Figure 3 : Forêt vide, avec menhir et avec un groupe de champignons

#### 2.2.3. Zones aquatiques

Les zones aquatiques — rivières et lacs — peuvent contenir un certain nombre de poissons, qui permettent aux joueurs d'obtenir des points. Les deux tuiles ci-dessous montrent respectivement une rivière contenant un poisson et un lac en contenant deux.

![board_01-fishes.png](i/board_01-fishes.png)

Figure 4 : Un poisson dans une rivière, et deux dans un lac

#### 2.2.4. Pouvoirs spéciaux

Six zones, toutes situées sur des tuiles menhir, possèdent un **pouvoir spécial** (_special power_). Leur fonctionnement détaillé sera décrit dans une étape ultérieure, mais un bref résumé — qui utilise parfois des concepts qui n'ont pas encore été introduits — est donné ci-dessous.

Trois pouvoirs spéciaux ont un effet immédiat, c.-à-d. que le joueur posant une tuile contenant une zone dotée d'un tel pouvoir spécial peut immédiatement effectuer une action, qui lui rapporte éventuellement des points. Ces pouvoirs sont :

*   le **chaman** (_shaman_), qui permet au joueur qui le pose de récupérer, s'il le désire, l'un de ses pions,
*   la **pirogue** (_logboat_), qui rapporte au joueur qui la pose un nombre de points dépendant du nombre de lacs accessibles à la pirogue,
*   la **fosse à pieux** (_hunting trap_), qui rapporte au joueur qui la pose un nombre de points dépendant des animaux présents sur les tuiles voisines.

Les tuiles contenant ces trois pouvoirs spéciaux sont visibles ci-dessous.

![board_01-chaman-logboat-trap.png](i/board_01-chaman-logboat-trap.png)

Figure 5 : Le chaman, la pirogue et la fosse à pieux

Les trois autres pouvoirs spéciaux n'ont pas d'effet immédiat mais influencent le décompte final des points. Il s'agit de :

*   la **grande fosse à pieux** (_pit trap_), qui rapporte aux chasseurs majoritaires du pré la contenant des points supplémentaires pour les animaux présents sur les tuiles voisines de la fosse,
*   le **feu** (_wild fire_), qui fait fuir tous les smilodons du pré qui le contient, évitant ainsi qu'ils ne dévorent des cerfs,
*   le **radeau** (_raft_), qui rapporte aux pêcheurs majoritaires du réseau hydrographique le contenant des points additionnels dépendant du nombre de lacs qu'il contient.

Les tuiles contenant ces trois pouvoirs spéciaux sont visibles ci-dessous.

![board_01-trap-fire-raft.png](i/board_01-trap-fire-raft.png)

Figure 6 : La grande fosse à pieux, le feu et le radeau

### 2.3. Occupants

Lorsqu'un joueur pose une tuile, il a également la possibilité de déposer un **occupant** (_occupant_) sur l'une des zones de la tuile.

Il y a deux sortes d'occupants, les **pions** (_pawns_) et les **huttes** (_huts_), qui jouent différents rôles en fonction du type de la zone sur laquelle ils sont placés :

*   un pion placé dans un pré est un **chasseur** (_hunter_),
*   un pion placé dans une forêt est un **cueilleur** (_gatherer_),
*   un pion placé dans une rivière est un **pêcheur** (_fisher_),
*   une hutte placée dans une zone aquatique est une **hutte de pêcheur** (_fisher hut_).

Ces occupants sont les seuls qu'il est valide de placer. Ainsi, il n'est pas autorisé de placer une hutte dans une forêt, ou un pion dans un lac.

Visuellement, les pions — indépendamment du rôle qu'ils jouent — sont représentés par des personnages portant une lance, tandis que les huttes sont représentées par des maisonnettes. Les occupants sont représentés de profil et coloriés en fonction du joueur auquel ils appartiennent (rouge, bleu, vert, jaune ou pourpre).

![pawns-huts.svg](i/pawns-huts.svg)

Figure 7 : Pions et huttes des différents joueurs

Les occupants permettent aux joueurs d'obtenir des points à différents moments de la partie, comme nous le verrons lors d'une étape ultérieure.

### 2.4. Plateau de jeu

Le jeu se joue sur un plateau carré comportant 25×25 cases, chacune d'entre elles pouvant accueillir une tuile. La case centrale du plateau est occupée par la tuile de départ.

Les cases du plateau sont identifiées par deux coordonnées, la première (x) identifiant la colonne, la seconde (y) identifiant la ligne. La case centrale, qui contient la tuile de départ, a les coordonnées (0, 0), tandis que ses quatre voisines ont les coordonnées suivantes :

*   celle du nord : (0, -1),
*   celle de l'est : (1, 0),
*   celle du sud : (0, 1), et
*   celle de l'ouest : (-1, 0).

En d'autres termes, les deux coordonnées augmentent dans le sens de lecture — de gauche à droite et de haut en bas.

### 2.5. Calcul des points

La manière exacte dont les points sont attribués aux joueurs sera décrite ultérieurement. Toutefois, la mise en œuvre de cette étape nécessite de déjà connaître le nombre de points obtenus dans différentes situations.

Souvent, les points obtenus le sont par les **occupants majoritaires** (_majority occupants_) d'un élément du paysage — pré, forêt, etc. Il s'agit du ou des joueurs possédant le plus grand nombre d'occupants dans cet élément du paysage, p. ex. le plus grand nombre de cueilleurs dans une forêt.

Ces points peuvent être obtenus en cours de partie, ou à la fin, comme décrit dans les deux sections suivantes. Certains termes utilisés, comme celui de « réseau hydrographique », ne seront expliqués en détail que dans une étape ultérieure, mais cela ne pose pas de problème pour la mise en œuvre de celle-ci.

#### 2.5.1. En cours de partie

Lorsqu'une forêt est fermée, les cueilleurs majoritaires remportent 2 points par tuile composant la forêt, et 3 points par groupe de champignons qu'elle contient.

Lorsqu'une rivière est fermée, les pêcheurs majoritaires remportent 1 point par tuile composant la rivière, et 1 point par poisson nageant dans la rivière elle-même ou dans l'un des éventuels lacs aux extrémités.

Lorsqu'un joueur pose la tuile contenant la pirogue, il obtient 2 points par lac du réseau hydrographique dont elle fait partie.

#### 2.5.2. En fin de partie

Les chasseurs majoritaires d'un pré remportent 3 points par mammouth, 2 par auroch et 1 par cerf qu'il contient. Toutefois, avant que les animaux ne soient comptés, chaque smilodon présent dans le pré dévore l'un de ses cerfs, qui ne rapporte alors aucun point.

Les propriétaires des huttes majoritaires d'un réseau hydrographique remportent 1 point par poisson présent dans ce réseau. De plus, si ce réseau contient le radeau, ils remportent également 1 point par lac qu'il contient.

## 3. Mise en œuvre Java

Les concepts importants pour cette étape ayant été introduits, il est temps de décrire leur mise en œuvre en Java. En plus des classes spécifiques à cette étape, une classe « utilitaire » est à réaliser : `Preconditions`, qui offre une méthode de validation d'argument.

Toutes les classes et interfaces de ce projet appartiendront au paquetage `ch.epfl.chacun` — qui sera désigné par le terme **paquetage principal** dans les descriptions d'étapes — ou à l'un de ses sous-paquetages.

Attention : jusqu'à l'étape 6 du projet (incluse), vous devez suivre à la lettre les instructions qui vous sont données dans l'énoncé, et **vous n'avez pas le droit d'apporter la moindre modification à l'interface publique des classes, interfaces et types énumérés décrits**.

En d'autres termes, vous ne pouvez pas ajouter des classes, interfaces ou types énumérés publics à votre projet, ni ajouter des attributs ou méthodes publics aux classes, interfaces et types énumérés décrits dans l'énoncé. Vous pouvez par contre définir des méthodes et attributs privés si cela vous semble judicieux.

Cette restriction sera levée dès l'étape 7.

### 3.1. Installation de Java et d'IntelliJ

Avant de commencer à programmer, il faut vous assurer que la version 21 de Java est bien installée sur votre ordinateur, car c'est celle qui sera utilisée pour ce projet.

Si vous ne l'avez pas encore installée, rendez-vous sur [le site Web du projet Adoptium](https://adoptium.net/), téléchargez le programme d'installation correspondant à votre système d'exploitation (macOS, Linux ou Windows), et exécutez-le.

Cela fait, si vous n'avez pas encore installé IntelliJ, téléchargez la version **Community Edition** — et **pas** la version Ultimate qui apparaît au sommet de la page — depuis [le site de JetBrains](https://www.jetbrains.com/idea/download/) et installez-la.

### 3.2. Importation du squelette

Une fois Java et IntelliJ installés, vous pouvez télécharger [le squelette de projet que nous mettons à votre disposition](f/chacun_skeleton.zip). Il s'agit d'une archive Zip dont vous devrez tout d'abord extraire le contenu à un emplacement de votre choix sur votre ordinateur — notez que certains navigateurs comme Safari extraient automatiquement le contenu de telles archives.

Une fois le contenu de l'archive extrait, vous constaterez qu'il se trouve en totalité dans un dossier nommé `ChaCuN`. Lancez IntelliJ, choisissez _Open_, sélectionnez ce dossier, et finalement cliquez _Ok_.

Le dossier `ChaCuN` contient les sous-dossiers suivants :

*   `resources`, destiné à contenir les « ressources » utiles au projet (images, etc.), et qui ne contient pour l'instant que les images des tuiles,
*   `src`, destiné à contenir le code source de votre projet, ainsi que divers fichiers que nous mettrons à votre disposition, et qui contient pour l'instant :
    *   `SignatureChecks_1.java`, un fichier de vérification de signatures pour l'étape 1, qui ne devrait plus contenir d'erreur lorsque vous aurez terminé la rédaction de cette étape,
    *   `Submit.java`, un programme qui vous permettra de rendre votre projet à la fin de chaque semaine,
*   `test`, destiné à contenir le code des tests unitaires de votre projet que nous vous fournirons ou que vous écrirez vous-même, et qui contient pour l'instant les tests de l'étape 1 — fournis exceptionnellement pour faciliter votre démarrage.

Le dossier `resources` doit être marqué comme « dossier de ressources » pour qu'il soit correctement géré par IntelliJ. Pour cela, faites un clic droit sur lui puis sélectionnez _Mark Directory As_ puis _Resources Root_. Vérifiez ensuite que le panneau _Project_ d'IntelliJ ressemble à l'image ci-dessous.

![intellij-project-skeleton;32.png](i/intellij-project-skeleton;32.png)

Figure 8 : Projet IntelliJ après importation du squelette

### 3.3. Classe `Preconditions`

Fréquemment, les méthodes d'un programme exigent que leurs arguments satisfassent certaines conditions. Par exemple, une méthode déterminant la valeur maximale d'un tableau d'entiers exige que ce tableau contienne au moins un élément.

De telles conditions sont souvent appelées **préconditions** (_preconditions_) car elles doivent être satisfaites _avant_ l'appel d'une méthode : c'est à l'appelant de s'assurer qu'il n'appelle la méthode qu'avec des arguments valides.

En Java, la convention veut que chaque méthode vérifie, autant que possible, ses préconditions et lève une exception — souvent [`IllegalArgumentException`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/IllegalArgumentException.html) — si l'une d'entre elles n'est pas satisfaite. Par exemple, une méthode `max` calculant la valeur maximale d'un tableau d'entiers, et exigeant logiquement que celui-ci contienne au moins un élément, pourrait commencer ainsi :

int max(int\[\] array) {
  if (! (array.length > 0))
    throw new IllegalArgumentException();
  // … reste du code
}

(Notez au passage que la méthode `max` ne déclare _pas_ qu'elle lève potentiellement `IllegalArgumentException` au moyen d'une clause `throws`, car cette exception est de type _unchecked_.)

La première classe à réaliser dans le cadre de ce projet a pour but de faciliter l'écriture de telles préconditions. En l'utilisant, la méthode ci-dessus pourrait être simplifiée ainsi :

int max(int\[\] array) {
  Preconditions.checkArgument(array.length > 0);
  // … reste du code
}

Cette classe est nommée `Preconditions` et appartient au paquetage principal. Elle est publique et finale et n'offre rien d'autre que la méthode `checkArgument` décrite plus bas. Elle a toutefois la particularité d'avoir un constructeur par défaut privé :

public final class Preconditions {
  private Preconditions() {}
  // … méthodes
}

Le but de ce constructeur privé est de rendre impossible la création d'instances de la classe, puisque cela n'a clairement aucun sens — elle ne sert que de conteneur à une méthode statique. Dans la suite du projet, nous définirons plusieurs autres classes du même type, que nous appellerons dès maintenant classes **non instanciables**.

La méthode publique (et statique) offerte par la classe `Preconditions` est :

*   `void checkArgument(boolean shouldBeTrue)`, qui lève l'exception `IllegalArgumentException` si son argument est faux, et ne fait rien sinon.

### 3.4. Type énuméré `PlayerColor`

Le type énuméré `PlayerColor` du paquetage principal, public, énumère les couleurs associées aux joueurs et qui sont, dans l'ordre :

*   `RED`, pour le joueur de couleur rouge,
*   `BLUE`, pour le joueur de couleur bleue,
*   `GREEN`, pour le joueur de couleur verte,
*   `YELLOW`, pour le joueur de couleur jaune,
*   `PURPLE`, pour le joueur de couleur pourpre.

En plus de ces valeurs, le type énuméré `PlayerColor` offre l'attribut public, statique et final suivant :

*   `List<PlayerColor> ALL`, une liste immuable contenant la totalité des valeurs du type énuméré, dans leur ordre de définition (voir les conseils de programmation ci-dessous).

#### 3.4.1. Conseils de programmation

La définition de l'attribut `ALL` peut vous poser quelques problèmes, car le concept de liste, représenté en Java par l'interface [`List`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/List.html), n'a pas encore été vu au cours. Toutefois, vous avez vu au premier semestre le concept de _tableau dynamique_ ([`ArrayList`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ArrayList.html)), qui est une sorte de liste. Pour l'instant, vous pouvez donc admettre que le type `List` est synonyme du type `ArrayList`, et que les méthodes offertes par une valeur de type `ArrayList` le sont aussi par une valeur de type `List`.

Sachant cela, il n'est pas très difficile de définir l'attribut `ALL` en s'aidant des deux méthodes statiques suivantes :

1.  la méthode `values` définie pour tous les types énumérés et retournant un tableau contenant les éléments du type énuméré dans leur ordre de définition,
2.  la méthode [`of`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/List.html#of(E...)) de l'interface `List`, qui retourne une liste immuable ayant les mêmes éléments que le tableau qu'on lui passe en argument.

L'extrait de programme ci-dessous, dont vous pouvez vous inspirer, montre comment la méthode `of` de `List` peut être utilisée pour obtenir une liste (`seasons`) des noms de saisons stockés dans un tableau (`seasonsArray`) :

String\[\] seasonsArray = new String\[\] {
  "printemps", "été", "automne", "hiver"
};
List<String> seasons = List.of(seasonsArray);

### 3.5. Type énuméré `Rotation`

Le type énuméré `Rotation` du paquetage principal, public, énumère les quatre rotations qu'il est possible d'appliquer à une tuile avant de la poser sur le plateau. Ces rotations sont, dans l'ordre :

*   `NONE`, qui correspond à une rotation nulle,
*   `RIGHT`, qui correspond à une rotation d'un quart de tour vers la droite (sens horaire),
*   `HALF_TURN`, qui correspond à une rotation d'un demi-tour,
*   `LEFT`, qui correspond à une rotation d'un quart de tour vers la gauche (sens antihoraire).

Tout comme `PlayerColor`, `Rotation` offre un attribut public, statique et final nommé `ALL` contenant la liste (immuable) de toutes les valeurs du type énuméré, dans l'ordre de définition. Elle offre de plus l'attribut public, statique et final suivant :

*   `COUNT`, qui contient le nombre d'éléments du type énuméré, qui est égal à la longueur de la liste `ALL`, c.-à-d. `ALL.size()`.

Finalement, `Rotation` offre également les méthodes publiques suivantes :

*   `Rotation add(Rotation that)`, qui retourne la somme de la rotation représentée par le récepteur (`this`) et l'argument (`that`),
*   `Rotation negated()`, qui retourne la négation de la rotation représentée par le récepteur — c.-à-d. la rotation qui, ajoutée au récepteur au moyen de `add`, produit la rotation nulle (`NONE`),
*   `int quarterTurnsCW()`, qui retourne le nombre de quarts de tours correspondant au récepteur (0, 1, 2 ou 3), dans le sens horaire — le suffixe `CW` signifie _clockwise_,
*   `int degreesCW()`, qui retourne l'angle correspondant au récepteur, en degrés, dans le sens horaire (0°, 90°, 180° ou 270°).

Notez bien que les méthodes `quarterTurnsCW` et `degreesCW` doivent retourner l'une des quatre valeurs mentionnées dans leur description, même si d'autres valeurs seraient mathématiquement équivalentes — comme –90° au lieu de 270°.

#### 3.5.1. Conseils de programmation

Souvenez-vous qu'en Java, tous les types énumérés possèdent une méthode [`ordinal`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Enum.html#ordinal()) qui retourne la position de l'élément auquel on l'applique dans son type énuméré. Par exemple, appliquée à `NONE`, cette méthode retourne 0, appliquée à `RIGHT` elle retourne 1, et ainsi de suite.

L'existence de cette méthode, et l'ordre choisi pour définir les éléments de `Rotation`, rendent triviale la définition des méthodes `quarterTurnsCW` et `degreesCW`.

De plus, combinée aux constantes `ALL` et `COUNT`, ainsi qu'à l'opérateur de reste de la division entière de Java (`%`), `ordinal` simplifie aussi grandement la définition des méthodes `add` et `negated`.

### 3.6. Type énuméré `Direction`

Le type énuméré `Direction` du paquetage principal, public, énumère les directions correspondant aux quatre points cardinaux. Dans l'ordre, il s'agit de :

*   `N`, qui correspond au nord (haut de l'écran),
*   `E`, qui correspond à l'est (droite de l'écran),
*   `S`, qui correspond au sud (bas de l'écran),
*   `W`, qui correspond à l'ouest (gauche de l'écran).

De plus, `Direction` possède des attributs `ALL` et `COUNT` similaires à ceux de `Rotation`, ainsi que les méthodes publiques suivantes :

*   `Direction rotated(Rotation rotation)`, qui retourne la direction correspondant à l'application de la rotation donnée au récepteur — p. ex. `N.rotated(RIGHT)` retourne `E`,
*   `Direction opposite()`, qui retourne la direction opposée à celle du récepteur.

### 3.7. Classe `Points`

La classe `Points` du paquetage principal, finale et non instanciable, offre des méthodes statiques permettant de calculer les points obtenus dans différentes situations. Il s'agit de :

*   `int forClosedForest(int tileCount, int mushroomGroupCount)`, qui retourne le nombre de points obtenus par les cueilleurs majoritaires d'une forêt fermée constituée de `tileCount` tuiles et comportant `mushroomGroupCount` groupes de champignons,
*   `int forClosedRiver(int tileCount, int fishCount)`, qui retourne le nombre de points obtenus par les pêcheurs majoritaires d'une rivière fermée constituée de `tileCount` tuiles et dans laquelle nagent `fishCount` poissons,
*   `int forMeadow(int mammothCount, int aurochsCount, int deerCount)`, qui retourne le nombre de points obtenus par les chasseurs majoritaires d'un pré comportant `mammothCount` mammouths, `aurochsCount` aurochs et `deerCount` cerfs — les cerfs dévorés par des smilodons n'étant pas inclus dans `deerCount`,
*   `int forRiverSystem(int fishCount)`, qui retourne le nombre de points obtenus par les pêcheurs majoritaires d'un réseau hydrographique dans lequel nagent `fishCount` poissons,
*   `int forLogboat(int lakeCount)`, qui retourne le nombre de points obtenus par le joueur déposant la pirogue dans un réseau hydrographique comportant `lakeCount` lacs,
*   `int forRaft(int lakeCount)`, qui retourne le nombre de points supplémentaires obtenus par les pêcheurs majoritaires du réseau hydrographique contenant le radeau et comportant `lakeCount` lacs.

Toutes ces méthodes lèvent `IllegalArgumentException` si leurs arguments ne satisfont pas les conditions suivantes :

*   un nombre de tuiles (`tileCount`) est toujours strictement supérieur à 1,
*   un nombre de lacs (`lakeCount`) est toujours strictement supérieur à 0,
*   un nombre de poissons (`fishCount`), d'animaux des prés (`mammothCount`, etc.) ou de groupes de champignons est toujours supérieur ou égal à 0.

Bien entendu, cette validation des arguments se fait au moyen de la méthode `checkArgument` de `Preconditions`.

### 3.8. Enregistrements Java

Avant de décrire `Occupant`, la prochaine classe à mettre en œuvre pour cette étape, il convient de décrire le concept de **classe enregistrement** (_record class_), ou simplement **enregistrement** (_record_), introduit dans la version 17 de Java.

Un enregistrement est un type particulier de classe qui peut se définir au moyen d'une syntaxe plus concise qu'une classe normale. Dans cette syntaxe, le mot-clef `class` est remplacé par `record` et les attributs de la classe sont donnés entre parenthèses, après son nom.

Par exemple, un enregistrement nommé `Complex` représentant un nombre complexe dont les attributs sont sa partie réelle `re` et sa partie imaginaire `im` peut se définir ainsi :

public record Complex(double re, double im) { }

Cette définition est équivalente à celle d'une classe finale (!) dotée de :

*   deux attributs privés et finaux nommés `re` et `im`,
*   un constructeur prenant en argument la valeur de ces attributs et les initialisant,
*   des méthodes d'accès (_getters_) publics nommés `re()` et `im()` pour ces attributs,
*   une méthode `equals` retournant vrai si et seulement si l'objet qu'on lui passe est aussi une instance de `Complex` et que ses attributs sont égaux à ceux de `this`,
*   une méthode `hashCode` compatible avec la méthode `equals` — le but de cette méthode et la signification de sa compatibilité avec `equals` seront examinés ultérieurement dans le cours,
*   une méthode `toString` retournant une chaîne composée du nom de la classe et du nom et de la valeur des attributs de l'instance, p. ex. `Complex[re=1.0, im=2.0]`.

En d'autres termes, la définition plus haut, qui tient sur une ligne, est équivalente à la définition suivante, qui n'utilise que des concepts que vous connaissez déjà :

public final class Complex {
  private final double re;
  private final double im;

  public Complex(double re, double im) {
    this.re = re;
    this.im = im;
  }

  public double re() { return re; }
  public double im() { return im; }

  @Override
  public boolean equals(Object that) {
    // … vrai ssi that :
    // 1. est aussi une instance de Complex, et
    // 2. ses attributs re et im sont identiques.
  }

  @Override
  public int hashCode() {
    // … code omis car peu important
  }

  @Override
  public String toString() {
    return "Complex\[re=" + re + ", im=" + im + "\]";
  }
}

Comme cet exemple l'illustre, les enregistrements permettent d'éviter d'écrire beaucoup de code répétitif, ce que les anglophones appellent du [_boilerplate code_](https://en.wikipedia.org/wiki/Boilerplate_code). Il faut toutefois bien comprendre qu'en dehors d'une syntaxe très concise, les enregistrements n'apportent — pour l'instant en tout cas — rien de nouveau à Java, dans le sens où il est toujours possible de récrire un enregistrement en une classe Java équivalente, comme ci-dessus. En cela, les enregistrements sont similaires aux types énumérés.

Il est bien entendu possible de définir des méthodes dans un enregistrement, qui viennent s'ajouter à celles définies automatiquement. Par exemple, pour doter l'enregistrement `Complex` d'une méthode `modulus` retournant son module, il suffit de l'ajouter entre les accolades, ainsi :

public record Complex(double re, double im) {
  public double modulus() { return Math.hypot(re, im); }
}

(La méthode [`Math.hypot(x,y)`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Math.html#hypot(double,double)) retourne \\(\\sqrt{x^2 + y^2}\\)).

Finalement, il est aussi possible de définir ce que l'on nomme un **constructeur compact** (_compact constructor_), qui augmente le constructeur que Java ajoute par défaut aux enregistrements. Un constructeur compact doit son nom au fait qu'il semble ne prendre aucun argument, et n'initialise pas explicitement les attributs. En réalité, il prend des arguments qui sont les mêmes que ceux de l'enregistrement (`re` et `im` dans notre exemple), et Java lui ajoute automatiquement des affectations de ces arguments aux attributs correspondants.

Par exemple, on pourrait vouloir ajouter un constructeur compact à la classe `Complex` pour lever une exception si l'un des arguments passés au constructeur était une valeur `NaN` (_not a number_, une valeur invalide). On pourrait le faire ainsi :

public record Complex(double re, double im) {
  public Complex {  // constructeur compact
    if (Double.isNaN(re) || Double.isNaN(im))
      throw new IllegalArgumentException();
  }
  // … méthode modulus
}

Ce constructeur compact serait automatiquement traduit en :

public final class Complex {
  // … attributs re et im

  public Complex(double re, double im) {
    if (Double.isNaN(re) || Double.isNaN(im))
      throw new IllegalArgumentException();
    this.re = re;  // ajouté automatiquement
    this.im = im;  // ajouté automatiquement
  }

  // … méthodes modulus, re, im, hashCode, etc.
}

Les enregistrements ne seront pas décrits en détail dans le cours, mais seront introduits au moyen d'exemples similaires à ceux ci-dessus dans la suite du projet. Les personnes intéressées par les détails de leur fonctionnement pourront se rapporter à la [§8.10 (_Record Classes_) de la spécification du langage](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.10).

### 3.9. Enregistrement `Occupant`

L'enregistrement `Occupant` du paquetage principal, public, représente un occupant — pion ou hutte — d'une zone. Ses attributs sont :

*   `Kind kind`, la sorte d'occupant dont il s'agit (voir ci-dessous),
*   `int zoneId`, l'identifiant de la zone dans laquelle se trouve l'occupant.

Le type `Kind` est un type énuméré imbriqué à l'intérieur de l'enregistrement `Occupant`, qui a donc la structure suivante :

public record Occupant( /\* attributs \*/ ) {
  public enum Kind { /\* sortes d'occupants \*/ }
  // … constructeur, méthodes
}

`Kind` possède les éléments suivants :

*   `PAWN`, qui représente un pion,
*   `HUT`, qui représente une hutte.

Notez que la couleur du joueur auquel appartient l'occupant ne fait pas partie des attributs de `Occupant`, car elle peut se déduire d'autres informations, comme nous le verrons ultérieurement.

`Occupant` possède un constructeur compact dont le seul but est de valider les arguments qui lui sont passés, et qui lève :

*   `NullPointerException` si `kind` est `null`,
*   `IllegalArgumentException` si `zoneId` est strictement négatif.

`Occupant` offre également la méthode publique et _statique_ suivante :

*   `int occupantsCount(Kind kind)`, qui retourne le nombre d'occupants de la sorte donnée que possède un joueur — 5 pour les pions, 3 pour les huttes.

#### 3.9.1. Conseils de programmation

Dans le constructeur compact, utilisez la méthode [`requireNonNull`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Objects.html#requireNonNull(T)) de [`Objects`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Objects.html) pour lever une `NullPointerException` lorsque `kind` est `null`.

### 3.10. Enregistrement `Animal`

L'enregistrement `Animal` du paquetage principal, public, représente un animal situé dans un pré. Il possède les attributs suivants :

*   `int id`, l'identifiant de l'animal,
*   `Kind kind`, la sorte d'animal dont il s'agit (voir ci-après).

Une fois encore, `Kind` est un type énuméré imbriqué à l'intérieur de l'enregistrement `Animal`, et qui possède les éléments suivants, dans l'ordre :

*   `MAMMOTH`, qui représente un mammouth,
*   `AUROCHS`, qui représente un auroch,
*   `DEER`, qui représente un cerf,
*   `TIGER`, qui représente un smilodon.

On pourrait imaginer doter `Animal` d'un constructeur compact chargé de valider les arguments, similaire à celui de `Occupant`. Afin de ne pas alourdir le code, nous avons toutefois décidé de ne pas le faire pour certaines classes comme `Animal`, ou les classes représentant les zones, dont toutes les instances sont créées au démarrage du programme — qui plus est par du code généré automatiquement, comme nous le verrons.

`Animal` possède une unique méthode publique :

*   `int tileId()`, qui retourne l'identifiant de la tuile sur laquelle se trouve l'animal.

#### 3.10.1. Conseils de programmation

Pour écrire la méthode `tileId`, vous pouvez vous aider de la méthode statique `tileId` de `Zone`, décrite plus loin à la §[3.12](#sec/java/zone).

### 3.11. Enregistrement `Pos`

L'enregistrement `Pos` du paquetage principal, public, représente la position d'une case du plateau de jeu. Il possède les attributs suivants :

*   `int x`, la coordonnée x de la position,
*   `int y`, la coordonnée y de la position.

`Pos` possède un attribut public, final et statique :

*   `Pos ORIGIN`, qui contient la position de l'origine (0, 0), c.-à-d. la case centrale du plateau de jeu, qui contient la tuile de départ.

Elle offre de plus les méthodes publiques suivantes :

*   `Pos translated(int dX, int dY)`, qui retourne la position obtenue en translatant la coordonnée x du récepteur de `dX` unités, et sa coordonnée y de `dY` unités,
*   `Pos neighbor(Direction direction)`, qui retourne la position voisine du récepteur dans la direction donnée.

#### 3.11.1. Conseils de programmation

Pour écrire la méthode `neighbor`, souvenez-vous qu'en Java un `switch` peut être utilisé pour distinguer les différents cas d'un type énuméré, et que ce `switch` peut avoir une valeur. Dès lors, il est possible d'écrire `neighbor` de manière concise et claire ainsi :

public Pos neighbor(Direction direction) {
  return switch (direction) {
    case N -> // … position du voisin nord
    // … autres cas
  }
}

### 3.12. Interface `Zone`

L'interface `Zone` du paquetage principal, publique, représente une zone d'une tuile. Elle est destinée à être implémentée par les classes représentant les différents types de zones — forêts, prés, etc. — décrites dans les sections suivantes.

`Zone` possède un type énuméré imbriqué nommé `SpecialPower` et représentant les six pouvoirs spéciaux qu'une zone peut posséder. Les éléments de ce type énuméré sont, dans l'ordre :

*   `SHAMAN`, qui représente le chaman,
*   `LOGBOAT`, qui représente la pirogue,
*   `HUNTING_TRAP`, qui représente la fosse à pieux,
*   `PIT_TRAP`, qui représente la grande fosse à pieux,
*   `WILD_FIRE`, qui représente le feu,
*   `RAFT`, qui représente le radeau.

`Zone` offre deux méthodes publiques et statiques :

*   `int tileId(int zoneId)`, qui retourne l'identifiant de la tuile contenant la zone dont l'identifiant est `zoneId`,
*   `int localId(int zoneId)`, qui retourne l'identifiant « local » de la zone dont l'identifiant est `zoneId`, c.-à-d. son numéro dans la tuile qui la contient, compris entre 0 et 9,

ainsi que la méthode (publique) abstraite suivante :

*   `int id()`, qui retourne l'identifiant de la zone,

et enfin les méthodes (publiques) par défaut suivantes :

*   `int tileId()`, qui retourne l'identifiant de la tuile contenant la zone,
*   `int localId()`, qui retourne l'identifiant « local » de la zone,
*   `SpecialPower specialPower()`, qui retourne le pouvoir spécial de la zone, ou `null` si elle n'en possède aucun — ce qui est ce que retourne cette méthode par défaut.

La raison pour laquelle `specialPower` est définie comme une méthode par défaut retournant `null` est que seuls les prés et les lacs peuvent avoir un pouvoir spécial. Les classes représentant les autres types de zones — forêts et rivières — peuvent donc hériter de la méthode par défaut qui retourne `null`.

#### 3.12.1. Conseils de programmation

Pour écrire la version statique de `tileId`, ainsi que la méthode `localId`, souvenez-vous que l'identifiant d'une zone est obtenu en multipliant par 10 l'identifiant de la tuile qui la contient, et en y ajoutant le numéro de la zone — que nous appelons ici son identifiant local.

### 3.13. Enregistrement `Zone.Forest`

L'enregistrement `Forest`, imbriqué dans l'interface `Zone`, représente une zone de type forêt. Il implémente l'interface `Zone` et possède les attributs suivants :

*   `int id`, l'identifiant de la zone,
*   `Kind kind`, la sorte de forêt dont il s'agit (voir plus bas).

Notez bien que le fait que cet enregistrement — qui implémente l'interface `Zone` — possède un attribut `id` implique que Java fournit automatiquement une redéfinition de la méthode abstraite `id()` de `Zone` ! Soyez sûrs de bien comprendre cela, car nous utiliserons ce petit « truc » à de nombreuses reprises dans le projet.

Le type énuméré `Kind`, imbriqué dans `Forest`, énumère les trois types de forêts qui existent et qui sont, dans l'ordre :

*   `PLAIN`, une forêt vide (_plain_ signifiant « simple » en anglais),
*   `WITH_MENHIR`, une forêt contenant au moins un menhir,
*   `WITH_MUSHROOMS`, une forêt contenant un groupe de champignons.

### 3.14. Enregistrement `Zone.Meadow`

L'enregistrement `Meadow`, imbriqué dans l'interface `Zone`, représente une zone de type pré. Il implémente l'interface `Zone` et possède les attributs suivants :

*   `int id`, l'identifiant de la zone,
*   `List<Animal> animals`, les animaux contenus dans le pré,
*   `SpecialPower specialPower`, l'éventuel pouvoir spécial du pré, qui peut bien entendu être inexistant (`null`).

Comme la plupart des classes de ce projet, `Meadow` doit être _immuable_. Nous verrons très prochainement au cours ce que cela signifie exactement, mais pour l'instant il vous suffit de savoir que cela implique de doter `Meadow` d'un constructeur compact qui copie, au moyen de la méthode [`copyOf`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/List.html#copyOf(java.util.Collection)) de `List`, la liste des animaux reçue, ainsi :

animals = List.copyOf(animals);

Cette copie — souvent appelée _copie défensive_ — garantit que les animaux d'un pré ne changent pas même si la liste d'animaux passée à son constructeur change.

### 3.15. Interface `Zone.Water`

L'interface `Zone.Water`, imbriquée dans l'interface `Zone`, représente une zone aquatique, c.-à-d. une rivière ou un lac. Elle étend l'interface `Zone` et lui ajoute la méthode abstraite (et publique) suivante :

*   `int fishCount()`, qui retourne le nombre de poissons nageant dans la zone.

Cette interface est destinée à être implémentée par les classes représentant des zones aquatiques, qui sont elles aussi des enregistrements, décrits ci-après.

### 3.16. Enregistrement `Zone.Lake`

L'enregistrement `Lake`, imbriqué dans l'interface `Zone`, représente une zone de type lac. Il implémente l'interface `Zone.Water` et possède les attributs suivants :

*   `int id`, l'identifiant de la zone,
*   `int fishCount`, le nombre de poissons nageant dans le lac,
*   `SpecialPower specialPower`, l'éventuel pouvoir spécial du lac, qui peut bien entendu être inexistant (`null`).

### 3.17. Enregistrement `Zone.River`

L'enregistrement `River`, imbriqué dans l'interface `Zone`, représente une zone de type rivière. Il implémente l'interface `Zone.Water` et possède les attributs suivants :

*   `int id`, l'identifiant de la zone,
*   `int fishCount`, le nombre de poissons nageant dans la rivière,
*   `Lake lake`, le lac auquel la rivière est connectée, ou `null` s'il n'y en a aucun.

En plus des méthodes automatiquement définies par Java pour les enregistrements, `River` possède la méthode publique suivante :

*   `boolean hasLake()`, qui retourne vrai si et seulement si la rivière est connectée à un lac — c.-à-d. si son attribut `lake` n'est pas `null`.

### 3.18. Interfaces scellées en Java

En Java, une interface peut normalement être implémentée par n'importe quelle classe. Par exemple, l'interface `Zone` déclarée ci-dessus pourrait très bien être implémentée par d'autres classes que celles déclarées à l'intérieur de `Zone` (`Forest`, `Meadow`, etc.).

Dans ce cas particulier, cela n'est toutefois pas souhaitable, car nous savons que les seuls types de zones qui existent dans le jeu sont ceux susmentionnés. Il serait donc bien de pouvoir communiquer cela à Java, afin d'interdire la définition d'autres classes implémentant l'interface `Zone`.

Cela peut se faire en **scellant** (_seal_) l'interface `Zone`, simplement en ajoutant le mot-clef `sealed` à l'interface, ainsi :

public sealed interface Zone { … }

Lorsqu'une interface est ainsi scellée, les seules classes qui ont le droit de l'implémenter sont celles se trouvant dans la même « unité de compilation », c.-à-d. le même fichier Java.

Nous verrons plus tard que le fait de déclarer les interfaces `Zone` et `Zone.Water` comme scellées sera très utile lorsque nous utiliserons du **filtrage de motifs** (_pattern matching_) pour manipuler les zones. Dès lors, pensez à sceller ces deux interfaces dès maintenant !

### 3.19. Tests

Pour vous aider à démarrer ce projet, des tests unitaires JUnit vous sont exceptionnellement fournis pour cette étape, et se trouvent dans le dossier `test` du squelette. Une fois que vous aurez terminé la rédaction des classes de cette étape, vous pourrez les exécuter en :

*   ajoutant JUnit à votre projet, comme expliqué dans [notre guide à ce sujet](../g/intellij-junit.html),
*   effectuant un clic droit sur le dossier `test` du projet et sélectionnant _Run 'All Tests'_.

### 3.20. Documentation

Une fois les tests exécutés avec succès, il vous reste à documenter la totalité des entités publiques (classes, attributs et méthodes) définies dans cette étape, au moyen de commentaires Javadoc, comme décrit dans [le guide consacré à ce sujet](../g/javadoc.html). Vous pouvez écrire ces commentaires en français ou en anglais, en fonction de votre préférence, mais **vous ne devez utiliser qu'une seule langue pour tout le projet**.

## 4. Résumé

Pour cette étape, vous devez :

*   installer la version 21 (et ni une plus récente, ni une plus ancienne !) de Java sur votre ordinateur, ainsi que la dernière version d'IntelliJ IDEA Community Edition (**attention : n'installez pas la version Ultimate**) ,
*   écrire les classes et interfaces `Preconditions`, `PlayerColor`, `Rotation`, `Direction`, `Points`, `Occupant`, `Animal`, `Pos` et `Zone` (et les éventuelles classes imbriquées qu'elles contiennent) selon les indications ci-dessus,
*   vérifier que les tests que nous vous fournissons s'exécutent sans erreur, et dans le cas contraire, corriger votre code,
*   documenter la totalité des entités publiques que vous avez définies,
*   (optionnel mais fortement recommandé) rendre votre code au plus tard le **23 février 2024 à 18h00**, en exécutant le programme `Submit` fourni dans le squelette, après avoir modifié les définitions des variables `TOKEN_1` et `TOKEN_2` pour qu'elles contiennent les jetons individuels des deux membres du groupe, disponibles sur [la page privée](https://cs108.epfl.ch/my/) de chacun d'eux. (Les personnes travaillant seules doivent mettre leur jeton individuel dans les deux variables.)

Ce premier rendu n'est pas noté, mais celui de la prochaine étape le sera. Dès lors, il est vivement conseillé de faire un rendu de test cette semaine afin de se familiariser avec la procédure à suivre.

## Notes de bas de page

[1](#fnr.1)

Il aurait pu sembler plus logique d'attribuer l'identifiant 0 à la tuile de départ, plutôt que 56, mais ces identifiants ont été déterminés automatiquement en fonction de la position des tuiles dans les pages cartonnées fournies avec le jeu physique, et la tuile de départ y figure au milieu d'une page.

[2](#fnr.2)

En anglais, auroch s'écrit _aurochs_ avec un _s_ à la fin, même au singulier. C'était aussi le cas en français jusqu'aux [rectifications orthographiques du français de 1990](https://fr.wikipedia.org/wiki/Rectifications_orthographiques_du_fran%C3%A7ais_en_1990), qui recommandent d'écrire simplement « auroch » au singulier.

  Tuiles     function orgHighlight() { document .querySelectorAll('pre.src') .forEach((el) => { hljs.highlightElement(el); }); } addEventListener('DOMContentLoaded', orgHighlight, false); addEventListener('load', orgHighlight, false);

# Tuiles

ChaCuN – étape 2

## 1. Introduction

Le but de cette seconde étape est d'écrire les classes permettant de représenter les tuiles du jeu.

Avant de la commencer, lisez le guide [_Sauvegarder son travail_](../g/backups.html), qui vous donnera des conseils importants concernant la sauvegarde de votre projet au cours du semestre.

## 2. Concepts

La majorité des concepts nécessaires à cette étape ont été introduits dans la précédente, mais il convient encore de préciser quelques détails.

### 2.1. Bords de tuiles

Une tuile comporte 4 **bords** (_sides_), chacun d'entre eux pouvant être d'une des trois sortes suivantes :

*   un bord **pré**, constitué d'une seule zone pré,
*   un bord **forêt**, constitué d'une seule zone forêt,
*   un bord **rivière**, constitué d'une zone rivière centrale, entourée de deux zones pré, généralement différentes.

Comme nous l'avons dit, deux tuiles ne peuvent être voisines sur le plateau de jeu que si leurs deux bords qui se touchent sont de la même sorte.

Par exemple, la tuile de départ, présentée ci-dessous avec les limites des zones en rouge pour faciliter la compréhension, a les bords suivants :

*   son bord nord est un bord pré (zone 0),
*   ses bords est et sud sont des bords forêt (zone 1),
*   son bord ouest est un bord rivière, le premier pré étant constitué de la zone 2, la rivière de la zone 3, et le second pré de la zone 0.

![zones_56;128.png](i/zones_56;128.png)

Figure 1 : La tuile 56 découpée en zones

Notez bien que les deux prés entourant une rivière sont toujours donnés dans l'ordre dans lequel on les rencontre en faisant le tour de la tuile dans le sens des aiguilles d'une montre, comme ci-dessus. Ainsi, la rivière du bord ouest de la tuile de départ est entourée des prés correspondant aux zones 2 et 0 **dans cet ordre**, et il serait faux d'inverser leur ordre.

### 2.2. Placement d'une tuile

À l'exception de la tuile de départ, toute tuile placée sur le plateau de jeu l'est par un joueur, que nous nommerons le **placeur** (_placer_) de la tuile en question.

Le placeur a le droit de tourner la tuile qu'il doit placer d'un nombre quelconque de quarts de tour pour garantir que ses bords sont compatibles avec ceux des tuiles voisines.

### 2.3. Occupants potentiels d'une tuile

Après avoir placé une tuile, le placeur a la possibilité de déposer un occupant — pion ou hutte — sur l'une de ses zones.

Pour cela, le placeur doit choisir lequel des **occupants potentiels** (_potential occupants_) de la tuile il désire effectivement déposer. Ces occupants potentiels sont donnés par les règles suivantes :

*   chaque **zone de bordure** (_side zone_), c.-à-d. qui touche au moins un bord, peut potentiellement être occupée par un pion,
*   chaque rivière qui n'est pas connectée à un lac peut potentiellement être occupée par une hutte,
*   chaque lac peut potentiellement être occupé par une hutte.

Pour mémoire, les lacs ne touchent jamais un bord, par contre toutes les autres zones d'une tuile touchent au moins l'un de ses bords.

Par exemple, les occupants potentiels de la tuile de départ — qui, en pratique, ne peut pas être occupée car elle n'est pas placée par un joueur — sont :

1.  un pion (chasseur) occupant la zone 0 (le pré contenant l'auroch),
2.  un pion (cueilleur) occupant la zone 1 (la forêt),
3.  un pion (chasseur) occupant la zone 2 (le pré vide),
4.  un pion (pêcheur) occupant la zone 3 (la rivière),
5.  une hutte (de pêcheur) occupant la zone 8 (le lac).

Notez que dans le cas général, le nombre d'occupants potentiels d'une tuile est toujours au moins égal au nombre de zones de la tuile, car chaque zone peut être occupée par au moins un occupant. Mais le nombre d'occupants potentiels peut être plus grand que le nombre de zones, car une rivière qui n'est pas connectée à un lac peut être occupée soit par un pion, soit par une hutte.

## 3. Mise en œuvre Java

### 3.1. Interface `TileSide`

L'interface `TileSide` du paquetage principal, publique et scellée (!), représente un bord de tuile. Elle est destinée à être implémentée par les classes représentant les trois sortes de bords qui existent — forêt, pré ou rivière — décrites dans les sections suivantes.

`TileSide` offre les méthodes publiques et abstraites suivantes :

*   `List<Zone> zones()`, qui retourne les zones qui touchent le bord représenté par le récepteur (`this`),
*   `boolean isSameKindAs(TileSide that)`, qui retourne vrai si et seulement si le bord donné (`that`) est de la même sorte que le récepteur (`this`).

### 3.2. Enregistrement `TileSide.Forest`

L'enregistrement `Forest`, public et imbriqué dans `TileSide` qu'il implémente, représente un bord de tuile forêt. Il possède l'unique attribut suivant :

*   `Zone.Forest forest`, la forêt qui touche le bord.

De plus, `Forest` fournit des définitions concrètes des méthodes `zones` et `isSameKindAs`.

### 3.3. Enregistrement `TileSide.Meadow`

L'enregistrement `Meadow`, public et imbriqué dans `TileSide` qu'il implémente, représente un bord de tuile pré. Il possède l'unique attribut suivant :

*   `Zone.Meadow meadow`, le pré qui touche le bord.

De plus, `Meadow` fournit des définitions concrètes des méthodes `zones` et `isSameKindAs`.

### 3.4. Enregistrement `TileSide.River`

L'enregistrement `River`, public et imbriqué dans `TileSide` qu'il implémente, représente un bord de tuile rivière. Il possède les attributs suivants :

*   `Zone.Meadow meadow1`, le premier pré qui entoure la rivière et touche le bord,
*   `Zone.River river`, la rivière qui touche le bord,
*   `Zone.Meadow meadow2`, le second pré qui entoure la rivière et touche le bord.

De plus, `River` fournit des définitions concrètes des méthodes `zones` et `isSameKindAs`.

Notez une fois encore que l'ordre des deux prés est significatif, puisqu'ils sont donnés dans l'ordre dans lequel ils sont rencontrés lors d'un parcours de la tuile dans le sens des aiguilles d'une montre. La méthode `zones` les fournit dans le même ordre.

### 3.5. Enregistrement `Tile`

L'enregistrement `Tile` du paquetage principal, public, représente une tuile qui n'a pas encore été placée. Il possède les attributs suivants :

*   `int id`, l'identifiant de la tuile,
*   `Kind kind`, la sorte de la tuile (voir plus bas),
*   `TileSide n`, le côté nord (_north_) de la tuile,
*   `TileSide e`, le côté est (_east_) de la tuile,
*   `TileSide s`, le côté sud (_south_) de la tuile,
*   `TileSide w`, le côté ouest (_west_) de la tuile.

Comme d'habitude, le type `Kind`, imbriqué dans l'enregistrement `Tile`, énumère les sortes de tuile qui existent et qui sont, dans l'ordre :

*   `START`, qui identifie l'unique tuile de départ,
*   `NORMAL`, qui identifie les tuiles normales,
*   `MENHIR`, qui identifie les tuiles menhir.

En plus de ces attributs et des méthodes automatiquement générées par Java, `Tile` possède les méthodes publiques suivantes :

*   `List<TileSide> sides()`, qui retourne la liste des quatre côtés de la tuile, dans l'ordre `n`, `e`, `s`, `w`,
*   `Set<Zone> sideZones()`, qui retourne l'ensemble des zones de bordure de la tuile, c.-à-d. celles qui touchent au moins un bord — toutes les zones, sauf les lacs,
*   `Set<Zone> zones()`, qui retourne l'ensemble de toutes les zones de la tuile, lacs compris.

Lisez les conseils de programmation qui suivent pour savoir ce que sont des ensembles, et comment écrire succinctement la méthode `zones`.

#### 3.5.1. Conseils de programmation

1.  Ensembles  
    
    Les méthodes `sideZones()` et `zones()` retournent toutes deux un _ensemble_ de type `Set<Zone>`. Les ensembles seront examinés prochainement au cours, mais pour l'instant vous pouvez les voir comme quelque chose de similaire aux listes (`List` / `ArrayList`), à la différence près qu'ils n'admettent pas de doublons. Cela signifie que, lorsqu'on tente d'ajouter un élément à un ensemble au moyen de la méthode `add`, il n'y est effectivement ajouté **que** s'il ne s'y trouve pas déjà.
    
    Cette différence peut être illustrée au moyen de l'extrait de programme suivant :
    
    List<Integer> l = new ArrayList<>();
    l.add(1); l.add(2); l.add(1); l.add(3);
    
    Set<Integer> s = new HashSet<>();
    s.add(1); s.add(2); s.add(1); s.add(3);
    
    System.out.println("l = " + l);
    System.out.println("s = " + s);
    
    qui affiche les deux lignes suivantes :
    
    l = \[1, 2, 1, 3\]
    s = \[1, 2, 3\]
    
    qui illustrent bien le fait que l'élément 1 est présent deux fois dans la liste, mais une seule fois dans l'ensemble.
    
    Il y a encore beaucoup à dire concernant les différences entre les listes et les ensembles, mais pour cette étape, vous pouvez simplement voir les ensembles comme des listes sans doublons, et utiliser les équivalences de la table ci-dessous pour écrire votre code.
    
     
    | Liste | Ensemble |
    | --- | --- |
    | `List<…>` | `Set<…>` |
    | `new ArrayList<>()` | `new HashSet<>()` |
    | `l.add(…)` | `s.add(…)` |
    | `l.addAll(…)` | `s.addAll(…)` |
    
    Notez que la méthode [`addAll`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Set.html#addAll(java.util.Collection)) des ensembles — tout comme celle des listes — accepte aussi bien un ensemble qu'une liste en argument. Comme nous le verrons au cours, il s'agit en fait de la même méthode, définie dans l'interface [`Collection`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Collection.html) qui est implémentée aussi bien par les listes que par les ensembles.
    
    La raison pour laquelle les méthodes `sideZones` et `zones` retournent des ensembles et pas des listes est que chaque zone de la tuile ne doit apparaître qu'une fois dans le résultat, et les ensembles garantissent cela. Par exemple, la zone forêt de la tuile de départ touche les côtés est et sud de la tuile, mais elle ne doit néanmoins apparaître qu'une seule fois dans le résultat de `sideZones` et `zones`.
    
2.  Filtrage de motifs pour `instanceof`  
    
    Une manière simple d'écrire la méthode `zones` consiste à parcourir l'ensemble des zones de bordures retourné par `sideZones` et, pour chaque rivière de cet ensemble, regarder si elle est connectée à un lac.
    
    Le parcours de l'ensemble retourné par `sideZones` peut se faire au moyen de la boucle _for-each_ que vous connaissez déjà et qui fonctionne également pour les ensembles.
    
    Pour déterminer si une zone est une rivière, vous pourriez bien entendu utiliser `instanceof` puis un transtypage (_cast_), pour écrire quelque chose comme :
    
    Zone zone = …;
    if (zone instanceof Zone.River) {
      Zone.River river = (Zone.River) zone;
      // … utilisation de river
    }
    
    Toutefois, un nouveau concept a été ajouté à Java 17, qui se nomme le **filtrage de motif pour `instanceof`** (_pattern matching for instanceof_) et qui permet de récrire le code ci-dessus ainsi :
    
    Zone zone = …;
    if (zone instanceof Zone.River river) {
      // … utilisation de river
    }
    
    Comme on le voit, l'idée est de déclarer la variable `river` directement dans le contexte du `instanceof`, sans devoir faire de transtypage, ce qui simplifie le code.
    
    Depuis Java 21, le filtrage de motif est aussi disponible dans le contexte d'un `switch`, comme nous le verrons à l'étape suivante. De manière générale, le filtrage de motif est un concept très puissant, et nous vous recommandons donc vivement de vous entraîner à l'utiliser dès maintenant.
    

### 3.6. Enregistrement `PlacedTile`

L'enregistrement `PlacedTile` du paquetage principal, public, représente une tuile qui a été placée. Il possède les attributs suivants :

*   `Tile tile`, la tuile qui a été placée,
*   `PlayerColor placer`, le placeur de la tuile, ou `null` pour la tuile de départ,
*   `Rotation rotation`, la rotation appliquée à la tuile lors de son placement,
*   `Pos pos`, la position à laquelle la tuile a été placée,
*   `Occupant occupant`, l'occupant de la tuile, ou `null` si elle n'est pas occupée.

Le constructeur compact de `PlacedTile` valide les arguments en vérifiant que ni `tile`, ni `rotation`, ni `pos` ne sont égaux à `null`. (Attention : `placer` et `occupant` peuvent très bien l'être !) Il lève `NullPointerException` si ce n'est pas le cas.

De plus, `PlacedTile` possède un constructeur secondaire qui prend les mêmes arguments que le principal, dans le même ordre, sauf le dernier (`occupant`). Ce constructeur secondaire appelle le principal en lui passant les arguments reçus, et `null` comme dernier argument (`occupant`). Ce constructeur a pour but de faciliter la création de tuiles placées sans occupant.

En plus de ces attributs et des méthodes automatiquement générées par Java, `PlacedTile` possède les méthodes publiques suivantes :

*   `int id()`, qui retourne l'identifiant de la tuile placée,
*   `Tile.Kind kind()`, qui retourne la sorte de la tuile placée,
*   `TileSide side(Direction direction)`, qui retourne le côté de la tuile dans la direction donnée, **en tenant compte de la rotation appliquée à la tuile**,
*   `Zone zoneWithId(int id)`, qui retourne la zone de la tuile dont l'identifiant est celui donné, ou lève `IllegalArgumentException` si la tuile ne possède pas de zone avec cet identifiant,
*   `Zone specialPowerZone()`, qui retourne la zone de la tuile ayant un pouvoir spécial — il y en a au plus une par tuile — ou `null` s'il n'y en a aucune,
*   `Set<Zone.Forest> forestZones()`, qui retourne l'ensemble, éventuellement vide, des zones forêt de la tuile,
*   `Set<Zone.Meadow> meadowZones()`, qui retourne l'ensemble, éventuellement vide, des zones pré de la tuile,
*   `Set<Zone.River> riverZones()`, qui retourne l'ensemble, éventuellement vide, des zones rivière de la tuile,
*   `Set<Occupant> potentialOccupants()`, qui retourne l'ensemble de tous les occupants potentiels de la tuile, ou un ensemble vide si la tuile est celle de départ — qui se reconnaît au fait que son placeur est `null`,
*   `PlacedTile withOccupant(Occupant occupant)`, qui retourne une tuile placée identique au récepteur (`this`), mais occupée par l'occupant donné, ou lève `IllegalArgumentException` si le récepteur est déjà occupé,
*   `PlacedTile withNoOccupant()`, qui retourne une tuile placée identique au récepteur, mais sans occupant,
*   `int idOfZoneOccupiedBy(Occupant.Kind occupantKind)`, qui retourne l'identifiant de la zone occupée par un occupant de la sorte donnée (pion ou hutte), ou -1 si la tuile n'est pas occupée, ou si l'occupant n'est pas de la bonne sorte.

### 3.7. Enregistrement `TileDecks`

L'enregistrement `TileDecks` du paquetage principal, public et immuable, représente les tas des trois sortes de tuile qui existent — départ, normale, menhir. Il peut sembler étrange de considérer que l'unique tuile de départ fait partie d'un tas, mais cela permet de traiter toutes les tuiles de manière uniforme et simplifie le code de certaines étapes ultérieures.

`TileDecks` possède les attributs suivants :

*   `List<Tile> startTiles`, qui contient la tuile de départ (ou rien du tout),
*   `List<Tile> normalTiles`, qui contient les tuiles normales restantes,
*   `List<Tile> menhirTiles`, qui contient les tuiles menhir restantes.

La première tuile de chacune des listes est celle qui se trouve au sommet du tas correspondant, la seconde est celle se trouvant juste au-dessous, et ainsi de suite.

Le constructeur compact de `TileDecks` se charge de garantir l'immuabilité de la classe en copiant — au moyen de la méthode [`copyOf`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/List.html#copyOf(java.util.Collection)) de [`List`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/List.html) — chacune des trois listes reçues.

De plus, `TileDecks` offre les méthodes publiques suivantes :

*   `int deckSize(Tile.Kind kind)`, qui retourne le nombre de tuiles disponibles dans le tas contenant les tuiles de la sorte donnée,
*   `Tile topTile(Tile.Kind kind)` qui retourne la tuile au sommet du tas contenant les tuiles de la sorte donnée, ou `null` si le tas est vide,
*   `TileDecks withTopTileDrawn(Tile.Kind kind)`, qui retourne un nouveau triplet de tas égal au récepteur (`this`) si ce n'est que la tuile du sommet du tas contenant les tuiles de la sorte donnée en a été supprimée ; lève `IllegalArgumentException` si ce tas est vide,
*   `TileDecks withTopTileDrawnUntil(Tile.Kind kind, Predicate<Tile> predicate)`, qui retourne un nouveau triplet de tas égal au récepteur sans les tuiles au sommet du tas contenant celles de la sorte donnée pour lesquelles la méthode [`test`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/Predicate.html#test(T)) de `predicate` retourne faux (voir les conseils de programmation).

#### 3.7.1. Conseils de programmation

La méthode [`subList`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/List.html#subList(int,int)) peut être utile pour écrire la méthode `withTopTileDrawn`.

La méthode `withTopTileDrawnUntil` peut être utilisée pour supprimer du sommet d'un tas toutes les tuiles ne satisfaisant pas une condition donnée — le prédicat passé en second argument. Dans le contexte de ce projet, cette méthode est destinée à être utilisée pour éliminer les tuiles qui ne peuvent pas être placées. En effet, lorsqu'un joueur tire la prochaine tuile et qu'il est impossible de la placer, les règles précisent qu'elle doit être éliminée du jeu.

La condition que doit satisfaire la tuile au sommet du tas est passée sous la forme d'une valeur de type `Predicate<Tile>`. [`Predicate`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/Predicate.html) est une interface de la bibliothèque standard Java qui représente un **prédicat**, c.-à-d. une expression booléenne (vraie ou fausse). Elle ne possède qu'une seule méthode abstraite, nommée `test`, et sa déclaration (simplifiée) ressemble donc à ceci :

public interface Predicate<T> {
  boolean test(T t);
}

Dès lors, pour utiliser `withTopTileDrawnUntil`, il est possible de déclarer une classe implémentant cette interface, puis de lui en passer une instance en second argument. Par exemple, si on désirait supprimer du sommet d'un tas toutes les tuiles contenant plus d'une zone de bordure, on pourrait définir la classe suivante :

public final class TileHasOneZone implements Predicate<Tile> {
  @Override
  public boolean test(Tile tile) {
    return tile.zones().size() == 1;
  }
}

dont la méthode `test` retourne vrai ssi la tuile qu'on lui passe possède exactement une zone de bordure. On pourrait utiliser une instance de cette classe pour supprimer du tas des tuiles normales toutes celles du sommet ayant plus d'une zone (ou que le tas soit vide) ainsi :

TileDecks decks = …;
TileDecks decks1 =
  decks.withTopTileDrawnUntil(NORMAL, new TileHasOneZone());

Nous verrons plus tard au cours qu'une notation bien plus concise, nommée _lambda_, existe pour écrire ce genre de code. Ainsi, plutôt que de devoir définir la classe `TileHasOneZone`, on pourrait utiliser une lambda pour spécifier le second argument de `withTopTileDrawnUntil`, de la manière suivante :

TileDecks decks1 =
  decks.withTopTileDrawnUntil(NORMAL,
                              t -> t.zones().size() == 1);

Comme nous n'avons pas encore vu les lambdas au cours, vous pouvez pour l'instant définir des classes similaires à `TileHasOneZone` pour effectuer vos tests.

### 3.8. Vérification des signatures

Pour faciliter votre travail, nous mettons à votre disposition [un fichier de vérification de signatures](f/SignatureChecks_2.java), nommé `SignatureChecks_2.java`, à importer dans votre projet dans le même dossier que celui contenant le fichier `SignatureChecks_1.java`. La classe qu'il contient fait référence à la totalité des classes et méthodes de cette étape, ce qui vous permet de vérifier que leurs noms et types sont corrects. Cela est capital, car la moindre faute à ce niveau empêcherait l'exécution de nos tests unitaires.

Nous vous fournirons de tels fichiers pour toutes les étapes jusqu'à la sixième (incluse), et il vous faudra penser à vérifier systématiquement qu'aucune erreur n'est signalée à leur sujet. Faute de cela, votre rendu pourrait se voir refusé par notre système.

### 3.9. Tests

À partir de cette étape, nous ne vous fournissons plus de tests unitaires, et il vous faut donc les écrire vous-même.

Notez que, pour les étapes 2 à 6, nous mettrons à disposition nos tests le lundi suivant le jour de rendu de chaque étape. Vous aurez alors tout intérêt à les incorporer à votre projet, ce qui peut poser un problème de nommage.

En effet, si vous nommez vos tests selon la convention standard, en ajoutant simplement le suffixe `Test` au nom de la classe testée, vos tests auront le même nom que les nôtres, et il ne vous sera pas possible d'avoir vos tests et les nôtres dans un même projet. Pour cette raison, nous vous recommandons d'adopter une autre convention de nommage pour vos tests, par exemple en entourant le nom de la classe testée au moyen du préfixe `My` et du suffixe `Test`. Ainsi, votre test pour la classe `Tile` pourrait être nommé `MyTileTest`.

## 4. Résumé

Pour cette étape, vous devez :

*   écrire les classes `TileSide`, `Tile`, `PlacedTile` et `TileDecks` selon les indications plus haut,
*   tester votre code,
*   documenter la totalité des entités publiques que vous avez définies,
*   rendre votre code au plus tard le **1er mars 2024 à 18h00**, au moyen du programme `Submit.java` fourni et des jetons disponibles sur votre [page privée](https://cs108.epfl.ch/my/).

Ce rendu est un rendu testé, auquel 18 points sont attribués, au prorata des tests unitaires passés avec succès.

N'attendez surtout pas le dernier moment pour effectuer votre rendu, car vous n'êtes pas à l'abri d'imprévus. **Souvenez-vous qu'aucun retard, aussi insignifiant soit-il, ne sera toléré !**

  Aires     function orgHighlight() { document .querySelectorAll('pre.src') .forEach((el) => { hljs.highlightElement(el); }); } addEventListener('DOMContentLoaded', orgHighlight, false); addEventListener('load', orgHighlight, false);

# Aires

ChaCuN – étape 3

## 1. Introduction

Le but de cette troisième étape est d'écrire les classes permettant de représenter les aires, qui sont des ensembles de zones de même type connectées entre elles.

## 2. Concepts

### 2.1. Aires

Lorsque deux tuiles sont placées côte à côte sur le plateau, un des bords de la première touche un des bords de la seconde. Comme nous l'avons vu, ces deux bords doivent être de la même sorte, ce qui implique que les zones des deux tuiles qui touchent leur bord commun se combinent en de plus grandes régions connexes que nous nommerons des **aires** (_areas_).

Par exemple, en plaçant côte à côte les tuiles 17, 56 et 27, la zone 0 de la tuile 56 — un pré comportant un auroch — forme une aire avec la zone 2 de la tuile 17 — un pré comportant un cerf. Cela est illustré par l'image ci-dessous, sur laquelle les limites des différentes aires sont mises en évidence en rouge.

![areas_17_56_27;128.png](i/areas_17_56_27;128.png)

Figure 1 : Aires du paysage composé des tuiles 17, 56 et 27

Il existe un total de quatre sortes d'aires dans ChaCuN :

1.  les aires **pré**, composées de zones pré connectées,
2.  les aires **forêt**, composées de zones forêt connectées,
3.  les aires **rivière**, composées de zones rivière connectées,
4.  les **réseaux hydrographiques** (_river systems_), composés de zones aquatiques (rivières ou lacs) connectées.

Notez que l'existence des réseaux hydrographiques implique qu'une zone rivière appartient toujours à deux aires : une aire rivière, et un réseau hydrographique.

Les aires sont importantes dans ChaCuN, puisque ce sont elles qui, sous certaines conditions, rapportent des points à leurs occupants majoritaires.

#### 2.1.1. Connexions ouvertes

Une aire possède un certain nombre de **connexions ouvertes** (_open connections_), qui sont les segments de bords « libres » que touchent les zones composant l'aire — un bord de tuile étant libre s'il ne touche pas un bord d'une autre tuile. Une aire dont le nombre de connexions ouvertes est supérieur à zéro est dite **ouverte** (_open_), tandis qu'une aire dont ce nombre est nul est dite **fermée** (_closed_).

L'image ci-dessous montre un plateau constitué de 9 tuiles formant plusieurs aires, dont deux prés. Le plus grand d'entre eux possède un total de dix connexions ouvertes, numérotées de 1 à 10. Notez en particulier que les connexions 5 et 6 correspondent à un seul bord de tuile, mais il s'agit néanmoins de deux connexions distinctes.

![meadow-river-both-sides;128.png](i/meadow-river-both-sides;128.png)

Figure 2 : Une aire pré avec 10 connexions ouvertes

De même, toutes les aires visibles sur la figure [1](#org9f9bd67) plus haut sont ouvertes. La forêt a une connexion ouverte, les trois prés comportant des animaux en ont deux chacun, et les deux prés vides — sur les tuiles des extrémités — en ont trois chacun. La rivière connectée au lac a une seule connexion ouverte, de même que le réseau hydrographique qui la contient. Les deux autres rivières — et réseaux hydrographiques correspondant — en ont deux.

Il est possible de fermer la forêt de la figure [1](#org9f9bd67) en rajoutant une tuile au sud de la tuile de départ, comme illustré à la figure [3](#org0eb105e) ci-dessous.

![board_03-closed-forest.png](i/board_03-closed-forest.png)

Figure 3 : Un paysage comportant une aire (de type forêt) fermée

La distinction entre aires ouvertes et fermées est importante car, comme nous l'avons vu, deux sortes d'aires — les forêts et les rivières — rapportent des points à leurs occupants majoritaires lorsqu'elles sont fermées.

#### 2.1.2. Connexion d'aires

Lorsqu'une nouvelle tuile est posée sur le plateau de jeu, les aires qu'elle contient — ou en tout cas certaines d'entre elles — se retrouvent connectées avec les aires des tuiles adjacentes pour former de nouvelles aires plus grandes.

Conceptuellement, la connexion des aires d'une tuile nouvellement posée avec celles déjà présentes sur le plateau est instantanée. Toutefois, pour bien comprendre le processus, et surtout pour pouvoir correctement le mettre en œuvre, il vaut la peine de le voir comme une séquence de connexions de paires d'aires entre elles.

Pour illustrer cela, imaginons le plateau suivant, constitué de trois tuiles. Les zones de ces tuiles forment un total de quatre aires : 3 prés — que nous ignorerons dans ce qui suit — et une forêt. La forêt comporte à ce stade deux connexions ouvertes : une sur le bord sud de la tuile de droite, et une sur le bord est de la tuile du bas.

![forest-4-connect_1;128.png](i/forest-4-connect_1;128.png)

Figure 4 : Un plateau de jeu comportant 3 tuiles et 4 aires

Admettons que l'on ajoute maintenant une quatrième tuile dans le coin en bas à droite de ce plateau. Dans un premier temps, on considère que les aires de cette tuile — un pré et une forêt — ne sont pas connectées à celles des tuiles voisines, comme illustré ci-dessous. La forêt de la nouvelle tuile comporte elle aussi deux connexions ouvertes.

![forest-4-connect_2;128.png](i/forest-4-connect_2;128.png)

Figure 5 : Le plateau après l'ajout d'une tuile, mais sans connexion des aires

Une fois la tuile posée, on procède à la connexion des aires. On peut par exemple commencer par connecter les aires de la nouvelle tuile avec celles de sa voisine ouest. Il en résulte la situation illustrée ci-dessous, où la forêt obtenue par combinaison de celle du plateau et celle de la nouvelle tuile comporte maintenant deux connexions ouvertes — une sur le bord sud de la tuile en haut à droite, et une sur le bord nord de la nouvelle tuile.

![forest-4-connect_3;128.png](i/forest-4-connect_3;128.png)

Figure 6 : Le plateau après connexion avec la voisine ouest

Finalement, on peut connecter la nouvelle tuile avec sa voisine du nord, ce qui a pour effet de supprimer les deux dernières connexions ouvertes de la forêt, qui est maintenant fermée. La situation finale est visible dans l'image ci-dessous.

![forest-4-connect_4;128.png](i/forest-4-connect_4;128.png)

Figure 7 : Le plateau après connexion avec la voisine nord

Comme cet exemple l'illustre, suite à la pose d'une tuile sur le plateau, une aire peut être connectée soit à une aire différente — comme lors de la connexion de la nouvelle tuile avec sa voisine ouest — soit à elle-même — comme lors de la connexion de la nouvelle tuile avec sa voisine nord.

Cette différence est importante, entre autres, pour déterminer le nombre de connexions ouvertes de l'aire résultant de la connexion. En effet, lorsqu'une aire est connectée à une aire différente d'elle, le nombre de connexions ouvertes de l'aire résultante est égal à la somme du nombre de connexions ouvertes des deux aires, moins deux — car la connexion a supprimé une connexion ouverte de la première aire, et une de la seconde.

Par contre, lorsqu'une aire est connectée à elle-même, alors le nombre de connexions ouvertes qui restent après cette connexion est simplement le nombre de connexions ouvertes de l'aire, moins deux — pour les mêmes raisons que précédemment.

#### 2.1.3. Occupants

Il a déjà été dit que, lorsqu'un joueur place une nouvelle tuile sur le plateau, il a éventuellement la possibilité d'occuper l'une des zones de cette tuile. Cela n'est toutefois autorisé **que** si l'aire à laquelle appartient la zone en question n'est pas déjà occupée.

Cela n'implique toutefois **pas** qu'une aire ne peut être occupée que par un seul occupant ! Il est en effet possible que deux aires disjointes soient occupées individuellement avant de se retrouver connectées par une tuile nouvellement posée, auquel cas l'aire résultante possède plus d'un occupant.

Par exemple, l'image ci-dessous montre 5 tuiles comportant trois aires forêt disjointes, entourées en rouge pour faciliter leur identification. Chacune d'entre elles est occupée par un seul pion, deux d'entre eux appartenant au joueur de couleur jaune, le dernier au joueur de couleur verte.

![multi-occupants-1;128.png](i/multi-occupants-1;128.png)

Figure 8 : Trois aires forêt occupées chacune par un cueilleur

En plaçant une sixième tuile comportant trois bords forêt, il est possible de joindre ces trois aires forêts disjointe en une unique aire forêt occupée par les trois occupants susmentionnés. Cette nouvelle aire, désormais fermée, est entourée sur l'image ci-dessous.

![multi-occupants-2;128.png](i/multi-occupants-2;128.png)

Figure 9 : Une aire forêt occupée par 3 cueilleurs (2 jaunes, 1 vert)

### 2.2. Partition des zones

Chacune des quatre différentes sortes d'aires forme ce que l'on nomme une **partition** (_partition_) de l'ensemble des zones qui lui correspond.

En mathématiques, une partition d'un ensemble \\(S\\) est un ensemble de sous-ensembles disjoints de \\(S\\) dont l'union est égale à \\(S\\). Par exemple, l'ensemble d'ensembles d'entiers \\(P\\) suivant : \\\[ P = \\{ \\{0\\}, \\{1,3,5,7\\}, \\{2,4,6\\}\\} \\\] est une partition de l'ensemble \\(S\\) suivant : \\\[ S = \\{0, 1, 2, 3, 4, 5, 6, 7 \\} \\\] En effet, les trois ensembles d'entiers qui constituent \\(P\\) sont disjoints — aucun entier n'appartient à plus d'un de ces ensembles — et leur union est égale à \\(S\\).

Si l'on considère, par exemple, l'ensemble de toutes les zones pré des tuiles posées sur le plateau de jeu, il devrait être clair que les aires pré en constituent une partition. En effet, les aires sont disjointes — aucune zone pré n'appartient à plus d'une aire pré — et l'union des zones qui constituent ces aires est égale à l'ensemble de toutes les zones pré.

De la même manière, les aires forêt constituent une partition des zones forêt ; les aires rivière constituent une partition des zones rivière ; et les réseaux hydrographiques constituent une partition des zones aquatiques — rivières et lacs.

## 3. Mise en œuvre Java

### 3.1. Enregistrement `Area`

L'enregistrement `Area` du paquetage principal, public et immuable, représente une aire. Il est générique et son paramètre de type, nommé `Z`, est le type des zones constituant l'aire. Il est donc borné par `Zone`.

`Area` possède les attributs suivants :

*   `Set<Z> zones`, l'ensemble des zones constituant l'aire,
*   `List<PlayerColor> occupants`, les couleurs des éventuels joueurs occupant l'aire, **triés par couleur**,
*   `int openConnections`, le nombre de connexions ouvertes de l'aire.

Notez bien que, malgré son nom, l'attribut `occupants` ne contient pas une liste de valeurs de type `Occupant`, mais simplement une liste de (couleur de) joueurs. En effet, la seule information importante au sujet des occupants d'une aire est leur propriétaire, donc le type `Occupant` ne conviendrait pas ici.

Le constructeur compact d'`Area` valide l'argument `openConnections` et lève `IllegalArgumentException` si celui-ci n'est pas positif ou nul. De plus, pour garantir l'immuabilité de la classe, il copie l'ensemble de zones reçu, ainsi que la liste des occupants, qui est de plus triée par couleur au préalable — voir les conseils de programmation plus bas pour savoir comment faire cela.

`Area` offre plusieurs méthodes publiques et statiques permettant d'obtenir différentes informations au sujet des différents types d'aires. Ces méthodes sont statiques car elles ne sont définies que pour des types spécifiques d'aires, et il n'est donc pas possible d'en faire des méthodes d'instance.

Par exemple, la première des méthodes ci-dessous, `hasMenhir`, n'est définie que pour les aires forêt, donc celles ayant le type `Area<Zone.Forest>`. Malheureusement, Java ne permet pas de définir des méthodes d'instances qui ne seraient disponibles que pour ce type d'aires, et pas les autres. Par contre, il est tout à fait possible de définir une méthode statique prenant en argument une aire de type `Area<Zone.Forest>`, et c'est donc la solution que nous avons choisie.

Les méthodes statiques de ce genre sont :

*   `static boolean hasMenhir(Area<Zone.Forest> forest)`, qui retourne vrai si et seulement si la forêt donnée contient au moins un menhir,
*   `static int mushroomGroupCount(Area<Zone.Forest> forest)`, qui retourne le nombre de groupes de champignons que contient la forêt donnée,
*   `static Set<Animal> animals(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals)`, qui retourne l'ensemble des animaux se trouvant dans le pré donné mais qui ne font pas partie de l'ensemble des animaux annulés donné — les animaux annulés pouvant p. ex. être des cerfs dévorés par des smilodons,
*   `static int riverFishCount(Area<Zone.River> river)`, qui retourne le nombre de poissons nageant dans la rivière donnée ou dans l'un des éventuels lacs se trouvant à ses extrémités — les poissons d'un lac donné ne devant être comptés qu'une seule fois même dans le cas où un unique lac termine la rivière aux deux bouts,
*   `static int riverSystemFishCount(Area<Zone.Water> riverSystem)`, qui retourne le nombre de poissons nageant dans le réseau hydrographique donné,
*   `static int lakeCount(Area<Zone.Water> riverSystem)`, qui retourne le nombre de lacs du réseau hydrographique donné.

En plus de ces méthodes statiques, `Area` offre les méthodes publiques suivantes, qui sont, elles, définies pour toutes les sortes d'aires :

*   `boolean isClosed()`, qui retourne vrai si et seulement si (ssi) l'aire est fermée,
*   `boolean isOccupied()`, qui retourne vrai ssi l'aire est occupée par au moins un occupant,
*   `Set<PlayerColor> majorityOccupants()`, qui retourne l'ensemble des occupants majoritaires de l'aire,
*   `Area<Z> connectTo(Area<Z> that)`, qui retourne l'aire résultant de la connexion du récepteur (`this`) à l'aire donnée (`that`),
*   `Area<Z> withInitialOccupant(PlayerColor occupant)`, qui retourne une aire identique au récepteur, si ce n'est qu'elle est occupée par l'occupant donné ; lève `IllegalArgumentException` si le récepteur est déjà occupé,
*   `Area<Z> withoutOccupant(PlayerColor occupant)`, qui retourne une aire identique au récepteur, mais qui comporte un occupant de la couleur donnée en moins ; lève `IllegalArgumentException` si le récepteur ne contient aucun occupant de la couleur donnée,
*   `Area<Z> withoutOccupants()`, qui retourne une aire identique au récepteur, mais totalement dénuée d'occupants,
*   `Set<Integer> tileIds()`, qui retourne l'ensemble de l'identité des tuiles contenant l'aire,
*   `Zone zoneWithSpecialPower(Zone.SpecialPower specialPower)`, qui retourne la zone de l'aire qui possède le pouvoir spécial donné, ou `null` s'il n'en existe aucune.

#### 3.1.1. Conseils de programmation

1.  Tri des occupants  
    
    Le constructeur compact doit trier par couleur la liste des occupants reçue. Cela signifie que les éléments de la liste doivent apparaître dans l'ordre de déclaration des éléments du type énuméré `PlayerColor` : les éventuelles occurrences de `RED` d'abord, suivies des éventuelles occurrences de `BLUE`, etc.
    
    Cela est beaucoup plus simple à garantir qu'il n'y paraît, d'une part car les éléments d'un type énuméré sont **comparables** — une notion que nous examinerons plus tard au cours mais qui signifie que ces éléments savent se comparer entre eux — et d'autre part car la méthode [`sort`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Collections.html#sort(java.util.List)) de [`Collections`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Collections.html) permet de trier une liste de valeurs comparables par ordre croissant.
    
    L'extrait de code ci-dessous, dont vous pouvez vous inspirer, illustre l'utilisation de ces deux propriétés pour trier une liste de valeurs de type `Direction` — le type énuméré défini à l'étape 1 :
    
    List<Direction> directions =
      List.of(Direction.S, Direction.N, Direction.E);
    List<Direction> sortedDirections =
      new ArrayList<>(directions);
    Collections.sort(sortedDirections);
    System.out.println(sortedDirections); // affiche \[N, E, S\]
    
2.  Méthode `riverFishCount`  
    
    Une manière d'éviter de compter à deux reprises les poissons d'un lac terminant une rivière aux deux bouts est de stocker les lacs déjà rencontrés dans un ensemble. Il est alors utile de savoir que la méthode [`add`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Set.html#add(E)) des ensembles retourne un booléen, qui est vrai si et seulement si l'élément ajouté ne se trouvait pas encore dans l'ensemble.
    
3.  Méthode `majorityOccupants`  
    
    La méthode `majorityOccupants` n'est pas totalement triviale à écrire, et nous vous suggérons donc de la rédiger de la manière suivante.
    
    Dans un premier temps, construisez un tableau d'entiers indexé par le nombre ordinal de chaque couleur (0 pour `RED`, 1 pour `BLUE`, etc.) et remplissez-le avec le nombre d'occupants de chaque couleur. Par exemple, si le joueur de couleur rouge possède deux occupants, celui de couleur verte aussi, tandis que celui de couleur bleue n'en possède qu'un, et les autres joueurs aucun, ce tableau est :
    
    \[2, 1, 2, 0, 0\]
    
    Cela fait — ou, mieux encore, au moment de la construction — déterminez le maximum des éléments de ce tableau et, s'il est supérieur à zéro, retournez l'ensemble des couleurs des joueurs possédant ce nombre maximum d'occupants.
    
4.  Méthode `connectTo`  
    
    Lors de l'écriture de la méthode `connectTo`, n'oubliez pas que l'aire passée en argument peut être soit une aire différente du récepteur (`this`), soit la même aire. Or la manière dont le nombre de connexions ouvertes de l'aire résultante est calculé dépend de cela, et il faut donc bien penser à distinguer ces deux cas.
    

### 3.2. Enregistrement `ZonePartition`

L'enregistrement `ZonePartition` du paquetage principal, publique et immuable, représente une partition de zones d'un type donné — c.-à-d. un ensemble d'aires formant une partition. Tout comme `Area`, `ZonePartition` est générique et son paramètre de type, nommé `Z` et borné par `Zone`, représente le type des zones de la partition.

Cet enregistrement possède un seul attribut :

*   `Set<Area<Z>> areas`, l'ensemble des aires formant la partition.

Bien entendu, pour garantir l'immuabilité de la classe, le constructeur compact de `ZonePartition` copie l'ensemble d'aires reçu au moyen de la méthode [`copyOf`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Set.html#copyOf(java.util.Collection)).

En plus du constructeur primaire, `ZonePartition` possède un constructeur secondaire qui ne prend aucun argument et initialise la partition avec un ensemble d'aires vide.

Finalement, `ZonePartition` offre la méthode publique suivante :

*   `Area<Z> areaContaining(Z zone)`, qui retourne l'aire contenant la zone passée en argument, ou lève `IllegalArgumentException` si la zone n'appartient à aucune aire de la partition.

### 3.3. Classe `ZonePartition.Builder`

La classe `Builder`, imbriquée statiquement dans `ZonePartition`, publique et finale, sert de bâtisseur à la classe `ZonePartition`. Tout comme `ZonePartition`, cette classe est générique, et son paramètre de type, nommé `Z`, est borné par `Zone`.

Comme d'habitude, les attributs de ce bâtisseur sont les mêmes que ceux de la classe qu'il construit, mais ils ne sont pas immuables. En d'autres termes, `Builder` possède comme unique attribut (privé) un ensemble d'aires non immuable, qui est une instance de `HashSet<Area<Z>>`.

L'unique constructeur de `Builder` prend en argument une partition de zones existante, de type `ZonePartition<Z>`, et initialise l'ensemble des aires du bâtisseur avec celui de cette partition. L'idée est que ce constructeur sera utilisé pour créer une partition de zones à partir d'une autre déjà existante.

En plus de ce constructeur, `Builder` offre les méthodes suivantes dont le but est généralement de modifier, d'une manière ou d'une autre, la partition en cours de construction :

*   `void addSingleton(Z zone, int openConnections)`, qui ajoute à la partition en cours de construction une nouvelle aire inoccupée, constituée uniquement de la zone donnée et possédant le nombre de connexions ouvertes donné,
*   `void addInitialOccupant(Z zone, PlayerColor color)`, qui ajoute à l'aire contenant la zone donnée un occupant initial de la couleur donnée, ou lève `IllegalArgumentException` si la zone n'appartient pas à une aire de la partition, ou si l'aire est déjà occupée,
*   `void removeOccupant(Z zone, PlayerColor color)`, qui supprime de l'aire contenant la zone donnée un occupant de la couleur donnée, ou lève `IllegalArgumentException` si la zone n'appartient pas à une aire de la partition, ou si elle n'est pas occupée par au moins un occupant de la couleur donnée,
*   `void removeAllOccupantsOf(Area<Z> area)`, qui supprime tous les occupants de l'aire donnée, ou lève `IllegalArgumentException` si l'aire ne fait pas partie de la partition,
*   `void union(Z zone1, Z zone2)`, qui connecte entre elles les aires contenant les zones données pour en faire une aire plus grande ; lève `IllegalArgumentException` si l'une des deux zones n'appartient pas à une aire de la partition,
*   `ZonePartition<Z> build()`, qui construit la partition de zones.

#### 3.3.1. Conseils de programmation

1.  Remplacement d'aires  
    
    Étant donné que `Area` est immuable, `addInitialOccupant` n'a pas d'autre choix que de procéder en trois phases : premièrement, trouver l'aire contenant la zone donnée ; deuxièmement, créer une nouvelle aire identique à celle trouvée mais avec l'occupant initial donné ; troisièmement, remplacer dans la partition l'ancienne aire par la nouvelle. Il ne faut surtout pas oublier de supprimer l'ancienne aire de la partition (au moyen de la méthode [`remove`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Set.html#remove(java.lang.Object))), faute de quoi il ne s'agit plus d'une partition !
    
    Cette manière de procéder est bien entendu aussi valable pour les autres méthodes qui changent une caractéristique d'une (ou plusieurs) aire(s) de la partition.
    
2.  Méthode `union`  
    
    Lors de l'écriture de la méthode `union`, souvenez-vous que les deux zones données peuvent appartenir à la même aire.
    

### 3.4. Tests

Comme pour l'étape précédente, nous ne vous fournissons plus de tests mais [un fichier de vérification de signatures](f/SignatureChecks_3.java) à importer dans votre projet.

## 4. Résumé

Pour cette étape, vous devez :

*   écrire les classes `Area`, `ZonePartition` et `ZonePartition.Builder` selon les indications donnés plus haut,
*   tester votre code,
*   documenter la totalité des entités publiques que vous avez définies,
*   rendre votre code au plus tard le **8 mars 2024 à 18h00**, au moyen du programme `Submit.java` fourni et des jetons disponibles sur votre [page privée](https://cs108.epfl.ch/my/).

Ce rendu est un rendu testé, auquel 18 points sont attribués, au prorata des tests unitaires passés avec succès.

N'attendez surtout pas le dernier moment pour effectuer votre rendu, car vous n'êtes pas à l'abri d'imprévus. **Souvenez-vous qu'aucun retard, aussi insignifiant soit-il, ne sera toléré !**

  Partitions et messages     function orgHighlight() { document .querySelectorAll('pre.src') .forEach((el) => { hljs.highlightElement(el); }); } addEventListener('DOMContentLoaded', orgHighlight, false); addEventListener('load', orgHighlight, false);

# Partitions et messages

ChaCuN – étape 4

## 1. Introduction

Cette quatrième étape a deux buts : premièrement, écrire une classe regroupant les différentes partitions de zones utilisées lors d'une partie ; et deuxièmement, écrire une classe permettant de garder trace des événements importants — principalement les gains de points — se produisant lors d'une partie.

## 2. Concepts

### 2.1. Partitions de zones

Comme nous l'avons vu, un total de quatre partitions de zones existent dans ChaCuN : celle des forêts, celle des prés, celle des rivières et celle des zones aquatiques — rivières et lacs. Ces partitions évoluent au cours de la partie, au fur et à mesure que des tuiles sont déposées sur le plateau, connectant certaines aires entre elles.

#### 2.1.1. Contenu exact

Une question qui se pose au sujet de ces partitions est celle de leur contenu exact : s'agit-il de partitions de _toutes_ les zones des tuiles du jeu — non seulement celles déjà posées, mais aussi celles qui ne le sont pas encore — ou s'agit-il de partitions des zones des tuiles déjà posées ?

Si ces partitions contiennent toujours les zones de _toutes_ les tuiles (posées ou non), alors au début d'une partie elles doivent être initialisées avec les aires de toutes les tuiles du jeu. Ensuite, au cours de la partie, lorsqu'une tuile est déposée et que certaines aires sont connectées entre elles, les partitions changent — dans la mesure où les aires qui les constituent évoluent — mais le nombre total de zones dans chaque partition ne change pas. La seule chose qui change est leur répartition dans les différentes aires.

Si les partitions ne contiennent que les zones des tuiles _posées_, alors au tout début d'une partie — avant même que la tuile de départ n'ait été placée sur le plateau — toutes les partitions sont vides. Au cours de la partie, lorsqu'une nouvelle tuile est déposée, ses aires sont d'abord ajoutées aux différentes partitions, puis certaines de ces aires sont connectées avec celles des tuiles voisines.

Pour ce projet, nous avons décidé d'utiliser la seconde solution, dans laquelle les différentes partitions ne contiennent que les zones des tuiles déjà posées. Cela réduit leur taille et peut faciliter le débogage.

#### 2.1.2. Ajout d'une tuile

Le fait que les partitions ne contiennent que les zones des tuiles déjà posées sur le plateau implique que, au moment où une tuile est déposée, les aires formées par ses zones doivent être ajoutées aux différentes partitions.

Par exemple, avant le début d'une partie, toutes les partitions sont vides, et au moment où la tuile de départ est posée, ses aires sont ajoutées aux différentes partitions. Pour mémoire, la tuile de départ comporte 5 zones visibles sur l'image ci-dessous.

![zones_56;128.png](i/zones_56;128.png)

Figure 1 : La tuile de départ (identifiant 56) découpée en zones

Au moment où elle est posée sur le plateau, les quatre partitions — qui sont vides à ce stade — doivent être augmentées ainsi :

*   celle des prés est augmentée de deux nouvelles aires, chacune composée d'une seule zone, qui sont respectivement les zones 0 (identifiant 560) et 2 (identifiant 562) de la tuile,
*   celle des forêts est augmentée d'une unique nouvelle aire, composée de la zone 1 (identifiant 561) de cette tuile,
*   celle des rivières est augmentée d'une unique nouvelle aire, composée de la zone 3 (identifiant 563) de cette tuile,
*   celle des zones aquatiques — les réseaux hydrographiques — est augmentée d'une unique nouvelle aire, composée des zones 3 (la rivière, identifiant 563) et 8 (le lac, identifiant 568) de cette tuile.

Ces partitions peuvent s'écrire de manière compacte en représentant chaque zone par son identifiant, et chaque partition par un ensemble de sous-ensembles de ces identifiants :

*   la partition des prés est {{560}, {562}},
*   la partition des forêts est {{561}},
*   la partition des rivières est {{563}},
*   la partition des zones aquatiques est {{563, 568}}.

Comme nous l'avons vu à l'étape précédente, chaque aire possède un certain nombre de connexions ouvertes. Dès lors, lorsqu'une tuile est posée, le nombre de connexions ouvertes de chacune de ses aires doit être déterminé. Cela peut se faire en comptant, pour chaque aire, le nombre de fois qu'une de ses zones apparaît sur l'un des bords de la tuile.

#### 2.1.3. Connexion des aires de deux tuiles

Lorsque deux tuiles sont placées côte à côte sur le plateau, les aires de leur côté commun doivent être connectées entre elles. Lorsque ce bord commun est un pré ou une forêt, cela ne pose pas de problème particulier ; lorsqu'il s'agit d'une rivière, il faut prendre garde à connecter les bonnes aires entre elles.

Par exemple, admettons que l'on dépose la tuile 17 à l'ouest de la tuile de départ, pour obtenir le plateau ci-dessous.

![board_04-two-tiles-river.png](i/board_04-two-tiles-river.png)

Figure 2 : Un plateau constitué des tuiles 17 et 56

En regardant cette image, on voit que le pré 2 de la tuile 17 — qui contient le cerf — doit être connecté avec le pré 0 de la tuile 56 — qui contient l'auroch. De même, le pré 0 de la tuile 17 — vide — doit être connecté avec le pré 2 — vide lui aussi — de la tuile 56. Or il faut se souvenir que les prés d'un bord rivière sont toujours donnés dans l'ordre dans lequel on les rencontre lors d'un parcours du pourtour de la tuile en sens horaire. Cela implique que les deux bords connectés dans l'image ci-dessus sont :

*   pour la tuile 17, le bord rivière constitué, dans l'ordre, du pré 2, de la rivière 1, puis du pré 0,
*   pour la tuile 56, le bord rivière constitué, dans l'ordre, du pré 2, de la rivière 3, puis du pré 0.

En d'autres termes, le premier pré du premier bord doit être connecté avec le second du second bord, et inversement.

Une fois ces connexions effectuées, on obtient les partitions suivantes :

*   la partition des prés est {{170,562}, {172,560}, {174}},
*   la partition des forêts est {{561}},
*   la partition des rivières est {{171,563}, {173}},
*   la partition des zones aquatiques est {{171,563,568}, {173}}.

### 2.2. Messages

Comme cela a été brièvement expliqué dans [l'introduction au projet](00_introduction.html), lors d'une partie de ChaCuN, des messages sont affichés sur la droite de l'interface, sur ce que nous nommerons le **tableau d'affichage** (_message board_). Le but de ces messages est d'informer les joueurs du déroulement de la partie, généralement pour leur dire qui a gagné des points, et pourquoi.

La partie graphique du tableau d'affichage sera mise en œuvre dans une étape ultérieure, mais la classe permettant de générer et de stocker les messages le sera dans celle-ci. Les sections suivantes énumèrent donc les différentes situations dans lesquelles un message doit être généré.

#### 2.2.1. Fermeture d'une forêt avec menhir

Lorsqu'un joueur utilise une tuile normale pour fermer une forêt, et que celle-ci contient au moins un menhir, il a le droit de jouer un second tour en plaçant une tuile menhir — à supposer qu'il en reste. Dans ce cas, un message est affiché sur le tableau d'affichage.

#### 2.2.2. Fermeture d'une forêt ou d'une rivière

Lorsqu'une forêt ou une rivière est fermée, et qu'elle est occupée par au moins un pion (cueilleur ou pêcheur), ses occupants majoritaires remportent des points. Un message est alors affiché sur le tableau d'affichage.

#### 2.2.3. Pose de la tuile contenant la fosse à pieu

Lorsqu'un joueur pose la tuile contenant la fosse à pieu, il remporte les points correspondant aux éventuels animaux qui se trouvent dans le même pré que la fosse, et sur l'une des 8 tuiles voisines : les 4 situées directement au nord, à l'est, au sud et à l'ouest de celle contenant la fosse, ainsi que les 4 situées dans les diagonales — au nord-est, au sud-est, au sud-ouest et au nord-ouest. Si le nombre de points en question est supérieur à 0, un message est affiché sur le tableau d'affichage.

Dans tous les cas, les animaux se trouvant dans le même pré que la fosse et sur l'une des 8 tuiles voisines sont ensuite annulés, c.-à-d. qu'ils sont ignorés pour le reste de la partie.

#### 2.2.4. Pose de la tuile contenant la pirogue

Lorsqu'un joueur pose la tuile contenant la pirogue, il remporte un nombre de points dépendant du nombre de lacs se trouvant dans le même réseau hydrographique qu'elle. Ce nombre de points est forcément supérieur à 0, car la pirogue elle-même se trouve dans un lac. Un message est donc affiché sur le tableau d'affichage.

#### 2.2.5. Fin de partie

À la fin de la partie, les occupants majoritaires des prés et des réseaux hydrographiques gagnent des points dépendant respectivement des animaux présents — et non annulés — dans le pré, et des poissons nageant dans le réseau hydrographique. Pour chaque pré ou réseau hydrographique occupé et rapportant un nombre de points non nul, un message est affiché sur le tableau d'affichage.

Si la tuile contenant la grande fosse à pieux a été posée, et que le pré dans lequel elle se trouve est occupé, alors ses occupants majoritaires reçoivent des points additionnels pour les éventuels animaux se trouvant dans le même pré que la fosse et sur l'une des 8 tuiles voisines. Si ce nombre de points est supérieur à 0, un message est affiché sur le tableau d'affichage.

Si la tuile contenant le radeau a été posée, et que le réseau hydrographique contenant le radeau est occupé, alors ses occupants majoritaires reçoivent des points additionnels, et un message est affiché sur le tableau d'affichage.

Finalement, lorsque tous les points ont été comptés, un dernier message nommant le ou les joueur(s) ayant remporté la partie, et les points qu'ils ont obtenus, est affiché sur le tableau d'affichage. (En cas d'égalité de points, les joueurs en ayant le plus se partagent la victoire.)

## 3. Mise en œuvre Java

### 3.1. Enregistrement `ZonePartitions`

L'enregistrement `ZonePartitions` (avec un `s` à la fin !) du paquetage principal, public et immuable, regroupe les quatre partitions de zones du jeu. Il possède donc les attributs suivants :

*   `ZonePartition<Zone.Forest> forests`, la partition des forêts,
*   `ZonePartition<Zone.Meadow> meadows`, la partition des prés,
*   `ZonePartition<Zone.River> rivers`, la partition des rivières,
*   `ZonePartition<Zone.Water> riverSystems`, la partition des zones aquatiques (rivières et lacs).

En dehors des méthodes ajoutées automatiquement par Java, `ZonePartitions` ne possède rien d'autre que l'attribut public, final et statique suivant :

*   `ZonePartitions EMPTY`, qui représente un groupe de 4 partitions vides.

### 3.2. Classe `ZonePartitions.Builder`

La classe `Builder`, publique, finale et imbriquée statiquement dans `ZonePartitions`, sert de bâtisseur à la classe `ZonePartitions`. Logiquement, ses attributs (privés) sont quatre _bâtisseurs_ de partitions de zones, de type `ZonePartition.Builder<…>`.

`Builder` offre un unique constructeur public :

*   `Builder(ZonePartitions initial)`, qui retourne un nouveau bâtisseur dont les quatre partitions sont initialement identiques à celles du groupe de quatre partitions donné.

En plus de ce constructeur, `Builder` offre les méthodes publiques ci-dessous. Avant de commencer à les programmer, il est fortement recommandé de lire les conseils de programmation plus bas.

*   `void addTile(Tile tile)`, qui ajoute aux quatre partitions les aires correspondant aux zones de la tuile donnée,
*   `void connectSides(TileSide s1, TileSide s2)`, qui connecte les deux bords de tuiles donnés, en connectant entre elles les aires correspondantes, ou lève `IllegalArgumentException` si les deux bords ne sont pas de la même sorte,
*   `void addInitialOccupant(PlayerColor player, Occupant.Kind occupantKind, Zone occupiedZone)`, qui ajoute un occupant initial, de la sorte donnée et appartenant au joueur donné, à l'aire contenant la zone donnée, ou lève `IllegalArgumentException` si la sorte d'occupant donnée ne peut pas occuper une zone de la sorte donnée,
*   `void removePawn(PlayerColor player, Zone occupiedZone)`, qui supprime un occupant — un pion — appartenant au joueur donné de l'aire contenant la zone donnée, ou lève `IllegalArgumentException` si la zone est un lac,
*   `void clearGatherers(Area<Zone.Forest> forest)`, qui supprime tous les occupants — des pions jouant le rôle de cueilleurs — de la forêt donnée,
*   `void clearFishers(Area<Zone.River> river)`, qui supprime tous les occupants — des pions jouant le rôle de pêcheurs — de la rivière donnée,
*   `ZonePartitions build()`, qui retourne le groupe de quatre partitions en cours de construction.

Notez que `removePawn` est destinée à être utilisée pour mettre en œuvre le pouvoir spécial du chaman, qui permet au joueur qui le pose de récupérer, s'il le désire, l'un de ses pions. C'est la raison pour laquelle `removePawn` ne peut que supprimer un pion, et pas une hutte, contrairement à `addInitialOccupant` qui permet d'ajouter aussi bien un pion qu'une hutte.

#### 3.2.1. Conseils de programmation

1.  Méthode `addTile`  
    
    La méthode `addTile` est destinée à être utilisée lorsqu'une nouvelle tuile est posée sur le plateau, afin d'ajouter ses aires aux différentes partitions. Il faut noter que `addTile` se charge uniquement d'ajouter les aires de la tuile, et pas de les connecter avec celles de tuiles voisines — que `addTile` ne peut de toute manière pas connaître. Néanmoins, la méthode `addTile` n'est pas totalement triviale à écrire, et nous vous conseillons donc une manière de procéder.
    
    Dans un premier temps, déterminez le nombre de connexions ouvertes de chaque _zone_, que vous stockez dans un tableau indexé par le numéro local de la zone — compris entre 0 et 9. Lorsqu'une rivière est attachée à un lac, considérez pour l'instant que la connexion entre les deux est ouverte.
    
    Par exemple, pour la tuile de départ, faites comme si la rivière n'était pas connectée au lac, ce qui implique que le nombre de connexions ouvertes de chaque zone est :
    
    *   2 pour les zones 0 (pré à l'auroch), 1 (forêt) et 3 (rivière),
    *   1 pour les zones 2 (pré vide) et 8 (lac).
    
    Dans un second temps, ajoutez toutes les zones aux différentes partitions, en tant que nouvelles aires inoccupées et constituées uniquement de la zone en question, avec le bon nombre de connexions ouvertes. Notez bien que le nombre de connexions ouvertes calculé précédemment est correct dans tous les cas sauf un : lorsqu'une rivière est connectée à un lac, elle forme une aire _rivière_ avec une connexion ouverte de moins que le nombre calculé, car le lac termine la rivière — mais pas le réseau hydrographique qui les contient les deux !
    
    Ainsi, lors de l'ajout de la tuile de départ, les aires sont à ce stade celles ci-dessous, le nombre entre crochets attaché à chacune d'entre elles donnant son nombre de connexions ouvertes :
    
    *   la partition des prés est {{560}\[2\], {562}\[1\]},
    *   la partition des forêts est {{561}\[2\]},
    *   la partition des rivières est {{563}\[1\]},
    *   la partition des zones aquatiques est {{563}\[2\], {568}\[1\]}.
    
    Finalement, parcourez une dernière fois les rivières attachées à un lac et, dans la partition des aires aquatiques, connectez leur aire avec celle du lac.
    
    Ainsi, lors de l'ajout de la tuile de départ, les aires finalement calculées sont les mêmes que ci-dessus, sauf celle des zones aquatiques, qui est maintenant {{563,568}\[1\]}, ce qui est correct.
    
2.  Méthode `connectSides`  
    
    La méthode `connectSides` a la caractéristique de devoir se comporter différemment en fonction du type exact de ses arguments. Par exemple, si elle est appelée avec deux bords pré, alors elle doit modifier la partition des prés ; si elle est appelée avec deux bords forêt, elle doit modifier la partition des forêts ; et ainsi de suite.
    
    Pour écrire le code correspondant, une solution serait de faire une séquence de tests avec `instanceof`, quelque chose comme :
    
    public void connectSides(TileSide s1, TileSide s2) {
      if (s1 instanceof TileSide.Meadow ms1) {
        Zone.Meadow m1 = ms1.meadow();
        if (s2 instanceof TileSide.Meadow ms2) {
          Zone.Meadow m2 = ms2.meadow();
          // … connecte m1 et m2
        } else {
          // … lève une exception
        }
      }
      // … autres cas
    }
    
    Ce genre de code est toutefois fastidieux à écrire, et peu élégant. Heureusement, depuis la version 21 de Java, il est possible de l'améliorer nettement grâce au **filtrage de motifs** (_pattern matching_), dont nous avons déjà vu une variante à l'étape 2 — celle liée à `instanceof`, qui est d'ailleurs utilisée dans le code ci-dessus.
    
    La première amélioration qu'il est possible d'apporter au code ci-dessus consiste à utiliser les **motifs d'enregistrements** (_record patterns_), qui permettent d'extraire dans des variables les composantes d'un enregistrement, en même temps que l'on teste son type exact. La version ci-dessous du code utilise cela pour directement nommer les zones pré `m1` et `m2` dans le contexte du test `instanceof` :
    
    public void connectSides(TileSide s1, TileSide s2) {
      if (s1 instanceof TileSide.Meadow(Zone.Meadow m1)) {
        if (s2 instanceof TileSide.Meadow(Zone.Meadow m2)) {
          // … connecte m1 et m2
        } else {
          // … lève une exception
        }
      }
      // … autres cas
    }
    
    La seconde amélioration que l'on peut apporter à ce code consiste à utiliser le filtrage de motif dans un `switch` plutôt que de faire une séquence de tests `instanceof`. On obtient alors le code suivant :
    
    public void connectSides4(TileSide s1, TileSide s2) {
      switch (s1) {
        case TileSide.Meadow(Zone.Meadow m1) -> {
          if (s2 instanceof TileSide.Meadow(Zone.Meadow m2)) {
            // … connecte m1 et m2
          } else {
            // … lève une exception
          }
        }
        // … autres cas
      }
    }
    
    Finalement, on peut encore apporter une troisième amélioration à ce code en tirant parti du fait qu'il est possible d'attacher, au moyen du mot-clef `when`, une condition à un cas d'un tel `switch`. La condition doit être vraie pour que le code correspondant au cas soit exécuté. On obtient alors le code suivant :
    
    public void connectSides(TileSide s1, TileSide s2) {
      switch (s1) {
        case TileSide.Meadow(Zone.Meadow m1)
         when s2 instanceof TileSide.Meadow(Zone.Meadow m2) ->
          // … connecte m1 et m2
        // … autres cas
        default ->
          // … lève une exception
      }
    }
    
    Le filtrage de motifs est un concept extrêmement puissant, développé à l'origine dans le contexte des langages de programmation fonctionnels, mais qui s'est depuis popularisé et a été ajouté à la plupart des langages modernes. Il vaut donc la peine de se familiariser avec lui, et les personnes intéressées trouveront plus de détails à son sujet dans [la §6 du guide des améliorations apportées à Java 21](https://docs.oracle.com/en/java/javase/21/language/pattern-matching.html).
    
3.  Méthodes `addInitialOccupant` et `removePawn`  
    
    Tout comme `connectSides`, `addInitialOccupant` et `removePawn` doivent se comporter différemment en fonction du type exact de la zone qu'on leur passe. S'il s'agit d'une zone forêt, alors elles doivent ajouter/enlever un pion (cueilleur) de l'aire forêt qui contient la zone ; s'il s'agit d'une zone pré, alors elles doivent ajouter/enlever un pion (chasseur) de l'aire pré qui contient la zone ; et ainsi de suite. Là aussi, un `switch` qui distingue ces différents cas au moyen du filtrage de motifs permet d'écrire le code de manière concise et élégante.
    

### 3.3. Interface `TextMaker` (fournie)

Pour faciliter votre travail, nous mettons à votre disposition l'interface `TextMaker`, publique, qui appartient au paquetage principal et est destinée à être implémentée par des classes capables de générer tout le texte qui apparaît dans l'interface graphique de ChaCuN — principalement le texte des messages, mais aussi celui donnant le nombre de points de chaque joueur, leur nom, etc.

Cette interface vous est fournie dans [une archive Zip](f/chacun_given_4.zip) qu'il vous faut télécharger avant d'en extraire le contenu. Cela fait, le plus simple pour la placer dans votre projet consiste à trouver le fichier `TextMaker.java` qu'elle contient puis de le glisser dans la zone _Project_ de votre projet IntelliJ, dans le paquetage `ch.epfl.chacun`.

Après avoir importé cette interface, lisez les commentaires attachés à ses méthodes, dont vous aurez besoin pour créer les messages du tableau d'affichage, comme expliqué à la §[3.5](#sec/java/messageboard) plus bas.

Notez que certaines méthodes de `TextMaker` prennent en argument une **table associative** de type `Map<…>`. Si vous êtes en avance dans le projet, il est possible que ce type de collection n'ait pas encore été vu au cours. Dans ce cas, nous vous conseillons d'ignorer les méthodes en question dans un premier temps, et d'y revenir une fois la matière examinée au cours.

### 3.4. Enregistrement `MessageBoard.Message`

L'enregistrement `Message`, imbriqué dans l'enregistrement `MessageBoard` décrit à la section suivante, public et immuable, représente un message affiché sur le tableau d'affichage. Il possède les attributs suivants :

*   `String text`, le texte du message,
*   `int points`, les points associés au message — qui peuvent valoir 0, par exemple si le message ne signale pas un gain de points,
*   `Set<PlayerColor> scorers`, l'ensemble des joueurs ayant remportés les points, qui peut être vide si le message ne signale pas un gain de points,
*   `Set<Integer> tileIds`, les identifiants des tuiles concernées par le message, ou un ensemble vide si le message ne concerne pas un ensemble de tuiles.

Les trois derniers attributs n'ont généralement de sens que pour les messages qui décrivent un gain de points, mais cela constitue la grande majorité d'entre eux. Par « tuiles concernées par le message », on entend généralement les tuiles qui ont permis au(x) joueur(s) de gagner les points.

En dehors de ces attributs et des méthodes ajoutées automatiquement par Java aux enregistrements, `Message` ne possède qu'un constructeur compact qui vérifie que le texte passé n'est pas `null`, que les points ne sont pas inférieurs à 0, et copie les deux ensembles pour garantir l'immuabilité.

### 3.5. Enregistrement `MessageBoard`

L'enregistrement `MessageBoard` du paquetage principal, public et immuable, représente le contenu du tableau d'affichage. Il possède les attributs suivants :

*   `TextMaker textMaker`, l'objet permettant d'obtenir le texte des différents messages,
*   `List<Message> messages`, la liste des messages affichés sur le tableau, du plus ancien au plus récent.

Le constructeur compact de `MessageBoard` garantit l'immuabilité de la classe. En dehors de ce constructeur, `MessageBoard` offre les méthodes publiques suivantes :

*   `Map<PlayerColor, Integer> points()`, qui retourne une table associant à tous les joueurs figurant dans les gagnants (_scorers_) d'au moins un message, le nombre total de points obtenus,
*   `MessageBoard withScoredForest(Area<Zone.Forest> forest)`, qui retourne un tableau d'affichage identique au récepteur, sauf si la forêt donnée est occupée, auquel cas le tableau contient un nouveau message signalant que ses occupants majoritaires ont remporté les points associés à sa fermeture,
*   `MessageBoard withClosedForestWithMenhir(PlayerColor player, Area<Zone.Forest> forest)`, qui retourne un tableau d'affichage identique au récepteur, mais avec un nouveau message signalant que le joueur donné a le droit de jouer un second tour après avoir fermé la forêt donnée, car elle contient un ou plusieurs menhirs,
*   `MessageBoard withScoredRiver(Area<Zone.River> river)`, qui retourne un tableau d'affichage identique au récepteur, sauf si la rivière donnée est occupée, auquel cas le tableau contient un nouveau message signalant que ses occupants majoritaires ont remporté les points associés à sa fermeture,
*   `MessageBoard withScoredHuntingTrap(PlayerColor scorer, Area<Zone.Meadow> adjacentMeadow)`, qui retourne un tableau d'affichage identique au récepteur, sauf si la pose de la fosse à pieux a permis au joueur donné, qui l'a posée, de remporter des points, auquel cas le tableau contient un nouveau message signalant cela — le pré donné comportant les mêmes occupants que le pré contenant la fosse, mais uniquement les zones se trouvant à sa portée,
*   `MessageBoard withScoredLogboat(PlayerColor scorer, Area<Zone.Water> riverSystem)`, qui retourne un tableau d'affichage identique au récepteur, mais avec un nouveau message signalant que le joueur donné a obtenu les points correspondants à la pose de la pirogue dans le réseau hydrographique donné,
*   `MessageBoard withScoredMeadow(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals)`, qui retourne un tableau d'affichage identique au récepteur, sauf si le pré donné est occupé et que les points qu'il rapporte à ses occupants majoritaires — calculés en faisant comme si les animaux annulés donnés n'existaient pas — sont supérieurs à 0, auquel cas le tableau contient un nouveau message signalant que ces joueurs-là ont remporté les points en question,
*   `MessageBoard withScoredRiverSystem(Area<Zone.Water> riverSystem)`, qui retourne un tableau d'affichage identique au récepteur, sauf si le réseau hydrographique donné est occupé et que les points qu'il rapporte à ses occupants majoritaires sont supérieurs à 0, auquel cas le tableau contient un nouveau message signalant que ces joueurs-là ont remporté les points en question,
*   `MessageBoard withScoredPitTrap(Area<Zone.Meadow> adjacentMeadow, Set<Animal> cancelledAnimals)`, qui retourne un tableau d'affichage identique au récepteur, sauf si le pré donné, qui contient la grande fosse à pieux, est occupé et que les points — calculés en faisant comme si les animaux annulés donnés n'existaient pas — qu'il rapporte à ses occupants majoritaires sont supérieurs à 0, auquel cas le tableau contient un nouveau message signalant que ces joueurs-là ont remporté les points en question ; comme pour la « petite » fosse à pieux, le pré donné comporte les mêmes occupants que le pré contenant la fosse, mais uniquement les zones se trouvant à sa portée,
*   `MessageBoard withScoredRaft(Area<Zone.Water> riverSystem)`, qui retourne un tableau d'affichage identique au récepteur, sauf si le réseau hydrographique donné, qui contient le radeau, est occupé, auquel cas le tableau contient un nouveau message signalant que ses occupants majoritaires ont remporté les points correspondants,
*   `MessageBoard withWinners(Set<PlayerColor> winners, int points)`, qui retourne un tableau d'affichage identique au récepteur, mais avec un nouveau message signalant que le(s) joueur(s) donné(s) a/ont remporté la partie avec le nombre de points donnés.

#### 3.5.1. Conseils de programmation

Les méthodes `withScoredHuntingTrap`, `withScoredMeadow` et `withScoredPitTrap` doivent toutes les trois déterminer quels sont les animaux présents dans le pré qu'on leur a donné, en prenant garde à exclure les éventuels animaux annulés. Cela peut se faire aisément au moyen d'une méthode de `Area`.

Une fois ces animaux déterminés, le nombre de mammouths, aurochs et cerfs peut être passé à une méthode de `Points` pour déterminer les points qu'ils rapportent.

Finalement, la totalité des animaux non annulés peut être passée à une méthode de `TextMaker` pour obtenir le texte du message à ajouter au tableau.

### 3.6. Tests

Comme pour l'étape précédente, nous ne vous fournissons plus de tests mais [un fichier de vérification de signatures](f/SignatureChecks_4.java) à importer dans votre projet.

Notez que, si nous vous avons fourni l'interface `TextMaker`, nous ne vous avons fourni aucune mise en œuvre de cette interface. Pour pouvoir tester la classe `MessageBoard`, il vous faudra donc, dans vos tests, créer une classe implémentant cette interface. Nous vous conseillons de rendre cette classe aussi simple que possible, et en particulier de ne pas essayer de générer des messages grammaticalement corrects à ce stade, car vous ferez cela dans une étape ultérieure. Une classe dont toutes les méthodes retournent une simple concaténation de la représentation textuelle des arguments suffit largement aux tests. Par exemple, la méthode `playersScoredForest` ci-dessous fait l'affaire :

String playersScoredForest(Set<PlayerColor> scorers,
                           int points,
                           int mushroomGroupCount,
                           int tileCount) {
  return new StringJoiner(" ")
    .add(scorers.toString())
    .add(String.valueOf(points))
    .add(String.valueOf(mushroomGroupCount))
    .add(String.valueOf(tileCount))
    .toString();
}

(La classe [`StringJoiner`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/StringJoiner.html) est un bâtisseur de chaînes similaire à [`StringBuilder`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/StringBuilder.html), mais qui sépare les différentes chaînes qu'on lui ajoute au moyen du séparateur fourni à son constructeur.)

## 4. Résumé

Pour cette étape, vous devez :

*   écrire les classes `ZonePartitions`, `ZonePartitions.Builder`, `MessageBoard` et `MessageBoard.Message` comme décrit plus haut,
*   tester votre code,
*   documenter la totalité des entités publiques que vous avez définies,
*   rendre votre code au plus tard le **15 mars 2024 à 18h00**, au moyen du programme `Submit.java` fourni et des jetons disponibles sur votre [page privée](https://cs108.epfl.ch/my/).

Ce rendu est un rendu testé, auquel 18 points sont attribués, au prorata des tests unitaires passés avec succès.

N'attendez surtout pas le dernier moment pour effectuer votre rendu, car vous n'êtes pas à l'abri d'imprévus. **Souvenez-vous qu'aucun retard, aussi insignifiant soit-il, ne sera toléré !**

  Plateau de jeu     function orgHighlight() { document .querySelectorAll('pre.src') .forEach((el) => { hljs.highlightElement(el); }); } addEventListener('DOMContentLoaded', orgHighlight, false); addEventListener('load', orgHighlight, false);

# Plateau de jeu

ChaCuN – étape 5

## 1. Introduction

Le but de cette étape est d'écrire la classe représentant le plateau de jeu.

## 2. Concepts

Comme cela a été dit à l'étape 1, ChaCuN se joue sur un plateau de jeu carré de 25×25 cases, la case centrale étant occupée par la tuile de départ. Ces cases sont indexées par une paire d'index (x, y) tels que la tuile de départ occupe la position (0, 0), et les deux index augmentent dans le sens de lecture — de gauche à droite, et de haut en bas.

### 2.1. Représentation du plateau de jeu

Le plateau de jeu peut être représenté de manière assez naturelle au moyen d'un tableau de tuiles. Ce tableau pourrait être bidimensionnel, mais un tableau unidimensionnel convient également très bien, et présente même certains avantages par rapport à un bidimensionnel — copie simplifiée, indexation au moyen d'un unique entier, etc. — donc c'est ce que nous utiliserons.

Ce tableau aura une taille de 625 (25×25) éléments, et sera organisé dans l'ordre de lecture en partant de la cellule en haut à gauche du plateau, et en parcourant les lignes avant les colonnes — ce qu'on appelle [l'ordre _row-major_](https://en.wikipedia.org/wiki/Row-_and_column-major_order) en anglais. En d'autres termes, le premier élément du tableau contiendra la tuile à la position (-12, -12), le second la tuile à la position (-11, -12), et ainsi de suite jusqu'au dernier élément du tableau qui contiendra la tuile à la position (12, 12). La tuile de départ, dont la position est (0, 0), sera donc toujours à l'index 312.

Cette représentation, très simple, a toutefois deux inconvénients.

Le premier est que, sachant qu'il y a un total de 95 tuiles dans le jeu, ce tableau de 625 éléments sera toujours en grande partie vide. Dès lors, certaines opérations qu'on pourrait vouloir effectuer sur lui — p. ex. parcourir la totalité des tuiles posées — sont assez coûteuses. En pratique, ce problème n'en est toutefois pas vraiment un, car un tableau de 625 éléments reste petit pour les ordinateurs modernes, et les performances ne sont de toute manière pas un soucis majeur dans ce projet.

Le second inconvénient de cette représentation est qu'il n'est pas possible de connaître l'ordre dans lequel les tuiles ont été posées sur le plateau. Cet inconvénient est plus sérieux pour nous, car nous aurons parfois besoin de savoir quelle tuile a été posée en dernier.

Une manière simple d'éviter ces deux inconvénients consiste à ajouter au tableau contenant les tuiles un second tableau contenant les index, dans le premier tableau, des tuiles posées, dans l'ordre dans lequel elles l'ont été. Par exemple, imaginons le plateau suivant, composé — de gauche à droite — des tuiles 17, 56 et 27.

![board_01-three-tiles.png](i/board_01-three-tiles.png)

Figure 1 : Le plateau composé des tuiles 17, 56 et 27

La tuile 56 étant la tuile de départ, c'est bien entendu elle qui a été déposée en premier. Admettons que la tuile 17 ait été déposée ensuite, puis la 27. Dans ce cas, les deux tableaux susmentionnés contiendraient les éléments suivants :

*   celui contenant les tuiles, d'une taille de 625 éléments, serait totalement vide sauf aux index suivants :
    *   311, qui contiendrait la tuile 17,
    *   312, qui contiendrait la tuile 56,
    *   313, qui contiendrait la tuile 27,
*   celui contenant les index des tuiles posées, d'une taille de 3 éléments, contiendrait, dans l'ordre, 312, 311 et 313.

Parcourir la totalité des tuiles posées est alors très rapide, puisqu'il suffit de parcourir les trois éléments du second tableau et les utiliser pour indexer le premier. Déterminer quelle tuile a été déposée en dernier est aussi très simple, puisqu'il suffit d'extraire le dernier élément du second tableau (313 ici) et de regarder quelle tuile se trouve à cet index dans le premier tableau (la tuile 27).

### 2.2. Positions d'insertion

Les cases du plateau qui ne contiennent aucune tuile mais dont au moins une des quatre cases voisines en contient une sont celles sur lesquelles la prochaine tuile pourrait potentiellement être déposée. Nous nommerons les positions de ces cases les **positions d'insertion** (_insertion positions_).

Par exemple, au tout début de la partie, lorsque la première tuile doit être déposée, les positions d'insertion sont celles des quatre cases voisines de la tuile de départ, c.-à-d. (-1,0), (0,-1), (1,0) et (0,1).

Dans l'interface graphique, les cases correspondant aux positions d'insertion seront coloriées avec la couleur du joueur courant, comme la copie d'écran présentée dans [l'introduction au projet](00_introduction.html) l'illustre.

### 2.3. Possibilité de jouer une tuile

Lorsque le joueur courant doit poser une tuile, il ne peut le faire que sur l'une des positions d'insertion, et à condition que les bords de la tuile qu'il pose — après l'avoir éventuellement tournée d'un certain nombre de quarts de tour — soient de la même sorte que ceux des tuiles voisines.

On peut dès lors déterminer relativement facilement si une tuile peut être placée ou non, puisqu'il suffit de faire une recherche exhaustive parmi toutes les positions d'insertion et toutes les rotations, pour voir si une combinaison convient.

Le plateau ci-dessous illustre une situation dans laquelle il peut être impossible de placer une tuile.

![board_05-unplaceable-tile.png](i/board_05-unplaceable-tile.png)

Figure 2 : Un plateau sans bord rivière libre

Il est facile de voir que la tuile ci-dessous ne peut pas y être placée, puisqu'aucune combinaison d'une des 6 positions d'insertions et des 4 rotations ne lui convient.

![board_05-all-river-tile.png](i/board_05-all-river-tile.png)

Figure 3 : Une tuile dont tous les bords sont des rivières

Lors d'une partie de ChaCuN, lorsque la prochaine tuile ne peut pas être jouée, elle est simplement éliminée du jeu.

### 2.4. Pré adjacent

Les deux fosses à pieux du jeu ont un comportement particulier, puisqu'elles ont un effet qui ne s'applique qu'aux zones du pré qu'elles occupent et qui se trouvent soit sur la tuile contenant la fosse, soit sur l'une de ses 8 voisines.

Nous nommerons ce sous-pré le **pré adjacent** (_adjacent meadow_) à la fosse. Une particularité de ce pré adjacent est que nous considérerons que ses occupants sont les mêmes que ceux du pré complet, et pas seulement ceux qui se trouvent sur les tuiles voisines de la fosse.

L'image ci-dessous montre un pré contenant la fosse à pieux et deux occupants, un rouge et un vert. Le pré complet est entouré de pointillés rouges, tandis que le pré adjacent à la fosse l'est d'un trait plein. Il est important de noter que les occupants du pré adjacent sont les mêmes que ceux du pré complet, à savoir un chasseur rouge et un chasseur vert, malgré le fait que le chasseur rouge occupe une zone qui n'appartient pas au pré adjacent — mais appartient au pré complet.

![trap-adjacent-meadow;128.png](i/trap-adjacent-meadow;128.png)

Figure 4 : Le pré adjacent à la petite fosse à pieux

Les chasseurs jaune et bleu ne font quant à eux pas partie du pré contenant la fosse à pieux, donc ils ne font pas non plus partie du pré adjacent — malgré le fait que le chasseur bleu se trouve sur une tuile qui est à la portée de la fosse.

## 3. Mise en œuvre Java

### 3.1. Classe `Board`

La classe `Board` du paquetage principale, publique et immuable, représente le plateau de jeu.

Contrairement à la plupart des classes écrites jusqu'à présent, il ne s'agit _pas_ d'un enregistrement, car ses attributs sont des tableaux Java primitifs, qui sont toujours modifiables. Ces tableaux ne doivent donc pas être exposés à l'extérieur, faute de quoi la classe ne peut être immuable. Nous sommes donc forcés d'en faire une classe « normale », car les enregistrements ne peuvent pas avoir d'attributs inaccessibles depuis l'extérieur.

`Board` possède les attributs (privés) suivants :

*   un tableau de tuiles placées de type `PlacedTile[]`, contenant 625 éléments pour la plupart égaux à `null`, comme décrit plus haut,
*   un tableau d'entiers de type `int[]`, contenant les index, dans le premier tableau, des tuiles posées sur le plateau, dans l'ordre dans lequel elles ont été posées,
*   une instance de `ZonePartitions`, dont le contenu correspond à celui du plateau — c.-à-d. que les partitions sont celles qui correspondent à celles des zones des tuiles posées,
*   l'ensemble des animaux annulés, de type `Set<Animal>`.

Ces différents attributs sont tous finaux et initialisés par un constructeur privé (!) qui, pour des questions de performances, ne fait aucune copie défensive de ses arguments ! Sachant que la classe doit être immuable, cela implique que ce sont aux utilisateurs du constructeur de faire attention à ce que les objets qu'ils lui passent en argument ne changent plus dans le futur. Comme ce constructeur est privé, tous ses utilisateurs se trouvent forcément dans la classe `Board` elle-même, et il n'est donc pas déraisonnable de placer une telle exigence sur eux.

En plus de ces attributs privés, `Board` possède les deux attributs publics, statiques et finaux suivants :

*   `int REACH`, la « portée » du plateau, qui est le nombre de cases qui séparent la case centrale de l'un des bords du plateau, soit 12,
*   `Board EMPTY`, le plateau vide, qui ne contient absolument aucune tuile, même pas celle de départ.

Finalement, `Board` offre les méthodes publiques suivantes :

*   `PlacedTile tileAt(Pos pos)`, qui retourne la tuile à la position donnée, ou `null` s'il n'y en a aucune ou si la position se trouve hors du plateau,
*   `PlacedTile tileWithId(int tileId)`, qui retourne la tuile dont l'identité est celle donnée, ou lève `IllegalArgumentException` si cette tuile ne se trouve pas sur le plateau,
*   `Set<Animal> cancelledAnimals()`, qui retourne l'ensemble des animaux annulés,
*   `Set<Occupant> occupants()`, qui retourne la totalité des occupants se trouvant sur les tuiles du plateau,
*   `Area<Zone.Forest> forestArea(Zone.Forest forest)`, qui retourne l'aire forêt contenant la zone donnée, ou lève `IllegalArgumentException` si la zone en question n'appartient pas au plateau,
*   `Area<Zone.Meadow> meadowArea(Zone.Meadow meadow)`, identique à `forestArea` mais pour une aire pré,
*   `Area<Zone.River> riverArea(Zone.River riverZone)`, identique à `forestArea` mais pour une aire rivière,
*   `Area<Zone.Water> riverSystemArea(Zone.Water water)`, identique à `forestArea` mais pour un réseau hydrographique,
*   `Set<Area<Zone.Meadow>> meadowAreas()`, qui retourne l'ensemble de toutes les aires pré du plateau,
*   `Set<Area<Zone.Water>> riverSystemAreas()`, identique à `meadowAreas` mais pour les réseaux hydrographiques,
*   `Area<Zone.Meadow> adjacentMeadow(Pos pos, Zone.Meadow meadowZone)`, qui retourne le pré adjacent à la zone donnée, sous la forme d'une aire qui ne contient que les zones de ce pré mais tous les occupants du pré complet, et qui, pour simplifier, ne possède aucune connexion ouverte,
*   `int occupantCount(PlayerColor player, Occupant.Kind occupantKind)`, qui retourne le nombre d'occupants de la sorte donnée appartenant au joueur donné et se trouvant sur le plateau,
*   `Set<Pos> insertionPositions()`, qui retourne l'ensemble des positions d'insertions du plateau,
*   `PlacedTile lastPlacedTile()`, qui retourne la dernière tuile posée — qui peut être la tuile de départ si la première tuile normale n'a pas encore été placée — ou `null` si le plateau est vide,
*   `Set<Area<Zone.Forest>> forestsClosedByLastTile()`, qui retourne l'ensemble de toutes les aires forêts qui ont été fermées suite à la pose de la dernière tuile, ou un ensemble vide si le plateau est vide,
*   `Set<Area<Zone.River>> riversClosedByLastTile()`, identique à `forestsClosedByLastTile` mais pour les aires rivières,
*   `boolean canAddTile(PlacedTile tile)`, qui retourne vrai ssi la tuile placée donnée pourrait être ajoutée au plateau, c.-à-d. si sa position est une position d'insertion et que chaque bord de la tuile qui touche un bord d'une tuile déjà posée est de la même sorte que lui,
*   `boolean couldPlaceTile(Tile tile)`, qui retourne vrai ssi la tuile donnée pourrait être posée sur l'une des positions d'insertion du plateau, éventuellement après rotation,
*   `Board withNewTile(PlacedTile tile)`, qui retourne un plateau identique au récepteur, mais avec la tuile donnée en plus, ou lève `IllegalArgumentException` si le plateau n'est pas vide et la tuile donnée ne peut pas être ajoutée au plateau,
*   `Board withOccupant(Occupant occupant)`, qui retourne un plateau identique au récepteur, mais avec l'occupant donné en plus, ou lève `IllegalArgumentException` si la tuile sur laquelle se trouverait l'occupant est déjà occupée,
*   `Board withoutOccupant(Occupant occupant)`, qui retourne un plateau identique au récepteur, mais avec l'occupant donné en moins,
*   `Board withoutGatherersOrFishersIn(Set<Area<Forest>> forests, Set<Area<River>> rivers)`, qui retourne un plateau identique au récepteur mais sans aucun occupant dans les forêts et les rivières données,
*   `Board withMoreCancelledAnimals(Set<Animal> newlyCancelledAnimals)`, qui retourne un plateau identique au récepteur mais avec l'ensemble des animaux donnés ajouté à l'ensemble des animaux annulés.

En plus de ces méthodes, `Board` redéfinit les méthodes `equals` et `hashCode` de `Object` pour faire en sorte que les instances de `Board` soient « comparées par structure » (voir les conseils de programmation).

#### 3.1.1. Conseils de programmation

1.  Méthode `cancelledAnimals`  
    
    Souvenez-vous que `Board` est une classe immuable, ce qui implique que la méthode `cancelledAnimal` doit garantir que l'ensemble qu'elle retourne n'est pas modifiable.
    
2.  Méthode `equals`  
    
    La méthode `equals` doit être redéfinie afin de garantir que les instances de `Board` sont comparées « par structure ». Cela signifie que deux instances de `Board` doivent être considérées comme égales si et seulement si le contenu de tous leurs attributs sont égaux.
    
    Souvenez-vous que pour tester si deux tableaux ont le même contenu, il n'est _pas_ valide d'utiliser leur méthode `equals`, car celle-ci fait une comparaison par référence ! Pour effectuer une comparaison par contenu, les différentes versions de la méthode (statique) [`equals`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html#equals(int%5B%5D,int%5B%5D)) de [`Arrays`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html) doivent être utilisées.
    
3.  Méthode `hashCode`  
    
    Comme nous le verrons plus tard au cours lorsque nous examinerons le hachage, la méthode `hashCode` doit toujours être redéfinie lorsque la méthode `equals` l'est.
    
    Pour les enregistrements, Java se charge automatiquement de redéfinir les deux méthodes, comme nous l'avons vu. Pour la classe `Board`, c'est à nous de le faire, étant donné qu'il s'agit d'une classe « normale ».
    
    Pour cela, votre méthode `hashCode` doit effectuer les opérations suivantes :
    
    *   passer le tableau contenant les tuiles à la méthode statique [`hashCode`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html#hashCode(java.lang.Object%5B%5D)) de [`Arrays`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html), pour obtenir un premier entier,
    *   faire de même avec le tableau contenant les index des tuiles,
    *   passer ces deux entiers et les deux autres attributs de la classe — le groupe de partitions et l'ensemble des animaux annulés — à la méthode [`hash`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Objects.html#hash(java.lang.Object...)) de [`Objects`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Objects.html), et retourner l'entier qu'elle retourne comme résultat de votre méthode `hashCode`.
    
    L'utilité de ces différentes opérations deviendra claire plus tard.
    
4.  Méthodes de dérivation  
    
    Toutes les méthodes « de dérivation » — celles dont le nom commence par `with` et qui permettent d'obtenir un nouveau plateau dérivé du récepteur — doivent prendre garde à copier les tableaux avant de les modifier puis de les passer au constructeur, faute de quoi l'immuabilité de la classe ne serait pas garantie.
    
    Lorsque les tableaux ne changent pas de taille, cette copie peut se faire au moyen de la méthode [`clone`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Object.html#clone()). Lorsqu'ils changent de taille, la méthode [`copyOf`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html#copyOf(int%5B%5D,int)) de [`Arrays`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html) est préférable, car elle permet de copier un tableau dans un nouveau tableau de taille différente de l'original.
    
    Les méthodes de dérivation doivent également s'assurer que les partitions de zones correspondent toujours au contenu du plateau. Pour ce faire, elles doivent toujours calculer non seulement les nouveaux tableaux contenant les tuiles et leurs indices, mais aussi les nouvelles partitions correspondantes.
    

### 3.2. Tests

Comme d'habitude, nous ne vous fournissons plus de tests mais [un fichier de vérification de signatures](f/SignatureChecks_5.java) à importer dans votre projet.

## 4. Résumé

Pour cette étape, vous devez :

*   écrire la classe `Board` selon les indications données plus haut,
*   tester votre code,
*   documenter la totalité des entités publiques que vous avez définies,
*   rendre votre code au plus tard le **22 mars 2024 à 18h00**, au moyen du programme `Submit.java` fourni et des jetons disponibles sur votre [page privée](https://cs108.epfl.ch/my/).

Ce rendu est un rendu testé, auquel 18 points sont attribués, au prorata des tests unitaires passés avec succès.

N'attendez surtout pas le dernier moment pour effectuer votre rendu, car vous n'êtes pas à l'abri d'imprévus. **Souvenez-vous qu'aucun retard, aussi insignifiant soit-il, ne sera toléré !**

  État de la partie     function orgHighlight() { document .querySelectorAll('pre.src') .forEach((el) => { hljs.highlightElement(el); }); } addEventListener('DOMContentLoaded', orgHighlight, false); addEventListener('load', orgHighlight, false);

# État de la partie

ChaCuN – étape 6

## 1. Introduction

Le but de cette étape, la dernière de la première partie du projet, est d'écrire la classe représentant l'état d'une partie de ChaCuN.

Notez que cette étape devra être rendue deux fois :

1.  pour le rendu testé habituel (délai : le **5/4** à **18h00**),
2.  pour le rendu intermédiaire (délai : le **12/4** à **18h00**).

Le deuxième de ces rendus sera corrigé par lecture du code de vos étapes 1 à 6, et il vous faudra donc soigner sa qualité et sa documentation. Il est fortement conseillé de lire [notre guide à ce sujet](../g/style.html).

## 2. Concepts

### 2.1. Déroulement d'un tour

Dans la plupart des cas, un tour de ChaCuN se déroule de manière extrêmement simple : le joueur courant tire la prochaine tuile du tas des tuiles normales et la place sur le plateau avant de poser éventuellement un occupant — pion ou hutte — sur l'une de ses zones. Ensuite, les points correspondants aux éventuelles forêts et rivières fermées par la pose de cette tuile sont attribués à leurs occupants majoritaires, et _tous_ les occupants de ces aires sont retournés à leurs propriétaires. Le tour du joueur suivant peut alors commencer, sauf s'il ne reste plus de tuile à placer, auquel cas la partie se termine.

En pratique, un tour peut être plus compliqué que cela, en raison de l'existence des tuiles menhir et des pouvoirs spéciaux que certaines d'entre elles possèdent.

En effet, comme nous l'avons vu, lorsque le joueur courant ferme au moins une forêt contenant un menhir en posant sa tuile, alors il a le droit de jouer un second tour en plaçant cette fois une tuile tirée du tas des tuiles menhir — en supposant qu'il en reste. Parmi ces tuiles menhir, certaines ont un pouvoir spécial, et trois de ces pouvoirs impliquent d'effectuer immédiatement une action :

*   le chaman donne au joueur qui le pose le droit de reprendre en main, s'il le désire, l'un des pions qu'il a posé précédemment sur le plateau,
*   la pirogue permet au joueur qui la pose d'obtenir des points qui dépendent du nombre de lacs se trouvant dans le réseau hydrographique qui la contient,
*   la fosse à pieux permet au joueur qui la pose d'obtenir des points qui dépendent des animaux présents dans le pré adjacent à la fosse, selon le principe décrit à la §[2.2](#sec/hunting-trap) ci-dessous.

Il faut noter que même si le joueur qui pose une tuile menhir ferme une forêt contenant un menhir, il n'a pas le droit à un troisième tour. Dès lors, un joueur joue toujours un maximum de deux tours avant de passer la main au joueur suivant.

### 2.2. Fosse à pieux

Lorsqu'un joueur pose la tuile contenant la fosse à pieux, il obtient les points correspondants aux animaux présents dans le pré adjacent.

Pour déterminer ces points, on commence par compter le nombre d'animaux des différents types — mammouths, aurochs, cerfs et smilodons — présents dans le pré adjacent.

Ensuite, si le nombre de smilodons est supérieur ou égal au nombre de cerfs, alors on ignore tous les cerfs — car ils sont dévorés par les smilodons. Sinon, on ignore un nombre de cerfs égal au nombre de smilodons, pour la même raison.

Finalement, on calcule les points avec les animaux restants, un mammouth rapportant 3 points, un auroch 2 et un cerf 1.

Une fois les points décomptés, _tous_ les animaux présents dans le pré adjacent à la fosse sont **annulés** (_cancelled_), c.-à-d. ignorés pour le reste de la partie. Dans le jeu physique — et dans l'interface graphique — ces animaux sont recouverts d'une croix indiquant leur annulation.

### 2.3. Calcul final des points

Une fois que la dernière tuile normale a été posée et que son placeur a terminé son tour — ou ses deux tours s'il a pu jouer une tuile menhir — le décompte final des points est effectué. Il consiste à attribuer aux joueurs possédant les occupants majoritaires des prés (chasseurs) et des réseaux hydrographiques (huttes de pêcheurs) les points correspondants aux aires qu'ils occupent, comme décrit ci-dessous.

#### 2.3.1. Prés

Les joueurs possédant les chasseurs majoritaires d'un pré obtiennent un nombre de points qui dépend des animaux qui s'y trouvent, selon les mêmes règles que pour la fosse à pieux — les cerfs mangés par les smilodons sont ignorés, et les points sont attribués en fonction des animaux restants. Toutefois, si le pré contient le feu — un pouvoir spécial — alors aucun cerf n'est dévoré par les smilodons, qui fuient le pré[1](#fn.1).

De plus, lorsqu'un pré contient la _grande_ fosse à pieux, les chasseurs majoritaires obtiennent une seconde fois les points associés aux animaux présents dans le pré adjacent à la fosse. Afin de maximiser ces points additionnels, lorsque la grande fosse à pieux se trouve dans un pré, les cerfs sont annulés en commençant par ceux qui ne se trouvent _pas_ dans le pré adjacent à la fosse.

#### 2.3.2. Réseaux hydrographiques

Les joueurs possédant les huttes majoritaires d'un réseau hydrographique obtiennent un point par poisson qui y nage.

De plus, si le réseau hydrographique contient le radeau, alors les propriétaires des huttes majoritaires obtiennent en plus un point par lac qu'il contient.

### 2.4. Actions

Une partie de ChaCuN progresse au fur et à mesure que les joueurs effectuent les actions que l'on attend d'eux. Par exemple, lorsque la partie vient de commencer, le premier joueur doit tout d'abord placer la première tuile du tas des tuiles normales ; cela fait, il doit éventuellement l'occuper ; ensuite, le prochain joueur doit tirer et placer la prochaine tuile ; et ainsi de suite.

En d'autres termes, à chaque instant d'une partie, une action doit être effectuée pour que la partie puisse progresser. Nous en distinguerons cinq, auxquelles nous attribuerons des noms anglais concis qui apparaîtront aussi dans le code. Il s'agit de :

`START_GAME`

la tuile de départ doit être placée au centre du plateau et la tuile au sommet du tas des tuiles normales doit être retournée en vue d'être placée par le premier joueur,

`PLACE_TILE`

le joueur courant doit placer la tuile courante, qui est soit une tuile normale soit une tuile menhir,

`RETAKE_PAWN`

le joueur courant — qui vient de poser la tuile contenant le chaman — doit décider s'il désire ou non reprendre l'un des pions qu'il a posé précédemment sur le plateau, et si oui, lequel,

`OCCUPY_TILE`

le joueur courant — qui vient de placer une tuile — doit décider s'il désire occuper l'une de ses zones au moyen d'un des occupants qu'il a en main,

`END_GAME`

le décompte des points et l'annonce du ou des vainqueurs doit être faite, car le dernier joueur a terminé son (ou ses) tour(s) et le tas des tuiles normales est vide.

Une fois qu'une action a été effectuée, il est possible de déterminer la prochaine action qui doit l'être, et ce jusqu'à ce que la partie soit terminée. Par exemple, une fois que l'action `PLACE_TILE` a été effectuée, c.-à-d. que le joueur courant a placé la prochaine tuile, on peut déterminer que la prochaine action à effectuer est :

*   `RETAKE_PAWN` si la tuile que le joueur vient de poser est celle contenant le chaman et qu'il possède au moins un pion sur le plateau, sinon
*   `OCCUPY_TILE` si au moins une zone de la tuile que le joueur vient de poser peut être occupée, et qu'il a l'occupant nécessaire en main, sinon
*   `PLACE_TILE` (tuile menhir) si le joueur a fermé, avec une tuile normale, au moins une forêt contenant un menhir, et qu'il reste encore une tuile menhir qu'il est possible de placer sur le tas correspondant, sinon
*   `PLACE_TILE` (tuile normale, à placer par le joueur suivant) s'il reste encore une tuile normale qu'il est possible de placer sur le tas correspondant, sinon
*   `END_GAME`.

Ces transitions entre les différentes actions à effectuer peuvent être représentées de manière graphique, comme ci-dessous.

![game-state-machine;16.png](i/game-state-machine;16.png)

Figure 1 : Transitions entre les actions à effectuer

Sur cette figure, chaque action à effectuer est représentée par un rectangle, et les flèches reliant ces actions représentent les transitions qui peuvent se produire entre elles. Pour ne pas alourdir le dessin, les transitions qui évitent `OCCUPY_TILE` car la dernière tuile posée ne peut pas être occupée ne sont pas représentées. Par exemple, il n'y a pas de flèche allant de `PLACE_TILE` (tuile normale) vers elle-même, alors que cette transition est possible, comme nous l'avons dit plus haut.

Pour faciliter la compréhension, les actions `PLACE_TILE` et `OCCUPY_TILE` apparaissent deux fois sur ce dessin : les occurrences de gauche correspondent au placement d'une tuile normale, celles de droite au placement d'une tuile menhir. En pratique, les actions consistant à placer ou occuper une tuile sont considérées comme les mêmes, indépendamment du type de la tuile en question.

Les actions `START_GAME` et `END_GAME` sont entourées deux fois car elles sont particulières : `START_GAME` est l'action de départ, celle qui doit être effectuée tout au début pour démarrer la partie, donc aucune flèche ne mène à elle ; `END_GAME` est quant à elle l'action de fin, après laquelle la partie est terminée et aucune autre action ne peut être effectuée, donc aucune flèche ne part d'elle.

### 2.5. Références

Les règles de la version physique du jeu sont décrites :

*   en français dans [le livret de règles](f/carcassonne_hg_rules_fr.pdf),
*   en anglais sur [le Wiki consacré à Carcassonne et ses variantes](https://wikicarpedia.com/car/Hunters_and_Gatherers_v2_Base_Game).

Il faut toutefois noter que les règles de notre version diffèrent parfois de celles du jeu original, principalement lorsque ces dernières sont ambiguës.

## 3. Mise en œuvre Java

### 3.1. Enregistrement `GameState`

L'enregistrement `GameState` du paquetage principal, public et immuable, représente l'état complet d'une partie de ChaCuN. C'est-à-dire qu'il contient la totalité des informations liées à une partie en cours. Ses attributs sont :

*   `List<PlayerColor> players`, la liste de tous les joueurs de la partie, dans l'ordre dans lequel ils doivent jouer — donc avec le joueur courant en tête de liste,
*   `TileDecks tileDecks`, les trois tas des tuiles restantes,
*   `Tile tileToPlace`, l'éventuelle tuile à placer, qui à été prise du sommet du tas des tuiles normales ou du tas des tuiles menhir, et qui peut être `null` si aucune tuile n'est à placer actuellement,
*   `Board board`, le plateau de jeu,
*   `Action nextAction`, la prochaine action à effectuer, le type `Action` étant décrit ci-dessous,
*   `MessageBoard messageBoard`, le tableau d'affichage contenant les messages générés jusqu'à présent dans la partie.

Le type énuméré `Action`, public et imbriqué dans `GameState`, représente la prochaine action à effectuer dans la partie et contient les éléments suivants, dans l'ordre : `START_GAME`, `PLACE_TILE`, `RETAKE_PAWN`, `OCCUPY_TILE` et `END_GAME`.

Le constructeur compact de `GameState` se charge de garantir l'immuabilité de la classe et de valider les arguments en vérifiant que :

*   le nombre de joueurs est au moins égal à 2,
*   soit la tuile à placer est `null`, soit la prochaine action est `PLACE_TILE`,
*   ni les tas de cartes, ni le plateau de jeu, ni la prochaine action, ni le tableau d'affichage ne sont nuls.

Pour faciliter la création de l'état initial d'une partie, `GameState` offre la méthode publique et statique suivante :

*   `GameState initial(List<PlayerColor> players, TileDecks tileDecks, TextMaker textMaker)`, qui retourne l'état de jeu initial pour les joueurs, tas et « créateur de texte » donnés, dont la prochaine action est `START_GAME` (donc la tuile à placer est `null`), et dont le plateau et le tableau d'affichage sont vides.

En plus de la méthode statique ci-dessus, `GameState` offre les méthodes d'instance publiques suivantes, qui permettent d'obtenir différentes informations au sujet de l'état de la partie :

*   `PlayerColor currentPlayer()`, qui retourne le joueur courant, ou `null` s'il n'y en a pas, c.-à-d. si la prochaine action est `START_GAME` ou `END_GAME`,
*   `int freeOccupantsCount(PlayerColor player, Occupant.Kind kind)`, qui retourne le nombre d'occupants libres — c.-à-d. qui ne sont pas actuellement placés sur le plateau de jeu — du type donné et appartenant au joueur donné,
*   `Set<Occupant> lastTilePotentialOccupants()`, qui retourne l'ensemble des occupants potentiels de la dernière tuile posée que le joueur courant pourrait effectivement placer — d'une part car il a au moins un occupant du bon type en main, et d'autre part car l'aire à laquelle appartient la zone que cet occupant occuperait n'est pas déjà occupée — ou lève `IllegalArgumentException` si le plateau est vide.

Finalement, `GameState` offre un certain nombre de méthodes dont le but est de gérer les transitions entre les différentes actions à effectuer. Ces méthodes sont destinées à être appelées uniquement lorsque la prochaine action à effectuer est une action spécifique, et leurs arguments sont les paramètres de cette action.

Par exemple, `withPlacedTile`, décrite plus bas, est destinée à être appelée lorsque la prochaine action à effectuer est `PLACE_TILE`, et elle prend donc en argument la tuile qui a été placée par le joueur courant. Elle effectue les éventuelles opérations nécessaires — p. ex. en attribuant les points correspondant à la pose de la tuile contenant la pirogue si c'est elle qui vient d'être posée — avant de déterminer la prochaine action à effectuer et de retourner l'état correspondant.

Ces méthodes — qui lèvent toutes une `IllegalArgumentException` (abrégée `IAE` dans leur description) si la prochaine action à effectuer n'est pas celle qu'elles attendent — sont :

*   `GameState withStartingTilePlaced()`, qui gère la transition de `START_GAME` à `PLACE_TILE` en plaçant la tuile de départ au centre du plateau et en tirant la première tuile du tas des tuiles normales, qui devient la tuile à jouer ; lève `IAE` si la prochaine action n'est pas `START_GAME`,
*   `GameState withPlacedTile(PlacedTile tile)`, qui gère toutes les transitions à partir de `PLACE_TILE` en ajoutant la tuile donnée au plateau, attribuant les éventuels points obtenus suite à la pose de la pirogue ou de la fosse à pieux (voir la [3.1.1.1](#sec/java/hunting-trap)), et déterminant l'action suivante — qui peut être `RETAKE_PAWN` si la tuile posée contient le chaman ; lève `IAE` si la prochaine action n'est pas `PLACE_TILE`, ou si la tuile passée est déjà occupée,
*   `GameState withOccupantRemoved(Occupant occupant)`, qui gère toutes les transitions à partir de `RETAKE_PAWN`, en supprimant l'occupant donné, sauf s'il vaut `null`, ce qui indique que le joueur ne désire pas reprendre de pion ; lève `IAE` si la prochaine action n'est pas `RETAKE_PAWN`, ou si l'occupant donné n'est ni `null`, ni un pion,
*   `GameState withNewOccupant(Occupant occupant)`, qui gère toutes les transitions à partir de `OCCUPY_TILE` en ajoutant l'occupant donné à la dernière tuile posée, sauf s'il vaut `null`, ce qui indique que le joueur ne désire pas placer d'occupant ; lève `IAE` si la prochaine action n'est pas `OCCUPY_TILE`.

Notez que `withStartingTilePlaced` peut faire l'hypothèse que la première tuile du tas des tuiles normales peut toujours être posée, ce qui est effectivement le cas en pratique car la tuile de départ comporte au moins un bord de chacune des trois sortes — pré, forêt et rivière.

#### 3.1.1. Conseils de programmation

1.  Gestion de la fosse à pieux  
    
    Comme expliqué à la §[2.2](#sec/hunting-trap), afin de calculer correctement les points obtenus par la pose de la fosse à pieux, il est nécessaire d'annuler au préalable les éventuels cerfs mangés par des smilodons. Malheureusement, le calcul des points est fait par la méthode `withScoredHuntingTrap` de `MessageBoard`, qui ne prend _pas_ d'ensemble d'animaux annulés en argument.
    
    Il s'agit d'une erreur de conception, mais pour faciliter l'organisation, nous n'allons pas la corriger avant le rendu intermédiaire. Dès lors, dans votre méthode `withPlacedTile`, vous pouvez déjà calculer l'ensemble des cerfs mangés par des smilodons, mais il vous faudra attendre une étape ultérieure pour le passer à la version corrigée de `withScoredHuntingTrap`.
    
2.  Possibilité d'occupation  
    
    Comme la figure [1](#org76c8fa2) l'illustre, l'action à effectuer après la pose d'une tuile (`PLACE_TILE`) ou la reprise d'un pion (`RETAKE_PAWN`) est normalement d'occuper la dernière tuile posée (`OCCUPY_TILE`).
    
    En réalité, il se peut que l'occupation de cette tuile soit impossible, soit car les aires auxquelles appartiennent ses zones sont déjà occupées, soit car le placeur n'a plus les occupants nécessaires en main.
    
    Dans ce cas-là, l'action `OCCUPY_TILE` doit être sautée, et l'action qui suit `OCCUPY_TILE` devient l'action suivante. Comme dit plus haut, les flèches correspondant à cette situation ne sont _pas_ présentées sur la figure [1](#org76c8fa2) afin de ne pas l'alourdir, mais il va de soi que votre code doit impérativement gérer ce cas-là.
    
    Une manière de le faire consiste à ajouter à `GameState` une méthode privée, nommée p. ex. `withTurnFinishedIfOccupationImpossible`, dont le but est de finir le tour si l'occupation de la dernière tuile posée est impossible. Ce que signifie exactement « finir le tour » est décrit à la §[3.1.1.4](#sec/java/with-turn-finished) plus bas.
    
    Cette méthode peut être appelée à la fin de toutes les méthodes qui produisent un état dont la prochaine action est `OCCUPY_TILE`, c.-à-d. `withPlacedTile` et `withOccupantRemoved`, afin de sauter cette action si elle n'est pas possible.
    
3.  Possibilité de retrait de pion  
    
    De la même manière que l'action `OCCUPY_TILE` ne doit être effectuée que si l'occupation de la dernière tuile posée est effectivement possible, l'action `RETAKE_PAWN` ne doit l'être que si le joueur courant a au moins un pion sur le plateau.
    
4.  Fin de tour  
    
    Une fois que la tuile à placer l'a été, et qu'elle a éventuellement été occupée, le tour actuel du joueur courant se termine.
    
    La fin de tour n'est pas totalement triviale à gérer, d'une part car il faut compter les points obtenus par les occupants majoritaires de toutes les éventuelles forêts et rivières fermées par la dernière tuile posée, et d'autre part car il faut déterminer la prochaine action à effectuer et qui, comme nous l'avons dit plus haut, peut être `PLACE_TILE` (pour le même joueur, s'il peut placer une tuile menhir, pour le joueur suivant sinon) ou `END_GAME`.
    
    Nous vous conseillons donc là aussi d'ajouter à votre classe `GameState` une méthode privée, nommée p. ex. `withTurnFinished`, se chargeant d'effectuer les opérations nécessaires, à savoir :
    
    *   déterminer les forêts et rivières fermées par la pose de la dernière tuile, et attribuer les points correspondants à leurs occupants majoritaires,
    *   déterminer si le joueur courant devrait pouvoir jouer un second tour, car il a fermé au moins une forêt contenant un menhir au moyen d'une tuile normale,
    *   éliminer du sommet du tas contenant la prochaine tuile à jouer la totalité de celles qu'il n'est pas possible de placer sur le plateau, s'il y en a,
    *   passer la main au prochain joueur si le joueur courant n'a pas le droit ou la possibilité de jouer une tuile menhir,
    *   terminer la partie si le joueur courant a terminé son ou ses tour(s) et qu'il ne reste plus de tuile normale jouable.
    
    Réfléchissez bien avant d'écrire le code qui détermine la prochaine tuile à jouer, en tenant compte de celles qu'il pourrait être impossible de placer, car il n'est pas trivial. Souvenez-vous en particulier que la classe `TileDecks` offre la méthode `withTopTileDrawnUntil`, qui peut faciliter la suppression du sommet d'un tas de toutes les tuiles impossibles à placer.
    
5.  Fin de partie  
    
    Lorsque le joueur courant a terminé son (ou ses) tour(s) et qu'il est temps de passer au joueur suivant, si aucune tuile du tas des tuiles normales ne peut être jouée, alors la partie est terminée.
    
    Comme la fin d'un tour, la fin de la partie n'est pas totalement triviale à gérer puisqu'elle implique d'établir le décompte final des points. Nous vous recommandons donc une fois encore de placer le code chargé de faire cela dans une méthode privée, nommée p. ex. `withFinalPointsCounted`.
    
    Cette méthode doit commencer par ajouter aux animaux annulés la totalité des cerfs dévorés par des smilodons, en tenant compte de l'éventuelle présence du feu dans un pré. De plus, lorsque le pré contient la grande fosse à pieux, les cerfs qui ne se trouvent pas à sa portée doivent être annulés en priorité, afin de maximiser les points rapportés par la fosse.
    
    Une fois les cerfs annulés, le décompte des points rapportés par les prés à leurs chasseurs majoritaires peut être effectué.
    
    De même, le décompte des points rapportés par les réseaux hydrographiques à leurs huttes majoritaires peut être effectué, en pensant à tenir compte de l'éventuelle présence du radeau.
    
    Une fois les points comptés, le(s) vainqueur(s) peuvent être déterminés pour ajouter un dernier message au tableau d'affichage, qui annonce la fin de la partie.
    
6.  Attribution des points  
    
    Comme nous l'avons vu à l'étape précédente, l'attribution des points aux joueurs se fait de manière indirecte, en ajoutant au tableau d'affichage les messages informant les joueurs des différents gains de points. Il ne faut donc pas oublier, dans vos différentes méthodes, d'ajouter les messages nécessaires au décompte des points, de même que ceux mentionnant la fermeture d'une forêt contenant un menhir et la fin de la partie.
    

### 3.2. Tests

Comme d'habitude, nous ne vous fournissons plus de tests mais [un fichier de vérification de signatures](f/SignatureChecks_6.java) à importer dans votre projet.

## 4. Résumé

Pour cette étape, vous devez :

*   écrire la classe `GameState` en fonction des indications données plus haut,
*   tester votre code,
*   documenter la totalité des entités publiques que vous avez définies,
*   rendre votre code au plus tard le **5 avril 2024 à 18h00**, au moyen du programme `Submit.java` fourni et des jetons disponibles sur votre [page privée](https://cs108.epfl.ch/my/).

Ce rendu est un rendu testé, auquel 18 points sont attribués, au prorata des tests unitaires passés avec succès.

N'attendez surtout pas le dernier moment pour effectuer votre rendu, car vous n'êtes pas à l'abri d'imprévus. **Souvenez-vous qu'aucun retard, aussi insignifiant soit-il, ne sera toléré !**

## Notes de bas de page

[1](#fnr.1)

Il faut noter que le feu ne fait fuire les smilodons du pré que lors du décompte final des points, et il n'a aucune influence sur le décompte des points de la fosse à pieux.