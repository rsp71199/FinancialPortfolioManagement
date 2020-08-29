package com.example.financialportfoliomanagement.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.financialportfoliomanagement.NetworkCalls.ChartsNetworkUtility;
import com.example.financialportfoliomanagement.R;
import com.example.financialportfoliomanagement.Utilities.ApiEndPoints;
import com.example.financialportfoliomanagement.Utilities.ChartMarker;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

public class ChartsActivity extends AppCompatActivity {

    private CandleStickChart candleStickChart;
    private LineChart lineChart;
    private CardView candleCard, lineCard;
    private ChartsNetworkUtility chartsNetworkUtility;
    private String TAG = "chartsActivity";
    private String FUNCTION = ApiEndPoints.TIME_SERIES_INTRADAY;
    private String SYMBOL;
    private String INTERVAL = "5min";
    private ProgressDialog progressDialog;
    int current_chart_id = 0;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        Intent intent = getIntent();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SYMBOL = intent.getStringExtra("SYMBOL");
        setTitle("Charts");
        SYMBOL = "IBM";
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        chartsNetworkUtility = new ChartsNetworkUtility(this);
        setCandleChart();
        setLineChart();
        lineCard.setVisibility(View.INVISIBLE);
        candleCard.setVisibility(View.VISIBLE);
        chartsNetworkUtility.setChart(lineChart, candleStickChart, SYMBOL
                , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id, progressDialog);
    }


    private void setCandleChart() {
        ChartMarker chartMarker = new ChartMarker(this, R.layout.tool_tip);
        candleCard = findViewById(R.id.candleChartCard);
        candleStickChart = findViewById(R.id.candleChart);
        candleStickChart.setBackgroundColor(Color.BLACK);
        candleStickChart.getDescription().setEnabled(false);
//        candleStickChart.setMaxVisibleValueCount(60);
        candleStickChart.setPinchZoom(true);
        candleStickChart.setBorderColor(Color.BLACK);
        candleStickChart.setDrawGridBackground(false);
//        candleStickChart.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
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
//        leftAxis.setDrawAxisLine(true);
        leftAxis.setTextColor(Color.WHITE);
        YAxis rightAxis = candleStickChart.getAxisRight();
        rightAxis.setEnabled(false);
        candleStickChart.getLegend().setEnabled(false);

    }

    @SuppressLint("ResourceAsColor")
    private void setLineChart() {
        lineCard = findViewById(R.id.lineChartCard);
        ChartMarker chartMarker = new ChartMarker(this, R.layout.tool_tip);
        lineChart = findViewById(R.id.lineChart);
        lineChart.setBackgroundColor(R.color.primaryExtraLight);
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
                lineCard.setVisibility(View.INVISIBLE);
                candleCard.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.lineChart: {
                current_chart_id = 1;
                chartsNetworkUtility.setChart(lineChart, candleStickChart, SYMBOL
                        , FUNCTION, INTERVAL, ApiEndPoints.alphaApi, current_chart_id, progressDialog);
                lineCard.setVisibility(View.VISIBLE);
                candleCard.setVisibility(View.INVISIBLE);
            }
            case R.id.barChart: {
                break;
            }


        }
        return super.onOptionsItemSelected(item);
    }


}