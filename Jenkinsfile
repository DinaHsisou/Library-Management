pipeline {
    agent any
    environment {
       SONAR_PROJECT_KEY = 'LibraryManagement'
       SONAR_SCANNER_HOME = tool 'SonarQubeScanner'
       DB_URL = "jdbc:mysql://localhost:3306/library_db"
       DB_USER = "root"
       DB_PASSWORD = "root"
    }
    tools {
        maven 'maven3'
        jdk 'JDK17'
    }
    stages {
        stage('Checkout') {
            steps {
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
                withCredentials([string(credentialsId: 'jenkins-sonar', variable: 'SONAR_TOKEN')]) {
                    withSonarQubeEnv('SonarQube') {
                        bat """
                            mvn clean verify sonar:sonar ^
                            -Dsonar.projectKey=%SONAR_PROJECT_KEY% ^
                            -Dsonar.login=%SONAR_TOKEN% ^

                        """
                    }
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
                    attachLog: true
                )
            }
        }
    }
}