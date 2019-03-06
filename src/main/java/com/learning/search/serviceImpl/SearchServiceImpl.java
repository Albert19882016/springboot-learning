package com.learning.search.serviceImpl;

import com.learning.search.esRepository.EmployeeESRepository;
import com.learning.search.esRepository.EmployeeScriptReporitory;
import com.learning.search.mapper.SearchServiceMapper;
import com.learning.search.mapper.SearchServiceRepository;
import com.learning.search.model.Employee;
import com.learning.search.service.SearchService;
import com.learning.search.utils.ESConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class SearchServiceImpl implements SearchService {

    private static Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired
    private EmployeeESRepository employeeESRepository;

    @Autowired
    private EmployeeScriptReporitory employeeScriptReporitory;

    @Autowired
    private SearchServiceMapper searchServiceMapper;

    @Autowired
    private SearchServiceRepository searchServiceRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    @Transactional(readOnly = true)
    public Employee search() {
        Map<String, Object> params = new HashMap<>();
        Employee employee = searchServiceMapper.search();
        String filters = String.format(ESConstants.term_keyword,"firstName","Shahaf");

        params.put("from",0);
        params.put("size",20);
        params.put("orderBy",String.format(ESConstants.sort_asc,"firstName"));
        params.put("filters", filters);
        params.put("musts", ESConstants.match_all);
        employee = employeeScriptReporitory.queryOne("search-template", params);
        return employee;
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findOne(String empNo) {
        return null;
    }

    @Override
    public void saveToEs() {
        List<Employee> employees = searchServiceRepository.findAll();
        for(Employee employee: employees) {
            employeeESRepository.save(employee);
        }
    }

    @Override
    public List<Employee> getOneFromEs() {
        Iterable<Employee> all = employeeESRepository.findAll();
        List<Employee> list = new ArrayList<>();
        Iterator<Employee> iterator = all.iterator();
        if (iterator.hasNext()){
            list.add(iterator.next());
        }
        return list;
    }
}
