package com.solace.demo.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.demo.model.Person;
import com.solace.demo.service.SolMessageService;
import com.solace.demo.service.SolMessageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class CustomMessageListener implements MessageListener {
    private static Logger logger = LoggerFactory.getLogger(CustomMessageListener.class);
    @Autowired
    SolMessageServiceImpl messageService;

    @Override
    public void onMessage(Message message) {
        String messageData;
        Person person;
        if(message instanceof TextMessage){
            TextMessage textMessage = (TextMessage)message;
            try{
                messageData = textMessage.getText();
                logger.info("Message Data :: "+messageData);
                ObjectMapper mapper = new ObjectMapper();
                person = mapper.readValue(messageData, Person.class);
                if(person == null) {
                    logger.error("Invalid message from the solace queue");
                }else {
                    logger.info("Successfully parsed solace message to object.");
                    messageService.processSolaceMessage(person);
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
        } else {
            logger.info(message.toString());
            logger.info("Invalid message. Skipping ....");
        }
    }
}
