package com.ericsson.cifwk.taf.executor

import groovy.util.logging.Slf4j;

import com.ericsson.cifwk.taf.executor.messagebus.EventMessage
import com.ericsson.cifwk.taf.executor.messagebus.MessageBusSender;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
/**
 * Simple monitor class allowing to read EventMessages from MessageBus
 *
 */
@Slf4j
class SimpleMonitor extends Thread{
	Channel receiveChannel
	QueueingConsumer c
	MessageBusSender sender

	private boolean finished = false
	/**
	 * Monitor instantiating listening thread
	 */
	SimpleMonitor(){
		try {
			setName("MONITOR")
			setDaemon(true)
			sender = new MessageBusSender();
			receiveChannel = sender.getConnection().createChannel();
			String queueName = receiveChannel.queueDeclare().getQueue();
			receiveChannel.queueBind(queueName, MessageBusSender.DESTINATION, "");
			c = new QueueingConsumer(receiveChannel);
			receiveChannel.basicConsume(queueName, true, c);
		} catch (Throwable ignore){
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run(){
		QueueingConsumer.Delivery delivery;
		while (! finished){
			try {
				delivery = c.nextDelivery();
				EventMessage msg = EventMessage.unmarshall(delivery.getBody())
				log.info(" [x] Received $msg");
			} catch (Exception e) {
				log.trace "Cannot get delivery"
				log.trace "Details: ",e
			}
		}
	}

	/**
	 * Closing all connections and finishing threaded excectution
	 */
	public void close(){
		finished = true
		receiveChannel?.close()
		sender?.close()
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void finalize(){
		close()
	}
}
