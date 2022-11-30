package com.panther.takeaway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panther.takeaway.common.BaseContent;
import com.panther.takeaway.common.CustomExcept;
import com.panther.takeaway.entity.*;
import com.panther.takeaway.mapper.OrderMapper;
import com.panther.takeaway.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrderMapper, Orders>
        implements OrdersService {

    @Resource
    private ShoppingCatService shoppingCatService;

    @Resource
    private UserService userService;

    @Resource
    private AddressBookService addressBookService;

    @Resource
    private OrderDetailService orderDetailService;

    @Override
    @Transactional
    public void submit(Orders orders) {

        // 获取用户ID
        Long currentID = BaseContent.getCurrentID();
        // 查询用户的购物车信息
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,currentID);
        List<ShoppingCart> list = shoppingCatService.list(lqw);// 购物车数据

        long orderId = IdWorker.getId();
        AtomicInteger amount = new AtomicInteger(0); // 原子操作保证线程安全

        List<OrderDetail> orderDetails = list.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        //订单表插入数据
        if(list.size() == 0 || list == null){
            throw new CustomExcept("购物车为空，无法下单！");
        }
        // 查询用户信息
        User user = userService.getById(currentID);
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());

        orders.setId(orderId); //订单号
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2); // 订单的状态
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(currentID);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        //向订单表插入数据，一条数据
        this.save(orders);

        //订单明细表插入数据（多条）
        orderDetailService.saveBatch(orderDetails);

        // 清空购物车
        shoppingCatService.remove(lqw);
    }
}
