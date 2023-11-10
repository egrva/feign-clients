# Feign clients

In this example we will look at working with feign technology.

* [Simple feign](#simple-feign)
* [Feign with Ribbon](#feign-with-ribbon)
* [Feign with Eureka naming server](#feign-with-eureka-naming-server)
* [Questions and answers](#questions-and-answers)

## Simple feign

For example, the basic service will be used: *Subway-service*.
It processes one request - it shows a list of metro stations.

*Feign* uses interfaces annotated with @FeignClient to generate API requests and map responses to Java classes.
It sends http requests to other services.

You only need to describe how to access the remote API service,
specifying details such as URL, request and response body, accepted headers, etc.
The Feign client will take care of the implementation details.

Let's create another service that will request a list of metro stations.
*Subway-info-base-feignclient*

Add the necessary dependencies to *pom.xml*
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-openfeign</artifactId>
	<version>2.1.3.RELEASE</version>
</dependency>
```

Now let's create feign-client

```java
@FeignClient(value="subway-service", url="http://localhost:8080")
public interface SubwayFeignService {

    @GetMapping("/subway/stations")
    SubwayStationDto getAllStations();
}
```
The method signature must be the same as that of the controller method we are accessing.

You also need to add an annotation to the main class.

```java
@SpringBootApplication
@EnableFeignClients
public class SubwayInfoBaseFeignclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubwayInfoBaseFeignclientApplication.class, args);
	}

}
```

This is all! Cool!

## Feign with Ribbon
The previous example is very cool, but we explicitly specified the request url. What if we had multiple instances that could tell us about existing subway stations?

To do this, we will use Ribbon - it will help distribute the load among several application instances.

Add the necessary dependencies to *pom.xml*
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
	<version>2.2.6.RELEASE</version>
</dependency>
```

Adding an annotation for working with Ribbon to our feign-client

```java
@FeignClient(value="subway-service")
@RibbonClient(name="subway-service")
public interface SubwayFeignService {

    @GetMapping("/subway/stations")
    SubwayStationDto getAllStations();
}
```

It is also necessary to pass the Ribbon urls on which the service instances are running,
who will tell us metro stations
```properties
subway-service.ribbon.eureka.enabled=false
subway-service.ribbon.listOfServers=http://localhost:8080,http://localhost:8085
```

All! It turned out even better!!

## Feign with Eureka naming server

But.. what happens if we want to increase the number of copies? I'll have to every time
manually add new paths? No - there is a solution!
Let's use Eureka naming service. We indicate to our service the eureka address and now, everyone
times when a new instance is created it will register with eureka and say
her address, and with the help of Ribbon we will be able to go to one of the copies, and even
without knowing specific addresses!

Let's create a service for Eureka *eureka-naming-service*

Let's add a dependency for Eureka
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

Let's create the main class

```java
@EnableEurekaServer
@SpringBootApplication
public class EurekaNamingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaNamingServiceApplication.class, args);
	}

}
```

And let’s say at what address we will have the Eureka server
```properties
spring.application.name=netflix-eureka-naming-server
server.port=8761

eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

Let's tell the service that knows metro stations that Eureka exists and that it needs to register there.

To do this, add a dependency

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    <version>2.1.2.RELEASE</version>
</dependency>
```

add an annotation to the main class

```java
@EnableDiscoveryClient
@SpringBootApplication
public class SubwayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubwayServiceApplication.class, args);
	}

}
```

and give him the Eureka server address

```properties
eureka.client.service-url.default-zone=http://localhost:8761/eureka
```

For feign client we will also add a dependency
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    <version>2.1.2.RELEASE</version>
</dependency>
```
add an annotation to the main class

```java
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class SubwayInfoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubwayInfoServiceApplication.class, args);
	}
}
```

and tell him where the Eureka server is

```properties
eureka.client.service-url.default-zone=http://localhost:8761/eureka
```

All. Now you can create an infinite number of instances of a service that contains information about metro stations, and feign-client won’t even know anything about it

yaaaaaay!

🎉🎉🎉🎉🎉🎉🎉🎉
## Questions and answers
1. What does the annotation for creating a feign client look like?
2. What is Ribbon for?
3. How to create a feign client using Eureka naming server

## Used technologies

* [Spring Cloud OpenFeign](https://cloud.spring.io/spring-cloud-openfeign/reference/html/) 
* [Ribbon](https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-ribbon.html)
* [Eureka](https://github.com/Netflix/eureka) 
