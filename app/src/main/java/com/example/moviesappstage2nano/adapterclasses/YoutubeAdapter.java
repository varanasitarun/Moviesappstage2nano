package com.example.moviesappstage2nano.adapterclasses;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.moviesappstage2nano.R;
import com.example.moviesappstage2nano.modellingclasses.VideoDetails;

import java.util.List;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.ViewHolderClass>
{
    public  static final String BASE_YOUTUBE_URL="https://www.youtube.com/watch?v=";
    private List<VideoDetails> list;

    public YoutubeAdapter(List<VideoDetails> mlist, Context context) {
        this.list = mlist;
        this.context = context;
    }

    private Context context;
    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderClass(LayoutInflater.from(context).inflate(R.layout.youtube_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position)
    {
        holder.name.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView name;
        public ViewHolderClass(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.youtubetext);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int position=getAdapterPosition();
            String key=list.get(position).getKey();
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_YOUTUBE_URL+key));
            context.startActivity(intent);

        }
    }
}
