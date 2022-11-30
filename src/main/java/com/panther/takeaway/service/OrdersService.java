package com.panther.takeaway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panther.takeaway.entity.Orders;

public interface OrdersService extends IService<Orders> {

    /**
     * 用户下单操作
     * @param orders 订单详情
     */
    void submit(Orders orders);

}
