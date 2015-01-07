/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.cvut.fit.dufekja1.shopAnalyst.churnDetection.bean;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jan Dufek
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LabeledOrderInformation {
    
    /**
     * how many orders the customer has been made
     */
    private int ordersCountBefore;
    
    /**
     * how many days the next order has been processed
     * if there is no one, it is number of days to today
     */
    private int daysToNextOrder;
    
    
    private BigDecimal price;
    
    /**
     * what is sum of all orders processed before
     * (first has this value set to zero)
     */
    private BigDecimal sumPricesBefore;
    
    private boolean customerChurned;
    
    public static final String CSVHeader = "\"orders_count_before\";\"days_to_next_order\";\"price\";\"sum_prices_before\";\"churn\"";
    
    private static final String CSVSpliter = ";";
    
    public LabeledOrderInformation() {
    }
    /**
     * 
     * @param ordersCountBefore how many orders the customer has been made
     * @param daysToNextOrder how many days the next order has been processed, if there is no one, it is number of days to today
     * @param price
     * @param sumPricesBefore what is sum of all orders processed before, (first has this value set to zero)
     * @param customerChurned set true or false if customer churned
     */
    public LabeledOrderInformation(int ordersCountBefore, int daysToNextOrder, BigDecimal price, BigDecimal sumPricesBefore, boolean customerChurned) {
        this.customerChurned = customerChurned;
        this.ordersCountBefore = ordersCountBefore;
        this.daysToNextOrder = daysToNextOrder;
        this.price = price;
        this.sumPricesBefore = sumPricesBefore;
    }
    
    public String toCSVString(){
        String s = ordersCountBefore + CSVSpliter + daysToNextOrder + CSVSpliter + price + CSVSpliter + sumPricesBefore + CSVSpliter;
        if (customerChurned){
            return s + "t";
        } else {
            return s + "f";
        }
    }

    public int getOrdersCountBefore() {
        return ordersCountBefore;
    }

    public void setOrdersCountBefore(int ordersCountBefore) {
        this.ordersCountBefore = ordersCountBefore;
    }

    public int getDaysToNextOrder() {
        return daysToNextOrder;
    }

    public void setDaysToNextOrder(int daysToNextOrder) {
        this.daysToNextOrder = daysToNextOrder;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSumPricesBefore() {
        return sumPricesBefore;
    }

    public void setSumPricesBefore(BigDecimal sumPricesBefore) {
        this.sumPricesBefore = sumPricesBefore;
    }

    public boolean isCustomerChurned() {
        return customerChurned;
    }

    public void setCustomerChurned(boolean customerChurned) {
        this.customerChurned = customerChurned;
    }
    
    
    
}
