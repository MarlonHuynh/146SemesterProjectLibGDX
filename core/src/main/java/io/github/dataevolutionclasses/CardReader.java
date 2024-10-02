package io.github.dataevolutionclasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CardReader {
    private String filePath;
    public CardReader(String filePath) {
        this.filePath = filePath;
    }
    public List<String[]> readCSV() {
        List<String[]> data = new ArrayList<>();
        String line;
        String delimiter = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                // Split the line into values based on the delimiter
                String[] values = line.split(delimiter);
                data.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
