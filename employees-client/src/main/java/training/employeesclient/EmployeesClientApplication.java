package training.employeesclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootApplication
public class EmployeesClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeesClientApplication.class, args);
	}

	@Bean
	public EmployeeService employeeService(WebClient.Builder builder) {
		var webClient = builder.baseUrl("http://localhost:8080").build();

		var factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();
		return factory.createClient(EmployeeService.class);
	}

}
