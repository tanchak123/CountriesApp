pipeline {
    agent any
    stages {

    stage('build') {
                steps {
                    script {
                       /* the return value gets caught and saved into the variable MY_CONTAINER */
                       MY_CONTAINER = bat(script: '@docker run -d -i maven:3.8-openjdk-17', returnStdout: true).trim()
                       echo "mycontainer_id is ${MY_CONTAINER}"
//                        /* python --version gets executed inside the Container */
                       bat "docker exec ${MY_CONTAINER} mvn --version "
//                        /* the Container gets removed */
//                        bat "docker rm -f ${MY_CONTAINER}"
                        }
                        echo 'Hello, Maven'

                        sh 'mvn -B -DskipTests clean package'
                        }
                    }

        stage('Run') {
//             agent { docker {image 'docker.io/library/openjdk:17' }}

            steps {
                script {
                    /* the return value gets caught and saved into the variable MY_CONTAINER */
                    MY_CONTAINER = bat(script: '@docker.io/library/openjdk:17', returnStdout: true).trim()
                }
            echo 'Hello, JDK'
            sh 'java -jar target/countries-app-1.0-SNAPSHOT.jar'
            }

        }
    }
}
