pipeline {
    agent { label "build" }
    stages {
        stage('Build') {
//             agent {
//              docker {
//                 image 'maven:3.8-openjdk-17'
//             }}
            steps {
                withDockerContainer (image: 'maven:3.8-openjdk-17') {

                echo 'Hello, Maven'

                 sh 'mvn -B -DskipTests clean package'
                 }
            }
        }
        stage('Run') {
            agent { docker {image 'docker.io/library/openjdk:17' }}
            steps {
                echo 'Hello, JDK'
                sh 'java -jar target/countries-app-1.0-SNAPSHOT.jar'
            }
        }
    }
}