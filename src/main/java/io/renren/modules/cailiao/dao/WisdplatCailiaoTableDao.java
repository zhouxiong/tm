package io.renren.modules.cailiao.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.renren.modules.cailiao.entity.WisdplatCailiaoEntity;
import io.renren.modules.cailiao.entity.WisdplatCailiaoTableEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-25 15:40:40
 */
@Mapper
public interface WisdplatCailiaoTableDao extends BaseMapper<WisdplatCailiaoTableEntity> {
	/**
	 * 根据属性查询
	 * @param map
	 * @return
	 */
	public List findProperty(Map<String,Object> map);
	
	int insert(WisdplatCailiaoTableEntity entity);
	
	public int deleteInfo(WisdplatCailiaoTableEntity entity);
	
}
