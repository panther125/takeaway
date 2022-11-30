package com.panther.takeaway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panther.takeaway.entity.DishFlavor;
import com.panther.takeaway.mapper.DishFlavorMapper;
import com.panther.takeaway.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
        implements DishFlavorService {
}
