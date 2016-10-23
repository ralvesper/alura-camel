package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RotaMovimentacoesXMLparaHTML {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {

			@Override
			public void configure() throws Exception {

				from("file:movimentacoes_entrada_xml?delay=5s&noop=true").
				to("xslt:movimentacoes-xml-para-html.xslt"). 
				setHeader("CamelFileName", simple("${file:name.noext}.html")).
				log("${body}").
	            to("file:movimentacoes_saida_html");
					
			}
			
		});

		context.start();
		
		Thread.sleep(20000);
		context.stop();
	}	
}
