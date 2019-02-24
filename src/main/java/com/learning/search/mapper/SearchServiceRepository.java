package com.learning.search.mapper;

import com.learning.search.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchServiceRepository extends JpaRepository<Employee, Long> {
}
