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
           always {
               script {
                   // Configuration email plus simple
                   def emailBody = """
                       <h2>Build ${currentBuild.currentResult}</h2>
                       <p>Job: ${env.JOB_NAME}</p>
                       <p>Build Number: ${env.BUILD_NUMBER}</p>
                       <p>Build URL: <a href='${env.BUILD_URL}'>${env.BUILD_URL}</a></p>
                   """

                   emailext (
                       to: 'dinahsisou@gmail.com',
                       subject: "Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}",
                       body: emailBody,
                       mimeType: 'text/html',
                       attachLog: true  // Attache les logs du build
                   )
               }
           }
       }
}
