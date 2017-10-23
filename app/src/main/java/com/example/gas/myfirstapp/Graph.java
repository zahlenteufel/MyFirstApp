package com.example.gas.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.icu.math.BigDecimal;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Graph extends AppCompatActivity {

    public static String RECORD_FILE = "weights.txt";

    private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.WEIGHT);

        BigDecimal num = new BigDecimal(message);

        // Save to disk..
        String string = String.format("%s\t%s\n", DATE_FORMAT.format(new Date()), num);

        try (FileOutputStream outputStream = openFileOutput(RECORD_FILE, Context.MODE_APPEND)) {
            outputStream.write(string.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView textView = (EditText) findViewById(R.id.multilineTextView);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        try {
            DataPoint[] dataPoints = readDataPoints();
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
            graph.addSeries(series);
            textView.setText(series.toString());
        } catch (ParseException e) {
            // do nothing yet.. show an error?
        }
    }

    private DataPoint[] readDataPoints() throws ParseException {
        List<DataPoint> result = new ArrayList<>();
        try (FileInputStream inputStream = openFileInput(RECORD_FILE)) {
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader buffreader = new BufferedReader(reader);
            String line;
            int i = 0;
            while ((line = buffreader.readLine()) != null) {
                Scanner scanIn = new Scanner(line).useDelimiter("\\t");
                //Date date = DATE_FORMAT.parse(scanIn.next());
                String s = scanIn.next();
                java.math.BigDecimal weight = scanIn.nextBigDecimal();
                result.add(new DataPoint(i++, weight.doubleValue()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toArray(new DataPoint[0]);
    }
}
