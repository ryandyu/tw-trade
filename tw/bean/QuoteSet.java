package com.sumridge.tw.bean;

import java.util.List;

public class QuoteSet {

	private final String quoteSetId;
	private final String securityId;
	private final String securityIdSource;
	private final int offeringType;
	private final int offerType;
	private final String ownerTraderId;
	private final String benchmarkSecurityId;
	private final String benchmarkSecurityIdSource;
	private final String benchmarkSymbolSfx;
	private final List<QuoteEntry> quoteEntries;
	private String quoteSetAction;

	private QuoteSet(QuoteSetBuilder builder) {
		this.quoteSetId = builder.quoteSetId;
		this.securityId = builder.securityId;
		this.securityIdSource = builder.securityIdSource;
		this.offeringType = builder.offeringType;
		this.offerType = builder.offerType;
		this.ownerTraderId = builder.ownerTraderId;
		this.benchmarkSecurityId = builder.benchmarkSecurityId;
		this.benchmarkSecurityIdSource = builder.benchmarkSecurityIdSource;
		this.benchmarkSymbolSfx = builder.benchmarkSymbolSfx;
		this.quoteEntries = builder.quoteEntries;
		this.quoteSetAction = builder.quoteSetAction;
	}

	public String getQuoteSetId() {
		return quoteSetId;
	}

	public String getSecurityId() {
		return securityId;
	}

	public String getSecurityIdSource() {
		return securityIdSource;
	}

	public int getOfferingType() {
		return offeringType;
	}

	public int getOfferType() {
		return offerType;
	}

	public String getOwnerTraderId() {
		return ownerTraderId;
	}

	public String getBenchmarkSecurityId() {
		return benchmarkSecurityId;
	}

	public String getBenchmarkSecurityIdSource() {
		return benchmarkSecurityIdSource;
	}

	public String getBenchmarkSymbolSfx() {
		return benchmarkSymbolSfx;
	}

	public List<QuoteEntry> getQuoteEntries() {
		return quoteEntries;
	}

	public String getQuoteSetAction() {
		return quoteSetAction;
	}

	public void setQuoteSetAction(String quoteSetAction) {
		this.quoteSetAction = quoteSetAction;
	}

	public static class QuoteSetBuilder {

		private final String quoteSetId;
		private String quoteSetAction;
		private String securityId;
		private String securityIdSource;
		private int offeringType;
		private int offerType;
		private String ownerTraderId;
		private String benchmarkSecurityId;
		private String benchmarkSecurityIdSource;
		private String benchmarkSymbolSfx;
		private List<QuoteEntry> quoteEntries;

		public QuoteSetBuilder(String quoteSetId) {
			this.quoteSetId = quoteSetId;
		}

		public QuoteSetBuilder quoteSetAction(String quoteSetAction) {
			this.quoteSetAction = quoteSetAction;
			return this;
		}

		public QuoteSetBuilder securityId(String securityId) {
			this.securityId = securityId;
			return this;
		}

		public QuoteSetBuilder securityIdSource(String securityIdSource) {
			this.securityIdSource = securityIdSource;
			return this;
		}

		public QuoteSetBuilder offeringType(int offeringType) {
			this.offeringType = offeringType;
			return this;
		}

		public QuoteSetBuilder offerType(int offerType) {
			this.offerType = offerType;
			return this;
		}

		public QuoteSetBuilder ownerTraderId(String ownerTraderId) {
			this.ownerTraderId = ownerTraderId;
			return this;
		}

		public QuoteSetBuilder benchmarkSecurityId(String benchmarkSecurityId) {
			this.benchmarkSecurityId = benchmarkSecurityId;
			return this;
		}

		public QuoteSetBuilder benchmarkSecurityIdSource(
				String benchmarkSecurityIdSource) {
			this.benchmarkSecurityIdSource = benchmarkSecurityIdSource;
			return this;
		}

		public QuoteSetBuilder benchmarkSymbolSfx(String benchmarkSymbolSfx) {
			this.benchmarkSymbolSfx = benchmarkSymbolSfx;
			return this;
		}

		public QuoteSetBuilder quoteEntries(List<QuoteEntry> quoteEntries) {
			this.quoteEntries = quoteEntries;
			return this;
		}

		public QuoteSet build() {
			return new QuoteSet(this);
		}
	}

}
