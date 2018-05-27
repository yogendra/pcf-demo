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
1. jq: JSON Processor

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
curl -s http://localhost:8080/actuator/health | jq

```

#### Run on PCF

You can create this application (and update it in future) using the simple `push` command on `cf` cli

```bash
cf push
```

After some processing, you should see lines showing 1 application instance in running or starting status
```bash
     state      since                  cpu      memory         disk           details
#0   running    2018-05-27T14:39:07Z   147.5%   424.9M of 2G   140.6M of 1G
``` 

Confused? Just hold on. Lets check the same Actuator health url for our application. In the output above you should see a line stating `route:...`. THats the path to your application. Lets use that and access our application.

```bash 
curl -s https://pcf-demo-2018.cfapps.io/actuator/health | jq
```

#### Magic of manifest.yml

Now! What just happened? How does it know what command to run?  Relax! Its not magic. All the infromation was in a `manifest.yml` file. We provide bare minimal information in our manifest file, such as name, memory, path, buildpack, routes and environment variables. 

Buildpacks are toolchains used to build your application. You can create your own or use one of the many that are published by cloudfoundry and community. We are using Java buildpack. No shell scripts, pipeline config, etc. in your application code. All that is ripped out and configured in your build pack. 


#### PCF Feature : Scale Application

You can scale an application without redeploying. Lets add one more instance to this application. You need to provide your desire no. of instances. PCF will do the right thing to scal up or scale down according to the difference between desired and actuals.

```bash
cf scale pcf-demo -i 2
```

See the application and instance status using:

```bash
cf app pcf-demo
```
This will show you 2 instances in starting or running state.

Lets try and scale it down and see if our request processing is interupted. On a second terminal, lets run a loop and keep requesting status, until we get error. So, if all works well, this loop will keep going, until we interrupt it.

```bash
CTR=0; while true 
do
    ((CTR++))
    reponse=
    if [ "`curl -s http://pcf-demo-2018.cfapps.io/actuator/health 2> /dev/null`" = '{"status":"UP"}' ]
    then 
        echo "R# $CTR: PASS" 
    else 
        echo "R# $CTR: FAIL"
    fi
done
```

Now, lets scale down application

``` bash
cf scale pcf-demo -i 1
```

This command has kill one instance. But platform will still continue to process request.


### Custom request processing

Now we have an application running in PCF. But it is not doing anything significant. 
Lets add a custom greeting service

Checkout `greeting` branch demonstrates custom request processing

``` bash 
git checkout greetings
./mvnw clean install spring-boot:run
```

On a second console, run forllowing command to test custom greeting api

```bash
curl -s 'http://localhost:8080/greetings?name=Yogendra'
```

This will give you a greetings with server time. Lets take this to PCF. Its just a push away

```bash
cf push
``` 
This would take few seconds to finish. At the end you should see status from instances.
Lets try to access same greetings service from PCF

```bash
curl -s https://pcf-demo-2018.cfapps.io/greetings?name=Yogendra
```
And this will yield same results


### Add Persistence

So far so good. Lets add some persistence to our application. We will create a To Do API.

This API will:

* Create new todo item by takign description from user
* Mark a todo item as completed
* Give you list of all items, only pending items and only complete items

Broadly: 
* We will use Spring JPA Rest
* Use an H2 database for local testing
* Use ElephantSQL on PCF for production


Solution is on persistence branch, so check it out, build and run

``` bash
git checkout todo
./mvnw clean install spring-boot:run
```

On a second console, 

* List todos
    ```bash
    curl -s http://localhost:8080/todos
    ``` 
* Create a new item
    ```bash
    curl -X POST http://localhost:8080/todos -d '{"description": "Publish Repo"}' 
    ```

This all works fine. Before we proceed to deploy on cloud, we need to provision and bind database service

#### Database Service

We will create a simple To Do Api. This api will be able to crate and query ToDo items. Get list of all all items:

```bash
curl http://localhost:8080/todos
```

Create a new todo item:

```bash
curl http://localhost:8080/todos -d '{"title": "Hello", "done": false}'
```

Delete a todo item

```bash
curl http://localhost:8080/todos/1 -X DELETE
```

We can use in memory database (H2) for testing locally. On cloud we will use ElephantSQL. Its a derivetive of PostgreSQL.


We will use ElephantSQL service. This is a postgres based database service. Lets search for it first.

```bash
cf marketplace -s elephantsql

...

service plan   description                                            free or paid
turtle         4 concurrent connections, 20MB Storage                 free
panda          20 concurrent connections, 2GB Storage                 paid
hippo          300 concurrent connections, 100 GB Storage             paid
elephant       300 concurrent connections, 1000 GB Storage, 500Mbps   paid
```

We an use turtle service plan for our demo. Let create a service first:

```bash
cf create-service elephantsql turtle pcf-demo-db
```

After creating service, you can need to bind it to your application.

```bash
cf bind-service pcf-demo pcf-demo-db
```

After binding the service, you need to restart application so that it can pickup bindings

```bash
cf restart pcf-demo
```


#### Run Persistence on PCF

We have updated application to allow posting and querying ToDo. We also have a bound service for SQL on PCF. Now we will push our application changes to PCF.

```bash
cf push pcf-demo
``` 

Once its up and running, you can test your API with following commands:
```bash
curl -s -H content-type:application/json https://pcf-demo-2018.cfapps.io/todos -d '{"title": "Bye", "done":false}'
```

List all ToDo items:

```base
curl https://pcf-demo-2018.cfapps.io
```




[pcf-signup]: https://try.run.pivotal.io/homepage
[cf-cli-download]: https://docs.cloudfoundry.org/cf-cli/install-go-cli.html
