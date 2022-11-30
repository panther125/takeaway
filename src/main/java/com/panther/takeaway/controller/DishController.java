package com.panther.takeaway.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panther.takeaway.DTO.DishDto;
import com.panther.takeaway.common.R;
import com.panther.takeaway.entity.Category;
import com.panther.takeaway.entity.Dish;
import com.panther.takeaway.entity.DishFlavor;
import com.panther.takeaway.service.CategoryService;
import com.panther.takeaway.service.DishFlavorService;
import com.panther.takeaway.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/dish")
@RestController
public class DishController {

    @Resource
    private DishService dishService;

    @Resource
    private DishFlavorService dishFlavorService;

    @Resource
    private CategoryService categoryService;

    @PostMapping
    public R<String> saveDish(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("菜品添加成功");
    }

    @GetMapping("/page1")
    public R<Page> page1(int page, int pageSize, String name){
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.like(Dish::getName,name).or().like(Dish::getDescription,name);
        dishService.page(pageInfo,lqw);
        Page<DishDto> pageDishInfo = new Page<>();

        BeanUtils.copyProperties(pageInfo,pageDishInfo,"records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) ->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);

            String Catename = category.getName();
            dishDto.setCategoryName(Catename);

            return dishDto;
        }).collect(Collectors.toList());

        pageDishInfo.setRecords(list);

        return R.success(pageDishInfo);
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.like(Dish::getName,name).or().like(Dish::getDescription,name);
        Page<DishDto> dishDtoPage = dishService.QueryAllDish(page, pageSize, name);

        return R.success(dishDtoPage);
    }

    /**
     * 回显菜品
     * @param id 菜品的编号
     */
    @GetMapping("{id}")
    public R<DishDto> getDish(@PathVariable("id") Long id){
        return R.success(dishService.getByIdWithFlavor(id));
    }

    @PutMapping
    public R<String> UpdateDish(@RequestBody DishDto dishDto){
        return dishService.updateWithFlavor(dishDto) ?
                R.success("保存成功") : R.error("保存失败");
    }

    @GetMapping("/list")
    public R<List<DishDto>> getAllDish(Dish dish){

        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        lqw.eq(Dish::getStatus,1);
        lqw.orderByAsc(Dish::getSort);

        List<Dish> list = dishService.list(lqw);
        List<DishDto> dishDtos = null;
        dishDtos = list.stream().map((item) ->{
            DishDto dishDto = new DishDto();
            dishDto.setId(item.getId());
            dishDto.setCategoryId(item.getCategoryId());
            dishDto.setName(item.getName());
            dishDto.setPrice(item.getPrice());
            dishDto.setImage(item.getImage());
            dishDto.setStatus(item.getStatus());
            dishDto.setSort(item.getSort());
            dishDto.setDescription(item.getDescription());

            // 查询菜品口味信息
            LambdaQueryWrapper<DishFlavor> flqw = new LambdaQueryWrapper<>();
            flqw.eq(DishFlavor::getDishId,item.getId());
            List<DishFlavor> list1 = dishFlavorService.list(flqw);
            dishDto.setFlavors(list1);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtos);
    }

}
