package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.impl.DefaultCamelContext;



/*	
 Há uma alternativa ao direct e multicast. Na rota e sub-rotas podemos aplicar algo chamado de Staged event-driven architecture ou simplesmente SEDA.
 A ideia do SEDA é que cada rota (e sub-rota) possua a sua fila dedicada de entrada e as rotas enviam mensagens para essas filas para se comunicar. 
 Dentro dessa arquitetura as mensagens são chamadas de eventos. A rota fica então consumindo as mensagens/eventos dessa fila, tudo em paralelo.
 Para usar SEDA basta substituir a palavra direct por seda. O multicast não é mais necessário:
*/
public class RotaPedidos2 {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {

			@Override
			public void configure() throws Exception {

				from("file:pedidos?delay=5s&noop=true").
					routeId("rota-pedidos").
					to("seda:http").
					to("seda:soap");
				
				from("seda:http").
					routeId("rota-http").
					setProperty("pedidoId", xpath("/pedido/id/text()")).
					setProperty("clienteId", xpath("/pedido/pagamento/email-titular/text()")).
					split().
						xpath("/pedido/itens/item").
					filter().
						xpath("/item/formato[text()='EBOOK']").
					setProperty("ebookId", xpath("/item/livro/codigo/text()")).
					marshal().xmljson().
					log("${id} - ${body}").
					setHeader(Exchange.HTTP_METHOD, HttpMethods.GET).
					setHeader(Exchange.HTTP_QUERY,simple("ebookId=${property.ebookId}&pedidoId=${property.pedidoId}&clienteId=${property.clienteId}")).
				to("http4://localhost:8080/webservices/ebook/item");
				
				from("seda:soap").
					routeId("rota-soap").
					log("chamando servico soap - ${body}").
				to("mock:soap");
			}
			
		});

		context.start();
		
		ProducerTemplate producer = context.createProducerTemplate();
        producer.sendBody("seda:soap", "<pedido> ... </pedido>");
		
		Thread.sleep(20000);
		context.stop();
	}	
}
