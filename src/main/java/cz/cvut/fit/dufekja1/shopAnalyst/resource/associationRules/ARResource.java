package cz.cvut.fit.dufekja1.shopAnalyst.resource.associationRules;

import cz.cvut.fit.dufekja1.shopAnalyst.associationRules.Generator;
import cz.cvut.fit.dufekja1.shopAnalyst.associationRules.bean.AssociationRule;
import cz.cvut.fit.dufekja1.shopAnalyst.Identifier;
import cz.cvut.fit.dufekja1.shopAnalyst.associationRules.bean.Transaction;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("ar")
public class ARResource extends Resource{

    protected static final String transactionsTmpStore = "transactionsTmp.txt";
    
    
    @PUT
    @Path("/add-transaction/{identifier}")
    @Consumes(MediaType.APPLICATION_XML)
    public void addTransaction(@PathParam("identifier") Identifier identifier, Transaction transaction) throws IOException {
        before(identifier);
        Writer output;
        List<String> transactionList = transaction.getItems();
        output = new BufferedWriter(new FileWriter(identifier.getWorkingDir() + "/" + transactionsTmpStore, true));
        for (int i = 0; i < transactionList.size(); i++) {
            output.append(formatString(transactionList.get(i)));
            output.append(";");
        }
        
        output.append(System.lineSeparator());
        output.close();
    }
    
    private void generateMapperFromTransactions(Identifier identifier) throws IOException {
        Map<String, Long> mapper = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(identifier.getWorkingDir() + "/" + transactionsTmpStore)); PrintWriter transactionsDatWriter = new PrintWriter(identifier.getWorkingDir() + "/" + transactionsDatStore)) {
            
            String line;
            Long lastGeneratedId = 0L;
            while ((line = br.readLine()) != null) {
               String[] items = line.split(";");
                for (int i = 0; i < items.length; i++) {
                    String item = items[i];
                    if (item == null || item.isEmpty()) continue;
                    Long itemId;
                    if (mapper.containsKey(item)){
                        itemId = mapper.get(item);
                    } else {
                        itemId = ++ lastGeneratedId;
                        mapper.put(item, itemId);
                    }
                    transactionsDatWriter.append(itemId + "");
                    if (i + 1 < items.length){
                        transactionsDatWriter.append(", ");
                    }
                }
                transactionsDatWriter.println();
            }
        }
        
        try (PrintWriter writer = new PrintWriter(identifier.getWorkingDir() + "/" + mappersDatStore)) {
            for (Map.Entry<String, Long> m : mapper.entrySet()) {
                writer.println(m.getValue() + ";" + m.getKey());
            }
        }
    }
    
    @POST
    @Path("/preprocess/{identifier}")
    public void preprocess(@PathParam("identifier") Identifier identifier) throws IOException {
        before(identifier);
        System.out.println("Start of preprocessing");
        if (!(new File(identifier.getWorkingDir() + "/" + transactionsDatStore).exists())){
            generateMapperFromTransactions(identifier);
        }
        Generator generator = new Generator(identifier);
        generator.preprocess();
    }
    
    @GET
    @Path("/generate/{identifier}")
    @Produces(MediaType.APPLICATION_XML)
    public List<AssociationRule> generate(@PathParam("identifier") Identifier identifier, 
                                          @DefaultValue("0.01") @QueryParam("min-support") double minSupport,
                                          @DefaultValue("0.01") @QueryParam("min-confidence") double minConfidence) throws IOException {
        before(identifier);
        System.out.println("Generating AR with min support " + minSupport + " and min confidence " + minConfidence);
        Generator generator = new Generator(identifier);
        return generator.generate(minSupport, minConfidence);
    }
    
    @POST
    @Path("/reset/{identifier}")
    public void reset(@PathParam("identifier") Identifier identifier) throws IOException {
        String dir = identifier.getWorkingDir();
        File dirFile = new File(dir);
        if (dirFile.exists()){
            deleteDirectory(dirFile);
        }
    }
    
    public static boolean deleteDirectory(File directory) {
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(null!=files){
                for(int i=0; i<files.length; i++) {
                    if(files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    }
                    else {
                        files[i].delete();
                    }
                }
            }
        }
        return(directory.delete());
    }
    
    
}
