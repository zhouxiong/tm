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

import io.renren.modules.cailiao.entity.WisdplatCailiaoTableEntity;
import io.renren.modules.cailiao.service.WisdplatCailiaoTableService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author 
 * @email 
 * @date 2019-12-25 15:40:40
 */
@RestController
@RequestMapping("generator/wisdplatcailiaotable")
public class WisdplatCailiaoTableController {
	private Logger logger= LoggerFactory.getLogger(WisdplatCailiaoTableController.class);
    @Autowired
    private WisdplatCailiaoTableService wisdplatCailiaoTableService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:wisdplatcailiaotable:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wisdplatCailiaoTableService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{ctId}")
    @RequiresPermissions("generator:wisdplatcailiaotable:info")
    public R info(@PathVariable("ctId") Long ctId){
		WisdplatCailiaoTableEntity wisdplatCailiaoTable = wisdplatCailiaoTableService.getById(ctId);

        return R.ok().put("wisdplatCailiaoTable", wisdplatCailiaoTable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:wisdplatcailiaotable:save")
    public R save(@RequestBody WisdplatCailiaoTableEntity wisdplatCailiaoTable){
		wisdplatCailiaoTableService.save(wisdplatCailiaoTable);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:wisdplatcailiaotable:update")
    public R update(@RequestBody WisdplatCailiaoTableEntity wisdplatCailiaoTable){
		wisdplatCailiaoTableService.updateById(wisdplatCailiaoTable);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:wisdplatcailiaotable:delete")
    public R delete(@RequestBody Long[] ctIds){
		wisdplatCailiaoTableService.removeByIds(Arrays.asList(ctIds));

        return R.ok();
    }

}
