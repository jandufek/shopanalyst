/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.cvut.fit.dufekja1.shopAnalyst.churnDetection.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jan Dufek
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ChurnProbability implements Comparable<ChurnProbability>{
    
    private Long orderId;
    
    private Double probabilityOfChurn;
    
    /**
     * which column in CSV output has order_id
     */
    private static final int ORDER_ID_COLUMN_NUMBER = 5;
    
    private static final int TRUE_CONFIDENCE_COLUMN_NUMBER = 7;
    
    private static final String CSVSpliter = ";";

    public static ChurnProbability parseFromCsv(String csvLine){
        String[] split = csvLine.split(CSVSpliter);
        ChurnProbability o = new ChurnProbability();
        o.setOrderId(Math.round(Double.valueOf(split[ORDER_ID_COLUMN_NUMBER - 1])));
        o.setProbabilityOfChurn(Double.valueOf(split[TRUE_CONFIDENCE_COLUMN_NUMBER - 1]));
        return o;
    }
    
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public double getProbabilityOfChurn() {
        return probabilityOfChurn;
    }

    public void setProbabilityOfChurn(double probabilityOfChurn) {
        this.probabilityOfChurn = probabilityOfChurn;
    }

    @Override
    public int compareTo(ChurnProbability t) {
        if (probabilityOfChurn < t.probabilityOfChurn){
            return 1;
        } else if (probabilityOfChurn > t.probabilityOfChurn){
            return -1;
        }
        return 0;
    }
    
    
    
}
