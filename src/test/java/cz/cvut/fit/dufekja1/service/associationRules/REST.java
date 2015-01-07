package cz.cvut.fit.dufekja1.service.associationRules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cz.cvut.fit.dufekja1.shopAnalyst.associationRules.bean.AssociationRule;
import cz.cvut.fit.dufekja1.shopAnalyst.associationRules.bean.DbMapper;
import cz.cvut.fit.dufekja1.shopAnalyst.associationRules.bean.DbTransaction;
import cz.cvut.fit.dufekja1.shopAnalyst.associationRules.bean.Transaction;
import cz.cvut.fit.dufekja1.shopAnalyst.churnDetection.bean.ChurnProbability;
import cz.cvut.fit.dufekja1.shopAnalyst.churnDetection.bean.LabeledOrderInformation;
import cz.cvut.fit.dufekja1.shopAnalyst.churnDetection.bean.UnlabeledOrders;

public class REST {

    private Client client;
    private static final String targetUrl = "http://localhost:8086";
    private static final String targetAddDbTransactionPath = "/ar/db/add-transaction/";
    private static final String targetAddTransactionPath = "/ar/add-transaction/";
    private static final String targetAddDbMapperPath = "/ar/db/add-mapper/";
    private static final String targetDbTransofrmAndCopy="/ar/db/transform-and-copy/";
    private static final String targetDbCopy="/ar/db/copy/";
    private static final String targetPreprocessPath = "/ar/preprocess/";
    private static final String targetGeneratePath = "/ar/generate/";
    private static final String targetResetPath = "/ar/reset/";
    
    private static final String targetChurnResetPath = "/churn/reset/";
    private static final String targetChurnAddOrderPath = "/churn/add-order-information/";
    private static final String targetChurnDetection = "/churn/detect-churn/";
    
    public static void main (String [] params) throws IOException{
        
        String identifier = "identifier1";
         preprocessing(identifier);
        // test1(identifier);
         generating(identifier);
         //churn(identifier);
    }
    
        
    private static void generating(String identifier){

        REST rest = new REST();
        List<AssociationRule> result = rest.generate(identifier, 0.005, 0.005);
        result.size();
    }
    
    private static void preprocessing(String identifier) throws IOException{
        REST rest = new REST();
        
        String basePath = "path";
        
        BufferedReader br = new BufferedReader(new FileReader(basePath + "/orders.csv"));
        String line;
        
        rest.dbTranformAndCopy(identifier, true);
        //rest.dbCopy(identifier);
        rest.preprocess(identifier);
    }
    
    public REST(){
        client = ClientBuilder.newClient();
    }

    private void addChurnOrderInformation(String identifier, LabeledOrderInformation info) {
        Response response = client.target(targetUrl)
                .path(targetChurnAddOrderPath + identifier)
                .request()
                .put(Entity.entity(info, MediaType.APPLICATION_XML));
        System.out.println(response.toString());
    }
    
    private List<ChurnProbability> detectChurn(String identifier, UnlabeledOrders orders) {
        return client.target(targetUrl)
                .path(targetChurnDetection + identifier)
                .queryParam("unlabeled-orders", orders)
                .request(MediaType.APPLICATION_XML)
                .get(new GenericType<List<ChurnProbability>>(){});
    }
    
    private void resetChurn(String identifier){
        Response response = client.target(targetUrl)
                .path(targetChurnResetPath + identifier)
                .request()
                .post(Entity.text(""));
        System.out.println(response.toString());
    }
    
    private void addTransaction(String identifier, Transaction transaction) {
        Response response = client.target(targetUrl)
                .path(targetAddTransactionPath + identifier)
                .request()
                .put(Entity.entity(transaction, MediaType.APPLICATION_XML));
        System.out.println(response.toString());
    }
    
    private void addDbTransaction(String identifier, DbTransaction transaction){
        Response response = client.target(targetUrl)
                .path(targetAddDbTransactionPath + identifier)
                .request()
                .put(Entity.entity(transaction, MediaType.APPLICATION_XML));
        System.out.println(response.toString());
    }
    
    private void addDbMapper(String identifier, DbMapper mapper){
        Response response = client.target(targetUrl)
                .path(targetAddDbMapperPath + identifier)
                .request()
                .put(Entity.entity(mapper, MediaType.APPLICATION_XML));
        System.out.println(response.toString());
    }
    
    private void dbTranformAndCopy(String identifier, boolean normalizeNames){
        Response response = client.target(targetUrl)
                .path(targetDbTransofrmAndCopy + identifier)
                .queryParam("normalize-names", normalizeNames)
                .request()
                .post(Entity.text(""));
        System.out.println(response.toString());
    }
    
    private void dbCopy(String identifier){
        Response response = client.target(targetUrl)
                .path(targetDbCopy + identifier)
                .request()
                .post(Entity.text(""));
        System.out.println(response.toString());
    }
    
    private void preprocess(String identifier){
        Response response = client.target(targetUrl)
                .path(targetPreprocessPath + identifier)
                .request()
                .post(Entity.text(""));
        System.out.println(response.toString());
    }
    
    private void reset(String identifier){
        Response response = client.target(targetUrl)
                .path(targetResetPath + identifier)
                .request()
                .post(Entity.text(""));
        System.out.println(response.toString());
    }
    
    private List<AssociationRule> generate (String identifier, double minSupport, double minConfidende){
        return client.target(targetUrl)
                .path(targetGeneratePath + identifier)
                .queryParam("min-support", minSupport)
                .queryParam("min-confidence", minConfidende)
                .request(MediaType.APPLICATION_XML)
                .get(new GenericType<List<AssociationRule>>(){});
    }
    
}
