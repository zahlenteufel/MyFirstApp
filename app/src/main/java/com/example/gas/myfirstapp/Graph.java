package com.example.gas.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.icu.math.BigDecimal;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Graph extends AppCompatActivity {

    private static Date DATE_0 = new Date(2017, 1, 1);
    private static Date DATE_1 = new Date(2017, 1, 2);
    private static Date DATE_3 = new Date(2017, 1, 4);


    private static Pair[] HARCODED = new Pair[]{
            Pair.create(DATE_0, 0L), Pair.create(DATE_1, 1L), Pair.create(DATE_3, 3L)
    };
    public static String RECORD_FILE = "weights.txt";
    private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    private TableLayout weightsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);

        // Get the Intent that started this activity and extract the string.
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.WEIGHT);

        BigDecimal num = new BigDecimal(message);

        // Save to disk.
        String string = String.format("%s\t%s\n", DATE_FORMAT.format(new Date()), num);

        try (FileOutputStream outputStream = openFileOutput(RECORD_FILE, Context.MODE_APPEND)) {
            outputStream.write(string.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        weightsTable = findViewById(R.id.weightsTable);
        LineChart graph = findViewById(R.id.graph);

        XAxis xAxis = graph.getXAxis();
        xAxis.setValueFormatter(this::formatValue);

        try {
            List<Entry> entries = readDataPoints();
            LineDataSet dataSet = new LineDataSet(entries, "Weight [Kg]");
            LineData lineData = new LineData(dataSet);
            graph.setData(lineData);
            xAxis.setAxisMinimum(dataSet.getXMin());
            xAxis.setAxisMaximum(dataSet.getXMax());

            graph.invalidate(); // Refresh.
            populateWeightsTable(entries);
        } catch (ParseException e) {
            // TODO: Show a popup saying the weight records are invalid and reset them.
        }
    }

    private void populateWeightsTable(List<Entry> dataPoints) {
        for (Entry dataPoint : dataPoints) {
            // Create a new row to be added.
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(
                    new TableRow.LayoutParams(
                            TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView date = new TextView(this);
            date.setText(formatValue(dataPoint.getX(), null));
            tr.addView(date);

            TextView weight = new TextView(this);
            weight.setText(String.valueOf(dataPoint.getY()));
            tr.addView(weight);

            weightsTable.addView(tr,
                    new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.FILL_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    private List<Entry> readDataPoints() throws ParseException {
        List<Entry> result = new ArrayList<>();
        for (Pair p : HARCODED) {
            Date date = (Date) p.first;
            long weight = (Long) p.second;
            result.add(new Entry(date.getTime(), weight));
        }

//        try (FileInputStream inputStream = openFileInput(RECORD_FILE)) {
//            InputStreamReader reader = new InputStreamReader(inputStream);
//            BufferedReader buffreader = new BufferedReader(reader);
//            String line;
//            while ((line = buffreader.readLine()) != null) {
//                Scanner scanIn = new Scanner(line).useDelimiter("\\t");
//                Date date = DATE_FORMAT.parse(scanIn.next());
//                java.math.BigDecimal weight = scanIn.nextBigDecimal();
//                result.add(new Entry((float) date.getTime(), weight.floatValue()));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return result;
    }

    // Format the float into a date...
    String formatValue(float value, AxisBase axis) {
        long timestamp = (long) value;
        Date date = new Date(timestamp);q
        return DATE_FORMAT.format(date);
    }
}
