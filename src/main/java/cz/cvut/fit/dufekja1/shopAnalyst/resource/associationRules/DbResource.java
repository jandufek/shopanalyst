/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.cvut.fit.dufekja1.shopAnalyst.resource.associationRules;

import cz.cvut.fit.dufekja1.shopAnalyst.associationRules.Generator;
import cz.cvut.fit.dufekja1.shopAnalyst.Identifier;
import cz.cvut.fit.dufekja1.shopAnalyst.associationRules.bean.DbMapper;
import cz.cvut.fit.dufekja1.shopAnalyst.associationRules.bean.DbTransaction;
import cz.cvut.fit.dufekja1.shopAnalyst.associationRules.db.ItemsJoiner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Jan Dufek
 */
@Path("ar/db")
public class DbResource extends Resource {
    
    private static final String transactionsCSVStore = "db_transactions.csv";
    private static final String mappersCSVStore = "db_mappers.csv";
    
    @PUT
    @Path("/add-transaction/{identifier}")
    @Consumes(MediaType.APPLICATION_XML)
    public void addTransaction(@PathParam("identifier") Identifier identifier, DbTransaction transaction) throws IOException {
        before(identifier);
        System.out.println("Transaction recieved");
        Writer output;
        output = new BufferedWriter(new FileWriter(identifier.getWorkingDir() + "/" + transactionsCSVStore, true));
        output.append(transaction.toString());
        output.append(System.lineSeparator());
        output.close();
    }
    
    @PUT
    @Path("/add-mapper/{identifier}")
    @Consumes(MediaType.APPLICATION_XML)
    public void addMapper(@PathParam("identifier") Identifier identifier, DbMapper mapper) throws IOException {
        before(identifier);
        System.out.println("Mapper recieved");
        Writer output;
        output = new BufferedWriter(new FileWriter(identifier.getWorkingDir() + "/" + mappersCSVStore, true));
        output.append(mapper.toString());
        output.append(System.lineSeparator());
        output.close();
    }
    
    @POST
    @Path("/transform-and-copy/{identifier}")
    public void transformAndCopy(@PathParam("identifier") Identifier identifier, 
                                  @DefaultValue("false") @QueryParam("normalize-names")boolean normalizeNames) throws IOException {
        before(identifier);
        String transactionsPath = identifier.getWorkingDir() + "/" + transactionsCSVStore;
        System.out.println("Start of transforming");
        String mappersPath = identifier.getWorkingDir() + "/" + mappersCSVStore;
        String transformedTransactionsPath = transactionsPath + ".transformed";
        String transformedMappersPath = mappersPath + ".transformed";
        ItemsJoiner ij = new ItemsJoiner(normalizeNames,
                                         transactionsPath, 
                                         transformedTransactionsPath,
                                         mappersPath, 
                                         transformedMappersPath);
        ij.transform();
        
        generateDatFile(identifier, transformedTransactionsPath);
        copy(transformedMappersPath, identifier.getWorkingDir() + "/" + mappersDatStore);
    }
    
    @POST
    @Path("/copy/{identifier}")
    public void copyDbTransactions(@PathParam("identifier") Identifier identifier) throws IOException {
        before(identifier);
        String transactionsPath = identifier.getWorkingDir() + "/" + transactionsCSVStore;
        String mappersPath = identifier.getWorkingDir() + "/" + mappersCSVStore;
        
        generateDatFile(identifier, transactionsPath);
        copy(mappersPath, identifier.getWorkingDir() + "/" + mappersDatStore);
    }
    
    private void generateDatFile(Identifier identifier, String from) throws IOException{
        String datFile = identifier.getWorkingDir() + "/" + transactionsDatStore;
        deleteIfExists(datFile);
        Generator gen = new Generator(identifier);
        gen.joinOrdersToItemset(from, datFile);
    }
    
    private void copy (String from, String to) throws IOException{
        deleteIfExists(to);        
        Files.copy(new File(from).toPath(), new File(to).toPath());
    }
}
