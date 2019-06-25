package com.example.readdata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.app.Activity;

public class MainActivity extends Activity {
    TextView textView;
    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;
    private int rowcounter,datacounter=0;
    private  List list;
    private String[] temp;
    private int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream inputStream = getResources().openRawResource(R.raw.mitbih_test);
        CSVFile csvFile = new CSVFile(inputStream);
        list = csvFile.read();
        temp = (String[]) list.get(rowcounter);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getGridLabelRenderer().setGridStyle( GridLabelRenderer.GridStyle.NONE );
        // data
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setDrawBorder(true);
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(200);
        viewport.setMinY(-10);
        viewport.setMaxY(30);
        viewport.setScrollable(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // we're going to simulate real time with thread that append data to the graph
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <list.size(); i++) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(counter <temp.length-1)
                            {
                                addEntry();
                                datacounter++;
                                counter++;
                                System.out.println("counter = "+counter+"row counter" + rowcounter);

                            }
                            else{
                                rowcounter++;
                                counter=0;
                            }
                        }
                    });

                    // sleep to slow down the add of entries
                    try {
                        Thread.sleep(60);
                    } catch (InterruptedException e) {
                        // manage error ...
                    }
                }
            }
        }).start();
    }

    // add  data to graph
    private void addEntry() {
    Double dataInput;
    temp = (String[]) list.get(rowcounter);
    System.out.println(Arrays.toString(temp));
    dataInput = Double.parseDouble(temp[counter]);
        // here, we choose to display max 10 points on the viewport and we scroll to end
       series.appendData(new DataPoint(lastX++, dataInput), false, 21892);

    }

}


