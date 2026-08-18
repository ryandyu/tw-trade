package com.sumridge.tw.bean;

import java.util.Date;
import java.util.GregorianCalendar;

import quickfix.field.PriceType;

public class AxeOrder
{
    private static Date expireDate;

    public static final boolean CHANGE_ONLY = true;
    
    public static final int CUTOFF_QTY = 250 * 1000;
    public static final double CUTOFF_SPD = 0.100;
    public static final double CUTOFF_PRC = 0.001;
    public static final String DELIMITER = "-";

    private String axeId;
    private String axeRefId;

    private String securityId;
    private String securityIdSource;
    private String benchmarkSecurityId;
    private String benchmarkSecurityIdSource;

    private String accountId;

    private char side;
    private int quantity;
    private int minQuantity;
    private int incQuantity;

    private double price = 0d;
    private Double spread;
    private int priceType;
    private int priceLevel;

    private char transType;

    private String traderId = "9331205";  //26618481 jjiang666
    private String targetList = "SumRidge Customer";

    static
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(GregorianCalendar.DATE, 1);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.MILLISECOND, 0);
        expireDate = cal.getTime();
    }

    public static Date getExpireDate()
    {
        return expireDate;
    }

    public static boolean isChanged(AxeOrder axe, AxeOrder old)
    {
        try
        {
            boolean changed = !CHANGE_ONLY;  //change to false for sending diff

            if (old == null)
            {
                if (axe.getQuantity() < CUTOFF_QTY)
                    changed = false;
                else
                    changed = true;
            }
            else if (old.getQuantity() < CUTOFF_QTY && axe.getQuantity() < CUTOFF_QTY)
                changed = false;
            else if (old.getQuantity() != axe.getQuantity())
                changed = true;
            else if (old.getPriceType() != axe.getPriceType())
                changed = true;
            else if (Math.abs(old.getPrice() - axe.getPrice()) >= CUTOFF_SPD && axe.getPriceType() == PriceType.SPREAD)
                changed = true;
            else if (Math.abs(old.getPrice() - axe.getPrice()) >= CUTOFF_PRC && axe.getPriceType() == PriceType.PERCENTAGE)
                changed = true;
            else if (Math.abs(old.getMinQuantity() - axe.getMinQuantity()) >= CUTOFF_QTY)
                changed = true;
            else if (Math.abs(old.getIncQuantity() - axe.getIncQuantity()) >= CUTOFF_QTY)
                changed = true;

            return changed;
        }
        catch (Throwable e)
        {
        }

        return false;
    }

    public static String key(String secId, char side, int level)
    {
        return secId + DELIMITER + side + DELIMITER + level;
    }

    public String key()
    {
        return key(this.securityId, this.side, this.priceLevel);
    }

    public String getAxeId()
    {
        return axeId;
    }

    public AxeOrder setAxeId(String axeId)
    {
        this.axeId = axeId;
        return this;
    }

    public String getAxeRefId()
    {
        return axeRefId;
    }

    public AxeOrder setAxeRefId(String axeRefId)
    {
        this.axeRefId = axeRefId;
        return this;
    }

    public String getSecurityId()
    {
        return securityId;
    }

    public AxeOrder setSecurityId(String securityId)
    {
        this.securityId = securityId;
        return this;
    }

    public String getSecurityIdSource()
    {
        return securityIdSource;
    }

    public AxeOrder setSecurityIdSource(String securityIdSource)
    {
        this.securityIdSource = securityIdSource;
        return this;
    }

    public String getBenchmarkSecurityId()
    {
        return benchmarkSecurityId;
    }

    public AxeOrder setBenchmarkSecurityId(String benchmarkSecurityId)
    {
        this.benchmarkSecurityId = benchmarkSecurityId;
        return this;
    }

    public String getBenchmarkSecurityIdSource()
    {
        return benchmarkSecurityIdSource;
    }

    public AxeOrder setBenchmarkSecurityIdSource(String benchmarkSecurityIdSource)
    {
        this.benchmarkSecurityIdSource = benchmarkSecurityIdSource;
        return this;
    }

    public String getAccountId()
    {
        return accountId;
    }

    public AxeOrder setAccountId(String accountId)
    {
        this.accountId = accountId;
        return this;
    }

    public char getSide()
    {
        return side;
    }

    public AxeOrder setSide(char side)
    {
        this.side = side;
        return this;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public AxeOrder setQuantity(int quantity)
    {
        this.quantity = quantity;
        return this;
    }

    public int getMinQuantity()
    {
        return minQuantity;
    }

    public AxeOrder setMinQuantity(int minQuantity)
    {
        this.minQuantity = minQuantity;
        return this;
    }

    public int getIncQuantity()
    {
        return incQuantity;
    }

    public AxeOrder setIncQuantity(int incQuantity)
    {
        this.incQuantity = incQuantity;
        return this;
    }

    public double getPrice()
    {
        return price;
    }

    public AxeOrder setPrice(double price)
    {
        this.price = price;
        return this;
    }

    public Double getSpread()
    {
        return spread;
    }

    public AxeOrder setSpread(Double spread)
    {
        this.spread = spread;
        return this;
    }

    public int getPriceType()
    {
        return priceType;
    }

    public AxeOrder setPriceType(int priceType)
    {
        this.priceType = priceType;
        return this;
    }

    public int getPriceLevel()
    {
        return priceLevel;
    }

    public AxeOrder setPriceLevel(int priceLevel)
    {
        this.priceLevel = priceLevel;
        return this;
    }

    public char getTransType()
    {
        return transType;
    }

    public AxeOrder setTransType(char transType)
    {
        this.transType = transType;
        return this;
    }

    public String getTraderId()
    {
        return traderId;
    }

    public AxeOrder setTraderId(String traderId)
    {
        this.traderId = traderId;
        return this;
    }

    public String getTargetList()
    {
        return targetList;
    }

    public AxeOrder setTargetList(String targetList)
    {
        this.targetList = targetList;
        return this;
    }

}
