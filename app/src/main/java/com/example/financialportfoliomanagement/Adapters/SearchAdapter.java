package com.example.financialportfoliomanagement.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.Activities.ChartsActivity;
import com.example.financialportfoliomanagement.Auth.Auth;
import com.example.financialportfoliomanagement.Interfaces.AuthOnCompleteUpdateInterface;
import com.example.financialportfoliomanagement.Models.SearchResult;
import com.example.financialportfoliomanagement.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    private List<SearchResult> mDataset;
    private Context context;
    private Auth auth;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView searchName;
        public TextView searchSymbol;
        public ImageButton add_to_list;

        public MyViewHolder(View v) {
            super(v);
            searchName = v.findViewById(R.id.searchName);
            searchSymbol = v.findViewById(R.id.searchSymbol);
            add_to_list = v.findViewById(R.id.add_to_list);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchAdapter(List<SearchResult> myDataset, Context cont, Auth auth) {
        this.auth = auth;
        this.mDataset = myDataset;
        this.context = cont;
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
        Log.i("TAG", mDataset.get(position).name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ChartsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("SYMBOL", mDataset.get(position).symbol);
                context.startActivity(i);
            }
        });
        holder.add_to_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDataset.get(position).added_to_list == false) {
                    auth.user.add_watch_list_item(mDataset.get(position).symbol);
                    auth.setUser(new AuthOnCompleteUpdateInterface() {
                        @Override
                        public void onFireBaseUserUpdateSuccess() {
                            Toast.makeText(context, "Added to watch list", Toast.LENGTH_LONG);
                            holder.add_to_list.setImageResource(R.drawable.options_added);

                        }

                        @Override
                        public void onFireBaseUserUpdateFailure() {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG);
                        }
                    }, auth.user);
                }
            }
        });
        if (mDataset.get(position).added_to_list) {
            holder.add_to_list.setImageResource(R.drawable.options_added);

        }
        holder.searchName.setText(mDataset.get(position).name);
        holder.searchSymbol.setText(mDataset.get(position).symbol);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mDataset == null) return 0;
        return mDataset.size();
    }
}
