package io.renren.modules.cailiao.dao;

import io.renren.modules.cailiao.entity.WisdplatCurveEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 曲线表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-22 18:10:33
 */
@Mapper
public interface WisdplatCurveDao extends BaseMapper<WisdplatCurveEntity> {
    List<WisdplatCurveEntity> queryListParentId(Long wc_cailiao_id);

    @Override
    int insert(WisdplatCurveEntity entity);
}
