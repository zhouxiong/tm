package io.renren.modules.cailiao.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 曲线表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-22 18:10:33
 */
@Data
@TableName("wisdplat_curve")
public class WisdplatCurveEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId
	private Long wcId;
	/**
	 * 曲线编号
	 */
	private String wcNo;
	/**
	 * 应力
	 */
	private String wcX;
	/**
	 * 应变
	 */
	private String wcY;
	/**
	 * 屈服强度缩放系数
	 */
	private String wcXishu;
	/**
	 * 材料ID
	 */
	private Long wcCailiaoId;
	/**
	 * 创建人
	 */
	private String createPerson;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 修改人
	 */
	private String updatePerson;
	/**
	 * 修改时间
	 */
	private Date updateDate;
	
}
