# BlazeMeter Taurus Examples
Here are some examples on how to run JMeter scripts by using [Taurus](https://gettaurus.org/).

Taurus is an open source automation tool created by Blazemeter which provides an abstraction layer over JMeter... also Grinder, Gatling and even Selenium tests.


In case you have already using JMeter scripts for performance/load testing, running JMeter through Taurus helps to overcome various challenges.

Taurus provides a simple way to create, run and analyze performance/load tests.

It allows you to execute existing JMX test scripts, parameterizing it via YAML, merging multiple test scripts easily into a single test run, real-time reporting and so on.

Also, it provides a reporting option by integrating with the [blazemeter.com](https://a.blazemeter.com/app/performance) service!



## Pre-requisite
* OS: Linux

    **Note:** The taurus scripts utilise ShellExec service to run shell command in order to generate HTML Report from *.jtl file. IF using Windows, it won't be able to generate the HTML Report but you can still run the test.
* Installed Taurus

    https://gettaurus.org/install/Installation/

## How to run?

The scenarios and the default load profiles are clearly defined in Taurus YAML script: *scripts/bzt/bzt-jmeter-load-test.yml*


To run load test with default settings:
```
bzt scripts/bzt/bzt-jmeter-load-test.yml
```

To run load test with default settings, and monitor life stats from BlazeMeter Service Dashboard:
```
bzt scripts/bzt/bzt-jmeter-load-test.yml -report
```

To run load test by overriding the user defined variables and thread group properties via command line:
```
bzt scripts/bzt/bzt-jmeter-load-test.yml \
-o settings.env.APP_HOST=test-api.k6.io \
-o execution.0.concurrency=1 \
-o execution.0.ramp-up=1s \
-o execution.0.iterations=10 \
-o execution.1.concurrency=1 \
-o execution.1.ramp-up=1s \
-o execution.1.hold-for=1m \
-report
```

To run load test and generate JMeter HTML Report upon test completion:
```
bzt scripts/bzt/bzt-jmeter-load-test.yml scripts/bzt/bzt-jmeter-load-test-reporting.yml\
-o modules.jmeter.properties="{'jmeter.reportgenerator.overall_granularity':60000}"
-o modules.jmeter.properties="{'jmeter.reportgenerator.report_title': Load Test Dashboard}"
```



## What if using Maven instead of Taurus?

By using maven, this is how to run the same scenarios as configured in **bzt-jmeter-load-test.yml**:
```
mvn clean verify -P jmeter-test-1 -f jmeter/pom.xml -DnumberOfThreads=2 -DrampUp=10 -DloopCount=10

mvn clean verify -P jmeter-test-2 -f jmeter/pom.xml -DnumberOfThreads=2 -DrampUp=20 -Dduration=60
```
As you can see, you'll need to repeat the command for two times.

Also, the jmeter scripts need to explicitly set the user defined variables from maven properties.

For more info, refer to [Functions and Variables](https://jmeter.apache.org/usermanual/functions.html)

## Useful References

[Taurus Configuration Syntax](https://gettaurus.org/docs/ConfigSyntax/)

