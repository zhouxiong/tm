package io.renren.modules.cailiao.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import io.renren.common.utils.PageUtils;
import io.renren.modules.cailiao.entity.WisdplatCailiaoEntity;
import io.renren.modules.cailiao.entity.WisdplatCurveEntity;

/**
 * 曲线表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-22 18:10:33
 */
public interface WisdplatCurveService extends IService<WisdplatCurveEntity> {

    PageUtils queryPage(Map<String, Object> params);
    List<WisdplatCurveEntity> queryListParentId(Long clId);


    int insert(WisdplatCurveEntity wisdplatCurveEntity);
    
    /**
     * 	根据Id属性查询
     * @param map
     * @return
     */
    public List<WisdplatCailiaoEntity> findCurveInfoById(Long wcid);
    /**
     * 	根据属性查询
     * @param map
     * @return
     */
    public List findProperty(Map<String,Object> map);
    
    public int deleteInfo(WisdplatCurveEntity entity);
}

