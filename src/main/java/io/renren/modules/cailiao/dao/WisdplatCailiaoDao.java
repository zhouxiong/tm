package io.renren.modules.cailiao.dao;

import io.renren.modules.cailiao.entity.WisdplatCailiaoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 材料信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-25 13:51:16
 */
@Mapper
public interface WisdplatCailiaoDao extends BaseMapper<WisdplatCailiaoEntity> {

    @Override
    int insert(WisdplatCailiaoEntity entity);
}
