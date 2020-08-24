package com.example.financialportfoliomanagement.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.Activities.ChartsActivity;
import com.example.financialportfoliomanagement.R;
import com.example.fpma.Models.SearchResult;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    private List<SearchResult> mDataset;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView searchName;
        public TextView searchSymbol;

        public MyViewHolder(View v) {
            super(v);
            searchName = v.findViewById(R.id.searchName);
            searchSymbol = v.findViewById(R.id.searchSymbol);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchAdapter(List<SearchResult> myDataset, Context cont) {
        mDataset = myDataset;
        context = cont;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result, parent, false);


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to the charts activity
            }
        });
        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ChartsActivity.class);
                i.putExtra("SYMBOL", mDataset.get(position).symbol);
                context.startActivity(i);
            }
        });
        holder.searchName.setText(mDataset.get(position).equityName);
        holder.searchSymbol.setText(mDataset.get(position).symbol);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
