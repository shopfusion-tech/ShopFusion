pipeline {
    agent any

    environment {
        APP_NAME = "shopfusion"
    }

    tools {
        maven "Maven"   // Name must match Jenkins Global Tool Config
        jdk "JDK17"
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo "Cloning Repository..."
                git 'https://github.com/your-username/shopfusion.git'
            }
        }

        stage('Build') {
            steps {
                echo "Building Application..."
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                echo "Running Tests..."
                sh 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                echo "Deploying Application..."

                sh '''
                pkill -f shopfusion || true
                nohup java -jar target/shopfusion-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
                '''
            }
        }
    }

    post {
        success {
            echo "Application Deployed Successfully on Port 9090"
        }
        failure {
            echo "Build Failed"
        }
    }
}
