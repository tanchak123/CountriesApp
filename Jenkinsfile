pipeline {

agent any
    stages {
        stage ('git install') {
            steps {
                sh 'whoami'
                sh 'sudo su'
                sh 'cd /countries-app'
                sh 'sudo yum install git -y'
                echo 'git installed'
            }
        }

        stage ('mvn install') {
            steps {
                sh 'sudo wget https://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo'
                sh "sudo sed -i s/$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo"
                sh 'sudo yum install -y apache-maven'
                echo 'mvn installed'
            }
        }

        stage ('Build') {
            steps {
                      sh "sudo mvn --version"
                      sh "sudo mvn -B -DskipTests clean package"
                      echo 'Maven checked project'
                    }
            }
        stage('Run') {
            steps {
//                 bat 'docker build -t jenkins-build .'
//                 bat 'docker run -d -p 8080:8080 jenkins-build'
                sh 'sudo java -jar target/countries-app-1.0-SNAPSHOT.jar'
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

