pipeline {
  agent any

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Unit Tests (Docker Maven)') {
      steps {
        sh '''
          docker run --rm \
            -v "$PWD":/app \
            -w /app \
            maven:3.9.6-eclipse-temurin-17 \
            mvn -B -ntp test
        '''
      }
      post {
        always {
          junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
        }
      }
    }
  }
}