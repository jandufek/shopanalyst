/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.cvut.fit.dufekja1.shopAnalyst;

/**
 *
 * @author Jan Dufek
 */
public class Identifier {
    
    public static String DATA_DIR;
    public static String HADOOP_DIR;
    private String identifier;

    public Identifier(String identifier) {
        this.identifier = identifier;
    }
    
    static {
        DATA_DIR = System.getProperty("user.dir") + "/data";
        HADOOP_DIR = System.getProperty("user.dir") + "/hadoop";
    }
    
    public String getWorkingDir (){
        return DATA_DIR + "/" + identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
    
    public boolean checkIdentifierFormat(){
        if (identifier == null){
            return false;
        }
        return identifier.matches("[A-Za-z0-9_]{3,}");
    }
    
}
