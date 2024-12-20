pipeline {
    agent any
    environment {
       SONAR_PROJECT_KEY = 'LibraryManagement'
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

        stage('Build and Test') {
            steps {
                bat """
                    mvn clean verify
                    mvn jacoco:report
                """
            }
        }

        stage('Quality Analysis') {
            steps {
                withCredentials([string(credentialsId: 'jenkins-sonar', variable: 'SONAR_TOKEN')]) {
                    withSonarQubeEnv('SonarQube') {
                        bat """
                            mvn sonar:sonar ^
                            -Dsonar.projectKey=%SONAR_PROJECT_KEY% ^
                            -Dsonar.host.url=http://localhost:9000 ^
                            -Dsonar.login=%SONAR_TOKEN% ^
                            -Dsonar.java.binaries=target/classes ^
                            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
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