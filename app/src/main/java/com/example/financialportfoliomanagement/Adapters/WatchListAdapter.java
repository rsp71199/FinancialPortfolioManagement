package com.example.financialportfoliomanagement.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.Activities.ChartsActivity;
import com.example.financialportfoliomanagement.Auth.Auth;
import com.example.financialportfoliomanagement.Interfaces.AuthOnCompleteUpdateInterface;
import com.example.financialportfoliomanagement.Models.WatchListItem;
import com.example.financialportfoliomanagement.NetworkCalls.WatchListAsyncTask;
import com.example.financialportfoliomanagement.R;

import java.util.List;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.MyViewHolder> {
    public List<WatchListItem> mDataset;
    public Context context;
    public Auth auth;
    public WatchListAsyncTask watchListAsyncTask;

    public void refresh(List<WatchListItem> dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView shortName, regularMarketChange, regularMarketPrice, marketName, regularMarketPreviousClose;
        ImageButton delete;

        public MyViewHolder(View v) {
            super(v);
            shortName = v.findViewById(R.id.shortName);
            regularMarketChange = v.findViewById(R.id.regularMarketChange);
            regularMarketPrice = v.findViewById(R.id.regularMarketPrice);
            regularMarketPreviousClose = v.findViewById(R.id.previousMarketPrice);
            delete = v.findViewById(R.id.delete_watch_item);
        }
    }

    public WatchListAdapter(List<WatchListItem> myDataset, Context cont, Auth auth) {
        this.auth = auth;
        this.mDataset = myDataset;
        this.context = cont;
    }

    @Override
    public WatchListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.watch_list_item, parent, false);
        return new WatchListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final WatchListAdapter.MyViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ChartsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.i("GOING CHART ACTIVITY", ">>>>>>>>>> chart set");
                i.putExtra("SYMBOL", mDataset.get(position).symbol);
                context.startActivity(i);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.user.delete_watch_list_item(mDataset.get(position).symbol);
                auth.setUser(new AuthOnCompleteUpdateInterface() {
                    @Override
                    public void onFireBaseUserUpdateSuccess() {
                        mDataset.remove(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFireBaseUserUpdateFailure() {

                    }
                }, auth.user);
            }
        });
        holder.shortName.setText(mDataset.get(position).symbol);
        holder.regularMarketPreviousClose.setText(mDataset.get(position).prev_close);
        holder.regularMarketPrice.setText(mDataset.get(position).close);
        holder.regularMarketChange.setText(mDataset.get(position).change);

    }

    @Override
    public int getItemCount() {
        if (mDataset == null) return 0;
        return mDataset.size();
    }
}

