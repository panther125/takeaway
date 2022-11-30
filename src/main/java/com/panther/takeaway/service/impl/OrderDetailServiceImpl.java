package com.panther.takeaway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panther.takeaway.entity.OrderDetail;
import com.panther.takeaway.mapper.OrderDetailMapper;
import com.panther.takeaway.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
        implements OrderDetailService {
}
