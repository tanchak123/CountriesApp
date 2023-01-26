pipeline {
    agent any
    stages {

    stage('build') {

                steps {
                wget https://dlcdn.apache.org/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.zip
                unzip apache-maven-3.8.6-bin.zip
                sudo mv apache-maven-3.8.6 /opt/maven
                mvn -version
//                         git url: 'https://github.com/cyrille-leclerc/multi-module-maven-project'

//                     script {
//                        /* the return value gets caught and saved into the variable MY_CONTAINER */
//                         MY_CONTAINER = bat(script: '@docker run -d -i maven:3.8-openjdk-17', returnStdout: true).trim()
//                         echo "mycontainer_id is ${MY_CONTAINER}"
// //                        /* python --version gets executed inside the Container */
//                         bat "docker exec ${MY_CONTAINER} mvn --version "
//                         bat "docker exec ${MY_CONTAINER} mvn clean "
//
// //                         bat "docker exec ${MY_CONTAINER} 'mvn -B -DskipTests clean package' "
//
// //                         bat (script: '@mvn -B -DskipTests clean package')
//
// //                        /* the Container gets removed */
// //                        bat "docker rm -f ${MY_CONTAINER}"
//                         }
        withMaven {
            sh 'mvn --version'
            sh "mvn -B -DskipTests clean package"
        }
                         echo 'Hello, Maven'
//
//
// //                         sh 'mvn --version'
// //                         sh 'mvn -B -DskipTests clean package'
                        }
                    }

        stage('Run') {
//             agent { docker {image 'docker.io/library/openjdk:17' }}

            steps {
                script {
                    /* the return value gets caught and saved into the variable MY_CONTAINER */
                    MY_CONTAINER_2 = bat(script: '@docker.io/library/openjdk:17', returnStdout: true).trim()
                }
            echo 'Hello, JDK'
            bat 'java -jar target/countries-app-1.0-SNAPSHOT.jar'
            }

        }
    }
}
