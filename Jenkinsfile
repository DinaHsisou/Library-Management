pipeline {
    agent any
    environment {
       SONAR_PROJECT_KEY = 'LibraryManagement'
       SONAR_SCANNER_HOME = tool 'SonarQubeScanner'
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
                    mvn clean verify org.jacoco:jacoco-maven-plugin:prepare-agent install
                """
            }
        }

        stage('Quality Analysis') {
            steps {
                withCredentials([string(credentialsId: 'sonarqube-project-token', variable: 'SONAR_TOKEN')]) {
                    withSonarQubeEnv('SonarQube') {
                        bat """
                            mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184:sonar ^
                            -Dsonar.host.url=http://localhost:9000 ^
                            -Dsonar.projectKey=%SONAR_PROJECT_KEY% ^
                            -Dsonar.token=%SONAR_TOKEN% ^
                            -Dsonar.java.binaries=target/classes ^
                            -Dsonar.java.test.binaries=target/test-classes ^
                            -Dsonar.java.libraries=target/dependency/*.jar ^
                            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml ^
                            -Dsonar.sources=src/main/java ^
                            -Dsonar.tests=src/test/java ^
                            -Dsonar.language=java ^
                            -Dsonar.java.coveragePlugin=jacoco
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