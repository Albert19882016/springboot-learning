package com.learning.search.mapper;

import com.learning.search.model.Employee;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchServiceMapper {

    Employee search();

}
