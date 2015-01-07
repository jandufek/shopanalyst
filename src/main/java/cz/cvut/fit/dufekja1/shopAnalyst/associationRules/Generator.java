/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.cvut.fit.dufekja1.shopAnalyst.associationRules;

import cz.cvut.fit.dufekja1.shopAnalyst.associationRules.bean.AssociationRule;
import cz.cvut.fit.dufekja1.shopAnalyst.Identifier;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author Jan Dufek
 */
public class Generator {
    
    private Identifier identifier;

    public Generator(Identifier identifier) {
        this.identifier = identifier;
    }   
    
    public List<AssociationRule> generate (Double minSupport, Double minConfidence){
        try {
            String resultFile = identifier.getWorkingDir() + "/results.txt";
            String resultCSVFile = identifier.getWorkingDir() + "/results.csv";
            generateResultFile(resultFile, minSupport, minConfidence);
            OutputParser op = new OutputParser();
            op.parse(resultFile);
            op.exportToCsv(resultCSVFile, 152730);
            System.out.println("Rules generated!");
            return op.getRules();
        } catch (Exception ex ){
            throw new WebApplicationException(ex);
        }
        
    }
    
    public void preprocess (){
        try {
            String hadoopItemsetName = identifier.getIdentifier() + "_dataset.dat";
            removeFileFromHadoop(hadoopItemsetName);
            putFileToHadoop(hadoopItemsetName);
            startMahoutProcessing(hadoopItemsetName);
            extractDataFiles();           
        } catch (IOException ex){
            ex.printStackTrace();
            throw new WebApplicationException(ex);
        }       
        
    }
    
    private void generateResultFile (String resultFile, Double minSupport, Double minConfidence) throws IOException{
        String scriptName = "./starter.sh";
        ProcessBuilder pb = new ProcessBuilder(scriptName, identifier.getWorkingDir(), minSupport.toString(), minConfidence.toString(), resultFile);
        pb.directory(new File(Identifier.HADOOP_DIR + "/Mahout"));        
        Process p = pb.start();
        handleProcess(p);
    }
    
    private void extractDataFiles() throws IOException{
        String scriptName = "./extract_files.sh";
        ProcessBuilder pb = new ProcessBuilder(scriptName, identifier.getWorkingDir());
        pb.directory(new File(Identifier.HADOOP_DIR));
        Process p = pb.start();
        handleProcess(p);
    }
    
    private void startMahoutProcessing(String hadoopItemsetName) throws IOException{
        String scriptName = "./dispatcher.sh";
        ProcessBuilder pb = new ProcessBuilder(scriptName, "mahout", hadoopItemsetName);
        pb.directory(new File(Identifier.HADOOP_DIR));
        Process p = pb.start();
        handleProcess(p);
    }
    
    private void removeFileFromHadoop(String hadoopItemsetName) throws IOException{
        String scriptName = "./dispatcher.sh";
        ProcessBuilder pb = new ProcessBuilder(scriptName, "hadoop-rm", hadoopItemsetName);
        pb.directory(new File(Identifier.HADOOP_DIR));
        Process p = pb.start();
        handleProcess(p);
    }
    
    private void putFileToHadoop(String hadoopItemsetName) throws IOException{
        String scriptName = "./dispatcher.sh";
        ProcessBuilder pb = new ProcessBuilder(scriptName, "hadoop-put", hadoopItemsetName, identifier.getWorkingDir() + "/*.dat");
        pb.directory(new File(Identifier.HADOOP_DIR));
        Process p = pb.start();
        handleProcess(p);
    }
    
    public void joinOrdersToItemset(String from, String to) throws IOException{
        String scriptName = "./joined_orders_to_itemset.sh";
        ProcessBuilder pb = new ProcessBuilder(scriptName, from, to);
        pb.directory(new File(Identifier.DATA_DIR));
        Process p = pb.start();
        handleProcess(p);
    }
    
    private void handleProcess(Process p) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println ("Stderr: " + line);
        }
        try {
            p.waitFor();
        } catch (InterruptedException ex){
            throw new WebApplicationException(ex);
        }
        
    }
    
    
    
}
