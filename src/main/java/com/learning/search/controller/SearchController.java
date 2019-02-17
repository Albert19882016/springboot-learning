package com.learning.search.controller;

import com.learning.search.model.Employee;
import com.learning.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/search")
@EnableAutoConfiguration
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public Employee search(){
        return searchService.search();
    }

    @RequestMapping(path = "/findByNo", method = RequestMethod.GET)
    public Employee findOne(@RequestParam("empNo")String empNo){
        return searchService.findOne(empNo);
    }

}
