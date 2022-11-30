package com.panther.takeaway.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.panther.takeaway.DTO.DishDto;
import com.panther.takeaway.entity.Dish;

public interface DishService extends IService<Dish> {

    // 新增菜品
    public void saveWithFlavor(DishDto dishDto);

    // 显示菜品
    public Page<DishDto> QueryAllDish(int page, int pageSize, String name);

    // 回显菜品和口味
    public DishDto getByIdWithFlavor(Long id);

    // 跟新菜品和口味
    public boolean updateWithFlavor(DishDto dishDto);
}
