package io.renren.modules.cailiao.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.cailiao.dao.WisdplatCurveDao;
import io.renren.modules.cailiao.entity.WisdplatCailiaoEntity;
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
        List<WisdplatCurveEntity> temp = new ArrayList();
        temp = baseMapper.queryListParentId(wc_cailiao_id);
        if(null!=temp && temp.size()>0) {
        	Iterator itelst=temp.iterator();
        	while(itelst.hasNext()) {
        		WisdplatCurveEntity crvEntity=(WisdplatCurveEntity)itelst.next();
        		Long wcId=crvEntity.getWcId();
        		baseMapper.deleteById(wcId);
        	}
        }
        return temp;
    }

    public int insert(WisdplatCurveEntity wisdplatCurveEntity) {

        return baseMapper.insert(wisdplatCurveEntity);
    }
    
    /**
	 * 根据ID属性查询
	 */
	public List findCurveInfoById(Long wcid) {
		List list=baseMapper.findCurveInfoById(wcid);
		return list;
	}
	/**
	 *   根据属性查询
	 */
	public List findProperty(Map<String, Object> map) {
		return baseMapper.findProperty(map);
	}

}
