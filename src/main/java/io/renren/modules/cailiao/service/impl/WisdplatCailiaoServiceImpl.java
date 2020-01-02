package io.renren.modules.cailiao.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.cailiao.dao.WisdplatCailiaoDao;
import io.renren.modules.cailiao.entity.WisdplatCailiaoEntity;
import io.renren.modules.cailiao.service.WisdplatCailiaoService;


@Service("wisdplatCailiaoService")
public class WisdplatCailiaoServiceImpl extends ServiceImpl<WisdplatCailiaoDao, WisdplatCailiaoEntity> implements WisdplatCailiaoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
//        IPage<WisdplatCailiaoEntity> page = this.page(
//                new Query<WisdplatCailiaoEntity>().getPage(params),
//                new QueryWrapper<WisdplatCailiaoEntity>()
//        );
//        
        Object objKey = params.get("key");
        JSONObject json = null;
        json = JSON.parseObject((String) objKey);
        String strClNo = (String)json.get("clNo"); //材料编号
        String strClName = (String)json.get("clName");//材料名称
        String strClPaihao = (String)json.get("clPaihao");//材料牌号
        String strClWendu = (String)json.get("clWendu");//材料温度
        String strClChangjia = (String)json.get("clChangjia");//材料厂家
        String strClFenleiId = (String)json.get("clFenleiId");//材料分类
        IPage<WisdplatCailiaoEntity> page = this.page(
                new Query<WisdplatCailiaoEntity>().getPage(params),
                new QueryWrapper<WisdplatCailiaoEntity>()
                        .like(StringUtils.isNotBlank(strClNo),"cl_no", strClNo)
                        .like(StringUtils.isNotBlank(strClName),"cl_name", strClName)
                        .like(StringUtils.isNotBlank(strClPaihao),"cl_paihao", strClPaihao)
                        .like(StringUtils.isNotBlank(strClWendu),"cl_wendu", strClWendu)
                        .like(StringUtils.isNotBlank(strClChangjia),"cl_changjia", strClChangjia)
                        .eq(StringUtils.isNotBlank(strClFenleiId),"cl_fenlei_id", strClFenleiId)
                        .orderByDesc("cl_id")
        );
        return new PageUtils(page);

    }
    /**
     * 根据ID查询返回List
     */
	public List<WisdplatCailiaoEntity> queryListParentId(Long clId) {
		List list=new ArrayList();
		list=(List) baseMapper.selectById(clId);
		return list;
	}
	/**
	 *  获取当前插入ID
	 */
	public Long maxId() {
		return baseMapper.maxId();
	}
	

}