package example;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class CommitGuru2Features implements Consumer<CSVRecord> {

    private CSVPrinter csvPrinter;
    private static final List<String> COLUMNS = Arrays.asList("fix", "ns", "nd", "nf", "entropy",
            "la", "ld", "lt", "ndev", "age", "nuc", "exp", "rexp", "sexp",
            "author_date_unix_timestamp", "classification", "contains_bug");



    public CommitGuru2Features() {
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("data/spark_features.csv"));
            this.csvPrinter = new CSVPrinter(writer,
                    CSVFormat.DEFAULT.withHeader(COLUMNS.toArray(new String[COLUMNS.size()])));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void accept(CSVRecord record) {
        try {
            List<String> values = COLUMNS.stream() //
                    .map(column -> record.get(column)) //
                    .collect(Collectors.toList());
            if (!record.get("fix").isEmpty()) {
                this.csvPrinter.printRecord(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.csvPrinter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
