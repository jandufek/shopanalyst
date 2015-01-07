/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.cvut.fit.dufekja1.shopAnalyst.resource.churnDetection;

import cz.cvut.fit.dufekja1.shopAnalyst.Identifier;
import cz.cvut.fit.dufekja1.shopAnalyst.churnDetection.Detector;
import cz.cvut.fit.dufekja1.shopAnalyst.churnDetection.bean.ChurnProbability;
import cz.cvut.fit.dufekja1.shopAnalyst.churnDetection.bean.LabeledOrderInformation;
import cz.cvut.fit.dufekja1.shopAnalyst.churnDetection.bean.OrderInformation;
import cz.cvut.fit.dufekja1.shopAnalyst.churnDetection.bean.UnlabeledOrderInformation;
import cz.cvut.fit.dufekja1.shopAnalyst.churnDetection.bean.UnlabeledOrders;
import cz.cvut.fit.dufekja1.shopAnalyst.resource.ResourceAbstract;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 *
 * @author Jan Dufek
 */
@Path("churn")
public class Resource extends ResourceAbstract{
    
    protected static final String churnLabeledFile = "churnLabeled.csv";
    protected static final String churnUnlabeledFile = "churnUnlabeled.csv";
    protected static final String churnOutputFile = "churnOutput.csv";
    
    protected static final String churnOrderInformationsFile = "churnOrderInformations.csv";
    
    @PUT
    @Path("/add-labeled-information/{identifier}")
    @Consumes(MediaType.APPLICATION_XML)
    public void addLabeledInformation(@PathParam("identifier") Identifier identifier, LabeledOrderInformation orderInformation) throws IOException {
        before(identifier);
        addLabeledInformationToFile(identifier, orderInformation);
    }
    
    private void addLabeledInformationToFile(Identifier identifier, LabeledOrderInformation orderInformation) throws IOException{
        Writer output;
        File file = new File(identifier.getWorkingDir() + "/" + churnLabeledFile);
        if (!file.exists()){
            makeCsvFile(LabeledOrderInformation.CSVHeader, file);
        }
        output = new BufferedWriter(new FileWriter(file, true));
        output.append(orderInformation.toCSVString());
        output.append(System.lineSeparator());
        output.close();
    }
    
    @PUT
    @Path("/add-order-information/{identifier}")
    @Consumes(MediaType.APPLICATION_XML)
    public void addOrderInformation(@PathParam("identifier") Identifier identifier, OrderInformation orderInformation) throws IOException {
        before(identifier);
        Writer output;
        System.out.println("Order information recieved");
        File file = new File(identifier.getWorkingDir() + "/" + churnOrderInformationsFile);
        if (!file.exists()){
            file.createNewFile();
        }
        output = new BufferedWriter(new FileWriter(file, true));
        output.append(orderInformation.toString());
        output.append(System.lineSeparator());
        output.close();
    }
    
    @POST
    @Path("/reset/{identifier}")
    public void reset(@PathParam("identifier") Identifier identifier) throws IOException {
        String dir = identifier.getWorkingDir();
        deleteIfExists(dir + "/" + churnLabeledFile);
    }
    
    @GET
    @Path("/detect-churn/{identifier}")
    @Produces(MediaType.APPLICATION_XML)
    public List<ChurnProbability> detectChurn(@PathParam("identifier") Identifier identifier) throws IOException {
        before(identifier);
        generateData(identifier);
        List<ChurnProbability> list = getProbabilites(identifier);
        
        Map<Long, ChurnProbability> map = new HashMap<>();
        for (ChurnProbability p : list){
            map.put(p.getOrderId(), p);
        }
        list.clear();
        list.addAll(map.values());
        Collections.sort(list);
        return list;
    }
    
    
    
    @GET
    @Path("/detect-churn-for-unlabeled/{identifier}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public List<ChurnProbability> detectChurnForUnlabeled(@PathParam("identifier") Identifier identifier, @QueryParam("unlabeled-orders") UnlabeledOrders unlabeledOrders) throws IOException {
        before(identifier);
        addUnlabeledOrderInformationsToFile(identifier, unlabeledOrders);
        
        return getProbabilites(identifier);
        
    }
    
    private void addUnlabeledOrderInformationsToFile(Identifier identifier, UnlabeledOrders unlabeledOrders) throws IOException{
         Writer output;
        File file = new File(identifier.getWorkingDir() + "/" + churnUnlabeledFile);
        makeCsvFile(UnlabeledOrderInformation.CSVHeader, file);
        
        output = new BufferedWriter(new FileWriter(file, true));
        for (UnlabeledOrderInformation o : unlabeledOrders.getOrders()){
            output.append(o.toCSVString());
            output.append(System.lineSeparator());
        }
        output.close();
    }
    
    private void addUnlabeledOrderInformationToFile(Identifier identifier, UnlabeledOrderInformation o)throws IOException{
         Writer output;
        File file = new File(identifier.getWorkingDir() + "/" + churnUnlabeledFile);
        if (!file.exists()){
            makeCsvFile(UnlabeledOrderInformation.CSVHeader, file);
        }       
        
        output = new BufferedWriter(new FileWriter(file, true));
        output.append(o.toCSVString());
        output.append(System.lineSeparator());
        output.close();
    }
    
    private List<ChurnProbability> getProbabilites(Identifier identifier) throws IOException{
        String resultsPath = identifier.getWorkingDir() + "/" + churnOutputFile;
        deleteIfExists(resultsPath);
        
        Detector detector = new Detector();
        detector.detect(identifier.getWorkingDir() + "/" + churnUnlabeledFile, identifier.getWorkingDir() + "/" + churnLabeledFile, resultsPath);
        
        try (BufferedReader br = new BufferedReader(new FileReader(resultsPath))) {
            
            String line;
            List<ChurnProbability> list = new ArrayList<>();
            br.readLine();
            while ((line = br.readLine()) != null) {
               ChurnProbability churnProbability = ChurnProbability.parseFromCsv(line);
               
               list.add(churnProbability);
            }
            return list;
        }
    }

    private void makeCsvFile(String header, File file) throws IOException {
        Writer output;

        output = new BufferedWriter(new FileWriter(file));
        output.append(header);
        output.append(System.lineSeparator());
        output.close();
    }

    private void generateData(Identifier identifier) throws IOException{
        DateTime today = new DateTime();
        
        // pul roku
        DateTime vestingDate = today.minusMonths(6);
        
        String infoFilePath = identifier.getWorkingDir() + "/" + churnOrderInformationsFile;
        Map<Long, List<OrderInformation>> map = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(infoFilePath))) {
            
            String line;
            
            while ((line = br.readLine()) != null) {
               OrderInformation oi = OrderInformation.parseFromCsv(line);
               if (!map.containsKey(oi.getCustomerId())){
                   map.put(oi.getCustomerId(), new ArrayList<OrderInformation>());
               }
               map.get(oi.getCustomerId()).add(oi);
            }
            
        }
        
        for (Map.Entry<Long, List<OrderInformation>> entry : map.entrySet()){
            List<OrderInformation> orders = entry.getValue();
            Collections.sort(orders);
            
            int count = 0;
            int ordersCount = orders.size();
            BigDecimal sum = BigDecimal.ZERO;
            for (OrderInformation o : orders){
                boolean isLast = (count + 1) >= ordersCount;
                DateTime nextOrderTime = (isLast) ? today : orders.get(count + 1).getCreatedDateTime();
                if (o.getCreatedDateTime().isBefore(vestingDate)){
                    
                    LabeledOrderInformation loi = new LabeledOrderInformation();
                    loi.setCustomerChurned(isLast);
                    loi.setDaysToNextOrder(Days.daysBetween(o.getCreatedDateTime(), nextOrderTime).getDays());
                    loi.setOrdersCountBefore(count);
                    loi.setPrice(new BigDecimal(o.getValue()));
                    loi.setSumPricesBefore(sum);
                    addLabeledInformationToFile(identifier, loi);
                } else {
                    
                    UnlabeledOrderInformation uoi = new UnlabeledOrderInformation();
                    uoi.setDaysToNextOrder(Days.daysBetween(o.getCreatedDateTime(), nextOrderTime).getDays());
                    uoi.setOrderId(o.getCustomerId());
                    uoi.setOrdersCountBefore(count);
                    uoi.setPrice(new BigDecimal(o.getValue()));
                    uoi.setSumPricesBefore(sum);
                    addUnlabeledOrderInformationToFile(identifier, uoi);
                }
                
                count++;
                sum = sum.add(new BigDecimal(o.getValue()));
            }
            
        }
        
    }
}
