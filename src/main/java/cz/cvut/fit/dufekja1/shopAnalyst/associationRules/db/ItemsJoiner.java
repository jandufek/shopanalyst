/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.cvut.fit.dufekja1.shopAnalyst.associationRules.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jan Dufek
 */
public class ItemsJoiner {
    
    private boolean normalizeNames;
    private String transactionsIn;
    private String transactionsOut;
    private String mappersIn;
    private String mappersOut;
    
    public ItemsJoiner(boolean normalizeNames, String transactionsIn, String transactionsOut, String mappersIn, String mappersOut) {
        this.transactionsIn = transactionsIn;
        this.transactionsOut = transactionsOut;
        this.mappersIn = mappersIn;
        this.mappersOut = mappersOut;
        this.normalizeNames = normalizeNames;
    }
    
    /**
     * Items file has to have format
     * Item_id; "Item name"
     * 
     * Order file has to have format
     * order_id;item_id
     * 
     */
    public void transform () throws IOException{
        
        Map<Long, Long> mapper = processItems(mappersIn, mappersOut);
        PrintWriter writer;
        try (BufferedReader br = new BufferedReader(new FileReader(transactionsIn))) {
            writer = new PrintWriter(transactionsOut);
            String line;
            while ((line = br.readLine()) != null) {
               String[] parts = line.split(";");
               if (parts.length != 2) continue;
               if (parts[0].isEmpty() || parts[1].isEmpty()) continue;
               Long orderId = Long.valueOf(parts[0]);
               Long oldItemId = Long.valueOf(parts[1]);
               Long newItemId = mapper.get(oldItemId);
               if (newItemId != null){
                   writer.println(orderId + " " + newItemId);
               }
               
            }
           
        }
        writer.close();
    }
    
    private Map<Long, Long> processItems(String itemsIn, String itemsOut) throws IOException{
        Map<String, List<Long>> items = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(itemsIn))) {
            String line;
            
            while ((line = br.readLine()) != null) {
               String[] parts = line.split(";");
               if (parts.length != 2) continue;
               Long itemId = Long.valueOf(parts[0].trim());
               String itemName = parts[1].trim();
               if (itemName.isEmpty()) continue;
               String transformedName = getTransformedName(itemName);
               if (transformedName == null || transformedName.isEmpty()) continue;
               if (items.containsKey(transformedName)){
                   items.get(transformedName).add(itemId);
               } else {
                   List<Long> oldIds = new ArrayList<>();
                   oldIds.add(itemId);
                   items.put(transformedName, oldIds);
               }
            }
        }
        
        Map<Long, Long> mapper = new HashMap<>();
        try (PrintWriter writer = new PrintWriter(itemsOut)) {
            Long newId = 1L;
            for (Map.Entry<String, List<Long>> newItem : items.entrySet()){
                writer.println(newId + ";" + newItem.getKey());
                for (Long oldId : newItem.getValue()){
                    mapper.put(oldId, newId);
                }
                newId++;
            }
        }
        return mapper;
    }
    
    private Pattern pattern;
    private String getTransformedName(String name){        
        if (!normalizeNames){
            return name;
        }
        if (pattern == null){
            pattern = Pattern.compile("(\\D*)\\d*.*");
        }
        Matcher m = pattern.matcher(name);
        if (!m.matches()){
            System.out.println("Didnt matched: " + name);
            return null;
        }
        return  m.group(1).trim();
    }
    
    
}
