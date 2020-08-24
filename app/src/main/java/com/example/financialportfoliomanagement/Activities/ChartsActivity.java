package com.example.financialportfoliomanagement.Activities;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.financialportfoliomanagement.R;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ChartsActivity extends AppCompatActivity {

    private CandleStickChart chart;
    private SeekBar seekBar;
    private RequestQueue queue;
    private StringRequest stringRequest;
    private ArrayList<CandleEntry> values = new ArrayList<>();
    private String TAG = "chartsActivity";
    private Button refresh;
    private String response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_charts);

        setTitle("CandleStickChartActivity");
        chart = findViewById(R.id.chart1);
        refresh = findViewById(R.id.refresh);
        seekBar = findViewById(R.id.seekbar);
        chart.setBackgroundColor(Color.WHITE);
        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(60);
        chart.setPinchZoom(true);
        chart.setDrawGridBackground(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(7, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        chart.getLegend().setEnabled(false);
        queue = Volley.newRequestQueue(this);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallVolley("IBM", "TIME_SERIES_INTRADAY", "5min", "B02L3PBPXDL1PUY4");
            }
        });
        seekBar.setProgress(20);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateValue(response, seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    private void CallVolley(String symbol, String function, String interval, String apiKey) {

        String url = "https://www.alphavantage.co/query?function="
                + function + "&symbol="
                + symbol + "&interval="
                + interval + "&outputsize=full&apikey="
                + apiKey;


        stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        response = res;
                        updateValue(response, 20);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        stringRequest.setTag(TAG);
        queue.add(stringRequest);
    }

    private void updateValue(String response, int size) {
        try {
            final JSONObject json = new JSONObject(response).getJSONObject("Time Series (5min)");
            final Iterator<String> x = json.keys();

            values.clear();
            try {
                int i = 0;
                while (x.hasNext()) {
                    String key = (String) x.next();
                    JSONObject jsonElement = new JSONObject(json.get(key).toString());
                    Log.i("TAG", json.get(key).toString());


                    float val = Float.valueOf(jsonElement.get("5. volume").toString()).floatValue();

                    float high = Float.valueOf(jsonElement.get("2. high").toString()).floatValue();
                    float low = Float.valueOf(jsonElement.get("3. low").toString()).floatValue();

                    float open = Float.valueOf(jsonElement.get("1. open").toString()).floatValue();
                    float close = Float.valueOf(jsonElement.get("4. close").toString()).floatValue();


                    Log.i("TAG", low + " " + high);
                    values.add(new CandleEntry(
                            i, high,
                            low,
                            open,
                            close,
                            getResources().getDrawable(R.drawable.common_google_signin_btn_icon_light)
                    ));
                    i++;
                }

                CandleDataSet set1 = new CandleDataSet(values.subList(0, size), "Data Set");
                set1.setDrawIcons(false);
                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                set1.setShadowColor(Color.DKGRAY);
                set1.setShadowWidth(0.7f);
                set1.setDecreasingColor(Color.RED);
                set1.setDecreasingPaintStyle(Paint.Style.FILL);
                set1.setIncreasingColor(Color.rgb(122, 242, 84));
                set1.setIncreasingPaintStyle(Paint.Style.STROKE);
                set1.setNeutralColor(Color.BLUE);

                CandleData data = new CandleData(set1);

                chart.setData(data);
                chart.invalidate();

            } catch (Exception e) {

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}