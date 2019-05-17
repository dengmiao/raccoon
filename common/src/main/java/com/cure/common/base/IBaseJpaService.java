package com.cure.common.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;

/**
 * @title: IBaseJpaService
 * @description:
 * @author: dengmiao
 * @create: 2019-05-17 16:58
 **/
@FunctionalInterface
public interface IBaseJpaService<Entity, ID extends Serializable> {

    /**
     * 获取repository
     * @return
     */
    IBaseRepository<Entity, ID> getRepository();

    /**
     * 根据ID获取
     * @param id
     * @return
     */
    default Entity get(ID id) {
        return getRepository().getOne(id);
    }

    /**
     * 获取所有列表
     * @return
     */
    default List<Entity> getAll() {
        return getRepository().findAll();
    }

    /**
     * 获取总数
     * @return
     */
    default Long getTotalCount() {
        return getRepository().count();
    }

    /**
     * 保存
     * @param entity
     * @return
     */
    default Entity save(Entity entity) {

        return getRepository().save(entity);
    }

    /**
     * 修改
     * @param entity
     * @return
     */
    default Entity update(Entity entity) {
        return getRepository().saveAndFlush(entity);
    }

    /**
     * 批量保存与修改
     * @param entities
     * @return
     */
    default Iterable<Entity> saveOrUpdateAll(Iterable<Entity> entities) {
        return getRepository().saveAll(entities);
    }

    /**
     * 删除
     * @param entity
     */
    default void delete(Entity entity) {
        getRepository().delete(entity);
    }

    /**
     * 根据Id删除
     * @param id
     */
    default void delete(ID id) {
        getRepository().deleteById(id);
    }

    /**
     * 批量删除
     * @param entities
     */
    default void delete(Iterable<Entity> entities) {
        getRepository().deleteAll(entities);
    }

    /**
     * 清空缓存，提交持久化
     */
    default void flush() {
        getRepository().flush();
    }

    /**
     * 根据条件查询获取
     * @param spec
     * @return
     */
    default List<Entity> findAll(Specification<Entity> spec) {
        return getRepository().findAll(spec);
    }

    /**
     * 分页获取
     * @param pageable
     * @return
     */
    default Page<Entity> findAll(Pageable pageable){
        return getRepository().findAll(pageable);
    }

    /**
     * 根据查询条件分页获取
     * @param spec
     * @param pageable
     * @return
     */
    default Page<Entity> findAll(Specification<Entity> spec, Pageable pageable) {
        return getRepository().findAll(spec, pageable);
    }

    /**
     * 获取查询条件的结果数
     * @param spec
     * @return
     */
    default long count(Specification<Entity> spec) {
        return getRepository().count(spec);
    }
}
