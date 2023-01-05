package com.panther.takeaway.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.panther.takeaway.Utils.ValidateCodeUtils;
import com.panther.takeaway.common.R;
import com.panther.takeaway.entity.User;
import com.panther.takeaway.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(HttpSession session, @RequestBody User user){

        if(StringUtils.isNotEmpty(user.getPhone())){
            // 生成四位随机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code:{}",code);

            session.setAttribute(user.getPhone(),code);

            // 将验证码保存在reids中并设置过期时间 1 分钟过期
            stringRedisTemplate.opsForValue().set("code",code,1L, TimeUnit.MINUTES);

            return R.success("亲，接受短信验证码！");
        }

        return R.success("短信验证码发送失败！");
    }

    /**
     * 移动端用户登录
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map user,HttpSession session ){

        // 校验手机号
        String phone = user.get("phone").toString();
        String code = (String)user.get("code");
        // 校验验证码
       // String codeINSession = (String)session.getAttribute(phone);

        String codeINRedis = (String) stringRedisTemplate.opsForValue().get("code");

        if(StringUtils.isNotEmpty(codeINRedis) && codeINRedis.equals(code)){

            // 登录成功
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getPhone,phone);
            User one = userService.getOne(lqw);
            if(one == null){
                one = new User();
                one.setPhone(phone);
                one.setStatus(1);
                userService.save(one);
            }
            session.setAttribute("user",one.getId());

            // 如果用户登录成功，将redis的验证码移除
            stringRedisTemplate.delete("code");

            return R.success(one);
        }
        // 判断是否为新用户
        return R.error("登录失败");
    }

}
