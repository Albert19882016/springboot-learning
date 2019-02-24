package com.learning.search.ESRepository;

import com.learning.search.model.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface SearchRepository extends ElasticsearchRepository<Employee, Long> {

}
