package com.panther.takeaway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panther.takeaway.DTO.SetmealDto;
import com.panther.takeaway.common.CustomExcept;
import com.panther.takeaway.entity.Setmeal;
import com.panther.takeaway.entity.SetmealDish;
import com.panther.takeaway.mapper.SetmealMapper;
import com.panther.takeaway.service.SetmealDishService;
import com.panther.takeaway.service.SetmealService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
        implements SetmealService {

    @Resource
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveSetmealWithDish(SetmealDto setmealDto) {

        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) ->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {

        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.in(Setmeal::getId,ids);
        lqw.eq(Setmeal::getStatus,1);

        if (this.count() > 0){
            throw new CustomExcept("套餐真在售卖，无法删除");
        }
        this.removeByIds(ids);
        setmealDishService.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> dlqw = new LambdaQueryWrapper<>();
        dlqw.in(SetmealDish::getId,ids);
        setmealDishService.remove(dlqw);
    }
}
