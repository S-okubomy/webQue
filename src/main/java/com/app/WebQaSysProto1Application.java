package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan
// @ComponentScan("controller") // コントローラのパッケージが違う場合、basePackages属性指定
public class WebQaSysProto1Application {

	public static void main(String[] args) {
		SpringApplication.run(WebQaSysProto1Application.class, args);
	}
}
