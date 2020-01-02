package io.renren.modules.cailiao.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.renren.modules.cailiao.dao.WisdplatLCSRDao;
import io.renren.modules.cailiao.entity.WisdplatLCSREntity;
import io.renren.modules.cailiao.service.WisdplatLCSRService;

/**
 * LCSR业务逻辑接口实现类
 * @author 70634
 *
 */
@Service
public class WisdplatLCSRServiceImpl extends ServiceImpl<WisdplatLCSRDao,WisdplatLCSREntity> implements WisdplatLCSRService{

	/**
	 * 新增
	 * @param entity
	 * @return
	 */
	public int insert(WisdplatLCSREntity entity) {
		return baseMapper.insert(entity);
	}
	/**
	 * 根据属性查询
	 * @param map
	 * @return
	 */
	public List findProperty(Map<String, Object> map) {
		return baseMapper.findProperty(map);
	}
	/**
	 *   删除
	 */
	public int deleteInfo(WisdplatLCSREntity entity) {
		return baseMapper.deleteInfo(entity);
	}

	
}
