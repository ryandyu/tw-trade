package com.sumridge.tw.bean;

public class QuoteEntry {

	private final String quoteEntryId;
	private final boolean bidEngaged, offerEngaged;
	private final int axeIndicator;
	private final double bidPx, offerPx;
	private final int bidSize, offerSize;

	private QuoteEntry(QuoteEntryBuilder builder) {
		this.quoteEntryId = builder.quoteEntryId;
		this.bidEngaged = builder.bidEngaged;
		this.offerEngaged = builder.offerEngaged;
		this.axeIndicator = builder.axeIndicator;
		this.bidPx = builder.bidPx;
		this.offerPx = builder.offerPx;
		this.bidSize = builder.bidSize;
		this.offerSize = builder.offerSize;
	}

	public String getQuoteEntryId() {
		return quoteEntryId;
	}

	public boolean isBidEngaged() {
		return bidEngaged;
	}

	public boolean isOfferEngaged() {
		return offerEngaged;
	}

	public int getAxeIndicator() {
		return axeIndicator;
	}

	public double getBidPx() {
		return bidPx;
	}

	public double getOfferPx() {
		return offerPx;
	}

	public int getBidSize() {
		return bidSize;
	}

	public int getOfferSize() {
		return offerSize;
	}

	public static class QuoteEntryBuilder {

		private final String quoteEntryId;
		private boolean bidEngaged, offerEngaged;
		private int axeIndicator;
		private double bidPx, offerPx;
		private int bidSize, offerSize;

		public QuoteEntryBuilder(String quoteEntryId) {
			this.quoteEntryId = quoteEntryId;
		}

		public QuoteEntryBuilder bidEngaged(boolean bidEngaged) {
			this.bidEngaged = bidEngaged;
			return this;
		}

		public QuoteEntryBuilder offerEngaged(boolean offerEngaged) {
			this.offerEngaged = offerEngaged;
			return this;
		}

		public QuoteEntryBuilder axeIndicator(int axeIndicator) {
			this.axeIndicator = axeIndicator;
			return this;
		}

		public QuoteEntryBuilder bidPx(double bidPx) {
			this.bidPx = bidPx;
			return this;
		}

		public QuoteEntryBuilder offerPx(double offerPx) {
			this.offerPx = offerPx;
			return this;
		}

		public QuoteEntryBuilder bidSize(int bidSize) {
			this.bidSize = bidSize;
			return this;
		}

		public QuoteEntryBuilder offerSize(int offerSize) {
			this.offerSize = offerSize;
			return this;
		}

		public QuoteEntry build() {
			return new QuoteEntry(this);
		}

	}

}
