package br.com.caelum.camel.mq;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.impl.DefaultCamelContext;

public class EnviaPedidosParaFila {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		context.addComponent("activemq", ActiveMQComponent.activeMQComponent("tcp://localhost:61616"));
		
		context.addRoutes(new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				
				from("file:pedidos?noop=true").
				delay(1000).
				log("Enviando ${file:name} para o ActiveMQ").
				to("activemq:queue:pedidos");
			}
			
		});

		context.start();
		
		Thread.sleep(20000);
		
		context.stop();
	}	
}
