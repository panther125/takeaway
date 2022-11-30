package com.panther.takeaway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panther.takeaway.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCarMapper extends BaseMapper<ShoppingCart> {
}
