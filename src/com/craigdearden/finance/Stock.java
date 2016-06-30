
package com.craigdearden.finance;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Holds simple information on a given stock.
 */

public final class Stock {

    private String _tickerSymbol;
    private String _company;
    private double _price;
    private double _change;

    Stock() {}

    public Stock(String tickerSymbol, String company, double price, double change) {
        _tickerSymbol = tickerSymbol.toUpperCase();
        _company = company;
        _price = price;
        _change = change;
    }

    /**
     * @return the _tickerSymbol
     */
    public String getTickerSymbol() {
        return _tickerSymbol;
    }

    /**
     * @return the _company
     */
    public String getCompany() {
        return _company;
    }
    
    /**
     * @param the company to set
     */
    public void setCompany(String company) {
        _company = company;
    }

    /**
     * @return the _price
     */
    public double getPrice() {
        return _price;
    }

    /**
     * @param price the _price to set
     */
    public void setPrice(double price) {
        _price = price;
    }

    /**
     * @return the _change
     */
    public double getChange() {
        return _change;
    }

    /**
     * @param change the _change to set
     */
    public void setChange(double change) {
        _change = change;
    }

    /**
     * @return The percent change in the stock price. 
     */
    private double percentChange() {
        Double percent = (_change / _price) * 100;
        Double truncated = new BigDecimal(percent).setScale(2,RoundingMode.UP).doubleValue();
        return truncated;
    }

    /**
     * @return A string containing the ticker symbol, the company name,
     * the stock price, and the stock price change.
     */
    public String toString() {
        return String.format("%-10s %-30s %-10s %-8s %-6s", _tickerSymbol,
                _company, "$ " + String.valueOf(_price), "$ " + String.valueOf(
                _change), String.valueOf(percentChange()) + "%");
    }

}
