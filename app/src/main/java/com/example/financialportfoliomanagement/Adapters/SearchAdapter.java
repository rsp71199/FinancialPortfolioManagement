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
    public static class MyViewHolder extends RecyclerView.ViewHolder {
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

    public SearchAdapter(List<SearchResult> myDataset, Context cont, Auth auth) {
        this.auth = auth;
        this.mDataset = myDataset;
        this.context = cont;
    }

    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result, parent, false);


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.i("TAG", mDataset.get(position).name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ChartsActivity.class);
                i.putExtra("SYMBOL", mDataset.get(position).symbol);
                context.startActivity(i);
            }
        });
        holder.add_to_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDataset.get(position).added_to_list == false) {
                    if(auth.user.add_watch_list_item(mDataset.get(position).symbol)){
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
                    }else{
                        Toast.makeText(context, "Limit reached", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        if (mDataset.get(position).added_to_list) {
            holder.add_to_list.setImageResource(R.drawable.options_added);

        }
        holder.searchName.setText(mDataset.get(position).name);
        holder.searchSymbol.setText(mDataset.get(position).symbol);

    }

    @Override
    public int getItemCount() {
        if (mDataset == null) return 0;
        return mDataset.size();
    }
}
