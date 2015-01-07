/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.cvut.fit.dufekja1.shopAnalyst.churnDetection.bean;

import java.sql.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderInformation implements Comparable<OrderInformation>{
    
    private static final String CSV_SPLITER = ";";
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    private Long customerId;
    private String created;
    private Double value;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
    
    public DateTime getCreatedDateTime(){
        return DateTime.parse(created, DateTimeFormat.forPattern(DATE_PATTERN));
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return customerId + ";" + created.toString() + ";" + value;
    }
    
    public static OrderInformation parseFromCsv(String csvLine){
        OrderInformation oi = new OrderInformation();
        String[] split = csvLine.split(CSV_SPLITER);
        oi.setCustomerId(Long.valueOf(split[0]));
        oi.setCreated(split[1]);
        oi.setValue(Double.valueOf(split[2]));
        
        return oi;
    }

    @Override
    public int compareTo(OrderInformation t) {
        return created.compareTo(t.created);
    }
    
    
    
}
