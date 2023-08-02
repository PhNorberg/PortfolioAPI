package com.philiphiliphilip.myportfolioapi.asset;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.philiphiliphilip.myportfolioapi.portfolio.Portfolio;
import com.philiphiliphilip.myportfolioapi.transaction.Transaction;
import jakarta.persistence.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "tickerSymbol"
)
@Entity
public class Asset {
    @Id
    @GeneratedValue
    private Integer id;
    private String tickerSymbol;
    private BigDecimal quantity;
    private BigDecimal purchasePrice;
    private Integer taxRate;
    private BigDecimal profitFactor;
    private BigDecimal profitDollars;
    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;
    @OneToMany(mappedBy = "asset", cascade = CascadeType.REMOVE)
    private List<Transaction> transactions;

    public Asset(Integer id, String tickerSymbol, Integer taxRate,
                 Portfolio portfolio, List<Transaction> transactions) {
        this.id = id;
        this.tickerSymbol = tickerSymbol;
        this.taxRate = taxRate;
        this.portfolio = portfolio;
        this.transactions = transactions;
        this.purchasePrice = BigDecimal.ZERO;
        this.quantity = BigDecimal.ZERO;
        this.profitFactor = BigDecimal.ZERO;
        updateQuantity();
        updatePurchasePrice();
        updateProfitFactor();
    }

    public Asset() {
    }

    public void updateProfitFactor() {
        // We have purchasePrice, the CURRENT PRICE has to be divided by purchasePrice.
        // Get current price through TwelveData API.
        Properties prop = new Properties();
        String apiKey = "";
        String REQUEST_URL = "";
        try {
            FileInputStream input = new FileInputStream("src/main/resources/secrets.properties");
            prop.load(input);
            apiKey = prop.getProperty("TWELVEDATA_API_KEY");
            REQUEST_URL = "https://api.twelvedata.com/time_series?symbol=" + tickerSymbol + "/USD&interval=1min&outputsize=1&apikey=" + apiKey;
        }
        catch (FileNotFoundException e) {
            // Could log exceptions aswell
            System.err.println("File not found " + e.getMessage());
        }
        catch (IOException e) {
            // Could log exceptions aswell
            System.err.println("Error reading file " + e.getMessage());
        }

        // Below is TwelveDatas own suggested Java Code to use their API.
        try {
            URL requestURL = new URL(REQUEST_URL);
            HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
            StringBuilder responseData = new StringBuilder();
            JSONParser parser = new JSONParser();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "twelve_java/1.0");
            connection.connect();

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Request failed. Error: " + connection.getResponseMessage());
            }

            Scanner scanner = new Scanner(requestURL.openStream());
            while (scanner.hasNextLine()) {
                responseData.append(scanner.nextLine());
            }

            JSONObject json = (JSONObject) parser.parse(responseData.toString());
            JSONObject meta = (JSONObject) json.get("meta");
            JSONArray values = (JSONArray) json.get("values");

            connection.disconnect();

            // Here my own code starts again
            JSONObject firstValueObject = (JSONObject)values.get(0);
            BigDecimal closeValues = new BigDecimal((String)firstValueObject.get("close"));
            this.profitFactor = closeValues.divide(purchasePrice, 10, RoundingMode.DOWN).setScale(2, RoundingMode.DOWN);

        }
        catch (MalformedURLException e){
            // Could log exceptions aswell
            System.err.println("Malformed URL " + e.getMessage());
    }
        catch (ProtocolException e){
            System.err.println("Protocol error (TCP perhaps) " + e.getMessage());
        }
        catch (IOException e) {
            // Could log exceptions aswell
            System.err.println("Error reading file " + e.getMessage());
        }
        catch (ParseException e){
            System.err.println("Error while parsing " + e.getMessage());
        }


    }

    public void updatePurchasePrice() {
        BigDecimal totalSpent = this.transactions.stream()
                .filter(transaction -> transaction.getTransactionType().equals("buy"))
                .map(transaction -> transaction.getPurchasePrice().multiply(transaction.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (this.quantity.equals(BigDecimal.ZERO)){
            this.purchasePrice = BigDecimal.ZERO;
        }
        else {
            this.purchasePrice = totalSpent.divide(this.quantity, 10, RoundingMode.DOWN).setScale(2, RoundingMode.DOWN);
        }
    }

    public void updateQuantity(){
        this.quantity = this.transactions.stream().map(transaction -> "buy".equals(transaction.getTransactionType()) ?
                transaction.getQuantity() : transaction.getQuantity().negate()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    public Integer getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Integer taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getProfitFactor() {
        return profitFactor;
    }

    public void setProfitFactor(BigDecimal profitPercentage) {
        this.profitFactor = profitPercentage;
    }

    public BigDecimal getProfitDollars() {
        return profitDollars;
    }

    public void setProfitDollars(BigDecimal profitDollars) {
        this.profitDollars = profitDollars;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "id=" + id +
                ", tickerSymbol='" + tickerSymbol + '\'' +
                ", quantity=" + quantity +
                ", purchasePrice=" + purchasePrice +
                ", taxRate=" + taxRate +
                ", portfolio=" + portfolio +
                ", transactions=" + transactions +
                '}';
    }
}
