package com.solace.demo.service;

import com.solace.demo.model.Person;

public interface SolMessageService {
    void processSolaceMessage(Person person);
}
