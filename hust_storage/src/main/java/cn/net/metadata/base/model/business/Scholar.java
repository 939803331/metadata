package cn.net.metadata.base.model.business;

import cn.net.metadata.base.model.BaseModel;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/*
 * Scholar
 */
@Getter
@Setter
@Entity
@Table(name = "buss_scholar")
@ApiModel(description = "学者表")
@org.hibernate.annotations.Table(appliesTo = "buss_scholar")
public class Scholar extends BaseModel implements Serializable {

	private static final long serialVersionUID = -1L;

	/*
	 *
	 */
	@Column(name = "scholar_unique")
	private String scholarUnique;

	/*
	 *
	 */
	@Column(name = "scholar_name")
	private String scholarName;

	/*
	 *
	 */
	@Column(name = "sex")
	private String sex;

	/*
	 *
	 */
	@Column(name = "org_name")
	private String orgName;

	/*
	 *
	 */
	@Column(name = "nationality")
	private String nationality;

	/*
	 *
	 */
	@Column(name = "position")
	private String position;

	/*
	 *
	 */
	@Column(name = "head_photo_url")
	private String headPhotoUrl;

	/*
	 *
	 */
	@Column(name = "subject")
	private String subject;

	/*
	 *
	 */
	@Column(name = "h_index")
	private Integer hIndex;

	/*
	 *
	 */
	@Column(name = "cite_count")
	private Integer citeCount;

	/*
	 *
	 */
	@Column(name = "ach_num")
	private Integer achNum;

	/*
	 *
	 */
	@Column(name = "ab")
	private String ab;

	/*
	 *
	 */
	@Column(name = "prof_title")
	private String profTitle;
}