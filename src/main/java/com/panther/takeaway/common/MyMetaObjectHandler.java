package com.panther.takeaway.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 自定义元数据处理器
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入操作，自动填充公共字段
     * @param metaObject
     */
    @Override
    public void insertFill( MetaObject metaObject) {

        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("createUser",BaseContent.getCurrentID());
        metaObject.setValue("updateUser",BaseContent.getCurrentID());

    }
    /**
     * 更新操作，自动填充公共字段
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContent.getCurrentID());
    }
}
