/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.service;

import java.io.Serializable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  // 应该使用Optional
  Mono<T> find(ID id);

  Flux<T> findAll();

  Mono<Page<T>> findAll(Specification<T> spec, Pageable pageable);

  Mono<T> save(T entity);

  Mono<T> update(T entity);

  Mono<Boolean> delete(ID id);

  Mono<Void> delete(T entity);
}
