package com.example.moviesappstage2nano.adapterclasses;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.moviesappstage2nano.R;
import com.example.moviesappstage2nano.MovieDescription.MovieResults;
import com.example.moviesappstage2nano.Outlinedata;

import java.io.Serializable;
import java.util.List;

public class OriginalAdapter extends RecyclerView.Adapter<OriginalAdapter.ViewHolder>
{
    public static final String IMAGE_BASE_URL="http://image.tmdb.org/t/p/w185";
    public static final String SHARE="PASS_KEY";
    private Context context;
    private List<MovieResults> mresult;

    public OriginalAdapter(Context context, List<MovieResults> result) {
        this.context = context;
        this.mresult = result;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.gridlayout_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.imageView.setAdjustViewBounds(false);
        holder.imageView.setPadding(2,0,4,0);
        Glide.with(context)
                .load(IMAGE_BASE_URL+mresult.get(position).getPosterPath())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mresult.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.main_activity_imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int position=getAdapterPosition();
            MovieResults result=mresult.get(position);
            Intent intent=new Intent(context, Outlinedata.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("PASS_KEY", (Serializable) result);
            intent.putExtras(bundle);
            context.startActivity(intent);

        }
    }
}


