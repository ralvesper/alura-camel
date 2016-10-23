package br.com.caelum.camel.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class TratadorMensagemJms implements MessageListener {

	public void onMessage(Message jmsMessage) {
		// código que processa a mensagem JMS
		try {
			System.out.println("TratadorMensagemJms working...");
			System.out.println("JMSMessageID: "+jmsMessage.getJMSMessageID());
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
		
	}
}
