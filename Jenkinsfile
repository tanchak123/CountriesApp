pipeline {
    agent none
    stages {
        stage('Build') {
            agent { docker 'maven:3.8.5-openjdk-17' }
            steps {
                echo 'Hello, Maven'
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Run') {
            agent { docker 'docker.io/library/openjdk:17' }
            steps {
                echo 'Hello, JDK'
                sh 'java -jar target/countries-app-1.0-SNAPSHOT.jar'
            }
        }
    }
}