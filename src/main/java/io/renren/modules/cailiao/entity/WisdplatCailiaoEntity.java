package io.renren.modules.cailiao.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 材料信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-25 13:51:16
 */
@Data
@TableName("wisdplat_cailiao")
public class WisdplatCailiaoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId
	private Long clId;
	/**
	 * 材料编号
	 */
	private String clNo;
	/**
	 * 材料名称
	 */
	private String clName;
	/**
	 * 材料牌号
	 */
	private String clPaihao;
	/**
	 * 温度
	 */
	private String clWendu;
	/**
	 * 厂家
	 */
	private String clChangjia;
	/**
	 * 材料密度(t/mm3)
	 */
	private String clMidu;
	/**
	 * 泊松比
	 */
	private String clBosongbi;
	/**
	 * 弹性模量(Mpa)
	 */
	private String clMoliang;
	/**
	 * SIGY(Mpa)
	 */
	private String clSigy;
	/**
	 * ETAN
	 */
	private String clEtan;
	/**
	 * FAIL
	 */
	private String clFail;
	/**
	 * C
	 */
	private String clC;
	/**
	 * P
	 */
	private String clP;
	/**
	 * 分类
	 */
	private String clFenleiId;
	/**
	 * Excel文件地址
	 */
	private String clFileAddr;
	/**
	 * 创建人
	 */
	private String clCreatePerson;
	/**
	 * 创建时间
	 */
	private Date clCreateDate;
	/**
	 * 修改人
	 */
	private String clUpdatePerson;
	/**
	 * 修改时间
	 */
	private Date clUpdateDate;


	//曲线ID数组
	@TableField(exist=false)
	private List<WisdplatCurveEntity> curveIdList;
	//文件类型
	@TableField(exist=false)
	private String fileType;
	//是否加密
	@TableField(exist=false)
	private String jiami;
	//盘符
	@TableField(exist=false)
	private String panfu;
	//文件名称
	@TableField(exist=false)
	private String fileurl;

}
