package cn.edu.jxnu.base.service.impl;

import cn.edu.jxnu.base.dao.IBaseDao;
import cn.edu.jxnu.base.entity.BaseEntity;
import cn.edu.jxnu.base.service.IBaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 系统基接口服务层实现
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
public abstract class BaseServiceImpl<T extends BaseEntity, ID extends Serializable> implements IBaseService<T, ID> {

    public abstract IBaseDao<T, ID> baseDao();

    @Override
    public T find(ID id) {
        return baseDao().getOne(id);
    }

    @Override
    public List<T> findAll() {
        return baseDao().findAll();
    }

    @Override
    public List<T> findList(ID[] ids) {
        List<ID> idList = Arrays.asList(ids);
        return baseDao().findAllById(idList);
    }

    @Override
    public List<T> findList(Specification<T> spec, Sort sort) {
        return baseDao().findAll(spec, sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return baseDao().findAll(pageable);
    }

    @Override
    public long count() {
        return baseDao().count();
    }

    @Override
    public long count(Specification<T> spec) {
        return baseDao().count(spec);
    }

    @Override
    public boolean exists(ID id) {
        return baseDao().existsById(id);
    }

    @Override
    public void save(T entity) {
        baseDao().save(entity);
    }

    public void save(Iterable<T> entitys) {
        baseDao().saveAll(entitys);
    }

    @Override
    public T update(T entity) {
        return baseDao().saveAndFlush(entity);
    }

    @Override
    public void delete(ID id) {
        baseDao().deleteById(id);
    }

    @Override
    public void deleteByIds(@SuppressWarnings("unchecked") ID... ids) {
        if (ids != null) {
            for (ID id : ids) {
                this.delete(id);
            }
        }
    }

    @Override
    public void delete(T[] entitys) {
        baseDao().deleteInBatch(Arrays.asList(entitys));
    }

    @Override
    public void delete(Iterable<T> entitys) {
        baseDao().deleteInBatch(entitys);
    }

    @Override
    public void delete(T entity) {
        baseDao().delete(entity);
    }

    @Override
    public List<T> findList(Iterable<ID> ids) {
        return baseDao().findAllById(ids);
    }

    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return baseDao().findAll(spec, pageable);
    }

}
