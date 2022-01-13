package com.baro.elasticsearch.service;

import com.baro.elasticsearch.document.Person;
import com.baro.elasticsearch.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void savePerson(final Person person) {
        personRepository.save(person);
    }

    public Person findPersonById(Long ppsId) {
        return personRepository.findById(ppsId).orElse(null);
    }
}
