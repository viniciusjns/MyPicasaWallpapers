package com.example.vinicius_pc.mypicasawallpapers.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinicius_pc.mypicasawallpapers.R;
import com.example.vinicius_pc.mypicasawallpapers.model.Category;
import com.example.vinicius_pc.mypicasawallpapers.model.Wallpaper;
import com.github.florent37.picassopalette.BitmapPalette;
import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Vinicius-PC on 18/04/2016.
 */
public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.MyViewHolder> {

    private List<Wallpaper> wallpapers;
    private Context context;
    private WallpaperOnItemClickListener wallpaperOnItemClick;

    public WallpaperAdapter(Context context, List<Wallpaper> wallpapers, WallpaperOnItemClickListener wallpaperOnItemClick) {
        this.context = context;
        this.wallpapers = wallpapers;
        this.wallpaperOnItemClick = wallpaperOnItemClick;
    }

    @Override
    public WallpaperAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_item, parent, false);
        WallpaperAdapter.MyViewHolder holder = new WallpaperAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final WallpaperAdapter.MyViewHolder holder, final int position) {

        final Wallpaper current = wallpapers.get(position);
        String description = current.getDescription();

        if(wallpaperOnItemClick != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wallpaperOnItemClick.onClickWallpaper(holder.itemView, position, holder.imgThumb);
                }
            });
        }

        Picasso.with(context).load(current.getUrl()).into(holder.imgThumb,
                PicassoPalette.with(current.getUrl(), holder.imgThumb)
                        .intoCallBack(new BitmapPalette.CallBack() {
                            @Override
                            public void onPaletteLoaded(Palette palette) {
                                holder.progressBar.setVisibility(View.GONE);
                            }
                        })
                     .use(PicassoPalette.Profile.MUTED)
                        .intoBackground(holder.legenda)
                     .use(PicassoPalette.Profile.VIBRANT)
                        .intoBackground(holder.legenda, PicassoPalette.Swatch.RGB));

        holder.tvTitle.setText((description.compareTo("") != 0) ? description : "Wallpaper " + (position + 1));
    }

    @Override
    public int getItemCount() {
        return wallpapers.size();
    }

    public interface WallpaperOnItemClickListener {
        void onClickWallpaper(View view, int position, ImageView imageView);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgThumb;
        TextView tvTitle;
        LinearLayout legenda;
        ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);

            imgThumb = (ImageView) itemView.findViewById(R.id.img_thumb);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            legenda = (LinearLayout) itemView.findViewById(R.id.legenda);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        }
    }
}
