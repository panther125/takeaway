package com.panther.takeaway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panther.takeaway.entity.Category;

public interface CategoryService extends IService<Category> {

    public void myRemove(Long id);
}
