package kam.dnb.loanapp;

import kam.dnb.loanapp.config.security.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class LoanApp {

	public static void main(String[] args) {
		SpringApplication.run(LoanApp.class, args);
	}

}
