package com.JIMS.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public StringEncrypter stringEncrypter() {
		String passPhrase = "akd89343My Pass Phrase";
		return new StringEncrypter(passPhrase); // Pass your passphrase or key here
	}
}
