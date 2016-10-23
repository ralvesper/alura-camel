package br.com.caelum.camel;

import javax.annotation.PostConstruct;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class Boot {

	@Autowired
    CamelContext context;

	@PostConstruct
	public void init() throws Exception {
		context.addComponent("activemq", ActiveMQComponent.activeMQComponent("tcp://localhost:61616"));
	}
	
	/*@Bean
	public RoutesBuilder rota() { 
	    return new RouteBuilder() {
	        @Override
	        public void configure() throws Exception {
	            from("file:pedidos").
	            to("activemq:queue:pedidos");
	        } 
	    };
	}*/

	public static void main(String[] args) {
		SpringApplication.run(Boot.class, args);
	}
}
