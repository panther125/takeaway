package com.panther.takeaway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panther.takeaway.entity.ShoppingCart;
import com.panther.takeaway.mapper.ShoppingCarMapper;
import com.panther.takeaway.service.ShoppingCatService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCarMapper, ShoppingCart>
        implements ShoppingCatService {
}
