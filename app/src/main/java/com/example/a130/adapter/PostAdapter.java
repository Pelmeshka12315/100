package com.example.a130.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a130.DbManager;
import com.example.a130.MainActivity;
import com.example.a130.NewPost;
import com.example.a130.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolderData> {
    private List<NewPost> arrayPost;
    private Context context;
    private OnItemClickCustom onItemClickCustom;
    private DbManager dbManager;

    public PostAdapter(List<NewPost> arrayPost, Context context, OnItemClickCustom onItemClickCustom)
    {
        this.arrayPost = arrayPost;
        this.context = context;
        this.onItemClickCustom = onItemClickCustom;
        this.dbManager = dbManager;
    }

    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ads,parent,false);
        return new ViewHolderData(view,onItemClickCustom);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderData holder, int position) {
        holder.setData(arrayPost.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayPost.size();
    }

    public class  ViewHolderData extends RecyclerView.ViewHolder implements  View.OnClickListener
    {
        public TextureView tvPiceTel, tvDisc, tvTitle;
        private ImageView imAds;
        private OnItemClickCustom onItemClickCustom;
        private LinearLayout edit_layout;
        private ImageButton deleteButton;

        public ViewHolderData(@NonNull View itemView, OnItemClickCustom onItemClickCustom) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPiceTel = itemView.findViewById(R.id.tvPriceTel);
            tvDisc = itemView.findViewById(R.id.tvDisk);
            imAds = itemView.findViewById(R.id.imAds);
            edit_layout = itemView.findViewById(R.id.edit_layout);
            deleteButton = itemView.findViewById(R.id.imDelete);
            itemView.setOnClickListener(this);
            this.onItemClickCustom = onItemClickCustom;
        }
        public void setData(NewPost newPost)
        {
            if(newPost.getUid().equals(MainActivity.MAUTH))
            {
                edit_layout.setVisibility(View.VISIBLE);
            }
            else
            {
                edit_layout.setVisibility(View.GONE);
            }
            Picasso.get().load(newPost.getImageId()).into(imAds);
            tvTitle.setText(newPost.getTitle());
            String price_tel = "Цена :" + newPost.getPrice() + " Тел : " + newPost.getTel();
            tvPiceTel.setText(price_tel);
            String textDisc = null;
            if(newPost.getDick().length() > 50) textDisc = newPost.getDick().substring(0,50) + "...";
            tvDisc.setText(textDisc);

            deleteButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dbManager.deleteItem(newPost);
                    arrayPost.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
                onItemClickCustom.onItemSelected(getAdapterPosition());
        }
    }
    public interface OnItemClickCustom
    {
        public void onItemSelected(int position);
    }
    public void updateAdapter(List<NewPost> ListData)
    {
         arrayPost.clear();
         arrayPost.addAll(ListData);
         notifyDataSetChanged();
    }
    public void setDbManager(DbManager dbManager)
    {
        this.dbManager = dbManager;
    }
}
