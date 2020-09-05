package com.example.financialportfoliomanagement.NetworkCalls;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.financialportfoliomanagement.Interfaces.OnCandleChartsDataRetrieveInterface;
import com.example.financialportfoliomanagement.Interfaces.OnChartDataRetrieveFailure;
import com.example.financialportfoliomanagement.Interfaces.OnLineChartDataRetrieveInterface;
import com.example.financialportfoliomanagement.Utilities.ApiEndPoints;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;

public class ChartsNetworkUtility {
    public RequestQueue queue;
    private StringRequest stringRequest;
    private Context context;





    public ChartsNetworkUtility(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }






}