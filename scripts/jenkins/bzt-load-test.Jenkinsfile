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
*/

def jenkins = [:]

pipeline {
    agent {
        label 'master'
    }

    options {
        // disableConcurrentBuilds()
        timestamps()
        skipDefaultCheckout()
    }

    stages {

        stage('Preparation') {
            steps {
                // Enabled skipDefaultCheckout()
                script {
                    // sh 'printenv'
                    echo "git url: ${scm.userRemoteConfigs[0].url}"
                    echo "git branch: ${scm.branches[0].name}"

                    // checkout from version control configured in pipeline job
                    checkout scm // git branch: 'main', credentialsId: '<credentialsId>', url: '<repositoryUrl>'

                    // load groovy methods
                    jenkins.general = load "scripts/groovy/common-methods.groovy"
                    jenkins.general.setDynamicParameterProperties()
                }

                stash name: 'bzt-source', includes: 'scripts/bzt/**,jmeter/**'
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

                script {
                    def extra_args = ""

                    // If true, uploads tests stats into BlazeMeter.com service
                    if (params.run_blazemeter_reporter) {
                        extra_args = "-report"
                    }

                    // -o modules.jmeter.properties=\"{'jmeter.reportgenerator.overall_granularity':1000}\"
                    sh """
                    mkdir -p results
                    bzt scripts/bzt/bzt-jmeter-load-test.yml \
                    scripts/bzt/bzt-jmeter-load-test-reporting.yml \
                    -o settings.env.APP_HOST=${params.app_host} \
                    -o execution.0.concurrency=${params.primary_concurrency} \
                    -o execution.0.ramp-up=${params.primary_ramp_up} \
                    -o execution.0.iterations=${params.primary_iterations} \
                    -o execution.1.concurrency=${params.secondary_concurrency} \
                    -o execution.1.ramp-up=${params.secondary_ramp_up} \
                    -o execution.1.hold-for=${params.secondary_duration} \
                    -o modules.jmeter.properties=\"{'jmeter.reportgenerator.overall_granularity':${params.jmeter_report_granularity}}\"  \
                    -o modules.jmeter.properties=\"{'jmeter.reportgenerator.report_title':Demo Load Test Dashboard}\"  \
                    ${extra_args}
                    """
                    // Move the reports from taurus artifacts dir location to workspace dir
                    sh """
                    mv /tmp/artifacts .
                    tar -zcvf logs.tar.gz **/*.log
                    tar -zcvf taurus-artifacts-${currentBuild.number}.tar.gz artifacts
                    """
                }
            }

			post {
                always {
                    archiveArtifacts artifacts: "**/*.tar.gz", fingerprint: true

                    perfReport errorFailedThreshold: 50,
                    errorUnstableThreshold: 10,
                    filterRegex: '',
                    sourceDataFiles: '**/results/results.xml'

                    publishHTML([allowMissing: false,
                        alwaysLinkToLastBuild: false,
                        keepAll: false,
                        reportDir: 'artifacts/reports',
                        reportFiles: '**/index.html',
                        reportName: 'HTML Report',
                        reportTitles: ''])
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
