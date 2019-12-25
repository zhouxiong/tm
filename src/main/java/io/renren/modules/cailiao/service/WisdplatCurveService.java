package io.renren.modules.cailiao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.cailiao.entity.WisdplatCailiaoEntity;
import io.renren.modules.cailiao.entity.WisdplatCurveEntity;

import java.util.List;
import java.util.Map;

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
}

