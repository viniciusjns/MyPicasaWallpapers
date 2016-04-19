package com.example.vinicius_pc.mypicasawallpapers.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinicius_pc.mypicasawallpapers.R;
import com.example.vinicius_pc.mypicasawallpapers.model.Category;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Vinicius-PC on 01/04/2016.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {

    private List<Category> mDataList;
    private LayoutInflater inflater;
    private Context context;
    private CategoryOnItemClickListener categoryItemClick;

    public NavigationDrawerAdapter(Context context, List<Category> data, CategoryOnItemClickListener categoryItemClick) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.mDataList = data;
        this.categoryItemClick = categoryItemClick;
    }

    public NavigationDrawerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_list_item, parent, false);
        NavigationDrawerAdapter.MyViewHolder holder = new NavigationDrawerAdapter.MyViewHolder(view);

        return holder;
    }

    public void onBindViewHolder(final NavigationDrawerAdapter.MyViewHolder holder, final int position) {
        final Category current = mDataList.get(position);

        holder.tvTitle.setText(current.getTitle());

        //
        if(categoryItemClick != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryItemClick.onClickCategory(holder.itemView, position, current.getId());
                }
            });
        }
    }

    public int getItemCount() {
        return mDataList.size();
    }

    public interface CategoryOnItemClickListener {
        void onClickCategory(View view, int position, String idAlbum);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        //ImageView imgIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            //imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            tvTitle = (TextView) itemView.findViewById(R.id.nv_title);

        }
    }
}
