pipeline{
    agent any

    tools {
        maven 'M3.9.6'
        nodejs 'N22.2.0'
    }

    stages{
        stage('Clear old folder'){
            steps{
                sshagent(['remote-server']){
                    bat 'ssh -o StrictHostKeyChecking=no -l ec2-user 52.74.38.178 sudo rm -r /var/truyencv/client/*'
                    bat 'ssh -o StrictHostKeyChecking=no -l ec2-user 52.74.38.178 sudo rm -r /var/truyencv/server/*'
                }
            }
        }

        stage('Build Client'){
            steps{
                dir('client'){
                    bat 'npm install'
                    bat 'npm run build'
                }
            }
        }


        stage('Build Backend'){
            steps{
                dir('server'){
                    bat 'mvn -B -DskipTests clean package'
                }
            }
        }


        stage('Deploy to EC2'){
            steps{
                sshPublisher(
                    publishers:[
                        sshPublisherDesc(
                            configName: "remove-server",
                            transfers:[
                                sshTransfer(
                                    sourceFiles: "client/dist/**/*",
                                    removePrefix: "client/dist",
                                    remoteDirectory: "client"
                                ),
                                sshTransfer(
                                    sourceFiles: "server/target/*.jar",
                                    removePrefix: "server/target",
                                    remoteDirectory: "server",
                                    execCommand: """
                                                                                 java -jar server.jar --spring.profiles.active=prod || echo "Failed to start server.jar"
                                                                                 sudo systemctl stop nginx || echo "Failed to stop nginx"
                                                                                 sudo systemctl start nginx || echo "Failed to start nginx"
                                                                                 sudo systemctl enable nginx || echo "Failed to enable nginx"
                                                                             """
                                )
                            ],
                        )
                    ]
                )
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}