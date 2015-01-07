/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.cvut.fit.dufekja1.shopAnalyst.churnDetection;

import com.rapidminer.RapidMiner;
import com.rapidminer.RepositoryProcessLocation;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.repository.MalformedRepositoryLocationException;
import com.rapidminer.repository.RepositoryLocation;
import com.rapidminer.tools.XMLException;
import java.io.IOException;
import com.rapidminer.Process;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.io.CSVExampleSetWriter;
import com.rapidminer.operator.nio.CSVExampleSource;

/**
 *
 * @author Jan Dufek
 */
public class Detector {
    
    private static final String UNLABELED_OPERATOR_NAME = "Unlabeled CSV";
    private static final String LABELED_OPERATOR_NAME = "Labeled CSV";
    private static final String CSV_FILE_OPERATOR_PARAMETER = "csv_file";
    
    public void detect(String unlabeledCSVFilePath, String labeledCSVFilePath, String outputFilePath){
        try {
            RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.EMBEDDED_WITHOUT_UI);
            RapidMiner.init();
            RepositoryLocation loc = new RepositoryLocation("//Local Repository/data/churn_using");
            Process process = new RepositoryProcessLocation(loc).load(null);
            for (Operator operator : process.getAllOperators()){
                if (operator instanceof CSVExampleSource){
                    if (operator.getName().equalsIgnoreCase(UNLABELED_OPERATOR_NAME)){
                        operator.setParameter(CSV_FILE_OPERATOR_PARAMETER, unlabeledCSVFilePath);
                    } else if (operator.getName().equalsIgnoreCase(LABELED_OPERATOR_NAME)){
                        operator.setParameter(CSV_FILE_OPERATOR_PARAMETER, labeledCSVFilePath);
                    }
                } else if (operator instanceof CSVExampleSetWriter){
                    operator.setParameter(CSV_FILE_OPERATOR_PARAMETER, outputFilePath);
                }
            }
            try {
                IOContainer run = process.run();
                IOObject[] ioObjects = run.getIOObjects();
                int a = ioObjects.length;
            } catch (OperatorException ex) {
                ex.printStackTrace();
            }
        } catch (IOException | MalformedRepositoryLocationException | XMLException ex){
            ex.printStackTrace();
        }
        
    }
    
}
