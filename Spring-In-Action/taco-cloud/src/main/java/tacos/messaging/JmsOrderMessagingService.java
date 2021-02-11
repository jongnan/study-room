package tacos.messaging;

import lombok.RequiredArgsConstructor;
//import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import tacos.Order;

import javax.jms.JMSException;
import javax.jms.Message;

@Service
@RequiredArgsConstructor
public class JmsOrderMessagingService implements OrderMessagingService{

    private final JmsTemplate jms;
    private final RabbitTemplate rabbit;
    private final KafkaTemplate<String, Order> kafkaTemplate;

    @Override
    public void sendOrder(Order order) {
        // JMS
         jms.convertAndSend("tacocloud.order.queue", order, this::addOrderSource);

        // RabbitMQ
        // MessageConverter converter = rabbit.getMessageConverter();
        // MessageProperties props = new MessageProperties();
        // Message message = converter.toMessage(order, props);
        // rabbit.send("tacocloud.order", message);

        // Kafka
        // kafkaTemplate.send("tacocloud.orders.topic", order);
    }

    @JmsListener(destination = "")
    public void receive() {

    }

    private Message addOrderSource(Message message) throws JMSException {
        message.setStringProperty("X_ORDER_SOURCE", "WEB");
        return message;
    }
}