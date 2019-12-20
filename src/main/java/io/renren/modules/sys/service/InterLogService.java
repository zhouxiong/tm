package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.InterLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-10-15 10:46:04
 */
public interface InterLogService extends IService<InterLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

