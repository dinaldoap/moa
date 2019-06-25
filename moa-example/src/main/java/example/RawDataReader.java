package example;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class RawDataReader {

    private String source;

    public void setSource(String source) {
        this.source = source;
    }

    public void read(Consumer<? super CSVRecord> consumer) {
        try {
            BufferedReader reader = Files
                    .newBufferedReader(Paths.get(String.format("data/raw/%s.csv", this.source)));
            CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            parser.forEach(consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
