package com.example.vinicius_pc.mypicasawallpapers.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.vinicius_pc.mypicasawallpapers.R;
import com.example.vinicius_pc.mypicasawallpapers.model.Wallpaper;
import com.example.vinicius_pc.mypicasawallpapers.utils.Utils;
import com.github.florent37.picassopalette.BitmapPalette;
import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WallpaperDetailActivity extends AppCompatActivity {

    @Bind(R.id.fab_plus)
    FloatingActionButton fab_plus;
    @Bind(R.id.fab_set_wallpaper)
    FloatingActionButton fabSetWallpaper;
    @Bind(R.id.fab_share)
    FloatingActionButton fabShare;

    @Bind(R.id.img_thumb)
    ImageView imageView;
    @Bind(R.id.legenda)
    LinearLayout legenda;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    Animation fabClose;
    Animation fabOpen;
    Animation rotateForward;
    Animation rotateBackward;

    private boolean isFabOpen = false;
    private Wallpaper wallpaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_detail);

        ButterKnife.bind(this);

        bindAnimations();
        getParams();
        setupValues();
    }

    private void setupValues() {
        Picasso.with(this).load(wallpaper.getUrl()).into(imageView,
                PicassoPalette.with(wallpaper.getUrl(), imageView)
                        .use(PicassoPalette.Profile.MUTED)
                        .intoBackground(legenda)
                        .use(PicassoPalette.Profile.VIBRANT)
                        .intoBackground(legenda, PicassoPalette.Swatch.RGB));
        tvTitle.setText(wallpaper.getDescription());

        YoYo.with(Techniques.BounceInDown)
                .duration(1000)
                .playOn(legenda);
    }

    private void getParams() {
        wallpaper = (Wallpaper) getIntent().getSerializableExtra("wallpaper");
    }

    private void bindAnimations() {
        fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close);
        fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        rotateForward = AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this,R.anim.rotate_backward);
    }

    @OnClick(R.id.fab_plus)
    public void onClickFab(View view) {

        animateFab();

    }

    @OnClick(R.id.fab_set_wallpaper)
    public void onClickSetWallpaper(View view) {

        setImageAsWallpaper();

    }

    public void animateFab(){

        if(isFabOpen){

            fab_plus.startAnimation(rotateBackward);
            fabShare.startAnimation(fabClose);
            fabSetWallpaper.startAnimation(fabClose);
            fabShare.setClickable(false);
            fabSetWallpaper.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab_plus.startAnimation(rotateForward);
            fabShare.startAnimation(fabOpen);
            fabSetWallpaper.startAnimation(fabOpen);
            fabShare.setClickable(true);
            fabSetWallpaper.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");

        }
    }

    private void setImageAsWallpaper() {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        Utils utils = new Utils(this);
        utils.setAsWallpaper(bitmap);
    }
}
