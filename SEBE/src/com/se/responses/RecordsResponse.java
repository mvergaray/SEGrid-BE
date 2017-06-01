package com.se.responses;

import java.util.List;

import com.se.structures.Record;

public class RecordsResponse {
	private int totalCount;
	private List<Record> data;
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<Record> getData() {
		return data;
	}
	public void setData(List<Record> data) {
		this.data = data;
	}
	
	
}
