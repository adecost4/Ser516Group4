pipeline {
  agent any

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Unit Tests + Coverage (Docker Maven)') {
      steps {
        sh '''
          docker run --rm \
            -v "$PWD":/app \
            -w /app \
            maven:3.9.6-eclipse-temurin-17 \
            mvn -B -ntp clean verify
        '''
      }
      post {
        always {
          junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
          archiveArtifacts artifacts: 'target/site/jacoco/**', allowEmptyArchive: true
          jacoco(
            execPattern: '**/target/jacoco.exec',
            classPattern: '**/target/classes',
            sourcePattern: '**/src/main/java'
          )
        }
      }
    }
  }
}