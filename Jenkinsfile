pipeline {
  agent any

  tools {
    maven 'Maven_3'
  }

  stages {
    stage('Checkout') { steps { checkout scm } }

    stage('Unit Tests') {
      steps { sh 'mvn -B -ntp test' }
      post {
        always { junit '**/target/surefire-reports/*.xml' }
      }
    }
  }
}