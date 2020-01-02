package io.renren.modules.cailiao.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import io.renren.common.utils.PageUtils;
import io.renren.modules.cailiao.entity.WisdplatCailiaoEntity;

/**
 * 材料信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-25 13:51:16
 */
public interface WisdplatCailiaoService extends IService<WisdplatCailiaoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 根据ID查询返回 List
     * @param clId
     * @return
     */
    List<WisdplatCailiaoEntity> queryListParentId(Long clId);
    
    /**
     *	获取当前最大ID
     * @return
     */
    public Long maxId();
    
}

