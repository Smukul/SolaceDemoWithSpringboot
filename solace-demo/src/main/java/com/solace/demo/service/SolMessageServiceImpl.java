package com.solace.demo.service;

import com.solace.demo.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SolMessageServiceImpl implements SolMessageService{
    private static Logger logger = LoggerFactory.getLogger(SolMessageServiceImpl.class);
    @Override
    public void processSolaceMessage(Person person) {
        logger.info("In processSolaceMessage ..");
        logger.info("Person ID :: "+person.getId());
    }
}
