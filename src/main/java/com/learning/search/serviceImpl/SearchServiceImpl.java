package com.learning.search.serviceImpl;

import com.learning.search.ESRepository.SearchRepository;
import com.learning.search.mapper.SearchServiceMapper;
import com.learning.search.model.Employee;
import com.learning.search.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SearchServiceImpl implements SearchService {

    private static Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private SearchServiceMapper searchServiceMapper;

    @Override
    @Transactional(readOnly = true)
    public Employee search() {
        Employee e = searchServiceMapper.search();

        return e;
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findOne(String empNo) {
        return null;
    }
}
