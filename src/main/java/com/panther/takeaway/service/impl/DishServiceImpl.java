package com.panther.takeaway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panther.takeaway.DTO.DishDto;
import com.panther.takeaway.entity.Category;
import com.panther.takeaway.entity.Dish;
import com.panther.takeaway.entity.DishFlavor;
import com.panther.takeaway.mapper.DishMapper;
import com.panther.takeaway.service.CategoryService;
import com.panther.takeaway.service.DishFlavorService;
import com.panther.takeaway.service.DishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
        implements DishService {

    @Resource
    private DishFlavorService dishFlavorService;

    @Resource
    private CategoryService categoryService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        // 保存菜品
        this.save(dishDto);

        Long dishId  = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors.stream().map((item) ->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        // 保存口味
        dishFlavorService.saveBatch(dishDto.getFlavors());

    }

    @Override
    public Page<DishDto> QueryAllDish(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> pageDishInfo = new Page<>();

        LambdaQueryWrapper<Dish> dlqw = new LambdaQueryWrapper<>();
        dlqw.like(!StringUtils.isBlank(name),Dish::getName,name).or().like(!StringUtils.isBlank(name),Dish::getDescription,name);

        this.page(pageInfo, dlqw);

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = new ArrayList<>();

        for(Dish item : records){
            DishDto dishDto = new DishDto();
            dishDto.setId(item.getId());
            dishDto.setPrice(item.getPrice());
            dishDto.setSort(item.getSort());
            dishDto.setImage(item.getImage());
            dishDto.setName(item.getName());
            dishDto.setUpdateTime(item.getUpdateTime());
            dishDto.setDescription(item.getDescription());
            Category category = categoryService.getById(item.getCategoryId());
            dishDto.setCategoryName(category.getName());
            list.add(dishDto);
        }

        pageDishInfo.setRecords(list);
        pageDishInfo.setCurrent(pageInfo.getCurrent());
        pageDishInfo.setCountId(pageInfo.getCountId());
        pageDishInfo.setSize(pageInfo.getSize());
        pageDishInfo.setTotal(pageInfo.getTotal());

        return pageDishInfo;
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {

        // 查询菜品
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,id);
        List<DishFlavor> list = dishFlavorService.list(lqw);
        dishDto.setFlavors(list);

        return dishDto;
    }

    @Override
    public boolean updateWithFlavor(DishDto dishDto) {

        this.updateById(dishDto);
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) ->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(lqw);

        return dishFlavorService.saveBatch(flavors);
    }


}
