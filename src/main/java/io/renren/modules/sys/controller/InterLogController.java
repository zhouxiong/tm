package io.renren.modules.sys.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.sys.entity.InterLogEntity;
import io.renren.modules.sys.service.InterLogService;



/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-10-15 10:46:04
 */
@RestController
@RequestMapping("/sys/interlog")
public class InterLogController extends AbstractController{
    @Autowired
    private InterLogService interLogService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:interlog:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = interLogService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{ilId}")
    @RequiresPermissions("sys:interlog:info")
    public R info(@PathVariable("ilId") Long ilId){
		InterLogEntity interLog = interLogService.getById(ilId);

        return R.ok().put("interLog", interLog);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:interlog:save")
    public R save(@RequestBody InterLogEntity interLog){
		interLogService.save(interLog);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:interlog:update")
    public R update(@RequestBody InterLogEntity interLog){
		interLogService.updateById(interLog);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:interlog:delete")
    public R delete(@RequestBody Long[] ilIds){
		interLogService.removeByIds(Arrays.asList(ilIds));

        return R.ok();
    }

}
