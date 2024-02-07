package nz.co.mymoney.ingestion;

import nz.co.mymoney.transaction.Transaction;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class TransactionParser {
    private static final Logger LOGGER = Logger.getLogger(TransactionParser.class.getName());

    public Integer parseAndImport(InputStream is, String fileName, Function<List<Transaction>, Integer> importer)
            throws IOException {
        if (fileName.toLowerCase().endsWith("zip")) {
            int i = 0;
            try (
                    ZipInputStream zipInputStream = new ZipInputStream(is)) {
                ZipEntry ze = zipInputStream.getNextEntry();
                while (ze != null) {
                    if (!ze.isDirectory())
                        i += importer.apply(parseFileStream(zipInputStream, ze.getName()));
                    ze  = zipInputStream.getNextEntry();
                }
            }
            return i;
        } else
            return importer.apply(parseFileStream(is, fileName));
    }

    private List<Transaction> parseFileStream(InputStream inputStream, String fileName) {
        String account = fileName.substring(0, 18);
        LOGGER.warning("parsing:" + fileName);
        try  {
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            List<Transaction> transactions = new ArrayList<>();
            do {
                if (!line.startsWith("Type"))
                    transactions.add(new Transaction(0, account, line));
                line = br.readLine();
            } while (line != null);
            return Collections.unmodifiableList(transactions);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
