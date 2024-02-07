package nz.co.mymoney.transaction;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_DATE;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/{month}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable ("month") String month) {
        LocalDate theMonth = LocalDate.parse(month, ISO_DATE);
        LocalDate start = theMonth.withDayOfMonth(1);
        LocalDate end = theMonth.plusMonths(1).withDayOfMonth(1);

        //todo get user from session
        List<Transaction> transactions = transactionRepository.findByUser(0, start, end);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
