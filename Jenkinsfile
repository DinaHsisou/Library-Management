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
                    emailext (
                        subject: "✅ Build Réussi #${env.BUILD_NUMBER}",
                        body: """Le build ${env.JOB_NAME} #${env.BUILD_NUMBER} a réussi!

                            Voir les détails: ${env.BUILD_URL}

                            Tests: ${currentBuild.tests}
                            SonarQube: [URL SonarQube]
                            """,
                        to: "dinahsisou@email.com"
                    )
                }
                failure {
                    emailext (
                        subject: "❌ Build Échoué #${env.BUILD_NUMBER}",
                        body: """Le build ${env.JOB_NAME} #${env.BUILD_NUMBER} a échoué.

                            Voir les logs: ${env.BUILD_URL}console
                            """,
                        to: "dinahsisou@email.com"
                    )
                }
            }