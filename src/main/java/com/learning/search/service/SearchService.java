package com.learning.search.service;

import com.learning.search.model.Employee;
import java.util.List;

public interface SearchService {

    Employee search();

    Employee findOne(String empNo);

    void saveToEs();

    List<Employee> getOneFromEs();
}
