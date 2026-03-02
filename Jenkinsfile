pipeline {
    agent any

    tools {
        jdk 'JDK17'        // Must match Global Tool Configuration name
        maven 'Maven3'     // Must match Global Tool Configuration name
    }

    environment {
        APP_NAME = "shopfusion"
        JAR_FILE = "target/shopfusion-0.0.1-SNAPSHOT.jar"
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Cloning Repository..."
                git 'https://github.com/your-username/shopfusion.git'
            }
        }

        stage('Build') {
            steps {
                echo "Building Application..."
                sh 'mvn clean package -DskipTests'
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
                echo "Stopping old application if running..."

                sh '''
                if pgrep -f shopfusion; then
                    pkill -f shopfusion
                fi
                '''

                echo "Starting new application on port 9090..."

                sh '''
                nohup java -jar ${JAR_FILE} > app.log 2>&1 &
                '''
            }
        }
    }

    post {
        success {
            echo "✅ ShopFusion deployed successfully on port 9090"
        }
        failure {
            echo "❌ Build Failed"
        }
    }
}
