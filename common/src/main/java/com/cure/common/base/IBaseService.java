package com.cure.common.base;

import java.io.Serializable;

/**
 * @title: IBaseService
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 20:08
 **/
public interface IBaseService<Entity, ID extends Serializable> extends IBaseJpaService<Entity, ID>, IBaseMyBatisService<Entity> {
}
