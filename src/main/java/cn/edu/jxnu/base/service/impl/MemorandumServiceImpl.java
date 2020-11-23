package cn.edu.jxnu.base.service.impl;

import cn.edu.jxnu.base.dao.IBaseDao;
import cn.edu.jxnu.base.dao.IMemorandumDao;
import cn.edu.jxnu.base.entity.Memorandum;
import cn.edu.jxnu.base.service.IMemorandumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author 梦境迷离.
 * @version v2.0 2020年11月20日
 */

@Service
@Transactional
public class MemorandumServiceImpl extends BaseServiceImpl<Memorandum, Integer> implements IMemorandumService {

    @Autowired
    private IMemorandumDao memorandumDao;

    public Memorandum find(String id) {
        return null;
    }

    @Override
    public IBaseDao<Memorandum, Integer> baseDao() {
        return this.memorandumDao;
    }

}
