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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


    @GetMapping("/overview/{month}")
    public ResponseEntity<Map> getOverView(@PathVariable("month") String month) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(authentication.getName());

        LocalDate theMonth = LocalDate.parse(month, ISO_DATE);
        LocalDate start = theMonth.withDayOfMonth(1);
        LocalDate end = theMonth.plusMonths(1).withDayOfMonth(1);

        List<Transaction> transactions = transactionRepository.findByUser(user.getId(), start, end);

        Map<String, Object> overview = new HashMap<>();
        List<Transaction> incomes = transactions.stream().filter(t -> t.getAmount() > 0).collect(Collectors.toList());
        List<Transaction> expenses = transactions.stream().filter(t -> t.getAmount() < 0).collect(Collectors.toList());

        double income = incomes.stream().mapToDouble(Transaction::getAmount).sum();
        double expense = expenses.stream().mapToDouble(Transaction::getAmount).sum();
        int totalTransactions = transactions.size();

        overview.put("income", income);
        overview.put("expense", expense);
        overview.put("totalTransactions", totalTransactions);
        overview.put("incomeVsExpense", groupByDate(transactions, incomes, expenses));
        overview.put("expenseByCategory", byCategory(expenses));
        return new ResponseEntity<>(overview, HttpStatus.OK);
    }

    private static List<String> excludes = Arrays.asList("Transfer");

    private Map<String, Object> byCategory(List<Transaction> expenses) {
        List<Map<String, Object>> amountByCategory = expenses.stream().map(t -> t.getCategory())
                .distinct()
                .filter(c -> !excludes.contains(c))
                .map(c -> {
                    double subTotal = expenses.stream().filter(e -> c.equals(e.getCategory())).mapToDouble(t -> t.getAmount()).sum();
                    Map<String, Object> datapoint = new HashMap();
                    datapoint.put("label", c);
                    datapoint.put("value", Long.valueOf(-(long) subTotal));
                    return datapoint;
                })
                .collect(Collectors.toList());
        Map<String, Object> series = new HashMap();
        series.put("series", amountByCategory);
        return series;

    }

    private Map<String, Object> groupByDate(List<Transaction> all, List<Transaction> incomes, List<Transaction> expenses) {
        List<LocalDate> dates = all.stream().map(t -> t.getDate()).distinct().sorted().collect(Collectors.toList());

        Map<String, Object> incomeVsExpenses = new HashMap<>();
        incomeVsExpenses.put("labels", dates.stream().map(ISO_DATE::format).collect(Collectors.toList()));
        incomeVsExpenses.put("series", Arrays.asList(
                toSeries(incomes, dates, "income", "column", "solid"),
                toSeries(expenses, dates, "expense", "area", "gradient")
        ));
        return incomeVsExpenses;
    }

    private Map<String, Object> toSeries(List<Transaction> transactions, List<LocalDate> dates, String name, String type, String fill) {
        Map<String, Object> series = new HashMap<>();
        List<Long> values = dates.stream()
                .map(d -> Long.valueOf(transactions.stream().filter(t -> d.equals(t.getDate())).mapToLong(t -> (long) t.getAmount()).sum()))
                .collect(Collectors.toList());
        series.put("name", name);
        series.put("type", type);
        series.put("fill", fill);
        series.put("data", values);
        return series;
    }


}
