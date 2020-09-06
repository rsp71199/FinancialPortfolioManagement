package com.example.financialportfoliomanagement.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.financialportfoliomanagement.Interfaces.OnCandleChartsDataRetrieveInterface;
import com.example.financialportfoliomanagement.Interfaces.OnChartDataRetrieveFailure;
import com.example.financialportfoliomanagement.Interfaces.OnLineChartDataRetrieveInterface;
import com.example.financialportfoliomanagement.NetworkCalls.NetworkUtility;
import com.example.financialportfoliomanagement.R;
import com.example.financialportfoliomanagement.Utilities.ApiEndPoints;
import com.example.financialportfoliomanagement.Utilities.ChartMarker;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChartsActivity extends AppCompatActivity {

    private CandleStickChart candleStickChart;
    private LineChart lineChart;
    private CardView candleCard, lineCard;
    private LinearLayout no_connection_view,progress_bar_view;
    private NetworkUtility networkUtility;
    private String TAG = "chartsActivity";
    private String FUNCTION = ApiEndPoints.TIME_SERIES_INTRADAY;
    private String SYMBOL;
    private String from;
    private String INTERVAL = "5min";
    int current_chart_id = 0;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout();
    }

    public void setLayout(){
        setContentView(R.layout.activity_charts);
        Intent intent = getIntent();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SYMBOL = "IBM";
        SYMBOL = intent.getStringExtra("SYMBOL");
        no_connection_view = findViewById(R.id.no_connection_view);
        progress_bar_view = findViewById(R.id.progress_bar_view);
        setTitle("Charts");
        setCandleChart();
        setLineChart();
        showProgressBar();
        setNetworkUtility();
    }


    private void setCandleChart() {
        Log.i("CHART ACTIVITY",">>>>>>>>>>candle chart set");
        ChartMarker chartMarker = new ChartMarker(this, R.layout.tool_tip);
        candleCard = findViewById(R.id.candleChartCard);
        candleStickChart = findViewById(R.id.candleChart);
        candleStickChart.setBackgroundColor(Color.BLACK);
        candleStickChart.getDescription().setEnabled(false);
        candleStickChart.setPinchZoom(true);
        candleStickChart.setBorderColor(Color.BLACK);
        candleStickChart.setDrawGridBackground(false);
        candleStickChart.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        candleStickChart.setClickable(true);
        candleStickChart.setMarker(chartMarker);
        XAxis xAxis = candleStickChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setLabelCount(4);
        xAxis.setDrawGridLines(false);
        YAxis leftAxis = candleStickChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setLabelCount(7, false);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.LTGRAY);
        leftAxis.setGridColor(R.color.White);
        leftAxis.setGridLineWidth(0.5f);
        leftAxis.setTextColor(Color.WHITE);
        YAxis rightAxis = candleStickChart.getAxisRight();
        rightAxis.setEnabled(false);
        candleStickChart.getLegend().setEnabled(false);

    }
    private void setLineChart() {
        Log.i("CHART ACTIVITY",">>>>>>>>>>line chart set");
        lineCard = findViewById(R.id.lineChartCard);
        ChartMarker chartMarker = new ChartMarker(this, R.layout.tool_tip);
        lineChart = findViewById(R.id.lineChart);
        lineChart.setBackgroundColor(getColor(R.color.Black));
        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(true);
        lineChart.setMarker(chartMarker);
        lineChart.setBorderColor(Color.BLACK);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.getAxisLeft().setDrawAxisLine(false);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getAxisLeft().setGridLineWidth(0.5f);
        lineChart.getAxisLeft().setGridColor(R.color.White);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setDrawLabels(true);
        lineChart.getAxisLeft().setTextColor(Color.WHITE);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getXAxis().setDrawLabels(true);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setLabelCount(4, true);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setDrawAxisLine(false);
        lineChart.getXAxis().setTextColor(Color.WHITE);


    }
    private void setNetworkUtility(){
        Log.i("CHART ACTIVITY",">>>>>>>>>>network utility set");
        networkUtility = new NetworkUtility(this);
        networkUtility.setOnCandleChartsDataRetrieveInterface(new OnCandleChartsDataRetrieveInterface() {
            @Override
            public void onCandleChartDataRetrieveSuccess(String res, String arrayKey) {
                Log.i("CANDLE",res);
                setCandleStickChartData(res,arrayKey);
                showCandleChart();
            }

            @Override
            public void onCandleChartDataRetrieveFailure() {
                showNoConnectionView();
                Log.i("NO CANDLE CHART",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        });
        networkUtility.setOnLineChartDataRetrieveInterface(new OnLineChartDataRetrieveInterface() {
            @Override
            public void onLineChartDataRetrieveSuccess(String res, String arrayKey) {
                setLineChartData(res,arrayKey);
                showLineChart();
                Log.i("CANDLE",res);
            }

            @Override
            public void onLineChartDataRetrieveFailure() {
                showNoConnectionView();
                Log.i("NO LINE CHART",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        });
        networkUtility.setOnChartDataRetrieveFailure(new OnChartDataRetrieveFailure() {
            @Override
            public void onFailure() {
                showNoConnectionView();
                Log.i("NO CHART",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        });
        networkUtility.setChart(SYMBOL
                , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id);

    }
    public void setCandleStickChartData(String response, String arrayKey) {
        try {
            final JSONObject json = new JSONObject(response).getJSONObject(arrayKey);
            final Iterator<String> x = json.keys();

            final ArrayList<CandleEntry> candleEntryArrayList = new ArrayList<>();
            final ArrayList<String> xAxis = new ArrayList<>();

            candleEntryArrayList.clear();
            final Runnable r = new Runnable() {
                public void run() {
                    try {
                        int i = 0;
                        while (x.hasNext()) {
                            String key = (String) x.next();
                            JSONObject jsonElement = new JSONObject(json.get(key).toString());
                            Log.i("TAG", jsonElement.toString());

                            xAxis.add(key);
                            float open = Float.valueOf(jsonElement.get("1. open").toString()).floatValue();
                            float high = Float.valueOf(jsonElement.get("2. high").toString()).floatValue();
                            float low = Float.valueOf(jsonElement.get("3. low").toString()).floatValue();
                            float close = Float.valueOf(jsonElement.get("4. close").toString()).floatValue();
                            float val = Float.valueOf(jsonElement.get("5. volume").toString()).floatValue();


                            candleEntryArrayList.add(new CandleEntry(
                                    i,
                                    high,
                                    low,
                                    open,
                                    close
                            ));

                            i++;
                        }
                        setCandleStickChartDataUtil(candleEntryArrayList,xAxis);
                    } catch (Exception e) {
                        showNoConnectionView();
                    }

                }

            };
            r.run();
        } catch (JSONException e) {
            e.printStackTrace();
            showNoConnectionView();
        }

    }
    public void setLineChartData(String response, String arrayKey) {
        try {
            final JSONObject json = new JSONObject(response).getJSONObject(arrayKey);
            final Iterator<String> x = json.keys();

            final ArrayList<Entry> entryHighArrayList = new ArrayList<>();
            final ArrayList<Entry> entryLowArrayList = new ArrayList<>();
            final ArrayList<String> xAxis = new ArrayList<>();
            entryHighArrayList.clear();

            final Runnable r = new Runnable() {
                public void run() {
                    try {
                        int i = 0;
                        while (x.hasNext()) {
                            String key = (String) x.next();
                            JSONObject jsonElement = new JSONObject(json.get(key).toString());
//                            Log.i("TAG", jsonElement.toString());

                            float open = Float.valueOf(jsonElement.get("1. open").toString()).floatValue();
                            float high = Float.valueOf(jsonElement.get("2. high").toString()).floatValue();
                            float low = Float.valueOf(jsonElement.get("3. low").toString()).floatValue();
                            float close = Float.valueOf(jsonElement.get("4. close").toString()).floatValue();
                            float val = Float.valueOf(jsonElement.get("5. volume").toString()).floatValue();

                            xAxis.add(key);
                            entryHighArrayList.add(new Entry(i, high));
                            entryLowArrayList.add(new Entry(i, low));


                            i++;
                        }
                        setLineChartDataUtil(entryHighArrayList, entryLowArrayList, xAxis);
                    } catch (Exception e) {
                        showNoConnectionView();
                    }

                }

            };
            r.run();
        } catch (JSONException e) {
            e.printStackTrace();
            showNoConnectionView();
        }
    }
    public void setCandleStickChartDataUtil(ArrayList<CandleEntry> candleEntryArrayList,ArrayList<String> xAxis) {
        CandleDataSet set1 = new CandleDataSet(candleEntryArrayList, "Data Set");
        candleStickChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxis));
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

        candleStickChart.setData(data);
        candleStickChart.invalidate();
    }
    public void setLineChartDataUtil(ArrayList<Entry> entryHighArrayList, ArrayList<Entry> entryLowArrayList, ArrayList<String> xAxis) {
        LineDataSet set1 = new LineDataSet(entryHighArrayList, "high ");
        LineDataSet set2 = new LineDataSet(entryLowArrayList, "low ");


        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxis));


        set1.setColor(Color.GREEN);
        set1.setLineWidth(1.0f);
        set1.setDrawValues(false);
        set1.setDrawCircles(false);
        set1.setMode(LineDataSet.Mode.LINEAR);
        set1.setDrawFilled(true);
        Drawable drawable1 = ContextCompat.getDrawable(this, R.drawable.fade_line_chart);
        set1.setFillDrawable(drawable1);


        set2.setColor(Color.RED);
        Drawable drawable2 = ContextCompat.getDrawable(this, R.drawable.fade_line_chart2);
        set2.setFillDrawable(drawable2);
        set2.setLineWidth(1.0f);
        set2.setDrawValues(false);
        set2.setDrawCircles(false);
        set2.setMode(LineDataSet.Mode.LINEAR);
        set2.setDrawFilled(true);


        // create a data object with the data sets
        LineData data1 = new LineData(set2);
        LineData data2 = new LineData(set2);

        // set data
        List<ILineDataSet> lines = new ArrayList<ILineDataSet>();
        lines.add(set1);
        lines.add(set2);
        lineChart.setData(new LineData(lines));
        lineChart.invalidate();
        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();
        l.setEnabled(false);
    }


    public void showProgressBar(){
        Log.i("CHART ACTIVITY",">>>>>>>>>>showing progress bar");
        progress_bar_view.setVisibility(View.VISIBLE);
        no_connection_view.setVisibility(View.INVISIBLE);
        candleCard.setVisibility(View.INVISIBLE);
        lineCard.setVisibility(View.INVISIBLE);
    }
    public void showNoConnectionView(){
        progress_bar_view.setVisibility(View.INVISIBLE);
        no_connection_view.setVisibility(View.VISIBLE);
        candleCard.setVisibility(View.INVISIBLE);
        lineCard.setVisibility(View.INVISIBLE);
    }
    public void showCandleChart(){
        progress_bar_view.setVisibility(View.INVISIBLE);
        no_connection_view.setVisibility(View.INVISIBLE);
        candleCard.setVisibility(View.VISIBLE);
        lineCard.setVisibility(View.INVISIBLE);
    }
    public void showLineChart(){
        progress_bar_view.setVisibility(View.INVISIBLE);
        no_connection_view.setVisibility(View.INVISIBLE);
        candleCard.setVisibility(View.INVISIBLE);
        lineCard.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.charts_appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                super.onBackPressed();
                finish();
            }
            case R.id.refresh: {
                showProgressBar();
                if (INTERVAL == "5min" || INTERVAL == "30min" || INTERVAL == "60min") {
                    networkUtility.setChart(SYMBOL
                            , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id);
                }
                break;
            }
            case R.id.interval1: {
                showProgressBar();
                INTERVAL = "5min";
                FUNCTION = ApiEndPoints.TIME_SERIES_INTRADAY;
                networkUtility.setChart(SYMBOL
                        , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id);
                break;
            }
            case R.id.interval2: {
                showProgressBar();
                INTERVAL = "30min";
                FUNCTION = ApiEndPoints.TIME_SERIES_INTRADAY;
                networkUtility.setChart(SYMBOL
                        , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id);
                break;
            }
            case R.id.interval3: {
                showProgressBar();
                INTERVAL = "60min";
                FUNCTION = ApiEndPoints.TIME_SERIES_INTRADAY;
                networkUtility.setChart(SYMBOL
                        , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id);
                break;
            }
            case R.id.interval4: {
                showProgressBar();
                FUNCTION = ApiEndPoints.TIME_SERIES_DAILY;
                networkUtility.setChart(SYMBOL
                        , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id);
                break;
            }
            case R.id.interval5: {
                showProgressBar();
                FUNCTION = ApiEndPoints.TIME_SERIES_WEEKLY;
                networkUtility.setChart(SYMBOL
                        , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id);
                break;
            }
            case R.id.interval6: {
                showProgressBar();
                FUNCTION = ApiEndPoints.TIME_SERIES_MONTHLY;
                networkUtility.setChart(SYMBOL
                        , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id);
                break;
            }
            case R.id.candleChart: {
                showProgressBar();
                current_chart_id = 0;
                networkUtility.setChart(SYMBOL
                        , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id);
                lineCard.setVisibility(View.INVISIBLE);
                candleCard.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.lineChart: {
                showProgressBar();
                current_chart_id = 1;
                networkUtility.setChart(SYMBOL
                        , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id);
                lineCard.setVisibility(View.VISIBLE);
                candleCard.setVisibility(View.INVISIBLE);
            }

        }
        return super.onOptionsItemSelected(item);
    }


}