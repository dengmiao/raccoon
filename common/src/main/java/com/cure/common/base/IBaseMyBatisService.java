package com.cure.common.base;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @title: IBaseMyBatisService
 * @description: 在此书写自定义实现方法
 * @author: dengmiao
 * @create: 2019-05-17 16:56
 **/
public interface IBaseMyBatisService<E> extends IService<E> {

    default void add() {
        // ..
    }
}
