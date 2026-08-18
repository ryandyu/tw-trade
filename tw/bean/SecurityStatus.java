package com.sumridge.tw.bean;

public class SecurityStatus {

	private String securityId;
	private int offerType;
	private String benchmarkSecurityId;
	private boolean omsFlag;

	public String getSecurityId() {
		return securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}

	public int getOfferType() {
		return offerType;
	}

	public void setOfferType(int offerType) {
		this.offerType = offerType;
	}

	public String getBenchmarkSecurityId() {
		return benchmarkSecurityId;
	}

	public void setBenchmarkSecurityId(String benchmarkSecurityId) {
		this.benchmarkSecurityId = benchmarkSecurityId;
	}

	public boolean isOmsFlagOn() {
		return omsFlag;
	}

	public void setOmsFlag(boolean omsFlag) {
		this.omsFlag = omsFlag;
	}

}
