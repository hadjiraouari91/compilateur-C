# ⚡ Mini-Compilateur C (Boucle While)

Ce projet est un mini-compilateur développé en **Java**. Il est capable d'analyser lexicalement et syntaxiquement un sous-ensemble du langage **C**, en se concentrant spécifiquement sur la structure de contrôle **`while`**.

Le projet inclut un **Scanner** (Analyseur Lexical) et un **Parser** (Analyseur Syntaxique) utilisant la méthode de la descente récursive.

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

📂 Structure du Projet
L'arborescence du projet est organisée comme suit :

Plaintext

ProjetCompilateur/
├── bin/                 # Contient les fichiers compilés (.class)
├── src/                 # Contient le code source (.java)
│   ├── Main.java        # Point d'entrée de l'application
│   ├── Lexer.java       # Logique de l'analyseur lexical
│   ├── Parser.java      # Logique de l'analyseur syntaxique
│   ├── Token.java       # Objet représentant un lexème
│   └── TokenType.java   # Énumération des types de tokens
│
├── test_complet.c       # Fichier de test valide (Cas nominaux)
├── test_erreurs.c       # Fichier de test avec erreurs (Pour tester la robustesse)
├── test_lexical.c       # Fichier de test du vocabulaire C
├── test_formatage.c     # Fichier de test des commentaires et formatage
├── Moncompil.jar  # L'exécutable final (généré après build)
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
echo Main-Class: Main > manifest.txt

# Créer l'archive JAR incluant les fichiers compilés du dossier bin
jar cvfm Moncompil.jar manifest.txt -C bin .
3. Exécution
Une fois l'archive .jar générée, vous pouvez lancer le compilateur en lui passant en argument le fichier C à analyser.

Commande d'exécution :

Bash

java -jar Moncompil.jar 'monfichier.c'
Exemple concret avec le fichier de test fourni :

Bash

java -jar Moncompil.jar test_complet.c
🧪 Tests
Le projet est fourni avec des fichiers de test pour vérifier le bon fonctionnement :

test_complet.c : Teste une boucle while valide avec des types C.

test_erreurs.c : Teste la détection d'erreurs (parenthèses manquantes).

test_lexical.c : Teste la reconnaissance de tous les mots-clés C.

test_formatage.c : Teste la gestion des commentaires complexes.