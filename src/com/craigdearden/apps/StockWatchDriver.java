/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.craigdearden.apps;

/**
 *
 * @author C1
 */
public class StockWatchDriver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        StockWatch sw = new StockWatch();
        sw.setFrequency(10);

        String[] tickerSymbols = {"ABG", "ABM", "ABR", "ABR-A", "ABR-B", "ABR-C",
            "ABRN", "ABT", "ABX", "AC", "ACC", "ACCO", "ACH", "ACM", "ACN",
            "ACP", "ACRE", "ACV", "ACW", "ADC", "ADM", "ADPT", "ADS", "ADX",
            "AEB", "AED"};

        sw.addStock(tickerSymbols);
        sw.start();

    }

}
