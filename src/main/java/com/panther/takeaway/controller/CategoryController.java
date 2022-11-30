package com.panther.takeaway.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panther.takeaway.DTO.DishDto;
import com.panther.takeaway.common.R;
import com.panther.takeaway.entity.Category;
import com.panther.takeaway.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @PostMapping
    public R<String> addCate(@RequestBody Category category){

        return categoryService.save(category) ?
                R.success("添加成功") : R.error("添加失败");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){

        Page<Category> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(Category::getSort);

        return R.success(categoryService.page(pageInfo,lqw));
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        categoryService.myRemove(ids);
        return R.success("删除成功！");
    }

    @PutMapping
    public R<String> updateCate(@RequestBody Category category){

        return categoryService.updateById(category) ?
                R.success("修改成功!") : R.error("修改失败!");
    }

    @GetMapping("/list")
    public R<List<Category>> getList(Category category){

        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(category.getType() != null,Category::getType,category.getType());
        lqw.orderByAsc(Category::getSort);

        return R.success(categoryService.list(lqw));
    }

}
