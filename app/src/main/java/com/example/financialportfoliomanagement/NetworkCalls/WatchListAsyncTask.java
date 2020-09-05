package com.example.financialportfoliomanagement.NetworkCalls;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.financialportfoliomanagement.Auth.Auth;
import com.example.financialportfoliomanagement.Interfaces.WatchListDataRetrieveInterface;
import com.example.financialportfoliomanagement.Models.WatchListItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WatchListAsyncTask extends AsyncTask<Integer, String, String> {
    Auth auth;
    Context context;
    RequestQueue queue;
    List<String> watch_list_symbols;
    ProgressDialog progressDialog;
    WatchListDataRetrieveInterface watchListDataRetrieveInterface;

    public WatchListAsyncTask(Context context, List<String> watch_list_symbols, ProgressDialog progressDialog) {
        this.context = context;
        this.watch_list_symbols = watch_list_symbols;
        this.progressDialog = progressDialog;
        queue = Volley.newRequestQueue(context);
    }

    public void setWatchListDataRetrieveInterface(WatchListDataRetrieveInterface watchListDataRetrieveInterface) {
        this.watchListDataRetrieveInterface = watchListDataRetrieveInterface;
    }


    @Override
    protected String doInBackground(Integer... integers) {
        final List<String> watch_list = watch_list_symbols;
        final int length = watch_list.size();
        final int[] i = {0};
        final List<WatchListItem> responses = new ArrayList<>();
        for (final String symbol : watch_list) {

            String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=B02L3PBPXDL1PUY4";
            final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("Tag", response);
                            i[0]++;
                            try {
                                JSONObject res = new JSONObject(response);
                                JSONObject global_quote = res.getJSONObject("Global Quote");
                                String open = global_quote.getString("02. open");
                                String high = global_quote.getString("03. high");
                                String low = global_quote.getString("04. low");
                                String price = global_quote.getString("05. price");
                                String prev_close = global_quote.getString("08. previous close");
                                String change = global_quote.getString("10. change percent");
                                Log.i("TAG", symbol + " " + open);
                                responses.add(new WatchListItem(symbol, "", low, high, open, price, change, prev_close));
                                if (i[0] == length) {
                                    watchListDataRetrieveInterface.onDataFetched(responses);
                                }
                            } catch (JSONException e) {
//                                e.printStackTrace();
                                responses.add(new WatchListItem(symbol, "", "--", "--"
                                        , "--", "--", "--", "--"));
                                if (i[0] == length) {
                                    watchListDataRetrieveInterface.onDataFetched(responses);
                                }
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("TAG", error.toString());
                }
            });

            queue.add(stringRequest);

        }
        return null;
    }
}
