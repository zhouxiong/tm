package io.renren.modules.cailiao.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.cailiao.entity.WisdplatCurveEntity;
import io.renren.modules.cailiao.service.WisdplatCurveService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 曲线表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-22 18:10:33
 */
@RestController
@RequestMapping("generator/wisdplatcurve")
public class WisdplatCurveController {
    @Autowired
    private WisdplatCurveService wisdplatCurveService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wisdplatcurve:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wisdplatCurveService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{wcId}")
    @RequiresPermissions("generator:wisdplatcurve:info")
    public R info(@PathVariable("wcId") Long wcId){
		WisdplatCurveEntity wisdplatCurve = wisdplatCurveService.getById(wcId);
        System.out.println("22222");
        return R.ok().put("wisdplatCurve", wisdplatCurve);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wisdplatcurve:save")
    public R save(@RequestBody WisdplatCurveEntity wisdplatCurve){
		wisdplatCurveService.save(wisdplatCurve);

        System.out.println("1111111111111111");
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wisdplatcurve:update")
    public R update(@RequestBody WisdplatCurveEntity wisdplatCurve){
		wisdplatCurveService.updateById(wisdplatCurve);
        System.out.println("8888888888888888888888888");
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wisdplatcurve:delete")
    public R delete(@RequestBody Long[] wcIds){
		wisdplatCurveService.removeByIds(Arrays.asList(wcIds));

        return R.ok();
    }

}
