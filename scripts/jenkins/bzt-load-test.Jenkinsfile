/*
 ============================================================================
 This Jenkins declarative pipeline (using docker pipeline plugin) demonstrate how to run
 load test with k6 (a JavaScript-based load testing tool) inside a container build agent.
 The image is based on the official k6 image from Docker Hub.
 ============================================================================

 Pre-requisite:
- Installed the following jenkins plugins:
	* https://wiki.jenkins.io/display/JENKINS/Git+Plugin
    * https://wiki.jenkins.io/display/JENKINS/Docker+Plugin
	* http://wiki.jenkins-ci.org/display/JENKINS/HTML+Publisher+Plugin
- Setup InfluxDB+Grafana for data visualisation purpose.
  The hostname is set as JENKINS_IPADDR system environment variable in Jenkins.
*/

properties(
  [
    buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '20')),
    parameters([
        string(name: "rampup_duration", defaultValue: "10s", description: 'Set a realistic duration for ramp-up. e.g. 30s, 1m'),
        string(name: "steady_duration", defaultValue: "1m", description: 'Set a duration for steady state. e.g. 30s, 1m')
    ])
  ]
)

pipeline {
    agent {
        label 'master'
    }

    options {
        disableConcurrentBuilds()
        timestamps()
        skipDefaultCheckout()
    }

    environment {
        def influxdb_host = "${JENKINS_IPADDR}" // To specify arbitrary hostname for InfluxDB

        def rampup_duration  = "${params.rampup_duration}"
        def steady_duration  = "${params.steady_duration}"
    }

    stages {

        stage('Preparation') {
            steps {
                // If option is enabled with skipDefaultCheckout()
                script {
                    def gitCredentialsId = 'hosang.chan'
                    REPO_BRANCH = 'main'
                    REPO_URL = 'git@github.com:<repository>/bzt-examples.git'
                    git branch: REPO_BRANCH, credentialsId: gitCredentialsId, url: REPO_URL
                }

                stash name: 'bzt-source', includes: 'bzt/**,jmx/**'
            }
		}


        stage('Run Load Testing') {
            agent {
                docker {
                    image 'blazemeter/taurus:latest'
                    label 'master'
                    args '--entrypoint=' //args '-u root --entrypoint=\'/bin/sh\''
                }
            }
            steps {

                unstash "bzt-source"

                echo 'Run load test with BlazeMeter Taurus, generate and publish reports'
                // docker run --rm \
                // -v /home/ubuntu/bzt/bzt-examples:/bzt-configs \
                // -v /home/ubuntu/bzt/bzt-results:/tmp/artifacts \
                // blazemeter/taurus bzt-jmeter-load-test.yml -o settings.env.RESULTS_DIR=results

                sh """
                mkdir -p results
                blazemeter/taurus scripts/bzt/bzt-jmeter-load-test.yml \
                -o settings.env.RESULTS_DIR=results
                """

                stash name: 'results', includes: 'results/**'

            }

			post {
                always {
                    archiveArtifacts artifacts: "results/**", fingerprint: true
                    cleanWs()
                }
            }
        }

    }

    post {
        always {
            cleanWs()
        }
    }
}
