package com.panther.takeaway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panther.takeaway.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
