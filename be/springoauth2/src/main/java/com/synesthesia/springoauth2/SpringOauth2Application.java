package com.synesthesia.springoauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

@SpringBootApplication
public class SpringOauth2Application {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(SpringOauth2Application.class, args);
	}

}
