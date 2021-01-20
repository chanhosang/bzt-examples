# bzt-examples
Here are some examples on how to run JMeter scripts by using [Taurus](https://gettaurus.org/).

Taurus is an open source automation tool created by Blazemeter which provides an abstraction layer over JMeter... also Grinder, Gatling and even Selenium tests.


In case you have already using JMeter scripts for performance/load testing, running JMeter through Taurus helps to overcome various challenges.

Taurus provides a simple way to create, run and analyze performance/load tests.

It allows you to execute existing JMX test scripts, parameterizing it via YAML, merging multiple test scripts easily into a single test run, real-time reporting and so on.

Also, it provides a reporting option by integrating with the‘blazemeter.com’ service!



## Pre-requisite
* Installed Taurus

    https://gettaurus.org/install/Installation/

## How to run?

To run a demo test:
```
bzt bzt-jmeter-load-test.yml -report
```

## Useful References

[Taurus Configuration Syntax](https://gettaurus.org/docs/ConfigSyntax/)

