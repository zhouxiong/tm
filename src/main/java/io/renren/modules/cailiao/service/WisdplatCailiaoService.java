package io.renren.modules.cailiao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.cailiao.entity.WisdplatCailiaoEntity;

import java.util.Map;

/**
 * 材料信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-25 13:51:16
 */
public interface WisdplatCailiaoService extends IService<WisdplatCailiaoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

