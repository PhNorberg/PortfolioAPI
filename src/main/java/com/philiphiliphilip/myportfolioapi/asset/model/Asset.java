package com.philiphiliphilip.myportfolioapi.asset.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.philiphiliphilip.myportfolioapi.portfolio.model.Portfolio;
import com.philiphiliphilip.myportfolioapi.transaction.model.Transaction;
import jakarta.persistence.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "tickerSymbol"
)
@Entity
public class Asset {

    private final static Logger log = LoggerFactory.getLogger(Asset.class);
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
    @OneToMany(mappedBy = "asset", cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE})
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
    }

    public Asset() {
    }

    public void updateStatistics(Transaction transaction) {
        updateQuantity();
        if (transaction.getTransactionType().equals("buy"))
            updatePurchasePrice();
        updateTotalInvested();
        if (transaction.getTransactionType().equals("buy"))
            updateProfitFactor();
        updateValueNow();
        updateGrossProfitDollars();
        updateNetProfitDollars();
        this.portfolio.updateStatistics();
    }

    public void updatePartialStatistics(){
        updateProfitFactor();
        updateValueNow();
        updateGrossProfitDollars();
        updateNetProfitDollars();
    }

    private void updateQuantity(){
        this.quantity = this.transactions.stream().map(transaction -> "buy".equals(transaction.getTransactionType()) ?
                transaction.getQuantity() : transaction.getQuantity().negate()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void updatePurchasePrice() {

        // Move backwards through transaction list, looking only at the transactions that have gotten us to this
        // current quantity.
        BigDecimal quantity = this.quantity;
        BigDecimal totalSpent = BigDecimal.ZERO;
        BigDecimal buyQuantity = BigDecimal.ZERO;

        List<Transaction> reverseTransactions = new ArrayList<>(this.transactions);
        Collections.reverse(reverseTransactions);
        for (Transaction transaction : reverseTransactions) {
            if (transaction.getTransactionType().equals("buy")) {
                buyQuantity = buyQuantity.add(transaction.getQuantity());
                if (buyQuantity.compareTo(quantity) == 0) {
                    totalSpent = totalSpent.add(transaction.getQuantity().multiply(transaction.getPurchasePrice()));
                    break;
                } else {
                    totalSpent = totalSpent.add(transaction.getQuantity().multiply(transaction.getPurchasePrice()));
                }
            } else {
                buyQuantity = buyQuantity.subtract(transaction.getQuantity());
                totalSpent = totalSpent.subtract(transaction.getQuantity().multiply(transaction.getPurchasePrice()));
            }
        }
        this.purchasePrice = totalSpent.divide(this.quantity, 10, RoundingMode.DOWN).setScale(2, RoundingMode.DOWN);
    }

    private void updateTotalInvested(){
        this.totalInvested = this.quantity.multiply(this.purchasePrice).setScale(2, RoundingMode.DOWN);
    }

    private void updateProfitFactor() {
        String url = createRequestURL();

        try{
            BigDecimal closeValue = callTwelveDataAPI(url);
            this.currentPrice = closeValue.setScale(2, RoundingMode.DOWN);
            this.profitFactor = closeValue.divide(purchasePrice, 10, RoundingMode.DOWN);
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

    private void updateGrossProfitDollars() {
        this.grossProfitDollars = this.totalInvested.multiply(this.profitFactor).subtract(totalInvested);
    }

    private void updateNetProfitDollars() {

        // Here it crashes when user tries to sell some asset he has 0 quantity of
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
