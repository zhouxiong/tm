package io.renren.modules.cailiao.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.cailiao.dao.WisdplatCailiaoTableDao;
import io.renren.modules.cailiao.entity.WisdplatCailiaoTableEntity;
import io.renren.modules.cailiao.service.WisdplatCailiaoTableService;


@Service("wisdplatCailiaoTableService")
public class WisdplatCailiaoTableServiceImpl extends ServiceImpl<WisdplatCailiaoTableDao, WisdplatCailiaoTableEntity> implements WisdplatCailiaoTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WisdplatCailiaoTableEntity> page = this.page(
                new Query<WisdplatCailiaoTableEntity>().getPage(params),
                new QueryWrapper<WisdplatCailiaoTableEntity>()
        );

        return new PageUtils(page);
    }

}