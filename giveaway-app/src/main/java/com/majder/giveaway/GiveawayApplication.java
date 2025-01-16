package com.majder.giveaway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulithic;

@Modulithic(
		sharedModules = {"useraccount", "utils"}
)
@SpringBootApplication
public class GiveawayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GiveawayApplication.class, args);
	}

}
