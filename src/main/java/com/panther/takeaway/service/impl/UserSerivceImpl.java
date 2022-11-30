package com.panther.takeaway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panther.takeaway.entity.User;
import com.panther.takeaway.mapper.UserMapper;
import com.panther.takeaway.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserSerivceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {


}
