package io.renren.modules.generator.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.renren.common.utils.ExcelUtils;
import io.renren.modules.generator.entity.WisdplatCurveEntity;
import io.renren.modules.generator.service.WisdplatCurveService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.generator.entity.WisdplatCailiaoEntity;
import io.renren.modules.generator.service.WisdplatCailiaoService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 材料信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-22 13:50:06
 */
@RestController
@RequestMapping("generator/wisdplatcailiao")
public class WisdplatCailiaoController {
    @Autowired
    private WisdplatCailiaoService wisdplatCailiaoService;
    @Autowired
    private WisdplatCurveService wisdplatCurveService;


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wisdplatcailiao:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wisdplatCailiaoService.queryPage(params);
//        ExcelUtils excelUtils = new ExcelUtils();
//        excelUtils.readExcelColumn("E:\\材料应力应变曲线-模板.xlsx");
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{clId}")
    @RequiresPermissions("generator:wisdplatcailiao:info")
    public R info(@PathVariable("clId") Long clId){
		WisdplatCailiaoEntity wisdplatCailiao = wisdplatCailiaoService.getById(clId);
//        System.out.println("wisdplatCurveService.queryListParentId(clId);==="+wisdplatCurveService.queryListParentId(clId));
        List<WisdplatCurveEntity> curveIdList = wisdplatCurveService.queryListParentId(clId);
        wisdplatCailiao.setCurveIdList(curveIdList);
        return R.ok().put("wisdplatCailiao", wisdplatCailiao);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wisdplatcailiao:save")
    public R save(@RequestBody WisdplatCailiaoEntity wisdplatCailiao,WisdplatCurveEntity wisdplatCurve){
//		wisdplatCailiaoService.save(wisdplatCailiao);
//        wisdplatCurveService.save(wisdplatCurve);
        wisdplatCailiaoService.insert(wisdplatCailiao);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wisdplatcailiao:update")
    public R update(@RequestBody WisdplatCailiaoEntity wisdplatCailiao){
		wisdplatCailiaoService.updateById(wisdplatCailiao);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wisdplatcailiao:delete")
    public R delete(@RequestBody Long[] clIds){
		wisdplatCailiaoService.removeByIds(Arrays.asList(clIds));

        return R.ok();
    }

}
