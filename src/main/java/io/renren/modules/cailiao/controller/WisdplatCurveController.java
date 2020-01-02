package io.renren.modules.cailiao.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.cailiao.entity.WisdplatCurveEntity;
import io.renren.modules.cailiao.service.WisdplatCurveService;



/**
 * 曲线表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-22 18:10:33
 */
@RestController
@RequestMapping("cailiao/wisdplatcurve")
public class WisdplatCurveController {
	
	private Logger logger= LoggerFactory.getLogger(WisdplatCurveController.class);
	
    @Autowired
    private WisdplatCurveService wisdplatCurveService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("cailiao:wisdplatcurve:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wisdplatCurveService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{wcId}")
    @RequiresPermissions("cailiao:wisdplatcurve:info")
    public R info(@PathVariable("wcId") Long wcId){
		WisdplatCurveEntity wisdplatCurve = wisdplatCurveService.getById(wcId);
        return R.ok().put("wisdplatCurve", wisdplatCurve);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("cailiao:wisdplatcurve:save")
    public R save(@RequestBody WisdplatCurveEntity wisdplatCurve){
		wisdplatCurveService.save(wisdplatCurve);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("cailiao:wisdplatcurve:update")
    public R update(@RequestBody WisdplatCurveEntity wisdplatCurve){
		wisdplatCurveService.updateById(wisdplatCurve);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("cailiao:wisdplatcurve:delete")
    public R delete(@RequestBody Long[] wcIds){
		wisdplatCurveService.removeByIds(Arrays.asList(wcIds));
        return R.ok();
    }

}
