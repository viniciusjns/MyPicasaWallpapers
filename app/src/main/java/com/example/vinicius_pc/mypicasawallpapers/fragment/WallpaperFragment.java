package com.example.vinicius_pc.mypicasawallpapers.fragment;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.vinicius_pc.mypicasawallpapers.R;
import com.example.vinicius_pc.mypicasawallpapers.adapter.NavigationDrawerAdapter;
import com.example.vinicius_pc.mypicasawallpapers.adapter.WallpaperAdapter;
import com.example.vinicius_pc.mypicasawallpapers.controller.AppController;
import com.example.vinicius_pc.mypicasawallpapers.model.Category;
import com.example.vinicius_pc.mypicasawallpapers.model.Wallpaper;
import com.example.vinicius_pc.mypicasawallpapers.utils.AppConst;
import com.example.vinicius_pc.mypicasawallpapers.utils.PrefManager;
import com.example.vinicius_pc.mypicasawallpapers.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WallpaperFragment extends Fragment {

    private String TAG = "FragmentWallpaper";

    private RecyclerView mRecyclerView;
    private WallpaperAdapter wallpaperAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String url;
    private ProgressDialog pDialog;
    private PrefManager pref;

    //private static WallpaperFragment wallpaperFragment;

    public WallpaperFragment() {
        // Required empty public constructor
    }

    public static WallpaperFragment newInstance() {
        return new WallpaperFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallpaper, container, false);
        pref = new PrefManager(getContext());

        getParams();
        setupProgressDialog();
        //setupRecyclerView(wallpapers);
        makeRequest(url);

        return view;
    }

    private void getParams() {
        Bundle bundle = getArguments();
        String idAlbum = bundle.getString("idAlbum");
        url = AppConst.URL_ALBUM_PHOTOS.replace("_GOOGLE_USERNAME_", pref.getGoogleUserName()).replace("_ALBUM_ID_", idAlbum);
    }

    private void setupRecyclerView(View view, List<Wallpaper> wallpapers) {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), pref.getNoOfGridColumns());
        wallpaperAdapter = new WallpaperAdapter(getContext(), wallpapers, new WallpaperAdapter.WallpaperOnItemClickListener() {
            @Override
            public void onClickWallpaper(View view, int position, ImageView imageView) {
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                Utils utils = new Utils(getContext());
                utils.setAsWallpaper(bitmap);

            }
        });
        mRecyclerView.setAdapter(wallpaperAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    private void setupProgressDialog() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
    }

    private void showProgressDialog() {
        if(!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if(pDialog.isShowing())
            pDialog.dismiss();
    }

    private void makeRequest(String url) {

        showProgressDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG,
                        "List of photos json reponse: "
                                + response.toString());
                try {
                    // Parsing the json response
                    List<Wallpaper> photosList = new ArrayList<>();
                    JSONArray entry = response.getJSONObject(AppConst.TAG_FEED)
                            .getJSONArray(AppConst.TAG_ENTRY);

                    // looping through each photo and adding it to list
                    // data set
                    for (int i = 0; i < entry.length(); i++) {
                        JSONObject photoObj = (JSONObject) entry.get(i);
                        JSONArray mediacontentArry = photoObj
                                .getJSONObject(AppConst.TAG_MEDIA_GROUP)
                                .getJSONArray(AppConst.TAG_MEDIA_CONTENT);

                        if (mediacontentArry.length() > 0) {
                            JSONObject mediaObj = (JSONObject) mediacontentArry.get(0);
                            Log.i(TAG, "***PHOTO OBJ**** " + photoObj.toString());
                            Log.i(TAG, "***MEDIA OBJ**** " + mediacontentArry.toString());

                            String url = mediaObj.getString(AppConst.TAG_IMG_URL);

                            /*String photoJson = photoObj.getJSONObject(AppConst.TAG_ID).getString(AppConst.TAG_T) + "&imgmax=d";

                            int width = mediaObj.getInt(AppConst.TAG_IMG_WIDTH);
                            int height = mediaObj.getInt(AppConst.TAG_IMG_HEIGHT);*/

                            String descriptionContent = photoObj
                                    .getJSONObject(AppConst.TAG_MEDIA_GROUP)
                                    .getJSONObject(AppConst.TAG_MEDIA_DESCRIPTION)
                                    .getString(AppConst.TAG_T).toString();

                            Wallpaper p = new Wallpaper(url, descriptionContent);

                            // Adding the photo to list data set
                            photosList.add(p);
                            hideProgressDialog();

                            Log.d(TAG, "Photo: " + url);
                        }
                    }

                    //resetWallpaperAdapter(photosList);
                    setupRecyclerView(getView(), photosList);


                    // Notify list adapter about dataset changes. So
                    // that it renders grid again
                    wallpaperAdapter.notifyDataSetChanged();


                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        // Remove the url from cache
        AppController.getInstance().getRequestQueue().getCache().remove(url);

        // Disable the cache for this url, so that it always fetches updated
        // json
        jsonObjReq.setShouldCache(false);

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

}
