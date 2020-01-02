package io.renren.modules.cailiao.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.cailiao.dao.WisdplatCailiaoTableDao;
import io.renren.modules.cailiao.entity.WisdplatCailiaoTableEntity;
import io.renren.modules.cailiao.service.WisdplatCailiaoTableService;


@Service("wisdplatCailiaoTableService")
public class WisdplatCailiaoTableServiceImpl extends ServiceImpl<WisdplatCailiaoTableDao, WisdplatCailiaoTableEntity> implements WisdplatCailiaoTableService {

	@Autowired
	private WisdplatCailiaoTableDao wcltDao;
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WisdplatCailiaoTableEntity> page = this.page(
                new Query<WisdplatCailiaoTableEntity>().getPage(params),
                new QueryWrapper<WisdplatCailiaoTableEntity>()
        );

        return new PageUtils(page);
    }
    /**
     *	 根据属性查询
     */
	public List findProperty(Map<String, Object> map) {
		return wcltDao.findProperty(map);
	}
	
	public int insert(WisdplatCailiaoTableEntity entity) {
		return wcltDao.insert(entity);
	}
	public int deleteInfo(WisdplatCailiaoTableEntity entity) {
		return wcltDao.deleteInfo(entity);
	}

}