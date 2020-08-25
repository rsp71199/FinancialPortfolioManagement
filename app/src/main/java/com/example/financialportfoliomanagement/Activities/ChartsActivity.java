package com.example.financialportfoliomanagement.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.financialportfoliomanagement.R;
import com.example.financialportfoliomanagement.Utilities.ApiEndPoints;
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
    private int dataSize = 0;
    private RequestQueue queue;
    private StringRequest stringRequest;
    private ArrayList<CandleEntry> values = new ArrayList<>();
    private String TAG = "chartsActivity";

    private String FUNCTION = ApiEndPoints.TIME_SERIES_INTRADAY;
    private String response;
    private String SYMBOL;
    private String INTERVAL = "5min";
    private ProgressDialog progressDialog;

    private boolean threadInRun = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_charts);
        Intent intent = getIntent();
        SYMBOL = intent.getStringExtra("SYMBOL");
        setTitle("Charts");
        chart = findViewById(R.id.chart1);

        seekBar = findViewById(R.id.seekbar);


        //placing toolbar in place of actionbar
//        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        chart.setBackgroundColor(Color.WHITE);
        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(60);
        chart.setPinchZoom(true);
        chart.setDrawGridBackground(false);
        chart.setScaleEnabled(true);
        chart.setScaleXEnabled(true);
        chart.setScaleYEnabled(true);
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
        chart.getViewPortHandler().setMaximumScaleX(50f);
        chart.getViewPortHandler().setMaximumScaleY(50f);
        chart.setPinchZoom(true);
        queue = Volley.newRequestQueue(this);
        progressDialog.setTitle("Loading charts...");
        seekBar.setProgress(20);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setChartData(seekBar.getProgress() * dataSize / 100);
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

    private void CallVolley(String symbol, String function, final String interval, String apiKey) {

        String arraySearchKey = "";
        progressDialog.show();
        String url = "";
        if (function == ApiEndPoints.TIME_SERIES_INTRADAY) {
            arraySearchKey = "Time Series (" + interval + ")";
            url = "https://www.alphavantage.co/query?function="
                    + function + "&symbol="
                    + symbol + "&interval="
                    + interval + "&outputsize=full&apikey="
                    + apiKey;
        } else if (function == ApiEndPoints.TIME_SERIES_DAILY) {
            arraySearchKey = "Time Series (Daily)";
            url = "https://www.alphavantage.co/query?function="
                    + function + "&symbol="
                    + symbol + "&outputsize=full&apikey="
                    + apiKey;
        } else if (function == ApiEndPoints.TIME_SERIES_WEEKLY) {
            arraySearchKey = "Weekly Time Series";
            url = "https://www.alphavantage.co/query?function="
                    + function + "&symbol="
                    + symbol + "&outputsize=full&apikey="
                    + apiKey;
        } else if (function == ApiEndPoints.TIME_SERIES_MONTHLY) {
            arraySearchKey = "Monthly Time Series";
            url = "https://www.alphavantage.co/query?function="
                    + function + "&symbol="
                    + symbol + "&outputsize=full&apikey="
                    + apiKey;
        }

        final String finalArraySearchKey = arraySearchKey;
        stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        response = res;
                        if (!threadInRun) {
                            threadInRun = true;
                            updateValue(response, 20, interval, finalArraySearchKey);
                        }

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

    private void updateValue(String response, final int size, String interval, String arrayKey) {
        try {
            final JSONObject json = new JSONObject(response).getJSONObject(arrayKey);
            final Iterator<String> x = json.keys();

            values.clear();
            final Runnable r = new Runnable() {
                public void run() {
                    try {
                        int i = 0;
                        while (x.hasNext()) {
                            String key = (String) x.next();
                            JSONObject jsonElement = new JSONObject(json.get(key).toString());
                            Log.i("TAG", jsonElement.toString());


                            float val = Float.valueOf(jsonElement.get("5. volume").toString()).floatValue();

                            float high = Float.valueOf(jsonElement.get("2. high").toString()).floatValue();
                            float low = Float.valueOf(jsonElement.get("3. low").toString()).floatValue();

                            float open = Float.valueOf(jsonElement.get("1. open").toString()).floatValue();
                            float close = Float.valueOf(jsonElement.get("4. close").toString()).floatValue();


//                            String[] labelA = key.split(" ")[1].split(":");
//                            String label = labelA[0] + "." + labelA[1];
//                            Log.i("TAG", low + " " + high + " " + Float.parseFloat(label));
                            values.add(new CandleEntry(
                                    i,
                                    high,
                                    low,
                                    open,
                                    close,
                                    getResources().getDrawable(R.drawable.common_google_signin_btn_icon_light)
                            ));
                            i++;
                        }

                        dataSize = i;
                        setChartData(size * dataSize / 100);
                    } catch (Exception e) {
                        progressDialog.cancel();
                        threadInRun = false;
                    }

                }

            };
            r.run();
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.cancel();
            threadInRun = false;
        }

    }

    private void setChartData(int size) {
        if (size == 0) return;
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
        progressDialog.cancel();
        threadInRun = false;
        chart.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.charts_bottom_appbar, menu);
//        MenuItem item = menu.findItem(R.id.action_search);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.search: {
                Intent i = new Intent(this, SearchActivity.class);
                startActivity(i);
                break;
            }
            case R.id.refresh: {
                if (!threadInRun) {
                    if (INTERVAL == "5min" || INTERVAL == "30min" || INTERVAL == "60min") {
                        CallVolley(SYMBOL, FUNCTION, INTERVAL, "B02L3PBPXDL1PUY4");
                    }

                }
                break;
            }
            case R.id.interval1: {
                if (!threadInRun) {

                    INTERVAL = "5min";
                    FUNCTION = ApiEndPoints.TIME_SERIES_INTRADAY;
                    CallVolley(SYMBOL, FUNCTION, INTERVAL, "B02L3PBPXDL1PUY4");
                }


                break;
            }
            case R.id.interval2: {
                if (!threadInRun) {

                    INTERVAL = "30min";
                    FUNCTION = ApiEndPoints.TIME_SERIES_INTRADAY;
                    CallVolley(SYMBOL, FUNCTION, INTERVAL, "B02L3PBPXDL1PUY4");


                }
                break;
            }
            case R.id.interval3: {
                if (!threadInRun) {

                    INTERVAL = "60min";
                    FUNCTION = ApiEndPoints.TIME_SERIES_INTRADAY;
                    CallVolley(SYMBOL, FUNCTION, INTERVAL, "B02L3PBPXDL1PUY4");


                }
                break;
            }
            case R.id.interval4: {
                if (!threadInRun) {
                    FUNCTION = ApiEndPoints.TIME_SERIES_DAILY;
                    CallVolley(SYMBOL, FUNCTION, INTERVAL, "B02L3PBPXDL1PUY4");
                }
                break;
            }
            case R.id.interval5: {
                if (!threadInRun) {
                    FUNCTION = ApiEndPoints.TIME_SERIES_WEEKLY;
                    CallVolley(SYMBOL, FUNCTION, INTERVAL, "B02L3PBPXDL1PUY4");
                }
                break;
            }
            case R.id.interval6: {
                if (!threadInRun) {
                    FUNCTION = ApiEndPoints.TIME_SERIES_MONTHLY;
                    CallVolley(SYMBOL, FUNCTION, INTERVAL, "B02L3PBPXDL1PUY4");
                }
                break;
            }
            case R.id.interval7: {
                break;
            }
            case R.id.candleChart: {
                break;
            }
            case R.id.lineChart: {
                break;
            }
            case R.id.barChart: {
                break;
            }


        }
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        CallVolley(SYMBOL, "TIME_SERIES_INTRADAY", "5min", "B02L3PBPXDL1PUY4");
    }
}