package io.renren.modules.cailiao.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.renren.modules.cailiao.entity.WisdplatCurveEntity;

/**
 * 曲线表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-22 18:10:33
 */
@Mapper
public interface WisdplatCurveDao extends BaseMapper<WisdplatCurveEntity> {
    List<WisdplatCurveEntity> queryListParentId(Long wc_cailiao_id);

    @Override
    int insert(WisdplatCurveEntity entity);
    
    public List<WisdplatCurveEntity> findCurveInfoById(Long wcid);
    /**
     * 	 根据属性查询
     * @param map
     * @return
     */
    public List findProperty(Map<String,Object> map);
    
    public int deleteInfo(WisdplatCurveEntity entity);
    
}
