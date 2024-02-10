package nz.co.mymoney.transaction;

import nz.co.mymoney.user.User;
import nz.co.mymoney.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_DATE;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserRepository userRepository;
    @GetMapping("/{month}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable("month") String month) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(authentication.getName());

        LocalDate theMonth = LocalDate.parse(month, ISO_DATE);
        LocalDate start = theMonth.withDayOfMonth(1);
        LocalDate end = theMonth.plusMonths(1).withDayOfMonth(1);

        //todo get user from session
        List<Transaction> transactions = transactionRepository.findByUser(user.getId(), start, end);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
