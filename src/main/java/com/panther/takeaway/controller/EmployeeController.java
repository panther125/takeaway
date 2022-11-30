package com.panther.takeaway.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panther.takeaway.common.R;
import com.panther.takeaway.entity.Employee;
import com.panther.takeaway.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        if("".equals(employee.getUsername()) || "".equals(employee.getPassword())){
            return R.error("请输入用户名或密码！！！");
        }

        // 1。密码MD5处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2. 根据用户名查讯数据库
        String username = employee.getUsername();
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,username);
        Employee emp = employeeService.getOne(lqw);

        // 判断用户是否存在
        if(emp == null){
            return R.error("登录失败！");
        }
        // 密码比对
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误！");
        }
        // 判断员工状态
        if(emp.getStatus() == 0){
            return R.error("账号异常！");
        }

        // 登录成功将对象存入Session
        request.getSession().setAttribute("employee",emp.getId());

        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){

        // 清除session
        request.getSession().removeAttribute("employee");

        return R.success("退出成功！");
    }

    /**
     * 保存员工
     */
    @PostMapping
    public R<String> Save(HttpServletRequest request,@RequestBody Employee employee){

        // 设置初始密码
        String initPWD = DigestUtils.md5DigestAsHex("123456".getBytes());

        employee.setPassword(initPWD);
        // 公共字段统一处理
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setCreateUser((Long)request.getSession().getAttribute("employee"));
//
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long)request.getSession().getAttribute("employee"));

        return  employeeService.save(employee) ? R.success("用户添加成功") : R.error("用户添加失败");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize,String name){

        Page<Employee> pageInfo = new Page<>(page,pageSize);

        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.like(!StringUtils.isEmpty(name),Employee::getName,name);
        lqw.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo,lqw);

        return R.success(pageInfo);
    }

    @GetMapping("/{id}")
    public R<Employee> getEmploy(@PathVariable("id") Long id){
        return R.success(employeeService.getById(id));
    }

    @PutMapping
    public R<String> errEmployee(HttpServletRequest request,@RequestBody Employee employee){

//       employee.setUpdateTime(LocalDateTime.now());
//       employee.setUpdateUser((Long)request.getSession().getAttribute("employee"));

       return employeeService.updateById(employee) ?
                R.success("修改成功！") : R.error("修改失败！");
    }


}
