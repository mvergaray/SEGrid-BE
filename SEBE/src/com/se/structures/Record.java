package com.se.structures;

import java.util.Date;

public class Record {
	private int idRecord;
	private String code;
	private String document;
	private String province;
	private String district;
	private String address;
	private String destination;
	private String sender;
	private Date date;
	private long time;
	private String reference;
	private String creationCode;
	private long creationDate;
	
	private static final long serialVersionUID = 1L;
	
	public Record() {
		super();
	}

	public Record(int idRecord, String code, String document, String province, String district,
			String address, String destination, String sender, Date date, long time, String reference,
			String creationCode, long creationDate) {
		super();
		this.idRecord = idRecord;
		this.code = code;
		this.document = document;
		this.province = province;
		this.district = district;
		this.address = address;
		this.destination = destination;
		this.sender = sender;
		this.date = date;
		this.time = time;
		this.reference = reference;
		this.creationCode = creationCode;
		this.creationDate = creationDate;
	}



	public int getIdRecord() {
		return idRecord;
	}

	public void setIdRecord(int idRecord) {
		this.idRecord = idRecord;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getCreationCode() {
		return creationCode;
	}

	public void setCreationCode(String creationCode) {
		this.creationCode = creationCode;
	}

	public long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
}
