# Library Management System

## Description
Un système de gestion de bibliothèque développé en Java, permettant de gérer les livres, les étudiants et les emprunts d'ouvrages dans une bibliothèque universitaire.

## Fonctionnalités
- Gestion des livres (ajout, modification, suppression, affichage)
- Gestion des étudiants (ajout, modification, suppression, affichage)
- Gestion des emprunts de livres (emprunt, retour, suivi)
- Interface console pour l'interaction utilisateur
- Base de données MySQL pour le stockage des données

## Technologies Utilisées
- Java 17
- Maven
- MySQL
- JUnit 5 pour les tests
- Jenkins pour l'intégration continue
- SonarQube pour l'analyse de code

## Prérequis
- JDK 17 ou supérieur
- Maven 3.x
- MySQL Server
- Jenkins (pour l'intégration continue)
- SonarQube (pour l'analyse de qualité)

## Installation

1. Configurer la base de données MySQL
```sql
CREATE DATABASE library_db;
USE library_db;
```

2. Compiler le projet
```bash
mvn clean install
```

## Structure du Projet
```
src/
├── main/
│   └── java/
│       └── com/
│           └── library/
│               ├── model/
│               │   ├── Book.java
│               │   ├── Student.java
│               │   └── Borrow.java
│               ├── dao/
│               │   ├── BookDAO.java
│               │   ├── StudentDAO.java
│               │   └── BorrowDAO.java
│               ├── service/
│               │   ├── BookService.java
│               │   ├── StudentService.java
│               │   └── BorrowService.java
│               └── Main.java
└── test/
    └── java/
        └── com/
            └── library/
                ├── BookServiceTest.java
                ├── StudentServiceTest.java
                └── BorrowServiceTest.java
```

## Tests
Pour exécuter les tests :
```bash
mvn test
```

## Configuration Jenkins
Le projet utilise Jenkins pour l'intégration continue avec les étapes suivantes :
1. Checkout : Récupération du code depuis GitHub
2. Build : Compilation du code avec Maven
3. Test : Exécution des tests unitaires
4. Quality Analysis : Analyse du code avec SonarQube
5. Deploy : Déploiement simulé

## Configuration SonarQube
Pour analyser le code avec SonarQube :
```bash
mvn sonar:sonar \
  -Dsonar.projectKey=LibraryManagement \
  -Dsonar.host.url=http://localhost:9100 \
  -Dsonar.login=token
```

## Contributeurs
- Dina HSISOU
- Université Cadi AYYAD
- École Nationale des Sciences Appliquées Marrakech
- Filière : Génie Informatique
- Module : Qualité Logiciel
- Encadré par : BOUARIFI Walid

