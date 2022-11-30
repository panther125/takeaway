package com.panther.takeaway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panther.takeaway.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
