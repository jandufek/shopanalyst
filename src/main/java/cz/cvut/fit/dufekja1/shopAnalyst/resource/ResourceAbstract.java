/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.cvut.fit.dufekja1.shopAnalyst.resource;

import cz.cvut.fit.dufekja1.shopAnalyst.Identifier;
import java.io.File;
import java.io.IOException;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author Jan Dufek
 */
public abstract class ResourceAbstract {
    
    protected void before(Identifier identifier) throws IOException {
        if (!identifier.checkIdentifierFormat()){
            throw new WebApplicationException("The identifier has to be at least 3 characters long and contains only alphanumeric characters or underscore.");
        }
        String dir = identifier.getWorkingDir();
        File dirFile = new File(dir);
        if (!dirFile.exists()){
            dirFile.mkdir();
        }
    }
    
    protected void deleteIfExists(String filename){
        File file = new File(filename);
        if (file.exists()){
            file.delete();
        }
    }
    
    protected boolean fileExists(String filename){
        File file = new File(filename);
        return file.exists();
    }
    
    protected String formatString(String string){
        return string.replaceAll(";", "").trim();
    }
}
