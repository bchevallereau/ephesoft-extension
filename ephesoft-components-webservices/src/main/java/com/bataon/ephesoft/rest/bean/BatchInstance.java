package com.bataon.ephesoft.rest.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "batchInstance")
public class BatchInstance {

	private String id;
	private float achievedPercentage;
	private String status;
	private String reviewer;
	private String validator;
	private String batchClassName;
	private String batchClassDescription;

	public BatchInstance() {
	}

	public BatchInstance(com.ephesoft.dcma.da.domain.BatchInstance batchInstance) {
		if (batchInstance != null) {
			this.id = batchInstance.getBatchInstanceID().toString();
			this.status = batchInstance.getStatus().toString();
			this.reviewer = batchInstance.getReviewUserName();
			this.validator = batchInstance.getValidationUserName();
			this.batchClassName = batchInstance.getBatchClass().getName();
			this.batchClassDescription = batchInstance.getBatchClass().getDescription();

			int nbOfModules = batchInstance.getBatchClass().getBatchClassModules().size();
			int nbOfExecutedModules = 0;
			if (batchInstance.getExecutedModules() != null)
				nbOfExecutedModules = batchInstance.getExecutedModules().split(";").length;
			
			this.achievedPercentage = (float) nbOfExecutedModules / nbOfModules;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getAchievedPercentage() {
		return achievedPercentage;
	}

	public void setAchievedPercentage(float achievedPercentage) {
		this.achievedPercentage = achievedPercentage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReviewer() {
		return reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public String getValidator() {
		return validator;
	}

	public void setValidator(String validator) {
		this.validator = validator;
	}

	public String getBatchClassName() {
		return batchClassName;
	}

	public void setBatchClassName(String batchClassName) {
		this.batchClassName = batchClassName;
	}

	public String getBatchClassDescription() {
		return batchClassDescription;
	}

	public void setBatchClassDescription(String batchClassDescription) {
		this.batchClassDescription = batchClassDescription;
	}

}
