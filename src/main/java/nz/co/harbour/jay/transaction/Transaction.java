package nz.co.harbour.jay.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Transaction {
    final private String raw;
    final private String account;
    final private String type;
    final private String details;
    final private String particulars;
    final private String code;
    final private String reference;
    final private double amount;
    final private LocalDate date;
    final private String foreignCcyAmount;
    final private String conversionCharge;

    private String tag;

    final private static DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Transaction(String account, String csvLine) {
        raw = csvLine;
        this.account = account;
        String[] columns = csvLine.split(",");
        type = columns[0].trim();
        details = columns[1].trim();
        particulars = columns[2].trim();
        code = columns[3].trim();
        reference = columns[4].trim();
        amount = Double.parseDouble(columns[5]);
        date = LocalDate.parse(columns[6], DF);
        if (columns.length > 8) {
            foreignCcyAmount = columns[7];
        } else
            foreignCcyAmount = null;
        if (columns.length > 9) {
            conversionCharge = columns[8];
        } else
            conversionCharge = null;
        TagUtil.tag(this);
    }

    public void tag(String newTag) {
       this.tag = newTag;
    }

    public String getTags() {
        return tag;
    }

    public String getCategory() {
        return TagUtil.getParentCategory(tag);
    }

    @JsonIgnore
    public String getAccount() {
        return account;
    }

    public String getType() {
        return type;
    }

    public String getDetails() {
        return details;
    }

    public String getParticulars() {
        return particulars;
    }

    public String getCode() {
        return code;
    }

    public String getReference() {
        return reference;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getForeignCcyAmount() {
        return foreignCcyAmount;
    }

    public String getConversionCharge() {
        return conversionCharge;
    }


    @JsonIgnore
    public String getRaw() {
        return raw;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "raw='" + raw + '\'' +
                ", account='" + account + '\'' +
                ", type='" + type + '\'' +
                ", details='" + details + '\'' +
                ", particulars='" + particulars + '\'' +
                ", code='" + code + '\'' +
                ", reference='" + reference + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", foreignCcyAmount='" + foreignCcyAmount + '\'' +
                ", conversionCharge='" + conversionCharge + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
