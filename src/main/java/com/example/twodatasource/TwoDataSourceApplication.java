package com.example.twodatasource;

import com.xpand.starter.canal.annotation.EnableCanalClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCanalClient
//@MapperScan(basePackages = {"com.example.twodatasource.test.mapper","com.example.twodatasource.test1.mapper"})
public class TwoDataSourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwoDataSourceApplication.class, args);
	}

}
