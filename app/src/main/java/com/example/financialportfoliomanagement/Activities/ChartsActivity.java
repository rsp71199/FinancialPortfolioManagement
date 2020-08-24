package com.example.financialportfoliomanagement.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financialportfoliomanagement.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

public class ChartsActivity extends AppCompatActivity  {

    private static final String TAG = "ChartsActivity";

    private LineChart mChart;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        mChart = (LineChart) findViewById(R.id.linechart);
//        mChart.setOnChartGestureListener(ChartsActivity.this);
//        mChart.setOnChartValueSelectedListener(ChartsActivity.this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);


    }
}