package com.epam.gym.message;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.gym.dto.training.TrainingReportRequest;
import com.epam.gym.exception.JMSException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainingReportMessageSenderTest {

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private TrainingReportMessageSender messageSender;

    @Test
    void testSend()  {
        TrainingReportRequest request = new TrainingReportRequest();

        Mockito.doNothing().when(jmsTemplate).convertAndSend(any(String.class), any(Object.class), any(MessagePostProcessor.class));

        messageSender.send(request);

        verify(jmsTemplate).convertAndSend(anyString(),  any(Object.class), any(MessagePostProcessor.class));
    }

    @Test
    void testSendWithJMSException()  {
        TrainingReportRequest request = new TrainingReportRequest();

        doThrow(new JMSException("Simulated JMSException")).when(jmsTemplate).convertAndSend(any(String.class), any(Object.class), any(MessagePostProcessor.class));

        assertThrows(com.epam.gym.exception.JMSException.class, () -> messageSender.send(request));

        verify(jmsTemplate).convertAndSend(anyString(), any(), any(MessagePostProcessor.class));

    }
}
