package io.renren.modules.generator.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.renren.common.utils.ExcelUtils;
import io.renren.modules.generator.dao.WisdplatCurveDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WisdplatCailiaoDao;
import io.renren.modules.generator.entity.WisdplatCailiaoEntity;
import io.renren.modules.generator.service.WisdplatCailiaoService;


@Service("wisdplatCailiaoService")
public class WisdplatCailiaoServiceImpl extends ServiceImpl<WisdplatCailiaoDao, WisdplatCailiaoEntity> implements WisdplatCailiaoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        System.out.println("params============"+params);
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


    @Override
    public int insert(WisdplatCailiaoEntity entity) {
        System.out.println("entity=========="+entity);
//        ExcelUtils excelUtils = new ExcelUtils();
//        excelUtils.readExcelColumn("E:\\材料应力应变曲线-模板.xlsx");
        return baseMapper.insert(entity);
    }

}
