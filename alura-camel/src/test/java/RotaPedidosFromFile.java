

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.impl.DefaultCamelContext;
import org.xml.sax.SAXParseException;

public class RotaPedidosFromFile {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				
				//onException(SAXParseException.class).handled(true).to("file:error-parsing");
				
				/*onException(Exception.class).
				    handled(true).
				        maximumRedeliveries(3).
				            redeliveryDelay(4000).
				        onRedelivery(new Processor() {

				            @Override
				            public void process(Exchange exchange) throws Exception {
				                    int counter = (int) exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER);
				                    int max = (int) exchange.getIn().getHeader(Exchange.REDELIVERY_MAX_COUNTER);
				                    System.out.println("Redelivery - " + counter + "/" + max );;
				            }
				    });*/
				
				errorHandler(
					    deadLetterChannel("file:erro").
					    	useOriginalMessage().
					    	logExhaustedMessageHistory(true).
					        maximumRedeliveries(3).//tente 3 vezes
					            redeliveryDelay(2000). //espera 2 segundos entre as tentativas 
					            	onRedelivery(new Processor() {
									
										@Override
										public void process(Exchange exchange) throws Exception {
											int counter = (int) exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER);
										    int max = (int) exchange.getIn().getHeader(Exchange.REDELIVERY_MAX_COUNTER);
										    System.out.println("Redelivery - " + counter + "/" + max );
											
										}
					            	})        
						);

				from("file:pedidos?delay=5s&noop=true").
				routeId("rota-pedidos").
				//log("${file:name}"). //logando nome do arquivo
				//delay(1000).
				to("validator:pedido.xsd");
//				multicast().
//					to("direct:http").
//					to("direct:soap");
				
				from("direct:http").
					routeId("rota-http").
					setProperty("pedidoId", xpath("/pedido/id/text()")).
					setProperty("clienteId", xpath("/pedido/pagamento/email-titular/text()")).
					split().
						xpath("/pedido/itens/item").
					filter().
						xpath("/item/formato[text()='EBOOK']").
					setProperty("ebookId", xpath("/item/livro/codigo/text()")).
					marshal().xmljson().
					//log("${id} - ${body}").
					setHeader(Exchange.HTTP_METHOD, HttpMethods.GET).
					setHeader(Exchange.HTTP_QUERY,simple("ebookId=${property.ebookId}&pedidoId=${property.pedidoId}&clienteId=${property.clienteId}")).
				to("http4://localhost:8080/webservices/ebook/item");
				
				from("direct:soap").
					routeId("rota-soap").
					to("xslt:pedido-para-soap.xslt"). 
					log("${body}").
				//to("mock:soap");
					setHeader(Exchange.CONTENT_TYPE,constant("text/xml")).
					to("http4://localhost:8080/webservices/financeiro");
					
			}
			
		});

		context.start();
		
//		ProducerTemplate producer = context.createProducerTemplate();
//        producer.sendBody("direct:soap", "<pedido> ... </pedido>");
		
		Thread.sleep(20000);
		context.stop();
	}	
}
