/* Author:      Craig Dearden
 * Date:        Jun 28, 2016
 * Name:        StockWatch.java
 * Description: 
 */
package com.craigdearden.apps;

import com.craigdearden.finance.Stock;
import java.io.IOException;
import java.util.LinkedList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Retrieves the stock prices for a specified set of ticker symbols and
 * perpetually updates them at a given frequency.
 *
 * <p>
 * A <code>StockWatch</code>...
 * </p>
 */
public class StockWatch implements Runnable {

    private final LinkedList<Stock> _stocks = new LinkedList<>();
    private final String threadName = "default";
    private Thread _t;
    private int _frequency = 1;

    StockWatch() {
    }

    /**
     * Constructs a <code>StockWatch</code> that updates specified stocks at a
     * specified frequency.
     *
     * @param tickerSymbols A String array of ticker symbols identifying the
     * stocks to check
     * @param frequency The frequency to update the stocks (in seconds).
     */
    StockWatch(String[] tickerSymbols, int frequency) {
        _frequency = frequency * 1000;

        for (String symbol : tickerSymbols) {
            Stock stock = retrieveStock(symbol);
            _stocks.add(stock);
        }
    }

    /**
     * @return the _frequency
     */
    public int getFrequency() {
        return _frequency / 1000;
    }

    /**
     * @param frequency the frequency to set
     */
    public void setFrequency(int frequency) {
        _frequency = frequency * 1000;
    }

    /**
     * Add a stock to the list of stocks to be watched.
     *
     * @param tickerSymbol A String identifying the stock to be added.
     * @throws IllegalArgumentException if the ticker symbol provided is null.
     */
    public void addStock(String tickerSymbol) throws IllegalArgumentException {
        if (tickerSymbol == null) {
            throw new IllegalArgumentException();
        }
        Stock stock = retrieveStock(tickerSymbol);
        _stocks.add(stock);
    }
    
    /**
     * Add stocks to the list of stocks to be watched.
     *
     * @param tickerSymbol A String array identifying the stocks to be added.
     * @throws IllegalArgumentException if the ticker symbol provided is null.
     */
    public void addStock(String[] tickerSymbol) throws IllegalArgumentException {
        if (tickerSymbol == null) {
            throw new IllegalArgumentException();
        }
        for (String ticker : tickerSymbol) {
            Stock stock = retrieveStock(ticker);
            _stocks.add(stock);
        }
    }

    /**
     * Flags the stock with the given ticker symbol and removes it from the list
     * of stocks to be watched.
     *
     * @param tickerSymbol A String identifying the stock to be removed.
     * @throws IllegalArgumentException If the ticker symbol provided is null.
     */
    public void removeStock(String tickerSymbol) throws IllegalArgumentException {
        if (tickerSymbol == null) {
            throw new IllegalArgumentException();
        }

        Stock flagged = null;
        for (Stock s : _stocks) {
            if (s.getTickerSymbol().equals(tickerSymbol)) {
                flagged = s;
            }
        }

        if (flagged != null) {
            _stocks.remove(flagged);
        }
    }

    /**
     * Retrieves the company name, the price of the stock, and the most recent
     * change in stock price from the web.
     *
     * @param tickerSymbol ticker symbol of stock to be retrieved.
     * @return stock A stock object containing company name, price of stock, and
     * most recent change in stock price.
     */
    private Stock retrieveStock(String tickerSymbol) {
        String company = null;
        double price = 0;
        double change = 0;
        String url = "https://www.google.com/finance?q=" + tickerSymbol;
        
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException ex) {
            System.out.println("Oops");
        }
        
        

        try {
            company = doc.getElementById("sharebox-data").child(0).attributes().get(
                    "content");
            price = Double.parseDouble(
                    doc.getElementById("price-panel").child(0).child(0).child(0).text());
            change = Double.parseDouble(
                    doc.getElementById("price-panel").child(0).child(
                    1).child(0).child(0).text());
        } catch (NullPointerException|NumberFormatException ex) {
            company = "Not available...";
        }
        
        Stock stock = new Stock(tickerSymbol, company, price, change);
        return stock;
    }

    private void checkStock(Stock stock) {
        double price = 0;
        double change = 0;
        String url = "https://www.google.com/finance?q=" +
                stock.getTickerSymbol();

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException ex) {
            System.out.println("Oops");
        }
        try {
            price = Double.parseDouble(
                    doc.getElementById("price-panel").child(0).child(0).child(0).text());
            change = Double.parseDouble(
                    doc.getElementById("price-panel").child(0).child(
                    1).child(0).child(0).text());
        } catch (NullPointerException|NumberFormatException ex) {
            stock.setCompany("Not available...");
        }

        stock.setPrice(price);
        stock.setChange(change);
    }

    public void checkStocks() {
        for (Stock s : _stocks) {
            checkStock(s);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                checkStocks();
                display();
                Thread.sleep(_frequency);
            }
        } catch (InterruptedException ex) {
            System.out.println("Thread interrupted...");
        }
    }

    public void start() {
        if (_t == null) {
            _t = new Thread(this, threadName);
            _t.start();
        } else {
            _t.start();
        }
    }

    /**
     * Displays a list of ticker symbols with their corresponding company names,
     * stock prices, and stock price changes
     */
    public void display() {
        for (Stock s : _stocks) {
            if (!s.getCompany().equals("Not available...")) 
                System.out.println(s);
             else
                System.out.printf("%-10s %-30s %n", s.getTickerSymbol(),
                s.getCompany());
        }
    }
}
