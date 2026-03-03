pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    environment {
        GIT_REPO = "https://github.com/shopfusion-tech/ShopFusion.git"
        GIT_BRANCH = "develop"
        APP_NAME = "shopfusion"
        PORT = "9090"
        LOG_FILE = "/var/log/shopfusion.log"
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
                    echo "===================================="
                    echo "Deploying ${APP_NAME}"
                    echo "===================================="

                    echo "Running as user:"
                    whoami

                    echo "Stopping old application if running..."
                    pkill -f ${jarPath} || true
                    sleep 3

                    echo "Starting application on port ${PORT}..."

                    # Detach from Jenkins completely
                    setsid ${javaBin} -Dserver.port=${PORT} -jar ${jarPath} \
                    > ${LOG_FILE} 2>&1 < /dev/null &

                    sleep 8

                    echo "Checking if application started..."

                    ps -ef | grep ${jarPath} | grep -v grep && echo "✅ Application is running" || (echo "❌ Application failed to start"; exit 1)

                    echo "Checking port status..."
                    netstat -tulnp | grep ${PORT} || echo "⚠️ Port ${PORT} not listening"

                    echo "Deployment completed."
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✅ ShopFusion deployed successfully on port ${PORT}"
        }
        failure {
            echo "❌ Deployment failed. Check logs: ${LOG_FILE}"
        }
    }
}