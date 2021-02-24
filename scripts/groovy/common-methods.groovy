#!groovy​

/**
 * Set the dynamic parameters in pipeline properties s
 */
def setDynamicParameterProperties() {

    String sectionHeaderStyle = '''
        background: LightGreen;
        text-align: center;
    '''

    String separatorStyle = '''
        border: none;
    '''

    props = []

    initParams = [
        [
            $class: 'ParameterSeparatorDefinition',
            sectionHeader: 'Primary Scenario Load Setting',
            separatorStyle: separatorStyle,
            sectionHeaderStyle: sectionHeaderStyle
        ],
        string(name: "primary_concurrency", defaultValue: "1", description: 'How many concurrent connections to execute the test?'),
        string(name: "primary_iterations", defaultValue: "1", description: 'Specify the number of times (iteration) to execute the test.'),
        string(name: "primary_ramp_up", defaultValue: "1s", description: 'If you are running more than 1 thread, please increase the ramp-up period accordingly.'),
        [
            $class: 'ParameterSeparatorDefinition',
            sectionHeader: 'Secondary Scenario Load Setting',
            separatorStyle: separatorStyle,
            sectionHeaderStyle: sectionHeaderStyle
        ],
        string(name: "secondary_concurrency", defaultValue: "1", description: 'How many concurrent connections to execute the test?'),
        string(name: "secondary_duration", defaultValue: "1m", description: 'Specify the duration of the test.'),
        string(name: "secondary_ramp_up", defaultValue: "1s", description: 'If you are running more than 1 thread, please increase the ramp-up period accordingly.'),
        [
            $class: 'ParameterSeparatorDefinition',
            sectionHeader: 'JMeter Setting',
            separatorStyle: separatorStyle,
            sectionHeaderStyle: sectionHeaderStyle
        ],
        string(name: "app_host", defaultValue: "test-api.k6.io", description: 'Specify the domain name to access application.'),
        string(name: "jmeter_report_granularity", defaultValue: "60000", description: 'Change this parameter if you want to change the granularity of over time graphs.'),
        [
            $class: 'ParameterSeparatorDefinition',
            sectionHeader: 'Flags',
            separatorStyle: separatorStyle,
            sectionHeaderStyle: sectionHeaderStyle
        ],
        booleanParam(
            description: "Enable to send interim and final test result to BlazeMeter Service (https://a.blazemeter.com/).",
            name: "run_blazemeter_reporter",
            defaultValue: false
        )
    ]

    props.add(buildDiscarder(logRotator(artifactDaysToKeepStr: '',
    artifactNumToKeepStr: '',
    daysToKeepStr: '',
    numToKeepStr: '20')))
    props.add(parameters(initParams))
    properties(props)
}


return this
