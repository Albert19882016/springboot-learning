package com.learning.search.service;

import com.learning.search.model.Employee;

public interface SearchService {

    Employee search();

    Employee findOne(String empNo);
}
