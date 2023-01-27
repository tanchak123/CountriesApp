pipeline {

agent any
    stages {
        stage ('Stop docker') {
            steps {
                script {
                        isUnix = isUnix();
                        echo "System is ${isUnix}"
                        ID = bat(
                        script: '@docker ps -q --filter ancestor=jenkins-build --format="{{.ID}}""', returnStdout: true)
                        bat "docker stop ${ID}"
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

