package com.learning.search.esRepository;

import com.learning.search.model.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeESRepository extends ElasticsearchRepository<Employee, Long> {

}
