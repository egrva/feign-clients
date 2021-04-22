# Feign clients

В этом примере мы рассмотрим работу с технологией feign.

* [Simple feign](#simple-feign)
* [Feign with Ribbon](#feign-with-ribbon)
* [Feign with Eureka naming server](#feign-with-eureka-naming-server)

## Simple feign

Для примера будет использоваться базовый сервис: *Subway-service*.
Он обрабатывает один запрос - показывает список станций метрополитена.

*Feign* использует интерфейсы аннотированные @FeignClient чтобы генерировать API запросы и мапить ответ на Java классы.
Он шлет http запросы другим сервисам.

Вам нужно только описать, как получить доступ к удаленной службе API, 
указав такие детали, как URL, тело запроса и ответа, принятые заголовки и т. д. 
Клиент Feign позаботится о деталях реализации.

Создадим другой сервис, который будет запрашивать список станций метрополитена.
*Subway-info-base-feignclient*

Добавим нужные зависимости в *pom.xml*
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-openfeign</artifactId>
	<version>2.1.3.RELEASE</version>
</dependency>
```

Теперь создадим feign-client

```java
@FeignClient(value="subway-service", url="http://localhost:8080")
public interface SubwayFeignService {

    @GetMapping("/subway/stations")
    SubwayStationDto getAllStations();
}
```
сигнатура метода должна быть такой же, как и у метода контроллера, к которому мы обращаемся.

Также необходимо добавить аннотацию в main класс.

```java
@SpringBootApplication
@EnableFeignClients
public class SubwayInfoBaseFeignclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubwayInfoBaseFeignclientApplication.class, args);
	}

}
```

Все! Круто!

## Feign with Ribbon
Предыдущий пример очень крутой, но мы явно указали url запроса. А что, 
если у нас будет несколько экземпляров, которые смогут говорить нам о существующих 
станциях метро?

Для этого воспользуемся Ribbon - он поможет распределить нагрузку среди нескольких
инстансов приложения.

Добавляем зависимость в pom.xml
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
	<version>2.2.6.RELEASE</version>
</dependency>
```

Добавляем аннотацию для работы с Ribbon нашему feign-client'у

```java
@FeignClient(value="subway-service")
@RibbonClient(name="subway-service")
public interface SubwayFeignService {

    @GetMapping("/subway/stations")
    SubwayStationDto getAllStations();
}
```

Также необходимо передать Ribbon urls на которых запущены экземпляры сервиса,
который подскажет нам станции метро 
```properties
subway-service.ribbon.eureka.enabled=false
subway-service.ribbon.listOfServers=http://localhost:8080,http://localhost:8085
```

Все! Получилось еще лучше!!

## Feign with Eureka naming server

Но.. что будет, если мы захотим увеличить количество экзепляров? Придется каждый раз 
вручную добавлять новые пути? Нет - есть решение!
Воспользуемся Eureka naming service. Укажем нашему сервису адрес eureka и теперь, каждый
раз, когда будет создаваться новый экземпляр, он будет регистрироваться в eureka и говорить
ей свой адрес, а с помощью Ribbon мы сможем ходить на какой-то из экзепляров, при этом даже 
не зная конкретных адресов! 

Создадим сервис для Eureka *eureka-naming-service*

 Добавим зависимость для Eureka
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

Создадим main класс

```java
@EnableEurekaServer
@SpringBootApplication
public class EurekaNamingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaNamingServiceApplication.class, args);
	}

}
```

И скажем на каком адресе у нас будет Eureka server
```properties
spring.application.name=netflix-eureka-naming-server
server.port=8761

eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

Скажем сервису, который знает станции метро, что существует Eureka и что ему надо 
там зарегистрироваться.

Для этого добавим зависимость

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    <version>2.1.2.RELEASE</version>
</dependency>
```

добавим аннотцию в main класс

```java
@EnableDiscoveryClient
@SpringBootApplication
public class SubwayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubwayServiceApplication.class, args);
	}

}
```

и передадим ему адрес Eureka server

```properties
eureka.client.service-url.default-zone=http://localhost:8761/eureka
```

Для feign client'а также добавим зависимость
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    <version>2.1.2.RELEASE</version>
</dependency>
```
добавим аннотцию в main класс

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

и скажем ему, где находится Eureka server

```properties
eureka.client.service-url.default-zone=http://localhost:8761/eureka
```

Все. Теперь можно создать бесконечное число экземпляров сервиса, который содержит инфу 
о станциях метрополитена, а feign-client даже ничего об этом не узнает

урааааа

🎉🎉🎉🎉🎉🎉🎉🎉
## Used technologies

* [Spring Cloud OpenFeign](https://cloud.spring.io/spring-cloud-openfeign/reference/html/) 
* [Ribbon](https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-ribbon.html)
* [Eureka](https://github.com/Netflix/eureka) 
