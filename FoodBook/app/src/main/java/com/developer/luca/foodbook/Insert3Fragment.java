package com.developer.luca.foodbook;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Insert3Fragment extends Fragment {

    private Activity mainActivity;
    private View view;
    private Recipe recipe;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainActivity = getActivity();
        view = inflater.inflate(R.layout.fragment_insert3, container, false);
        recipe = ((InsertActivity) getActivity()).recipe;


        return view;
    }
}
