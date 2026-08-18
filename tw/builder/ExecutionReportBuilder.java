package com.sumridge.tw.builder;

import java.util.List;

import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.field.AvgPx;
import quickfix.field.BenchmarkPrice;
import quickfix.field.ClOrdID;
import quickfix.field.CouponRate;
import quickfix.field.CumQty;
import quickfix.field.Currency;
import quickfix.field.ExecID;
import quickfix.field.ExecType;
import quickfix.field.LeavesQty;
import quickfix.field.OrdStatus;
import quickfix.field.OrderID;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.PriceType;
import quickfix.field.Product;
import quickfix.field.QuoteRespID;
import quickfix.field.SecurityID;
import quickfix.field.SecurityIDSource;
import quickfix.field.SecuritySubType;
import quickfix.field.SecurityType;
import quickfix.field.SettlDate;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.Text;
import quickfix.field.TradeDate;
import quickfix.field.TransactTime;
import quickfix.field.Yield;

public class ExecutionReportBuilder
{

    private static final Logger LOG = LoggerFactory.getLogger(ExecutionReportBuilder.class);

    private String orderId;
    private String quoteRespId;
    private String clOrdId;
    private String execId;
    private java.util.Date transactTime, tradeDate, settlDate;
    private char execType, ordStatus;
    private String symbol;
    private String securityId, securityIdSource;
    private int product;
    private String securityType, securitySubType;
    private double yield;
    private double benchmarkPrice, benchmarkYield;
    private double outrightPrice;
    private String currency;
    private double coverPrice;
    private double leavesQty, cumQty;
    private double avgPx;
    private char side;
    private int priceType;
    private double price;
    private double orderQty;
    private double couponRate;
    private List<quickfix.fix50.ExecutionReport.NoPartyIDs> noPartyIds;
    private int noOfDealers;
    private boolean intermediated;
    private String ownerTraderId;
    private String text;
    private String confCoriBook, confTrsyBook;
    private double executionFeeSchedule;
    private int executionFeeType;
    private double executionFeeMarkup;
    private double executionFeeAmount;
    private double hdgqty;
    private String otherTrdRefId;
    private double fxRate;
    private List<quickfix.fix50.ExecutionReport.NoLegs> noLegs;
    private boolean hedgeIsPartOfNetHedge;
    private boolean tradeSummary;

    public ExecutionReportBuilder orderId(String orderId)
    {
        this.orderId = orderId;
        return this;
    }

    public ExecutionReportBuilder quoteRespId(String quoteRespId)
    {
        this.quoteRespId = quoteRespId;
        return this;
    }

    public ExecutionReportBuilder clOrdId(String clOrdId)
    {
        this.clOrdId = clOrdId;
        return this;
    }

    public ExecutionReportBuilder execId(String execId)
    {
        this.execId = execId;
        return this;
    }

    public ExecutionReportBuilder transactTime(java.util.Date transactTime)
    {
        this.transactTime = transactTime;
        return this;
    }

    public ExecutionReportBuilder tradeDate(java.util.Date tradeDate)
    {
        this.tradeDate = tradeDate;
        return this;
    }

    public ExecutionReportBuilder settlDate(java.util.Date settlDate)
    {
        this.settlDate = settlDate;
        return this;
    }

    public ExecutionReportBuilder execType(char execType)
    {
        this.execType = execType;
        return this;
    }

    public ExecutionReportBuilder ordStatus(char ordStatus)
    {
        this.ordStatus = ordStatus;
        return this;
    }

    public ExecutionReportBuilder symbol(String symbol)
    {
        this.symbol = symbol;
        return this;
    }

    public ExecutionReportBuilder securityId(String securityId)
    {
        this.securityId = securityId;
        return this;
    }

    public ExecutionReportBuilder securityIdSource(String securityIdSource)
    {
        this.securityIdSource = securityIdSource;
        return this;
    }

    public ExecutionReportBuilder product(int product)
    {
        this.product = product;
        return this;
    }

    public ExecutionReportBuilder securityType(String securityType)
    {
        this.securityType = securityType;
        return this;
    }

    public ExecutionReportBuilder securitySubType(String securitySubType)
    {
        this.securitySubType = securitySubType;
        return this;
    }

    public ExecutionReportBuilder yield(double yield)
    {
        this.yield = yield;
        return this;
    }

    public ExecutionReportBuilder benchmarkPrice(double benchmarkPrice)
    {
        this.benchmarkPrice = benchmarkPrice;
        return this;
    }

    public ExecutionReportBuilder benchmarkYield(double benchmarkYield)
    {
        this.benchmarkYield = benchmarkYield;
        return this;
    }

    public ExecutionReportBuilder outrightPrice(double outrightPrice)
    {
        this.outrightPrice = outrightPrice;
        return this;
    }

    public ExecutionReportBuilder currency(String currency)
    {
        this.currency = currency;
        return this;
    }

    public ExecutionReportBuilder coverPrice(double coverPrice)
    {
        this.coverPrice = coverPrice;
        return this;
    }

    public ExecutionReportBuilder leavesQty(double leavesQty)
    {
        this.leavesQty = leavesQty;
        return this;
    }

    public ExecutionReportBuilder cumQty(double cumQty)
    {
        this.cumQty = cumQty;
        return this;
    }

    public ExecutionReportBuilder avgPx(double avgPx)
    {
        this.avgPx = avgPx;
        return this;
    }

    public ExecutionReportBuilder side(char side)
    {
        this.side = side;
        return this;
    }

    public ExecutionReportBuilder priceType(int priceType)
    {
        this.priceType = priceType;
        return this;
    }

    public ExecutionReportBuilder price(double price)
    {
        this.price = price;
        return this;
    }

    public ExecutionReportBuilder orderQty(double orderQty)
    {
        this.orderQty = orderQty;
        return this;
    }

    public ExecutionReportBuilder couponRate(double couponRate)
    {
        this.couponRate = couponRate;
        return this;
    }

    public ExecutionReportBuilder noPartyIds(List<quickfix.fix50.ExecutionReport.NoPartyIDs> noPartyIds)
    {
        this.noPartyIds = noPartyIds;
        return this;
    }

    public ExecutionReportBuilder noOfDealers(int noOfDealers)
    {
        this.noOfDealers = noOfDealers;
        return this;
    }

    public ExecutionReportBuilder intermediated(boolean intermediated)
    {
        this.intermediated = intermediated;
        return this;
    }

    public ExecutionReportBuilder ownerTraderId(String ownerTraderId)
    {
        this.ownerTraderId = ownerTraderId;
        return this;
    }

    public ExecutionReportBuilder text(String text)
    {
        this.text = text;
        return this;
    }

    public ExecutionReportBuilder confCoriBook(String confCoriBook)
    {
        this.confCoriBook = confCoriBook;
        return this;
    }

    public ExecutionReportBuilder confTrsyBook(String confTrsyBook)
    {
        this.confTrsyBook = confTrsyBook;
        return this;
    }

    public ExecutionReportBuilder executionFeeSchedule(double executionFeeSchedule)
    {
        this.executionFeeSchedule = executionFeeSchedule;
        return this;
    }

    public ExecutionReportBuilder executionFeeType(int executionFeeType)
    {
        this.executionFeeType = executionFeeType;
        return this;
    }

    public ExecutionReportBuilder executionFeeMarkup(double executionFeeMarkup)
    {
        this.executionFeeMarkup = executionFeeMarkup;
        return this;
    }

    public ExecutionReportBuilder executionFeeAmount(double executionFeeAmount)
    {
        this.executionFeeAmount = executionFeeAmount;
        return this;
    }

    public ExecutionReportBuilder hdgqty(double hdgqty)
    {
        this.hdgqty = hdgqty;
        return this;
    }

    public ExecutionReportBuilder otherTrdRefId(String otherTrdRefId)
    {
        this.otherTrdRefId = otherTrdRefId;
        return this;
    }

    public ExecutionReportBuilder fxRate(double fxRate)
    {
        this.fxRate = fxRate;
        return this;
    }

    public ExecutionReportBuilder noLegs(List<quickfix.fix50.ExecutionReport.NoLegs> noLegs)
    {
        this.noLegs = noLegs;
        return this;
    }

    public ExecutionReportBuilder hedgeIsPartOfNetHedge(boolean hedgeIsPartOfNetHedge)
    {
        this.hedgeIsPartOfNetHedge = hedgeIsPartOfNetHedge;
        return this;
    }

    public ExecutionReportBuilder tradeSummary(boolean tradeSummary)
    {
        this.tradeSummary = tradeSummary;
        return this;
    }

    public quickfix.fix50.ExecutionReport build()
    {
        LOG.debug("Creating quickfix.fix50.ExecutionReport message.");
        quickfix.fix50.ExecutionReport executionReport = new quickfix.fix50.ExecutionReport();
        executionReport.set(new OrderID(this.orderId));
        if (this.quoteRespId != null)
            executionReport.set(new QuoteRespID(this.quoteRespId));
        if (this.clOrdId != null)
            executionReport.set(new ClOrdID(this.clOrdId));
        executionReport.set(new ExecID(this.execId));
        if (this.transactTime != null)
            executionReport.setString(TransactTime.FIELD, DateTimeFormat.forPattern("yyyyMMdd-HH:mm:ss.SSS").print(this.transactTime.getTime()));
        if (this.tradeDate != null)
            executionReport.setString(TradeDate.FIELD, DateTimeFormat.forPattern("yyyyMMdd").print(this.tradeDate.getTime()));
        if (this.settlDate != null)
            executionReport.setString(SettlDate.FIELD, DateTimeFormat.forPattern("yyyyMMdd").print(this.settlDate.getTime()));
        executionReport.set(new ExecType(this.execType));
        executionReport.set(new OrdStatus(this.ordStatus));
        executionReport.set(new Symbol(this.symbol));
        if (this.securityId != null)
            executionReport.set(new SecurityID(this.securityId));
        if (this.securityIdSource != null)
            executionReport.set(new SecurityIDSource(this.securityIdSource));
        if (this.product != 0)
            executionReport.set(new Product(this.product));
        if (this.securityType != null)
            executionReport.set(new SecurityType(this.securityType));
        if (this.securitySubType != null)
            executionReport.set(new SecuritySubType(this.securitySubType));
        if (this.yield != 0)
            executionReport.set(new Yield(this.yield));
        if (this.benchmarkPrice != 0)
            executionReport.set(new BenchmarkPrice(this.benchmarkPrice));
        if (this.benchmarkPrice != 0)
            executionReport.setDouble(22570, this.benchmarkYield);
        if (this.outrightPrice != 0)
            executionReport.setDouble(20115, this.outrightPrice);
        if (this.currency != null)
            executionReport.set(new Currency(this.currency));
        if (this.coverPrice != 0)
            executionReport.setDouble(20111, this.coverPrice);
        executionReport.set(new LeavesQty(this.leavesQty));
        executionReport.set(new CumQty(this.cumQty));
        executionReport.set(new AvgPx(this.avgPx));
        if (this.side != 0)
            executionReport.set(new Side(this.side));
        if (this.priceType != 0)
            executionReport.set(new PriceType(this.priceType));
        if (this.price != 0)
            executionReport.set(new Price(this.price));
        if (this.orderQty != 0)
            executionReport.set(new OrderQty(this.orderQty));
        if (this.couponRate != 0)
            executionReport.set(new CouponRate(this.couponRate));
        if (this.noPartyIds != null)
            for (quickfix.fix50.ExecutionReport.NoPartyIDs noPartyId : this.noPartyIds)
                executionReport.addGroup(noPartyId);
        if (this.noOfDealers != 0)
            executionReport.setInt(20086, this.noOfDealers);
        if (this.intermediated)
            executionReport.setBoolean(20262, this.intermediated);
        if (this.ownerTraderId != null)
            executionReport.setString(6153, this.ownerTraderId);
        if (this.text != null)
            executionReport.set(new Text(this.text));
        if (this.confCoriBook != null)
            executionReport.setString(22631, this.confCoriBook);
        if (this.confTrsyBook != null)
            executionReport.setString(22632, this.confTrsyBook);
        if (this.executionFeeSchedule != 0)
            executionReport.setDouble(20056, this.executionFeeSchedule);
        if (this.executionFeeType != 0)
            executionReport.setInt(20058, this.executionFeeType);
        if (this.executionFeeMarkup != 0)
            executionReport.setDouble(20060, this.executionFeeMarkup);
        if (this.executionFeeAmount != 0)
            executionReport.setDouble(20062, this.executionFeeAmount);
        if (this.hdgqty != 0)
            executionReport.setDouble(20250, this.hdgqty);
        if (this.otherTrdRefId != null)
            executionReport.setString(22634, this.otherTrdRefId);
        if (this.fxRate != 0)
            executionReport.setDouble(22630, this.fxRate);
        if (this.noLegs != null)
            for (quickfix.fix50.ExecutionReport.NoLegs noLeg : this.noLegs)
                executionReport.addGroup(noLeg);
        if (this.hedgeIsPartOfNetHedge)
            executionReport.setBoolean(22638, this.hedgeIsPartOfNetHedge);
        if (this.tradeSummary)
            executionReport.setBoolean(22636, this.tradeSummary);
        
        return executionReport;
    }

}
