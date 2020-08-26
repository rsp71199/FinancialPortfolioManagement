package com.example.financialportfoliomanagement.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financialportfoliomanagement.NetworkCalls.ChartsNetworkUtility;
import com.example.financialportfoliomanagement.R;
import com.example.financialportfoliomanagement.Utilities.ApiEndPoints;
import com.example.financialportfoliomanagement.Utilities.ChartMarker;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

public class ChartsActivity extends AppCompatActivity {

    private CandleStickChart candleStickChart;
    private LineChart lineChart;
    private ChartsNetworkUtility chartsNetworkUtility;
    private String TAG = "chartsActivity";
    private String FUNCTION = ApiEndPoints.TIME_SERIES_INTRADAY;
    private String SYMBOL;
    private String INTERVAL = "5min";
    private ProgressDialog progressDialog;
    int current_chart_id = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        Intent intent = getIntent();

        SYMBOL = intent.getStringExtra("SYMBOL");
        setTitle("Charts");
        SYMBOL = "IBM";
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        chartsNetworkUtility = new ChartsNetworkUtility(this);
        setCandleChart();
        setLineChart();
        chartsNetworkUtility.setChart(lineChart, candleStickChart, SYMBOL
                , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id, progressDialog);
    }


    private void setCandleChart() {
        ChartMarker chartMarker = new ChartMarker(this, R.layout.tool_tip);
        candleStickChart = findViewById(R.id.candleChart);
        candleStickChart.setBackgroundColor(Color.WHITE);
        candleStickChart.getDescription().setEnabled(false);
        candleStickChart.setMaxVisibleValueCount(60);
        candleStickChart.setPinchZoom(true);
        candleStickChart.setBorderColor(Color.BLACK);
        candleStickChart.setDrawGridBackground(true);
        candleStickChart.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        candleStickChart.setClickable(true);
        candleStickChart.setMarker(chartMarker);
        XAxis xAxis = candleStickChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        YAxis leftAxis = candleStickChart.getAxisLeft();
        leftAxis.setLabelCount(7, false);
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawAxisLine(true);
        YAxis rightAxis = candleStickChart.getAxisRight();
        rightAxis.setEnabled(false);
        candleStickChart.getLegend().setEnabled(false);

    }

    private void setLineChart() {

        ChartMarker chartMarker = new ChartMarker(this, R.layout.tool_tip);
        lineChart = findViewById(R.id.lineChart);
        lineChart.setDrawGridBackground(true);
        lineChart.getDescription().setEnabled(true);
        lineChart.setMarker(chartMarker);

        lineChart.setBorderColor(Color.BLACK);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getAxisRight().setEnabled(true);
        lineChart.getAxisLeft().setDrawLabels(true);
        lineChart.getAxisRight().setDrawLabels(true);
        lineChart.getXAxis().setDrawLabels(true);
        lineChart.getXAxis().setDrawGridLines(true);
        lineChart.getXAxis().setDrawAxisLine(true);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                lineChart.highlightValue(h);
                Log.i("TAG", "_____________________" + e.toString());
            }

            @Override
            public void onNothingSelected() {

            }
        });


    }


    @Override
    protected void onStop() {
        super.onStop();
        if (chartsNetworkUtility.queue != null) {
            chartsNetworkUtility.queue.cancelAll(TAG);
        }
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
                    if (INTERVAL == "5min" || INTERVAL == "30min" || INTERVAL == "60min") {
                        chartsNetworkUtility.setChart(lineChart, candleStickChart, SYMBOL
                                , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id, progressDialog);
                    }
                break;
            }
            case R.id.interval1: {
                INTERVAL = "5min";
                FUNCTION = ApiEndPoints.TIME_SERIES_INTRADAY;
                chartsNetworkUtility.setChart(lineChart, candleStickChart, SYMBOL
                        , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id, progressDialog);
                break;
            }
            case R.id.interval2: {
                INTERVAL = "30min";
                FUNCTION = ApiEndPoints.TIME_SERIES_INTRADAY;
                chartsNetworkUtility.setChart(lineChart, candleStickChart, SYMBOL
                        , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id, progressDialog);
                break;
            }
            case R.id.interval3: {
                INTERVAL = "60min";
                FUNCTION = ApiEndPoints.TIME_SERIES_INTRADAY;
                chartsNetworkUtility.setChart(lineChart, candleStickChart, SYMBOL
                        , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id, progressDialog);
                break;
            }
            case R.id.interval4: {
                FUNCTION = ApiEndPoints.TIME_SERIES_DAILY;
                chartsNetworkUtility.setChart(lineChart, candleStickChart, SYMBOL
                        , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id, progressDialog);
                break;
            }
            case R.id.interval5: {
                FUNCTION = ApiEndPoints.TIME_SERIES_WEEKLY;
                chartsNetworkUtility.setChart(lineChart, candleStickChart, SYMBOL
                        , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id, progressDialog);
                break;
            }
            case R.id.interval6: {
                FUNCTION = ApiEndPoints.TIME_SERIES_MONTHLY;
                chartsNetworkUtility.setChart(lineChart, candleStickChart, SYMBOL
                        , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id, progressDialog);
                break;
            }
            case R.id.candleChart: {
                current_chart_id = 0;
                chartsNetworkUtility.setChart(lineChart, candleStickChart, SYMBOL
                        , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id, progressDialog);
                break;
            }
            case R.id.lineChart: {
                current_chart_id = 1;
                chartsNetworkUtility.setChart(lineChart, candleStickChart, SYMBOL
                        , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id, progressDialog);
            }
            case R.id.barChart: {
                break;
            }


        }
        return super.onOptionsItemSelected(item);
    }


}