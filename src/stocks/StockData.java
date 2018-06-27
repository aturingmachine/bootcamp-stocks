package stocks;

import java.sql.Date;

public class StockData {
    private String symbol;
    private double price;
    private int volume;
    private Date date;

    public StockData(String symbol, double price, int volume, Date date) {
        this.symbol = symbol;
        this.price = price;
        this.volume = volume;
        this.date = date;
    }

    private StockData() {}


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}