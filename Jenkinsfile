pipeline {
    agent any

    environment {
        APP_NAME = 'inventory-management'
        DOCKERHUB_NAMESPACE = 'dikshanta07'
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
                bat 'gradlew.bat test --no-daemon'
            }
        }

        stage('Docker Build & Push') {
            when {
                expression {
                    return env.GIT_BRANCH in ['origin/main', 'main', 'origin/master', 'master']
                }
            }
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'docker-hub-credentials',
                    usernameVariable: 'DOCKERHUB_USER',
                    passwordVariable: 'DOCKERHUB_TOKEN'
                )]) {
                    powershell '''
                        $ErrorActionPreference = "Stop"
                        $IMAGE = "$env:DOCKERHUB_NAMESPACE/$env:APP_NAME"
                        Write-Host "Building $($IMAGE):$env:IMAGE_TAG and $($IMAGE):latest"

                        docker build -t "$($IMAGE):$env:IMAGE_TAG" -t "$($IMAGE):latest" .
                        if ($LASTEXITCODE -ne 0) { throw "docker build failed" }

                        docker login -u "$env:DOCKERHUB_USER" --password "$env:DOCKERHUB_TOKEN"
                        if ($LASTEXITCODE -ne 0) { throw "docker login failed" }

                        docker push "$($IMAGE):$env:IMAGE_TAG"
                        if ($LASTEXITCODE -ne 0) { throw "docker push (tag) failed" }

                        docker push "$($IMAGE):latest"
                        if ($LASTEXITCODE -ne 0) { throw "docker push (latest) failed" }

                        docker logout
                        Write-Host "Published $($IMAGE):$env:IMAGE_TAG"
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline succeeded. Docker images are pushed to Docker Hub on main/master only."
        }
        failure {
            echo 'Pipeline failed. Check Jenkins console output.'
        }
    }
}