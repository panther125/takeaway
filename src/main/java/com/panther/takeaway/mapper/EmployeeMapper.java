package com.panther.takeaway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panther.takeaway.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
