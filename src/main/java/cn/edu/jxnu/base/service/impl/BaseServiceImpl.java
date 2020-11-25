/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.service.impl;

import cn.edu.jxnu.base.dao.IBaseDao;
import cn.edu.jxnu.base.entity.BaseEntity;
import cn.edu.jxnu.base.service.IBaseService;
import java.io.Serializable;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 系统基接口服务层实现
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
public abstract class BaseServiceImpl<T extends BaseEntity, ID extends Serializable>
        implements IBaseService<T, ID> {

    public abstract IBaseDao<T, ID> baseDao();

    @Override
    public Mono<T> find(ID id) {
        // TODO getOne 在找不到时就会抛出异常
        Optional<T> uOpt = baseDao().findById(id);
        if (uOpt.isPresent()) {
            return Mono.just(uOpt.get());
        } else {
            return Mono.empty();
        }
    }

    @Override
    public Flux<T> findAll() {
        return Flux.fromIterable(baseDao().findAll());
    }

    @Override
    public Mono<T> save(T entity) {
        return Mono.just(baseDao().save(entity));
    }

    @Override
    public Mono<T> update(T entity) {
        return Mono.just(baseDao().saveAndFlush(entity));
    }

    @Override
    public Mono<Boolean> delete(ID id) {
        baseDao().deleteById(id);
        return Mono.just(true);
    }

    @Override
    public Mono<Void> delete(T entity) {
        baseDao().delete(entity);
        return Mono.empty().then();
    }

    @Override
    public Mono<Page<T>> findAll(Specification<T> spec, Pageable pageable) {
        return Mono.justOrEmpty(baseDao().findAll(spec, pageable));
    }
}
