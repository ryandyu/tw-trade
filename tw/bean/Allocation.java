package com.sumridge.tw.bean;

public class Allocation {

	private final java.util.Date tradeDate;
	private final Integer platformId;
	private final String execId;
	private final String orderId;
	private final String clOrdId;
	private final String allocId;
	private final Integer allocStatus;
	private final String side;
	private final char allocTransType;
	private String customer;
	private String clientId;
	private String alertCode;
	private String shortName;
	private Integer realSize;
	private Double principal;
	private Double accruedInterest;
	private Double netMoney;

	private Allocation(AllocationBuilder builder) {
		this.tradeDate = builder.tradeDate;
		this.platformId = builder.platformId;
		this.execId = builder.execId;
		this.orderId = builder.orderId;
		this.clOrdId = builder.clOrdId;
		this.allocId = builder.allocId;
		this.allocStatus = builder.allocStatus;
		this.customer = builder.customer;
		this.clientId = builder.clientId;
		this.alertCode = builder.alertCode;
		this.shortName = builder.shortName;
		this.realSize = builder.realSize;
		this.principal = builder.principal;
		this.accruedInterest = builder.accruedInterest;
		this.netMoney = builder.netMoney;
		this.side = builder.side;
		this.allocTransType = builder.allocTransType;
	}

	public java.util.Date getTradeDate() {
		return tradeDate;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public String getExecId() {
		return execId;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getClOrdId() {
		return clOrdId;
	}

	public String getAllocId() {
		return allocId;
	}

	public Integer getAllocStatus() {
		return allocStatus;
	}

	public String getSide() {
		return side;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getAlertCode() {
		return alertCode;
	}

	public void setAlertCode(String alertCode) {
		this.alertCode = alertCode;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Integer getRealSize() {
		return realSize;
	}

	public void setRealSize(Integer realSize) {
		this.realSize = realSize;
	}

	public Double getPrincipal() {
		return principal;
	}

	public void setPrincipal(Double principal) {
		this.principal = principal;
	}

	public Double getAccruedInterest() {
		return accruedInterest;
	}

	public void setAccruedInterest(Double accruedInterest) {
		this.accruedInterest = accruedInterest;
	}

	public Double getNetMoney() {
		return netMoney;
	}

	public void setNetMoney(Double netMoney) {
		this.netMoney = netMoney;
	}

	public char getAllocTransType() {
		return allocTransType;
	}

	public static class AllocationBuilder {

		private java.util.Date tradeDate;
		private Integer platformId;
		private String execId;
		private String allocId;
		private Integer allocStatus;
		private String orderId;
		private String clOrdId;
		private String customer;
		private String clientId;
		private String alertCode;
		private String shortName;
		private Integer realSize;
		private Double principal;
		private Double accruedInterest;
		private Double netMoney;
		private String side;
		private char allocTransType;

		public AllocationBuilder tradeDate(java.util.Date tradeDate) {
			this.tradeDate = tradeDate;
			return this;
		}

		public AllocationBuilder platformId(Integer platformId) {
			this.platformId = platformId;
			return this;
		}

		public AllocationBuilder execId(String execId) {
			this.execId = execId;
			return this;
		}

		public AllocationBuilder allocId(String allocId) {
			this.allocId = allocId;
			return this;
		}

		public AllocationBuilder allocStatus(Integer allocStatus) {
			this.allocStatus = allocStatus;
			return this;
		}

		public AllocationBuilder orderId(String orderId) {
			this.orderId = orderId;
			return this;
		}

		public AllocationBuilder clOrdId(String clOrdId) {
			this.clOrdId = clOrdId;
			return this;
		}

		public AllocationBuilder customer(String customer) {
			this.customer = customer;
			return this;
		}

		public AllocationBuilder clientId(String clientId) {
			this.clientId = clientId;
			return this;
		}

		public AllocationBuilder alertCode(String alertCode) {
			this.alertCode = alertCode;
			return this;
		}

		public AllocationBuilder shortName(String shortName) {
			this.shortName = shortName;
			return this;
		}

		public AllocationBuilder realSize(Integer realSize) {
			this.realSize = realSize;
			return this;
		}

		public AllocationBuilder principal(Double principal) {
			this.principal = principal;
			return this;
		}

		public AllocationBuilder accruedInterest(Double accruedInterest) {
			this.accruedInterest = accruedInterest;
			return this;
		}

		public AllocationBuilder netMoney(Double netMoney) {
			this.netMoney = netMoney;
			return this;
		}

		public AllocationBuilder side(String side) {
			this.side = side;
			return this;
		}

		public AllocationBuilder allocTransType(char allocTransType) {
			this.allocTransType = allocTransType;
			return this;
		}

		public Allocation build() {
			return new Allocation(this);
		}
	}

}
