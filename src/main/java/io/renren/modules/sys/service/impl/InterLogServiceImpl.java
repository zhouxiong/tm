package io.renren.modules.sys.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.sys.dao.InterLogDao;
import io.renren.modules.sys.entity.InterLogEntity;
import io.renren.modules.sys.service.InterLogService;


@Service("interLogService")
public class InterLogServiceImpl extends ServiceImpl<InterLogDao, InterLogEntity> implements InterLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	String requestDate=String.valueOf(params.get("requestDate"));
    	if("null".equals(requestDate) || null==requestDate || "".equals(requestDate)) {
    		requestDate="";
    	}
        IPage<InterLogEntity> page = this.page(
                new Query<InterLogEntity>().getPage(params),
                new QueryWrapper<InterLogEntity>()
                .like(StringUtils.isNotBlank(requestDate),"request_date",requestDate)
        );

        return new PageUtils(page);
    }

}