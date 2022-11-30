package com.panther.takeaway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panther.takeaway.entity.Employee;
import com.panther.takeaway.mapper.EmployeeMapper;
import com.panther.takeaway.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {


}
