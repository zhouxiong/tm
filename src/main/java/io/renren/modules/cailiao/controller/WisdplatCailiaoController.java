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

import io.renren.modules.cailiao.entity.WisdplatCailiaoEntity;
import io.renren.modules.cailiao.service.WisdplatCailiaoService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 材料信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-25 13:51:16
 */
@RestController
@RequestMapping("cailiao/wisdplatcailiao")
public class WisdplatCailiaoController {
    @Autowired
    private WisdplatCailiaoService wisdplatCailiaoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("cailiao:wisdplatcailiao:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wisdplatCailiaoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{clId}")
    @RequiresPermissions("cailiao:wisdplatcailiao:info")
    public R info(@PathVariable("clId") Long clId){
		WisdplatCailiaoEntity wisdplatCailiao = wisdplatCailiaoService.getById(clId);

        return R.ok().put("wisdplatCailiao", wisdplatCailiao);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("cailiao:wisdplatcailiao:save")
    public R save(@RequestBody WisdplatCailiaoEntity wisdplatCailiao){
		wisdplatCailiaoService.save(wisdplatCailiao);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("cailiao:wisdplatcailiao:update")
    public R update(@RequestBody WisdplatCailiaoEntity wisdplatCailiao){
		wisdplatCailiaoService.updateById(wisdplatCailiao);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("cailiao:wisdplatcailiao:delete")
    public R delete(@RequestBody Long[] clIds){
		wisdplatCailiaoService.removeByIds(Arrays.asList(clIds));

        return R.ok();
    }

}
