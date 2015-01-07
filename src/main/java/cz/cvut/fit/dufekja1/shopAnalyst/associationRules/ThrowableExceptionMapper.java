/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.cvut.fit.dufekja1.shopAnalyst.associationRules;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.glassfish.grizzly.utils.Exceptions;

/**
 *
 * @author Jan Dufek
 */
@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException ex) {
        ex.printStackTrace();
        return Response.status(400).entity(Exceptions.getStackTraceAsString(ex)).type("text/plain")
                .build();
    }

}
