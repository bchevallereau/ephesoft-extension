package com.bataon.ephesoft.rest.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "batchInstances")
@XmlAccessorType(XmlAccessType.FIELD)
public class BatchInstanceList {

	private int count;

	@XmlElementWrapper(name = "items")
	@XmlElement(name = "batchInstance")
	private List<BatchInstance> batchInstances;

	public BatchInstanceList() {
	}

	public BatchInstanceList(List<BatchInstance> batchInstances) {
		this.batchInstances = batchInstances;
		this.count = batchInstances.size();
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<BatchInstance> getBatchInstances() {
		return batchInstances;
	}

	public void setBatchInstances(List<BatchInstance> batchInstances) {
		this.batchInstances = batchInstances;
	}

	public void init(
			List<com.ephesoft.dcma.da.domain.BatchInstance> batchInstances) {
		this.batchInstances = new ArrayList<BatchInstance>();
		for (com.ephesoft.dcma.da.domain.BatchInstance batchInstance : batchInstances) {
			if (batchInstance != null)
				this.batchInstances.add(new BatchInstance(batchInstance));
		}
		this.count = this.batchInstances.size();
	}

}
