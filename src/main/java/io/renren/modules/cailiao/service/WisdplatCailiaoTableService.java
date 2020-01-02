package io.renren.modules.cailiao.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import io.renren.common.utils.PageUtils;
import io.renren.modules.cailiao.entity.WisdplatCailiaoTableEntity;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-25 15:40:40
 */
public interface WisdplatCailiaoTableService extends IService<WisdplatCailiaoTableEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 	根据属性查询
     * @param map
     * @return
     */
    public List findProperty(Map<String,Object> map);
    
    int insert(WisdplatCailiaoTableEntity entity);
    /**
     * 删除方法
     * @param entity
     * @return
     */
    public int deleteInfo(WisdplatCailiaoTableEntity entity);
}

