package com.example.vinicius_pc.mypicasawallpapers.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vinicius_pc.mypicasawallpapers.R;
import com.example.vinicius_pc.mypicasawallpapers.activity.MainActivity;
import com.example.vinicius_pc.mypicasawallpapers.utils.PrefManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends DialogFragment {

    @Bind(R.id.et_google_username)
    EditText etGoogleUsername;
    @Bind(R.id.et_number_columns)
    EditText etNumberOfColumns;
    @Bind(R.id.btn_save_settings)
    Button btnSaveSettings;
    @Bind(R.id.btn_cancel_settings)
    Button btnCancelSettings;

    PrefManager pref;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    public static void showDialog(FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("dialog_settings");
        if(prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        new SettingsFragment().show(ft, "dialog_settings");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        pref = new PrefManager(getContext());
        ButterKnife.bind(this, view);
        getSettings();
        return view;
    }

    @OnClick(R.id.btn_save_settings)
    public void onClickSaveSettings(View view) {

        String googleUsername = etGoogleUsername.getText().toString().trim();
        int numberColumns = Integer.parseInt(etNumberOfColumns.getText().toString());

        if(numberColumns > 5) {
            Toast.makeText(getContext(), "Quantidade de colunas invalida. Maximo permitido: 5", Toast.LENGTH_SHORT).show();
        } else {
            if (googleUsername.compareTo(pref.getGoogleUserName()) == 0 && numberColumns == pref.getNoOfGridColumns()) {
                this.dismiss();
            } else {
                pref.setGoogleUsername(googleUsername);
                pref.setNoOfGridColumns(numberColumns);

                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        }

    }

    @OnClick(R.id.btn_cancel_settings)
    public void onClickCancelSettings(View view) {
        this.dismiss();
    }

    private void getSettings() {

        etGoogleUsername.setText(pref.getGoogleUserName().toString());
        etNumberOfColumns.setText(String.valueOf(pref.getNoOfGridColumns()));

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
