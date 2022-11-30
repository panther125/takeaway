package com.panther.takeaway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panther.takeaway.common.CustomExcept;
import com.panther.takeaway.entity.Category;
import com.panther.takeaway.entity.Dish;
import com.panther.takeaway.entity.Setmeal;
import com.panther.takeaway.mapper.CategoryMapper;
import com.panther.takeaway.service.CategoryService;
import com.panther.takeaway.service.DishService;
import com.panther.takeaway.service.SetmealService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {

    @Resource
    private DishService dishService;

    @Resource
    private SetmealService setmealService;

    /**
     * 自定义删除操作
     */
    @Override
    public void myRemove(Long id) {

        // 删除前需判断分类是否有关联菜品或套餐
       LambdaQueryWrapper<Dish> Dlqw = new LambdaQueryWrapper<>();
        Dlqw.eq(Dish::getCategoryId,id);
        if( dishService.count(Dlqw) > 0){
           throw new CustomExcept("当前分类关联了菜品，不能直接删除！");
        }

        LambdaQueryWrapper<Setmeal> Slqw = new LambdaQueryWrapper<>();
        Slqw.eq(Setmeal::getCategoryId,id);
        if( setmealService.count(Slqw) > 0){
            throw new CustomExcept("当前分类关联了套餐，不能直接删除！");
        }
        super.removeById(id);
    }
}
