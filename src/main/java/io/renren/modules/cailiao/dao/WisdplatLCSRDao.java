package io.renren.modules.cailiao.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.renren.modules.cailiao.entity.WisdplatLCSREntity;

/**
 * LCSR的Mapper接口类
 * @author 70634
 *
 */
@Mapper
public interface WisdplatLCSRDao extends BaseMapper<WisdplatLCSREntity>{

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
	 *  删除
	 * @param entity
	 * @return
	 */
	public int deleteInfo(WisdplatLCSREntity entity);
}
