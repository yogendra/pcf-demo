# PCF Demo

This is a simple application demonstrating the PCF capabilities using Spring Boot
I have organized the project into separate branched to demonstrate a journey.

Clone this repository on your local machine and start digging

``` bash
git clone https://github.com/yogendra/pcf-demo.git pcf-demo
cd pcf-demo
git checkout init
```

You need to go to the `init` branch because I have kept master branch for the final state.

## Pre-requisites

This demo requires:

1. Git
1. Java
1. Eclipse or IntelliJe or any other ide
1. [CloudFoundry CLI][cf-cli-download]

## Run Application 

### Login to PCF
You should first connect to your PWS account. If you don't have one, get one [here][pcf-signup] by following simple signup process.
You can now login to your PWS

``` bash 
cf login -a api.run.pivotal.io
```

Just follow on-screen instruction to login to you account.

### Create application

#### Run Locally

Lets quickly build and test application locally, before we proceed to PCF deployment.

```bash
./mvnw clean install
```

This command will build and test the application code. Additionally, lets run it locally and test

```bash
./mvnw spring-boot:run
```

On a second termincal windows, run a curl command to check status.

```bash
curl -s http://localhsot:8080/actuator/health | jq

```

#### Run on PCF

You can create this application (and update it in future) using the simple `push` command on `cf` cli

```bash
cf push
```

After some processing, you should see lines showing 2 application instances running/starting
```bash
     state      since                  cpu      memory         disk           details
#0   running    2018-05-27T14:39:07Z   147.5%   424.9M of 2G   140.6M of 1G
#1   starting   2018-05-27T14:38:45Z   88.4%    455.3M of 2G   140.6M of 1G
``` 

This is a good point to check the application status using a Actuator's health endpoint

```bash 
curl -s http://pcf-demo-2018.cfapps.io/actuator/health | jq
```

[pcf-signup]: https://try.run.pivotal.io/homepage
[cf-cli-download]: https://docs.cloudfoundry.org/cf-cli/install-go-cli.html