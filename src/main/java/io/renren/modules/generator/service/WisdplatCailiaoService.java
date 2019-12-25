package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WisdplatCailiaoEntity;

import java.util.Map;

/**
 * 材料信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-22 13:50:06
 */
public interface WisdplatCailiaoService extends IService<WisdplatCailiaoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //插入保存
    int insert(WisdplatCailiaoEntity wisdplatCailiaoEntity);
}

