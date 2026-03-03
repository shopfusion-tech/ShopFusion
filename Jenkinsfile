pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    environment {
        GIT_REPO = "https://github.com/shopfusion-tech/ShopFusion.git"
        GIT_BRANCH = "develop"
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
                    pkill -f ${jarPath} || true
                    sleep 3

                    echo "Starting application via Jenkins..."

                    # VERY IMPORTANT - Prevent Jenkins from killing process
                    export BUILD_ID=dontKillMe

                    nohup ${javaBin} \
                    -Dserver.port=${PORT} \
                    -Dserver.address=0.0.0.0 \
                    -jar ${jarPath} \
                    > /tmp/shopfusion.log 2>&1 &

                    sleep 6

                    echo "Verifying application process..."
                    ps -ef | grep ${jarPath} | grep -v grep || exit 1

                    echo "Application started successfully."
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✅ ShopFusion deployed and running on port ${PORT}"
        }
        failure {
            echo "❌ Deployment failed. Check /tmp/shopfusion.log"
        }
    }
}