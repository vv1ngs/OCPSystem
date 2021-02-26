package org.vvings.ocpsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication(scanBasePackages = {"org.vvings.ocpsystem"})
@EnableCaching
@EnableRedisHttpSession
@MapperScan("org.vvings.ocpsystem.dao")
public class OcpSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OcpSystemApplication.class, args);
	}

}
