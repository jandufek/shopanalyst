/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.cvut.fit.dufekja1.shopAnalyst.associationRules;

import cz.cvut.fit.dufekja1.shopAnalyst.associationRules.bean.AssociationRule;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author Jan Dufek
 */
public class OutputParser {
    
    
    
    private List<AssociationRule> rules;
    
    public List<AssociationRule> parse(String inputFile) throws IOException{
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            rules = new ArrayList<>();
            Pattern pattern = Pattern.compile("(.*) => ([^;]*); occ=([^;]*); supp=([^;]*); conf=([^;]*)(; lift=([^;]*); conviction=([^;]*))?");
            while ((line = br.readLine()) != null) {
               Matcher m = pattern.matcher(line);
               if (m.matches()){
                   String fromString = m.group(1);
                   String[] fromSplited = fromString.split(";");
                   List<String> from = new ArrayList<>();
                   for (String fromPart : fromSplited) {
                       from.add(fromPart.replace("\"", "").trim());
                   }
                   if (from.get(0).isEmpty()){
                       continue;
                   }
                   AssociationRule ar;
                   if (m.group(6) == null){
                       ar = new AssociationRule(from, m.group(2).trim(), Integer.valueOf(m.group(3).trim()),
                           Double.valueOf(m.group(4).trim()), Double.valueOf(m.group(5).trim()), 0.0, 0.0);
                   } else {
                       ar = new AssociationRule(from, m.group(2).trim(), Integer.valueOf(m.group(3).trim()),
                           Double.valueOf(m.group(4).trim()), Double.valueOf(m.group(5).trim()),
                           Double.valueOf(m.group(7).trim()), Double.valueOf(m.group(8).trim()));
                   }
                   
                   
                   rules.add(ar);
               }
            }
        }
        filterIt();
        Collections.sort(rules);
        return rules;
        
    }
    
    private void filterIt(){
        // odstraneni duplicit typu A=>B, B=>A, pokud to najdu, tak necham tu s vetsi conviction
        List<AssociationRule> newList = new ArrayList<>();
        for (int i = 0; i < rules.size(); i++){
            AssociationRule ar1 = rules.get(i);

            if (ar1.getFrom().size() == 1){
                boolean add = true;
                for (int j = 0; j < rules.size(); j++){
                    if (i == j) continue;
                    AssociationRule ar2 = rules.get(j);
                    if (ar2.getFrom().size() == 1 && ar2.getFrom().get(0).equals(ar1.getTo()) && ar2.getTo().equals(ar1.getFrom().get(0))){
                        // nalezena duplicita, resim pouze pokud je ar1 pred ar2
                        if (i < j) {
                            if (ar1.getConviction() < ar2.getConviction()){
                                newList.add(ar2);
                            } else {
                                newList.add(ar1);
                            }
                        } 
                        add = false;
                    }
                }
                if (add){
                    newList.add(ar1);
                }
            } else if (ar1.getFrom().size() > 1){
                newList.add(ar1);
            }

        }
        rules = newList;
    }
    public void exportToCsv(String outputFile, int numberOfTransactions)throws IOException{
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            writer.println("Pokud si objedná;Tak také;S pravděpodobností;U kolika cca zákazníků se vyskytlo; Support; Konfidence; Lift; Konvikce");
            for (AssociationRule ar : rules) {
                long pst = Math.round(ar.getConfidence() * 100);
                long count = Math.round((double)numberOfTransactions * ar.getSupport());
                writer.println(ar.getFromLikeString() + ";" + ar.getTo() + ";" + pst + " %;" + count + ";" + ar.getSupport() + ";" + ar.getConfidence() + ";" + ar.getLift() + ";" + ar.getConviction());
            }
        }
    }

    public List<AssociationRule> getRules() {
        return rules;
    }
    
}


