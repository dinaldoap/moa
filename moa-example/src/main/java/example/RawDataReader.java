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

    public void read(Consumer<? super CSVRecord> consumer) {
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get("data/raw/mongo.csv"));
            CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            parser.forEach(consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
