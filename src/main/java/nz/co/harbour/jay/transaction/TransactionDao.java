package nz.co.harbour.jay.transaction;

import javax.sql.DataSource;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class TransactionDao {


    private static final List<String> excludes = Arrays.asList("Salary", "House");
    private final DataSource dataSource;
    private List<Transaction> cachedTransactions;

    public TransactionDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(List<Transaction> transactions) {

    }

    public List<Transaction> getTransactions(int year, int month) {
        if (cachedTransactions == null) {
            try {
                cachedTransactions = parseFolder(getFolderPath());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return getTransactionsByMonth(year, month);
    }

    private List<Transaction> getTransactionsByMonth(int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1);

        return cachedTransactions.stream()
                .filter(t -> t.getDate().isAfter(start) && t.getDate().isBefore(end))
                .collect(Collectors.toList());
    }

    private Path getFolderPath() throws URISyntaxException, IOException {
        URI uri = getClass().getClassLoader().getResource("anz").toURI();
        if ("jar".equals(uri.getScheme())) {
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap(), null);
            return fileSystem.getPath("anz");
        } else {
            return Paths.get(uri);
        }
    }


    private List<Transaction> parseFolder(Path folder) {
        List<Transaction> transactions = new ArrayList<>();

        try {
            Files.walk(folder).forEach(p -> {
                if (p == folder) {
                    return;
                }
                if (!Files.isDirectory(p)) {
                    transactions.addAll(parseFile(p));
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    private List<Transaction> parseFile(Path sourceFilePath) {
        String fileName = sourceFilePath.getFileName().toString();
        String account = fileName.substring(0, 18);
        try {

            return Files.lines(sourceFilePath)
                    .filter(l -> !l.startsWith("Type"))
                    .map(line -> new Transaction(account, line))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


//    private List<Transaction> parseFile(Path sourceFilePath) {
//        String fileName = sourceFilePath.getFileName().toString();
//        String account = fileName.substring(0, 18);
//        try (FileWriter fw = new FileWriter(sourceFilePath.getFileName() + ".msk");
//             PrintWriter bw = new PrintWriter(fw)
//        ) {
//
//            return Files.lines(sourceFilePath)
//                    .filter(l -> !l.startsWith("Type"))
//                    .map(line -> mask(bw, account, line))
//                    .collect(Collectors.toList());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    private Transaction mask(PrintWriter writer, String account, String line) {
//        Transaction transaction = new Transaction(account, line);
//        if (!excludes.contains(transaction.getCategory()))
//            writer.println(line);
//        return transaction;
//    }
}
