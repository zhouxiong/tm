package io.renren.modules.cailiao.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import io.renren.modules.cailiao.entity.WisdplatLCSREntity;

/**
 * LCSR业务逻辑接口类
 * @author 70634
 *
 */
public interface WisdplatLCSRService extends IService<WisdplatLCSREntity>{

	/**
	 * 新增
	 */
	int insert(WisdplatLCSREntity entity);
	/**
	 * 根据属性查询
	 * @param map
	 * @return
	 */
	public List findProperty(Map<String,Object> map);
	/**
	 * 删除
	 * @param entity
	 * @return
	 */
	public int deleteInfo(WisdplatLCSREntity entity);
}
