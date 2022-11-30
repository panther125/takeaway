package com.panther.takeaway.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panther.takeaway.common.BaseContent;
import com.panther.takeaway.common.R;
import com.panther.takeaway.entity.Orders;
import com.panther.takeaway.service.OrdersService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/order")
@RestController
public class OrdersController {

    @Resource
    private OrdersService ordersService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("下单成功");
    }


    @GetMapping("/userPage")
    public R<Page> getOrder(int page, int pageSize){

        Page<Orders> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Orders::getUserId, BaseContent.getCurrentID());

        return R.success(ordersService.page(pageInfo,lqw));
    }
}
