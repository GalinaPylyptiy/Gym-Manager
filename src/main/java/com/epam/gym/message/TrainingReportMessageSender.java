package com.epam.gym.message;

import com.epam.gym.dto.training.TrainingReportRequest;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class TrainingReportMessageSender {

    private static final String QUEUE_NAME = "training-report";
    private final JmsTemplate jmsTemplate;
    private Logger LOG = LoggerFactory.getLogger(TrainingReportMessageSender.class);

    public TrainingReportMessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(TrainingReportRequest request) {
        try {
            LOG.info("Sending message to ActiveMQ broker: " + request);
            jmsTemplate.convertAndSend(QUEUE_NAME, request, this::setHeaders);
            LOG.info("Message was sent to ActiveMQ broker");
        } catch (Exception ex) {
            LOG.error("Error sending message to broker", ex.getCause());
            throw new com.epam.gym.exception.JMSException("Error sending message to broker");
        }
    }

    private Message setHeaders(Message message) throws JMSException {
        String transactionIdKey = "transactionId";
        String transactionId = MDC.get(transactionIdKey);
        message.setStringProperty(transactionIdKey, transactionId);
        return message;
    }
}
