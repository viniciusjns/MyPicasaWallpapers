package com.example.vinicius_pc.mypicasawallpapers.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.vinicius_pc.mypicasawallpapers.R;
import com.example.vinicius_pc.mypicasawallpapers.adapter.NavigationDrawerAdapter;
import com.example.vinicius_pc.mypicasawallpapers.controller.AppController;
import com.example.vinicius_pc.mypicasawallpapers.model.Category;
import com.example.vinicius_pc.mypicasawallpapers.utils.AppConst;
import com.example.vinicius_pc.mypicasawallpapers.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vinicius-PC on 01/04/2016.
 */
public class NavigationDrawerFragment extends Fragment {

    private String url = AppConst.URL_PICASA_ALBUMS;
    private ActionBarDrawerToggle mDrawerToogle;
    private DrawerLayout mDrawerLayout;
    private WallpaperFragment wallpaperFragment;
    private String idFirstAlbum;
    private PrefManager pref;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        pref = new PrefManager(getContext());
        makeRequest(url.replace("_GOOGLE_USERNAME_", pref.getGoogleUserName()));

        return view;
    }

    private void setUpRecyclerView(View view, List<Category> categories) {

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);
        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(getActivity(), categories, new NavigationDrawerAdapter.CategoryOnItemClickListener() {
            @Override
            public void onClickCategory(View view, int position, String idAlbum) {
                //Toast.makeText(getContext(), "id: " + idAlbum, Toast.LENGTH_SHORT).show();
                Log.i("NavDrawerFragment", "idAlbum: " + idAlbum);
                setupFragmentWallpapers(idAlbum);
                closeDrawer();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void setUpDrawer(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {

        mDrawerLayout = drawerLayout;

        mDrawerToogle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToogle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToogle.syncState();
            }
        });
    }

    private void closeDrawer() {
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void setupFragmentWallpapers(String idAlbum) {
        Bundle bundle = new Bundle();
        bundle.putString("idAlbum", idAlbum);
        wallpaperFragment = WallpaperFragment.newInstance();
        wallpaperFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, wallpaperFragment, "FragmentWallpaper").commit();
    }

    private void makeRequest(String url) {

        // Preparing volley's json object request
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("NavDrawerFragment", "Albums Response: " + response.toString());
                List<Category> albums = new ArrayList<>();
                try {
                    // Parsing the json response
                    JSONArray entry = response.getJSONObject(AppConst.TAG_FEED)
                            .getJSONArray(AppConst.TAG_ENTRY);

                    // loop through albums nodes and add them to album
                    // list
                    for (int i = 0; i < entry.length(); i++) {
                        JSONObject albumObj = (JSONObject) entry.get(i);
                        // album id
                        String albumId = albumObj.getJSONObject(
                                AppConst.TAG_GPHOTO_ID).getString(AppConst.TAG_T);

                        // album title
                        String albumTitle = albumObj.getJSONObject(
                                AppConst.TAG_ALBUM_TITLE).getString(AppConst.TAG_T);

                        Category album = new Category();
                        album.setId(albumId);
                        album.setTitle(albumTitle);

                        // add album to list
                        albums.add(album);

                        Log.d("NavDrawerFragment", "Album Id: " + albumId
                                + ", Album Title: " + albumTitle);
                    }

                    idFirstAlbum = albums.get(0).getId();
                    setupFragmentWallpapers(idFirstAlbum);
                    setUpRecyclerView(getView(), albums);

                    // Store albums in shared pref
                    //AppController.getInstance().getPrefManger().storeCategories(albums);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "JSONException: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("NavDrawerFragment", "Volley Error: " + error.getMessage());

            }
        });

        // disable the cache for this request, so that it always fetches updated
        // json
        jsonObjReq.setShouldCache(false);

        // Making the request
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

}
