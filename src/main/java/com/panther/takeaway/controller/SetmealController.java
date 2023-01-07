package com.panther.takeaway.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panther.takeaway.DTO.SetmealDto;
import com.panther.takeaway.common.R;
import com.panther.takeaway.entity.Category;
import com.panther.takeaway.entity.Setmeal;
import com.panther.takeaway.service.CategoryService;
import com.panther.takeaway.service.SetmealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Resource
    private SetmealService setmealService;

    @Resource
    private CategoryService categoryService;

    @CacheEvict(value = "setmealCache",allEntries = true)
    @PostMapping
    public R<String> saveSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.saveSetmealWithDish(setmealDto);
        return R.success("操作成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> DtopageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.like(!StringUtils.isBlank(name),Setmeal::getName,name)
            .or().like(!StringUtils.isBlank(name),Setmeal::getDescription,name);

        setmealService.page(pageInfo,lqw);
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) ->{
            SetmealDto setmealDto = new SetmealDto();
            //BeanUtils.copyProperties(records,setmealDto);
            setmealDto.setId(item.getId());
            setmealDto.setName(item.getName());
            setmealDto.setImage(item.getImage());
            setmealDto.setPrice(item.getPrice());
            setmealDto.setStatus(item.getStatus());
            setmealDto.setUpdateTime(item.getUpdateTime());
            Category category = categoryService.getById(item.getCategoryId());
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());
        DtopageInfo.setRecords(list);
        DtopageInfo.setTotal(pageInfo.getTotal());
        DtopageInfo.setCurrent(pageInfo.getCurrent());
        DtopageInfo.setSize(pageInfo.getSize());
        return R.success(DtopageInfo);
    }

    @DeleteMapping
    public R<String> dedleteSetemal(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.success("套餐删除成功！");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") int status,@RequestParam List<Long> ids){

        List<Setmeal> setmeals = setmealService.listByIds(ids);
        setmeals = setmeals.stream().map((item) ->{
           item.setStatus(status);
            return item;
        }).collect(Collectors.toList());
        setmealService.updateBatchById(setmeals);
        return R.success("套餐删除成功！");
    }

    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId+'_'+#setmeal.status")
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){

        // 封装条件参数
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        lqw.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        lqw.orderByDesc(setmeal.getUpdateTime() != null,Setmeal::getUpdateTime);

        return R.success(setmealService.list(lqw));
    }
}
