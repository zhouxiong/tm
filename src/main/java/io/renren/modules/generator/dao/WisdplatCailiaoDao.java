package io.renren.modules.generator.dao;

import io.renren.modules.generator.entity.WisdplatCailiaoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 材料信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-22 13:50:06
 */
@Mapper
public interface WisdplatCailiaoDao extends BaseMapper<WisdplatCailiaoEntity> {

    @Override
    int insert(WisdplatCailiaoEntity entity);
}
