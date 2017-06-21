package com.mybatis.mapper;

import com.mybatis.bean.Employee;

/**
 * Created by chenrun on 2017/6/21.
 */
public interface EmployeeMapper {
    public Employee selectEmployeeById(Integer integer);
}
