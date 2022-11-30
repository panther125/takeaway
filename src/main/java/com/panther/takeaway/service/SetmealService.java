package com.panther.takeaway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panther.takeaway.DTO.SetmealDto;
import com.panther.takeaway.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    public void saveSetmealWithDish(SetmealDto setmealDto);

    public void removeWithDish(List<Long> ids);

}
