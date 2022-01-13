package com.baro.elasticsearch.controller;

import com.baro.elasticsearch.document.Person;
import com.baro.elasticsearch.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public void savePerson(@RequestBody final Person person) {
        personService.savePerson(person);
    }

    @GetMapping("/{ppsId}")
    public Person findByPersonId(@PathVariable final Long ppsId) {
        return personService.findPersonById(ppsId);
    }
}
