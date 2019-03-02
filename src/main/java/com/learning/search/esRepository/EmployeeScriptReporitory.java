package com.learning.search.esRepository;

import com.learning.search.model.Employee;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeScriptReporitory extends ElasticsearchScriptFactory<Employee> {
}
