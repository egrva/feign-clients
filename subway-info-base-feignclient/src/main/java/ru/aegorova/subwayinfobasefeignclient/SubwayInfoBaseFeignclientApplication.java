package ru.aegorova.subwayinfobasefeignclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SubwayInfoBaseFeignclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubwayInfoBaseFeignclientApplication.class, args);
	}

}
