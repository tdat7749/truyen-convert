pipeline{
    agent any

    tools {
        maven 'M3.9.6'
        nodejs 'N22.2.0'
    }

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
        REGISTRY = "hyperiondat"
        SPRING_IMAGE_NAME="truyencv-spring-app"
        ANGULAR_IMAGE_NAME="truyencv-angular-app"
        SPRING_PATH_DOCKERFILE="server/Dockerfile"
        ANGULAR_PATH_DOCKERFILE="client/Dockerfile"
    }

    stages{
        stage('Build and Push Images') {
            parallel {
                        stage('Build and Push Spring Boot') {
                            steps {
                                script {
                                    dockerBuildAndPush("${REGISTRY}/${SPRING_IMAGE_NAME}", "${env.BUILD_ID}", "${SPRING_PATH_DOCKERFILE}", 'server')
                                }
                            }
                        }
                        stage('Build and Push Angular') {
                            steps {
                                script {
                                    dockerBuildAndPush("${REGISTRY}/${ANGULAR_IMAGE_NAME}", "${env.BUILD_ID}", "${ANGULAR_PATH_DOCKERFILE}", 'client')
                                }
                            }
                        }
                    }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    bat "find k8s -name '*.yaml' | xargs kubectl apply -f"
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}

// def getNewTag(String image) {
//     def response = sh(
//         script: "curl -s https://registry.hub.docker.com/v1/repositories/${image}/tags",
//         returnStdout: true
//     ).trim()
//     def tags = readJSON text: response
//     if (tags.size() == 0) {
//         return "v1"
//     } else {
//         def latestTag = tags.collect { it.name }.max { a, b ->
//             a.replace("v", "").toInteger() <=> b.replace("v", "").toInteger()
//         }
//         return "v${latestTag.replace('v', '').toInteger() + 1}"
//     }
// }

def dockerBuildAndPush(String image, String tag, String dockerfilePath, String buildContext) {
    def dockerImage = docker.build("${image}:${tag}", "-f ${dockerfilePath} ${buildContext}")
    docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS) {
        dockerImage.push()
    }
}