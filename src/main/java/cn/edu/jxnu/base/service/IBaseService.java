/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.service;

import java.io.Serializable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 系统服务层基接口
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
public interface IBaseService<T, ID extends Serializable> {

    Mono<T> find(ID id);

    Flux<T> findAll();

    Flux<T> findList(ID[] ids);

    Flux<T> findList(Iterable<ID> ids);

    Mono<Page<T>> findAll(Pageable pageable);

    Mono<Page<T>> findAll(Specification<T> spec, Pageable pageable);

    Mono<Long> count();

    Mono<Long> count(Specification<T> spec);

    Mono<Boolean> exists(ID id);

    Mono<T> save(T entity);

    Mono<T> update(T entity);

    Mono<Boolean> delete(ID id);

    Mono<Void> deleteByIds(@SuppressWarnings("unchecked") ID... ids);

    Mono<Void> delete(T[] entitys);

    Mono<Void> delete(Iterable<T> entitys);

    Mono<Void> delete(T entity);

    /** 返回list的全部分页查询 */
    Flux<T> findList(
            Specification<T> spec,
            Sort sort); // List<T>是ArrayList类的泛型等效类，该类使用大小可按需动态增加的数组实现IList<T>泛型接口
}
