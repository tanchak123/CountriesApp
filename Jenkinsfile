pipeline {

agent any
    stages {
        stage ('git install') {
            steps {
                sudo 'yum install git -y'
                echo 'git installed'
            }
        }

        stage ('mvn install') {
            steps {
                sudo 'yum install -y maven.noarch'
                echo 'mvn installed'
            }
        }

        stage ('Build') {
            steps {
                      sudo "mvn --version"
                      sudo "mvn -B -DskipTests clean package"
                      echo 'Maven checked project'
                    }
            }
        stage('Run') {
            steps {
//                 bat 'docker build -t jenkins-build .'
//                 bat 'docker run -d -p 8080:8080 jenkins-build'
                sudo 'java -jar target/countries-app-1.0-SNAPSHOT.jar'
                echo 'project run'
            }
        }
//         stage ('Push') {
//             steps {
//                 bat "docker login"
//                 bat "docker image tag jenkins-build tanchak12/countries-test-app"
//                 bat "docker push tanchak12/countries-test-app"
//             }
//         }
    }
}

