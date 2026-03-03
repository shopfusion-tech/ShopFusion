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
        APP_NAME = "shopfusion"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: "${GIT_BRANCH}",
                    url: "${GIT_REPO}"
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                script {

                    def jarPath = "${env.WORKSPACE}/target/shopfusion-0.0.1-SNAPSHOT.jar"
                    def logPath = "/var/log/shopfusion.log"
                    def javaBin = tool name: 'JDK17', type: 'jdk'

                    sh """
                    echo "Stopping old application..."
                    pkill -f ${jarPath} || true
                    sleep 3

                    echo "Starting new application..."

                    nohup ${javaBin}/bin/java \
                    -Dserver.port=${PORT} \
                    -Dserver.address=0.0.0.0 \
                    -jar ${jarPath} \
                    > ${logPath} 2>&1 &

                    echo \$! > ${env.WORKSPACE}/app.pid

                    sleep 5

                    ps -p \$(cat ${env.WORKSPACE}/app.pid) || exit 1

                    echo "Application started successfully."
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
            echo "❌ Deployment failed. Check logs at /var/log/shopfusion.log"
        }
    }
}
