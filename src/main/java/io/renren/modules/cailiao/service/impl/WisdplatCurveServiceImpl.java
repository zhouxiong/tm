package io.renren.modules.cailiao.service.impl;

import io.renren.modules.cailiao.dao.WisdplatCailiaoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.cailiao.dao.WisdplatCurveDao;
import io.renren.modules.cailiao.entity.WisdplatCurveEntity;
import io.renren.modules.cailiao.service.WisdplatCurveService;


@Service("wisdplatCurveService")
public class WisdplatCurveServiceImpl extends ServiceImpl<WisdplatCurveDao, WisdplatCurveEntity> implements WisdplatCurveService {

//    @Autowired
//    public WisdplatCailiaoDao wisdplatCailiaoDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WisdplatCurveEntity> page = this.page(
                new Query<WisdplatCurveEntity>().getPage(params),
                new QueryWrapper<WisdplatCurveEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<WisdplatCurveEntity> queryListParentId(Long wc_cailiao_id) {
//        System.out.println("11111===="+baseMapper.queryListParentId(wc_cailiao_id));
        List<WisdplatCurveEntity> temp = new ArrayList();
        temp = baseMapper.queryListParentId(wc_cailiao_id);
        return temp;
    }

    @Override
    public int insert(WisdplatCurveEntity wisdplatCurveEntity) {


        return baseMapper.insert(wisdplatCurveEntity);
    }

}
