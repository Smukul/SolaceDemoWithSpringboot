package com.solace.demo.config;

import com.solace.demo.listener.CustomExceptionListener;
import com.solace.demo.listener.CustomMessageListener;
import com.solacesystems.jms.SolConnectionFactory;
import com.solacesystems.jms.SolJmsUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;


@Configuration
@PropertySource({"classpath:application.properties"})
public class BeanConfig {
    private static final Logger logger = LoggerFactory.getLogger(BeanConfig.class);
    @Autowired
    private Environment env;
    @Autowired
    private CustomExceptionListener exceptionListener;

    @Bean
    public SolConnectionFactory solConnectionFactory() throws Exception{
        SolConnectionFactory connectionFactory = SolJmsUtility.createConnectionFactory();
        connectionFactory.setHost(env.getProperty("solace.java.host"));
        connectionFactory.setVPN(env.getProperty("solace.java.msgVpn"));
        connectionFactory.setUsername(env.getProperty("solace.java.clientUsername"));
        connectionFactory.setPassword(env.getProperty("solace.java.clientPassword"));
        connectionFactory.setClientID(env.getProperty("solace.java.clientName"));
        return connectionFactory;
    }

    @Bean
    public CustomMessageListener jmsMessageListener() {
        return new CustomMessageListener();
    }

    @Bean
    public Connection connection(){
        Connection connection = null;
        Session session;
        try{
            connection = solConnectionFactory().createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(env.getProperty("solace.message.consumer.queue"));
            MessageConsumer messageConsumer = session.createConsumer(queue);
            messageConsumer.setMessageListener(jmsMessageListener());
            connection.setExceptionListener(exceptionListener);
            connection.start();
            logger.info("Connection Started. Awaiting for message ...");
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return connection;
    }
}
