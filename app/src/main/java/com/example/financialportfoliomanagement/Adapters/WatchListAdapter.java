package com.example.financialportfoliomanagement.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.Auth.Auth;
import com.example.financialportfoliomanagement.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.financialportfoliomanagement.Interfaces.AuthOnCompleteUpdateInterface;
import com.example.financialportfoliomanagement.Interfaces.WatchListDataRetrieveInterface;
import com.example.financialportfoliomanagement.Models.WatchListItem;
import com.example.financialportfoliomanagement.NetworkCalls.WatchListAsyncTask;
import com.example.financialportfoliomanagement.R;

import java.util.List;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.MyViewHolder> {
    public List<WatchListItem> mDataset;
    public Context context;
    public Auth auth;
    public ProgressDialog progressDialog;
    public WatchListAsyncTask watchListAsyncTask;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public void refresh() {
        progressDialog.setTitle("loading...");
        progressDialog.show();
        Log.i("Tag", ">>>>>>>>>>>>>>>>>>>>>>>>>>refreshing");
        auth.getUser(new AuthOnCompleteRetreiveInterface() {
            @Override
            public void onFireBaseUserRetrieveSuccess() {
                Log.i("Tag", ">>>>>>>>>>>>>>>>>>>>>>>>>>fetching user");
//                progressDialog = new ProgressDialog(context);
                watchListAsyncTask = new WatchListAsyncTask(context, auth.user.getWatch_list_symbols(), progressDialog);
                watchListAsyncTask.setWatchListDataRetrieveInterface(new WatchListDataRetrieveInterface() {
                    @Override
                    public void onDataFetched(List<WatchListItem> listItems) {
                        mDataset = listItems;
                        progressDialog.cancel();
                        Log.i("Tag", ">>>>>>>>>>>>>>>>>>>>>>>>>>changing data");
                        notifyDataSetChanged();

                    }
                });
                watchListAsyncTask.execute(1);
            }

            @Override
            public void onFireBaseUserRetrieveFailure() {

            }
        });
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

    // Provide a suitable constructor (depends on the kind of dataset)
    public WatchListAdapter(List<WatchListItem> myDataset, Context cont, Auth auth, ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
        this.auth = auth;
        this.mDataset = myDataset;
        this.context = cont;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WatchListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.watch_list_item, parent, false);


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to the charts activity
            }
        });
        return new WatchListAdapter.MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final WatchListAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(context, ChartsActivity.class);
//                i.putExtra("SYMBOL", mDataset.get(position));
//                context.startActivity(i);
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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mDataset == null) return 0;
        return mDataset.size();
    }
}

