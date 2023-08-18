package com.philiphiliphilip.myportfolioapi.asset;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.philiphiliphilip.myportfolioapi.portfolio.model.Portfolio;
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
import java.util.ArrayList;
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
    private String assetType;
    private BigDecimal quantity;
    private BigDecimal purchasePrice;
    private BigDecimal currentPrice;
    private BigDecimal totalInvested;
    private BigDecimal valueNow;
    private BigDecimal taxRate;
    private BigDecimal profitFactor;
    private BigDecimal grossProfitDollars;
    private BigDecimal netProfitDollars;
    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;
    @OneToMany(mappedBy = "asset", cascade = CascadeType.REMOVE)
    private List<Transaction> transactions;
    public Asset(String tickerSymbol, String assetType, BigDecimal taxRate) {
        this.tickerSymbol = tickerSymbol;
        this.assetType = assetType;
        this.taxRate = taxRate;
        this.transactions = new ArrayList<>();
        this.quantity = BigDecimal.ZERO;
        this.purchasePrice = BigDecimal.ZERO;
        this.currentPrice = BigDecimal.ZERO; // Updated inside updateProfitFactor(). Might refactor it out later.
        this.totalInvested = BigDecimal.ZERO;
        this.valueNow = BigDecimal.ZERO;
        this.profitFactor = BigDecimal.ZERO;
        this.grossProfitDollars = BigDecimal.ZERO;
        this.netProfitDollars = BigDecimal.ZERO;
        //updateStatistics();
    }

    public Asset() {
    }

    public void updateStatistics() {
        updateQuantity();
        updatePurchasePrice();
        updateTotalSpent();
        updateProfitFactor();
        updateValueNow();
        updateProfitDollars();
        updateNetProfitDollars();
        this.portfolio.updateStatistics();
    }

    private void updateQuantity(){
        this.quantity = this.transactions.stream().map(transaction -> "buy".equals(transaction.getTransactionType()) ?
                transaction.getQuantity() : transaction.getQuantity().negate()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void updatePurchasePrice() {
        BigDecimal totalSpent = this.transactions.stream()
                .filter(transaction -> transaction.getTransactionType().equals("buy"))
                .map(transaction -> transaction.getPurchasePrice().multiply(transaction.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Quantity can be 0 even if "buy"'s are found, since sells can have brought holdings down to 0.
        if (this.quantity.equals(BigDecimal.ZERO)){
            this.purchasePrice = BigDecimal.ZERO;
        }
        else {
            this.purchasePrice = totalSpent.divide(this.quantity, 10, RoundingMode.DOWN).setScale(2, RoundingMode.DOWN);
        }
    }
    private void updateTotalSpent(){
        this.totalInvested = this.quantity.multiply(this.purchasePrice).setScale(2, RoundingMode.DOWN);
    }
    private void updateProfitFactor() {
        String url = createRequestURL();

        try{
            BigDecimal closeValue = callTwelveDataAPI(url);
            this.currentPrice = closeValue.setScale(2, RoundingMode.DOWN);
            this.profitFactor = closeValue.divide(purchasePrice, 10, RoundingMode.DOWN).setScale(2, RoundingMode.DOWN);
        } catch (MalformedURLException e) {
            System.err.println("Malformed URL " + e.getMessage());
        } catch (ProtocolException e) {
            System.err.println("Protocol error " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading file " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Error while parsing " + e.getMessage());
        }
    }

    private String createRequestURL(){
        String apiKey = readAPIKey();
        // In future, if more assetTypes show up, use switch/case.
        return (assetType.equals("crypto") ?
                "https://api.twelvedata.com/time_series?symbol=" + tickerSymbol + "/USD&interval=1min&outputsize=1&apikey=" + apiKey :
                "https://api.twelvedata.com/time_series?symbol=" + tickerSymbol + "&interval=1min&outputsize=1&apikey=" + apiKey);
    }

    private String readAPIKey(){

        try {
            Properties prop = new Properties();
            FileInputStream input = new FileInputStream("src/main/resources/secrets.properties");
            prop.load(input);
            return prop.getProperty("TWELVEDATA_API_KEY");
        }
        catch (FileNotFoundException e) {
            // Could log exceptions aswell
            System.err.println("File not found " + e.getMessage());
            return "";
        }
        catch (IOException e) {
            // Could log exceptions aswell
            System.err.println("Error reading file " + e.getMessage());
            return "";
        }

    }
    private BigDecimal callTwelveDataAPI(String requestURL) throws IOException, ParseException {
        // This call fetches last closing price of given Asset.

            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            StringBuilder responseData = new StringBuilder();
            JSONParser parser = new JSONParser();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "twelve_java/1.0");
            connection.connect();

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Request failed. Error: " + connection.getResponseMessage());
            }

            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNextLine()) {
                responseData.append(scanner.nextLine());
            }

            JSONObject json = (JSONObject) parser.parse(responseData.toString());
            // To do: Check for "status" code "error" or "ok" which tells you if its a valid API call or not.
            JSONObject meta = (JSONObject) json.get("meta");
            JSONArray values = (JSONArray) json.get("values");

            connection.disconnect();

            JSONObject firstValueObject = (JSONObject)values.get(0);
            return new BigDecimal((String)firstValueObject.get("close"));

    }

    private void updateValueNow(){
        this.valueNow = this.totalInvested.multiply(this.profitFactor).setScale(2, RoundingMode.DOWN);
    }

    private void updateProfitDollars() {
        BigDecimal grossProfit = this.valueNow.multiply(this.profitFactor);
        this.grossProfitDollars = grossProfit.subtract(this.totalInvested);
    }

    private void updateNetProfitDollars() {
        BigDecimal taxRateDecimal = this.taxRate.divide(new BigDecimal(100), RoundingMode.DOWN);
        BigDecimal taxAmount = this.grossProfitDollars.multiply(taxRateDecimal);
        this.netProfitDollars = this.grossProfitDollars.subtract(taxAmount);

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

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
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

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getTotalInvested() {
        return totalInvested;
    }

    public void setTotalInvested(BigDecimal totalSpent) {
        this.totalInvested = totalSpent;
    }

    public BigDecimal getValueNow() {
        return valueNow;
    }

    public void setValueNow(BigDecimal valueNow) {
        this.valueNow = valueNow;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getProfitFactor() {
        return profitFactor;
    }

    public void setProfitFactor(BigDecimal profitPercentage) {
        this.profitFactor = profitPercentage;
    }

    public BigDecimal getGrossProfitDollars() {
        return grossProfitDollars;
    }

    public void setGrossProfitDollars(BigDecimal profitDollars) {
        this.grossProfitDollars = profitDollars;
    }

    public BigDecimal getNetProfitDollars() {
        return netProfitDollars;
    }

    public void setNetProfitDollars(BigDecimal netProfitDollars) {
        this.netProfitDollars = netProfitDollars;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
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
