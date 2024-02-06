package nz.co.harbour.jay.transaction;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/money")
public class TransactionResource {
    @Inject
    private TransactionDao transactionDao;


    @Path("/{year}/{month}/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transaction> getTransactions(
            @PathParam("year") int year,
            @PathParam("month") int month
    ) {
        return transactionDao.getTransactions(year,month);
    }
}
