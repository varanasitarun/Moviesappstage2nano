package com.example.moviesappstage2nano.adapterclasses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviesappstage2nano.R;
import com.example.moviesappstage2nano.modellingclasses.ReviewDetails;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>
{
    private List<ReviewDetails> dataList;

    public ReviewAdapter(List<ReviewDetails> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    private Context context;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.review_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        holder.author.setText(dataList.get(position).getAuthor());
        holder.comment.setText(dataList.get(position).getComment());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView author;
        TextView comment;
        private ViewHolder(View itemView) {
            super(itemView);
            author=itemView.findViewById(R.id.id_author);
            comment=itemView.findViewById(R.id.id_comment);
        }
    }
}
