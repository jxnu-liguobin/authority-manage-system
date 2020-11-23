package cn.edu.jxnu.base.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;

/**
 * 系统服务层基接口
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
public interface IBaseService<T, ID extends Serializable> {

    T find(ID id);

    List<T> findAll();

    List<T> findList(ID[] ids);

    List<T> findList(Iterable<ID> ids);

    Page<T> findAll(Pageable pageable);

    Page<T> findAll(Specification<T> spec, Pageable pageable);

    long count();

    long count(Specification<T> spec);

    boolean exists(ID id);

    void save(T entity);

    T update(T entity);

    void delete(ID id);

    void deleteByIds(@SuppressWarnings("unchecked") ID... ids);

    void delete(T[] entitys);

    void delete(Iterable<T> entitys);

    void delete(T entity);

    /**
     * 返回list的全部分页查询
     */
    List<T> findList(Specification<T> spec, Sort sort);// List<T>是ArrayList类的泛型等效类，该类使用大小可按需动态增加的数组实现IList<T>泛型接口
}