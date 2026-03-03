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
            def javaBin = "/bin/java"

            sh """
            echo "Stopping old app..."
            pkill -f ${jarPath} || true
            sleep 3

            echo "Starting application fully detached..."

            setsid env BUILD_ID=dontKillMe \
            nohup ${javaBin} \
            -Dserver.port=9090 \
            -Dserver.address=0.0.0.0 \
            -jar ${jarPath} \
            > /tmp/shopfusion.log 2>&1 < /dev/null &

            sleep 5

            ps -ef | grep ${jarPath} | grep -v grep || exit 1

            echo "Application started and detached successfully."
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