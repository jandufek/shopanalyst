/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.cvut.fit.dufekja1.shopAnalyst.associationRules.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jan Dufek
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DbTransaction {
    
    @XmlElement
    private Long transactionId;
    
    @XmlElement
    private Long itemId;
    
    private static final String transactionSeparator = ";";

    public DbTransaction() {
    }

    public DbTransaction(Long transactionId, Long itemId) {
        this.transactionId = transactionId;
        this.itemId = itemId;
    }
    
    public DbTransaction(String transactionString){
        String[] split = transactionString.split(transactionSeparator);
        transactionId = Long.valueOf(split[0]);
        itemId = Long.valueOf(split[1]);
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return transactionId + transactionSeparator + itemId;
    }
    
    
    
    
    
}
