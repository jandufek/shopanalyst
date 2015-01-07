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
public class DbMapper {
    
    @XmlElement
    private Long itemId;
    @XmlElement
    private String name;
    
    private static final String mapperSeparator = ";";

    public DbMapper() {
    }

    public DbMapper(Long itemId, String name) {
        this.itemId = itemId;
        this.name = name;
    }
    
    public DbMapper(String mapperString){
        String[] split = mapperString.split(mapperSeparator);
        itemId = Long.valueOf(split[0]);
        name = split[1];
    }

    @Override
    public String toString() {
        return itemId + mapperSeparator + name;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
    
}
