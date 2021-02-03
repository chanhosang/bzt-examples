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


To run load test:
```
bzt scripts/bzt/bzt-jmeter-demo-1.yml
```

To run load test and monitor life stats from BlazeMeter Service Dashboard:
```
bzt scripts/bzt/bzt-jmeter-demo-1.yml -report
```

To run load test and specify parameter values for different load via command line:
```
bzt scripts\bzt\bzt-jmeter-demo-3.yml -o settings.env.THREAD_USERS=2 -o settings.env.THREAD_RAMPUP=4s
```

To run load test that will generate JMeter HTML Report:
```
bzt scripts/bzt/bzt-jmeter-load-test.yml \
-o settings.env.RESULTS_DIR=results \
-o settings.env.THREAD_USERS=2 \
-o settings.env.THREAD_ITERATION=20 \
-o settings.env.THREAD_RAMPUP=20s
```



## What if using Maven instead of Taurus?

By using maven, this is how to run the same scenarios as configurewd in **bzt-jmeter-load-test.yml**:
```
cd maven
mvn clean verify -P jmeter-test-1 -f pom.xml -DnumberOfThreads=3 -DrampUp=20 -DloopCount=20
mvn clean verify -P jmeter-test-2 -f pom.xml -DnumberOfThreads=3 -DrampUp=20 -Dduration=20
```

## Useful References

[Taurus Configuration Syntax](https://gettaurus.org/docs/ConfigSyntax/)

