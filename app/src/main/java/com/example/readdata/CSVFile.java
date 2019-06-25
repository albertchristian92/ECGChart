package com.example.readdata;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVFile {
    InputStream inputStream;

    public CSVFile(InputStream inputStream) {
        this.inputStream = inputStream;
    }



    public List read() {
        List resultList = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                resultList.add(row);
                //Log.d("ggg",row[0]);
            }
        } catch (IOException ex) {
            Log.d("ggg","err1");
            throw new RuntimeException("Error in reading CSV file: " + ex);

        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.d("ggg","err2");
                throw new RuntimeException("Error while closing input stream: " + e);

            }
        }
        return resultList;
    }
}