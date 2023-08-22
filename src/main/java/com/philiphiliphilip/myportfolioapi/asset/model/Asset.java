package com.philiphiliphilip.myportfolioapi.asset.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.philiphiliphilip.myportfolioapi.exception.EmptyApiKeySetException;
import com.philiphiliphilip.myportfolioapi.portfolio.model.Portfolio;
import com.philiphiliphilip.myportfolioapi.transaction.model.Transaction;
import jakarta.persistence.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

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
        updatePurchasePrice(transaction);
        updateTotalInvested();
        updateProfitFactor(transaction);
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

    private void updatePurchasePrice(Transaction transaction) {

        // If a sell event brought down holdings to 0.
        if (quantity.compareTo(BigDecimal.ZERO) == 0){
            this.purchasePrice = BigDecimal.ZERO;
        } else if(transaction.getTransactionType().equals("sell")){
            // Do nothing if transaction type is of sell and didn't bring holdings to 0.
            // This doesn't impact purchase price.
        } else {
            // Move backwards through transaction list, looking only at the transactions that have gotten us to this
            // current quantity.
            BigDecimal quantity = this.quantity;
            BigDecimal totalSpent = BigDecimal.ZERO;
            BigDecimal buyQuantity = BigDecimal.ZERO;

            List<Transaction> reverseTransactions = new ArrayList<>(this.transactions);
            Collections.reverse(reverseTransactions);
            for (Transaction reverseTransaction : reverseTransactions) {
                if (reverseTransaction.getTransactionType().equals("buy")) {
                    buyQuantity = buyQuantity.add(reverseTransaction.getQuantity());
                    if (buyQuantity.compareTo(quantity) == 0) {
                        totalSpent = totalSpent.add(reverseTransaction.getQuantity().multiply(reverseTransaction
                                .getPurchasePrice()));
                        break;
                    } else {
                        totalSpent = totalSpent.add(reverseTransaction.getQuantity()
                                .multiply(reverseTransaction.getPurchasePrice()));
                    }
                } else {
                    buyQuantity = buyQuantity.subtract(reverseTransaction.getQuantity());
                    totalSpent = totalSpent.subtract(reverseTransaction.getQuantity()
                            .multiply(reverseTransaction.getPurchasePrice()));
                }
            }
            this.purchasePrice = totalSpent.divide(this.quantity, 10, RoundingMode.DOWN)
                    .setScale(2, RoundingMode.DOWN);
        }
    }

    private void updateTotalInvested(){
        this.totalInvested = this.quantity.multiply(this.purchasePrice).setScale(2, RoundingMode.DOWN);
    }

    /*
    Updating of profit factor from an asset point of view when a transaction happened.
     */
    private void updateProfitFactor(Transaction transaction) {

        // If a sell event brought down holdings to 0.
        if (quantity.compareTo(BigDecimal.ZERO) == 0){
            this.purchasePrice = BigDecimal.ZERO;
        } else if(transaction.getTransactionType().equals("sell")) {
            // Do nothing if transaction type is of sell and didn't bring holdings to 0.
            // This doesn't impact purchase price.
        }
            else {
                String url = createRequestURL();

                try {
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
    }

    /*
    Updating of profit factor from a portfolio point of view.
     */
    private void updateProfitFactor() {

        if (quantity.compareTo(BigDecimal.ZERO) == 0) {
            this.profitFactor = BigDecimal.ZERO;
        }
        else {
            // Fetch current price and update profit factor without relying on any transaction
            String url = createRequestURL();

            try {
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
        }

    private String createRequestURL(){
        String apiKey = readAPIKey();
        // In future, if more assetTypes show up, use switch/case.
        return (assetType.equals("crypto") ?
                "https://api.twelvedata.com/time_series?symbol=" + tickerSymbol + "/USD&interval=1min&outputsize=1&apikey=" + apiKey :
                "https://api.twelvedata.com/time_series?symbol=" + tickerSymbol + "&interval=1min&outputsize=1&apikey=" + apiKey);
    }

    private String readAPIKey(){

        // Code from amazon
        String secretName = "twelvedata-API-key";
        Region region = Region.of("eu-north-1");

        // Create a Secrets Manager client
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse getSecretValueResponse;

        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            // For a list of exceptions thrown, see
            // https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
            throw e;
        }


        // Returns {"TWELVEDATA_API_KEY" : "key value"}
        String secret = getSecretValueResponse.secretString();

        // Parse the JSON string and extract the API Key
        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(secret);
            String apiKey = (String) json.get("TWELVEDATA_API_KEY");
            return apiKey;
        } catch (ParseException e) {
            throw new EmptyApiKeySetException();
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
