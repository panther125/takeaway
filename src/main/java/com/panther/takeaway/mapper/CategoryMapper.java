package com.panther.takeaway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panther.takeaway.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
