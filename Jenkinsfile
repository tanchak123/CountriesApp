pipeline {

agent any
    stages {
      stage ('Build') {
      steps{
        git url: 'https://github.com/cyrille-leclerc/multi-module-maven-project'
            withMaven(
                // Maven installation declared in the Jenkins "Global Tool Configuration"
                maven: 'maven-checked', // (1)
                // Use `$WORKSPACE/.repository` for local repository folder to avoid shared repositories
                mavenLocalRepo: '$WORKSPACE/.repository', // (2)
                // Maven settings.xml file defined with the Jenkins Config File Provider Plugin
                // We recommend to define Maven settings.xml globally at the folder level using
                // navigating to the folder configuration in the section "Pipeline Maven Configuration / Override global Maven configuration"
                // or globally to the entire master navigating to  "Manage Jenkins / Global Tools Configuration"
                mavenSettingsConfig: '5818a04b-7474-4880-bba6-c0e1c232f8bd' // (3)
            )
             {

              // Run the maven build
              sh "mvn clean verify"

            }
      }
      }
    }


//     agent any
//     stages {

//   stage ('Download') {
//     steps {
//         git url: 'https://github.com/cyrille-leclerc/multi-module-maven-project'
//     }
//   }
//
//   stage ('Build') {
//     steps {
//         withMaven {
//           sh "mvn clean verify"
//         } // withMaven will discover the generated Maven artifacts, JUnit Surefire & FailSafe reports and FindBugs reports
//     }
//   }
//     stage('build') {
//
//                 steps {
//                     script {
//                        /* the return value gets caught and saved into the variable MY_CONTAINER */
//                         MY_CONTAINER = bat(script: '@docker run -d -i maven:3.8-openjdk-17', returnStdout: true).trim()
//                         echo "mycontainer_id is ${MY_CONTAINER}"
// //                        /* python --version gets executed inside the Container */
//                         bat "docker exec ${MY_CONTAINER} mvn --version "
// //                         bat "docker exec ${MY_CONTAINER} mvn clean "
//
// //                         bat "docker exec ${MY_CONTAINER} 'mvn -B -DskipTests clean package' "
//
// //                         bat (script: '@mvn -B -DskipTests clean package')
//
// //                        /* the Container gets removed */
// //                        bat "docker rm -f ${MY_CONTAINER}"
//                         }
//         withMaven {
//             sh 'mvn --version'
//             sh "mvn -B -DskipTests clean package"
//         }
//                          echo 'Hello, Maven'
// //
// //
// // //                         sh 'mvn --version'
// // //                         sh 'mvn -B -DskipTests clean package'
//                         }
//                     }

//         stage('Run') {
// //             agent { docker {image 'docker.io/library/openjdk:17' }}
//
//             steps {
//                 script {
//                     /* the return value gets caught and saved into the variable MY_CONTAINER */
//                     MY_CONTAINER_2 = bat(script: '@docker.io/library/openjdk:17', returnStdout: true).trim()
//                 }
//             echo 'Hello, JDK'
//             bat 'java -jar target/countries-app-1.0-SNAPSHOT.jar'
//             }
//
//         }
//     }
}
