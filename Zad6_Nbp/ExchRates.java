import pl.jrj.mdb.IMdbManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Michał Śliwa
 */
@Path("/exchangeRate")
public class ExchRates {

    private static final long serialVersionUID = 8323012551755541446L;

    /**
     * Funkcja obsluguje zapytanie get
     * nastepnie wywoluje funcke obliczania sredniwego kursu
     * i wysyla odpowiedz na strone
     * @param code kod waluty
     * @return sredni kurs
     */
    @GET
    @Path("/{code}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getRates(@PathParam("code") String code){

        String currId = "";
        try {
            Context ctx = new InitialContext();
            IMdbManager remote = (IMdbManager)ctx.
                    lookup("java:global/mdb-project/MdbManager!pl.jrj.mdb.IMdbManager");
            currId = remote.currencyId();
        } catch (NamingException e) {
            e.printStackTrace();
        }


        Rates rates = new Rates();
        double result =rates.getRates(code, currId);
        result = Math.round(result*10000);
        result = result /10000;
        return Response.ok().entity(String.format("%.4f", result)).build();
    }
}
