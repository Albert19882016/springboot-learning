package com.learning.search.model;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Employee既是数据库的实体类，也是ES的Document,
 * 然后是elasticsearch的repository接口，继承的是ElasticsearchRepository
 * https://www.cnblogs.com/shuaiqing/p/9233174.html
 *
 * spring-data-mongo和spring-data-elasticsearch都属于spring的spring-data项目，提供各种数据库数据访问的封装(mongo、redis、oracle、mysql、jpa、elasticsearch等等等等)。
 * 他们封装的格式基本一样；ElasticsearchRepository 和 JpaRepository都继承spring-data-commons的PagingAndSortingRepository。
 *
 * 在使用同一个实体类的时候，需要避免repository扫描问题，需要让ElasticsearchRepository只扫描ES的repository包，
 * 其他各自只扫自己的包。
 *
 * 因此在各自的配置类中加入 includeFilters
 */
@Entity
@Table(name = "employees")
@Document(indexName = "employees", type = "employee")
public class Employee implements Serializable {

    @Id
    private Long id;

    private String empNo;

    @Field(type=FieldType.Text)
    private String firstName;

    private String birthDate;

    private String lastName;

    private Byte gender;

    private String hireDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }
}
