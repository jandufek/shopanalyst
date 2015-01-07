/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.cvut.fit.dufekja1.shopAnalyst.associationRules.bean;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jan Dufek
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AssociationRule implements Comparable<AssociationRule>{
    private List<String> from;
    private String to;
    private Double support;
    private Double confidence;
    private Double lift;
    private Double conviction;
    private Integer occurence;

    public AssociationRule(List<String> from, String to, Integer occurence, Double support, Double confidence, Double lift, Double conviction) {
        this.from = from;
        this.to = to;
        this.support = support;
        this.confidence = confidence;
        this.lift = lift;
        this.conviction = conviction;
        this.occurence = occurence;
    }
    
    public AssociationRule(String from, String to, Integer occurence, Double support, Double confidence, Double lift, Double conviction) {
        this.from = new ArrayList<>();
        this.from.add(from);
        this.to = to;
        this.support = support;
        this.confidence = confidence;
        this.lift = lift;
        this.conviction = conviction;
        this.occurence = occurence;
    }

    public AssociationRule() {
    }
    
    

    @Override
    public int compareTo(AssociationRule t) {
        if (confidence < t.confidence) return 1;
        if (confidence > t.confidence) return -1;
        return 0;
    }
    
    public String getFromLikeString(){
        boolean first = true;
        String ret = "";
        for (String item : from) {
            if (first){
                first = false;
            } else {
                ret += ", ";
            }
            ret += item.toString();
        }
        return ret;
    }

    public List<String> getFrom() {
        return from;
    }

    public void setFrom(List<String> from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Double getSupport() {
        return support;
    }

    public void setSupport(Double support) {
        this.support = support;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Double getLift() {
        return lift;
    }

    public void setLift(Double lift) {
        this.lift = lift;
    }

    public Double getConviction() {
        return conviction;
    }

    public void setConviction(Double conviction) {
        this.conviction = conviction;
    }


    public Integer getOccurence() {
        return occurence;
    }

    public void setOccurence(Integer occurence) {
        this.occurence = occurence;
    }
    
    
    
    
    
}
