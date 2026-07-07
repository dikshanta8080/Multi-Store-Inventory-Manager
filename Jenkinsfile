pipeline {
    agent any

    environment {
        APP_NAME = 'inventory-management'
        DEPLOY_DIR = '/opt/inventory-management'
        IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
        disableConcurrentBuilds()
    }

    triggers {
        pollSCM('H/5 * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test --no-daemon'
            }
        }

        stage('Build JAR') {
            steps {
                sh './gradlew bootJar -x test --no-daemon'
            }
        }

        stage('Deploy to VPS') {
            when {
                anyOf {
                    branch 'main'
                    branch 'master'
                }
            }
            steps {
                sshagent(credentials: ['vps-ssh-credentials']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${VPS_USER}@${VPS_HOST} "
                            set -e
                            cd ${DEPLOY_DIR}
                            git fetch origin
                            git reset --hard origin/main
                            export IMAGE_TAG=${IMAGE_TAG}
                            docker compose --env-file .env build app
                            docker compose --env-file .env up -d
                            docker image prune -f
                        "
                    """
                }
            }
        }
    }

    post {
        success {
            echo "Deployment successful: ${APP_NAME}:${IMAGE_TAG}"
        }
        failure {
            echo 'Pipeline failed. Check Jenkins console output.'
        }
    }
}
