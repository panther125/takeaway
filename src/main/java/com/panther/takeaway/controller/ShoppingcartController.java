package com.panther.takeaway.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.panther.takeaway.common.BaseContent;
import com.panther.takeaway.common.R;
import com.panther.takeaway.entity.ShoppingCart;
import com.panther.takeaway.service.ShoppingCatService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingcartController {

    @Resource
    private ShoppingCatService shoppingCartService;

    /**
     * 购物车控制层
     * @param shoppingCart
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContent.getCurrentID();
        shoppingCart.setUserId(currentId);

        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);

        if(dishId != null){
            //添加到购物车的是菜品和不同口味
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
            queryWrapper.eq(ShoppingCart::getDishFlavor,shoppingCart.getDishFlavor());

        }else{
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        //查询当前菜品或者套餐是否在购物车中
        //SQL:select * from shopping_cart where user_id = ? and dish_id/setmeal_id = ? and dishFlavor = ?
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        if(cartServiceOne != null){
            //如果已经存在，就在原来数量基础上加一
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        }else{
            //如果不存在，则添加到购物车，数量默认就是一
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }

        return R.success(cartServiceOne);
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContent.getCurrentID());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        //SQL:delete from shopping_cart where user_id = ?

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContent.getCurrentID());

        shoppingCartService.remove(queryWrapper);

        return R.success("清空购物车成功");
    }

}
