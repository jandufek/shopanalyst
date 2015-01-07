/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.cvut.fit.dufekja1.shopAnalyst.churnDetection.bean;

import java.util.ArrayList;
import java.util.List;
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
public class UnlabeledOrders {
    
    @XmlElement
    private List<UnlabeledOrderInformation> orders = new ArrayList<>();

    public List<UnlabeledOrderInformation> getOrders() {
        return orders;
    }

    public void setOrders(List<UnlabeledOrderInformation> orders) {
        this.orders = orders;
    }
    
    public void addUnlabeledOrder(UnlabeledOrderInformation o){
        orders.add(o);
    }
    
}
