package com.solace.demo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

@Component
public class CustomExceptionListener implements ExceptionListener {
    private static Logger logger = LoggerFactory.getLogger(CustomExceptionListener.class);
    @Override
    public void onException(JMSException e) {
        logger.error(e.getLinkedException().getMessage());
        e.printStackTrace();
    }
}
