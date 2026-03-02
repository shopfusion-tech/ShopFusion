pipeline {
    agent any

    tools {
        jdk 'JDK17'      // Must match Global Tool Configuration
        maven 'Maven3'   // Must match Global Tool Configuration
    }

    environment {
        GIT_REPO = "https://github.com/shopfusion-tech/ShopFusion.git"
        GIT_BRANCH = "develop"
        APP_NAME = "shopfusion"
        PORT = "9090"
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Cloning ${GIT_BRANCH} branch..."
                git branch: "${GIT_BRANCH}",
                    url: "${GIT_REPO}"
            }
        }

        stage('Build') {
            steps {
                echo "Building application..."
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                echo "Running tests..."
                sh 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                script {

                    def jarPath = "${env.WORKSPACE}/target/shopfusion-0.0.1-SNAPSHOT.jar"
                    def javaHome = tool 'JDK17'
                    def javaBin = "${javaHome}/bin/java"

                    sh """
                    echo "Stopping old application if running..."
                    pkill -f ${APP_NAME} || true

                    echo "Starting new application on port ${PORT}..."

                    BUILD_ID=dontKillMe \\
                    nohup ${javaBin} -jar -Dserver.port=${PORT} ${jarPath} \\
                    > ${env.WORKSPACE}/app.log 2>&1 &
                    """
                }
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
