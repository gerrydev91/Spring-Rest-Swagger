package sv.gerry.RESTJpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


//import sv.gerry.RESTJpa.config.SwaggerConfig;
import sv.gerry.RESTJpa.repositories.LaptopRepository;


@SpringBootApplication
public class RestJpaApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(RestJpaApplication.class, args);
		
	}

}
