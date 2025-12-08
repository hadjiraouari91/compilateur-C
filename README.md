# ⚡ Mini-Compilateur C (Boucle While)

Ce projet est un mini-compilateur développé en **Java**. Il est capable d'analyser lexicalement et syntaxiquement un sous-ensemble du langage **C**, en se concentrant spécifiquement sur la structure de contrôle **`while`**.

Le projet inclut désormais une **Interface Graphique (GUI)** moderne, en plus du mode console classique.

## 👤 Informations Auteur

* **Auteur :** Ouari Hadjira
* **Module :** Compilation
* **Année :** 2025 / 2026
* **Université :** A. Mira de Béjaïa

---

## 🚀 Fonctionnalités

### 1. Analyse Lexicale (Scanner)
L'analyseur lexical parcourt le code source caractère par caractère pour générer des tokens.
* ✅ **Mots-clés C :** Supporte `int`, `float`, `char`, `void`, `if`, `else`, `while`, `do`, `return`, `include`.
* ✅ **Types de données :** Identifiants, Nombres (entiers et flottants), Chaînes de caractères (`"..."`).
* ✅ **Gestion des blancs :** Ignore les espaces, tabulations et sauts de ligne.
* ✅ **Commentaires :** Ignore les commentaires sur une ligne (`//`) et les blocs (`/* ... */`).
* ✅ **Tokens Spéciaux :** Reconnaissance automatique du nom et prénom de l'auteur :
  * `Hadjira` → détecté comme token `MON_PRENOM`.
  * `Ouari` → détecté comme token `MON_NOM`.
* **Gestion d'erreurs :** Détecte les caractères inconnus et les chaînes non terminées.

### 2. Analyse Syntaxique (Parser)
L'analyseur syntaxique vérifie la conformité grammaticale de la structure `while`.
* ✅ **Grammaire validée :**
  ```text
  WHILE_STMT -> while ( CONDITION ) { CORPS }
  CONDITION  -> EXPRESSION OPERATEUR EXPRESSION
✅ Robustesse : Le parser ignore les instructions hors du scope (comme les déclarations, le main, etc.) pour se focaliser uniquement sur la validation du while.

✅ Mode Panique (Panic Mode) : En cas d'erreur syntaxique (ex: oubli de ) ou }), le compilateur affiche l'erreur, se synchronise et continue l'analyse pour trouver d'autres erreurs potentielles sans s'arrêter brutalement.

3. Interface Graphique

✅ Éditeur de code intégré avec coloration syntaxique basique.

✅ Visualisation en temps réel des tokens dans un tableau.

✅ Console intégrée pour voir les erreurs et les succès de compilation directement dans l'application.

📂 Structure du Projet
L'arborescence du projet est organisée comme suit :

Plaintext

ProjetCompilateur/
├── bin/                 # Contient les fichiers compilés (.class)
├── src/                 # Contient le code source (.java)
│   ├── Main.java        # Point d'entrée (Gestion Console/GUI)
│   ├── CompilerGUI.java # Interface Graphique
│   ├── Lexer.java       # Logique de l'analyseur lexical
│   ├── Parser.java      # Logique de l'analyseur syntaxique
│   ├── Token.java       # Objet représentant un lexème
│   └── TokenType.java   # Énumération des types de tokens
│
├── test_complet.c       # Fichier de test valide (Cas nominaux)
├── test_erreurs.c       # Fichier de test avec erreurs (Pour tester la robustesse)
├── test_lexical.c       # Fichier de test du vocabulaire C
├── test_formatage.c     # Fichier de test des commentaires et formatage
├── Moncompil.jar        # L'exécutable final (généré après build)
└── README.md            # Documentation du projet
🛠️ Installation et Utilisation
Prérequis
Java JDK (version 8 ou supérieure) doit être installé.

1. Compilation
Le projet sépare les fichiers sources des fichiers compilés. Ouvrez un terminal à la racine du projet :

Bash

# Créer le dossier bin si il n'existe pas
mkdir bin

# Compiler les fichiers sources vers le dossier bin
javac -d bin src/*.java
2. Création de l'exécutable (.jar)
Pour distribuer le compilateur sous forme de fichier unique :

Bash

# Créer le fichier manifeste indiquant la classe principale
# (Utilisez l'encodage ASCII pour éviter les erreurs sur Windows PowerShell)
echo Main-Class: Main > manifest.txt

# Créer l'archive JAR incluant les fichiers compilés du dossier bin
jar cvfm Moncompil.jar manifest.txt -C bin .

3. Exécution
Le compilateur dispose de deux modes :

Bash

java -jar Moncompil.jar
