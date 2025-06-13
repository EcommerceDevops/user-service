pipeline {
  agent any
  stages {
    stage('Hola GitHub') {
      steps {
        echo "✅ Webhook recibido desde GitHub 🎉"
      }
    }

    stage('Build & Test') {
      steps {
        echo "🔩 Dando permisos de ejecución al wrapper de Maven..."
        sh 'chmod +x mvnw'
        
        echo "🚀 Compilando el proyecto y ejecutando pruebas (esto generará el reporte de cobertura)..."
        
        sh './mvnw clean verify'
      }
    }

    stage('SonarQube analysis') {
      steps {
        withSonarQubeEnv('SonarQube') {
          script {
            def scannerHome = tool 'SonarQube Scanner'
            sh "${scannerHome}/bin/sonar-scanner -X"
          }
        }
      }
    }


    stage('Quality Gate') {
      steps {
        timeout(time: 5, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
        }
      }
    }

  }
}
