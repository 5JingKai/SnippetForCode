package org.example.receiver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.http.HttpClient;
import java.time.Duration;

@SpringBootApplication
public class ReceiverApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReceiverApplication.class, args);
    }

	@Bean
	HttpClient httpClient() {
		return HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(10))
				.build();
	}

}
