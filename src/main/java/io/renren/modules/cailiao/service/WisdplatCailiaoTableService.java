package io.renren.modules.cailiao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.cailiao.entity.WisdplatCailiaoTableEntity;

import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-25 15:40:40
 */
public interface WisdplatCailiaoTableService extends IService<WisdplatCailiaoTableEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

