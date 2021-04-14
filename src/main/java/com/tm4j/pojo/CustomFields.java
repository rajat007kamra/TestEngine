
package com.tm4j.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomFields {

	@SerializedName("FLOW TYPE")
	@Expose
	private Object fLOWTYPE = null;
	@SerializedName("REVIEWER (QA)")
	@Expose
	private Object tCREVIEWERQA = null;
	@SerializedName("BATTERY")
	@Expose
	private String bATTERY;
	@SerializedName("REVIEWER (DEV)")
	@Expose
	private Object tCREVIEWERDEV = null;
	@SerializedName("ENTERPRISE-ID")
	@Expose
	private String eNTERPRISEID;
	@SerializedName("FUNCTIONALITY")
	@Expose
	private String fUNCTIONALITY = "";
	@SerializedName("TEST SCENARIO")
	@Expose
	private String tESTSCENARIO = "";
	@SerializedName("TYPE")
	@Expose
	private String type;
	@SerializedName("CATEGORY")
	@Expose
	private String cATEGORY;
	@SerializedName("MODE")
	@Expose
	private String mODE;
	@SerializedName("REALM / MODULE / SCREEN")
	@Expose
	private String rEALMMODULESCREEN = "";
	@SerializedName("JSON")
	@Expose
	private String jSON;

	@SerializedName("AUTOMATED?")
	@Expose
	private String automated = "YES";
	@SerializedName("CAPTION")
	@Expose
	private String caption;	
	
	@SerializedName("SUBCATEGORY")
	@Expose
	private String subCategory;

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}	

	public Object getFLOWTYPE() {
		return fLOWTYPE;
	}

	public void setFLOWTYPE(Object fLOWTYPE) {
		this.fLOWTYPE = fLOWTYPE;
	}

	public Object getTCREVIEWERQA() {
		return tCREVIEWERQA;
	}

	public void setTCREVIEWERQA(Object tCREVIEWERQA) {
		this.tCREVIEWERQA = tCREVIEWERQA;
	}

	public String getBATTERY() {
		return bATTERY;
	}

	public void setBATTERY(String bATTERY) {
		this.bATTERY = bATTERY;
	}

	public Object getTCREVIEWERDEV() {
		return tCREVIEWERDEV;
	}

	public void setTCREVIEWERDEV(Object tCREVIEWERDEV) {
		this.tCREVIEWERDEV = tCREVIEWERDEV;
	}

	public String getENTERPRISEID() {
		return eNTERPRISEID;
	}

	public void setENTERPRISEID(String eNTERPRISEID) {
		this.eNTERPRISEID = eNTERPRISEID;
	}

	public String getFUNCTIONALITY() {
		return fUNCTIONALITY;
	}

	public void setFUNCTIONALITY(String fUNCTIONALITY) {
		this.fUNCTIONALITY = fUNCTIONALITY;
	}

	public String getTESTSCENARIO() {
		return tESTSCENARIO;
	}

	public void setTESTSCENARIO(String tESTSCENARIO) {
		this.tESTSCENARIO = tESTSCENARIO;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCATEGORY() {
		return cATEGORY;
	}

	public void setCATEGORY(String cATEGORY) {
		this.cATEGORY = cATEGORY;
	}

	public String getMODE() {
		return mODE;
	}

	public void setMODE(String mODE) {
		this.mODE = mODE;
	}

	public String getREALMMODULESCREEN() {
		return rEALMMODULESCREEN;
	}

	public void setREALMMODULESCREEN(String rEALMMODULESCREEN) {
		this.rEALMMODULESCREEN = rEALMMODULESCREEN;
	}

	public String getJSON() {
		return jSON;
	}

	public void setJSON(String jSON) {
		this.jSON = jSON;
	}

	public String getAutomated() {
		return automated;
	}

	public void setAutomated(String automated) {
		this.automated = automated;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

}
