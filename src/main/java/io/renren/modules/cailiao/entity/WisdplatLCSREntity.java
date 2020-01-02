package io.renren.modules.cailiao.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 
 * LSCR实体类
 * @author 70634
 *
 */
@Data
@TableName("wisdplat_lcsr")
public class WisdplatLCSREntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@TableId
	private Long lcsrId;//主键ID 
	
	private String lcsrNo;//曲线编号
	
	private String lcsrRate;//应变率
	
	private String lcsrXishu;//屈服强度缩放系数
	
	private Long wcCailiaoId;//材料ID
	
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
