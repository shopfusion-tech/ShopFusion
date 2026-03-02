pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    environment {
        APP_NAME = "shopfusion"
        JAR_FILE = "target/shopfusion-0.0.1-SNAPSHOT.jar"
        GIT_BRANCH = "develop"
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Cloning ${GIT_BRANCH} branch..."

                git branch: "${GIT_BRANCH}",
                    url: 'https://github.com/shopfusion-tech/ShopFusion.git'
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
            script {
                def jarPath = "${env.WORKSPACE}/target/shopfusion-0.0.1-SNAPSHOT.jar"

                sh """
                if pgrep -f shopfusion; then
                    pkill -f shopfusion
                fi

                nohup java -jar -Dserver.port=9090 ${jarPath} > ${env.WORKSPACE}/app.log 2>&1 &
                """
                }
            }
        }
    }

    post {
        success {
            echo "✅ ShopFusion (develop branch) deployed successfully on port 9090"
        }
        failure {
            echo "❌ Build Failed"
        }
    }
}
