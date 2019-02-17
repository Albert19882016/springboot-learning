package com.learning.search.ESRepository;

import com.learning.search.model.EmployeeIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface SearchRepository extends ElasticsearchRepository<EmployeeIndex, Long> {

}
