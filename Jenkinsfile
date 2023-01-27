pipeline {

agent any
    stages {
        stage ('Stop docker') {
            steps {
                script {
                        result = "Windows"
                        if (isUnix()) {
                            result = "Linux";
                        }

                        echo "System is ${result}"

                        ID = bat(
                        script: '@docker ps -q --filter ancestor=jenkins-build --format="{{.ID}}""', returnStdout: true)
                        echo "${ID}"
                        if (!ID.isEmpty()) {
                            bat "docker stop ${ID}"
                        }
                }
            }
        }
        stage ('Build') {
            steps {
                      bat "mvn --version"
                      bat "mvn -B -DskipTests clean package"
                      echo 'Hello, Maven'
                    }
            }
        stage('Run') {
            steps {
                bat 'docker build -t jenkins-build .'
                bat 'docker run -d -p 8081:8080 jenkins-build'
                echo 'Hello, JDK'
            }
        }
    }
}

