package io.renren.modules.cailiao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-25 15:40:40
 */
@Data
@TableName("wisdplat_cailiao_table")
public class WisdplatCailiaoTableEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId
	private Long ctId;
	/**
	 * Table编号
	 */
	private String ctTableNo;
	/**
	 * 应变率
	 */
	private String ctYingbianRate;
	/**
	 * 曲线ID
	 */
	private String ctCurveId;
	/**
	 * 材料ID
	 */
	private Long ctClId;
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
