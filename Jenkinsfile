pipeline {
    agent any
    tools {
        maven 'maven3'
        jdk 'JDK21'
        jdk 'JDK17'  // Pour SonarQube

    }
    stages {
        stage('Checkout') {
            steps {

               // Pas besoin de spécifier git car Jenkins le fait automatiquement
               checkout scm

            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }
        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }
        stage('Quality Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat 'mvn sonar:sonar'
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'Déploiement simulé réussi'
            }
        }
    }
    post {
        success {
            emailext to: 'dinahsisou@gmail.com',
                subject: 'Build Success',
                body: 'Le build a été complété avec succès.'
        }
        failure {
            emailext to: 'dinahsisou@gmail.com',
                subject: 'Build Failed',
                body: 'Le build a échoué.'
        }
    }
}
