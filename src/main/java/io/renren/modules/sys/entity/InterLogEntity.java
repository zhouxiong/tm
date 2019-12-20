package io.renren.modules.sys.entity;

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
 * @date 2019-10-15 10:46:04
 */
@Data
@TableName("inter_log")
public class InterLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long ilId;
	/**
	 * 
	 */
	private String requestArgu;
	/**
	 * 
	 */
	private String requestDate;
	/**
	 * 
	 */
	private String responseArgu;
	/**
	 * 
	 */
	private Integer useHours;
	/**
	 * 
	 */
	private String createPerson;
	/**
	 * 
	 */
	private Integer laiyuan;
	/**
	 * 
	 */
	private Integer ilType;

}
