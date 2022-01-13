package com.baro.elasticsearch.controller;

import com.baro.elasticsearch.document.Passport;
import com.baro.elasticsearch.document.Person;
import com.baro.elasticsearch.search.dto.SearchRequestDTO;
import com.baro.elasticsearch.service.PassportService;
import com.baro.elasticsearch.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passport")
public class PassportController {

    private final PassportService passportService;

    @Autowired
    public PassportController(PassportService passportService) {
        this.passportService = passportService;
    }

    @PostMapping
    public void index(@RequestBody final Passport passport) {
        passportService.index(passport);
    }

    @GetMapping("/{ppsId}")
    public Passport findByPPSId(@PathVariable final Long ppsId) {
        return passportService.getByPPSId(ppsId);
    }

    @PostMapping("/search")
    public List<Passport> search(@RequestBody final SearchRequestDTO searchRequestDTO) {
        return passportService.search(searchRequestDTO);
    }
}
