package com.example.anskaal.dynamicviewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by anskaal on 2/29/16.
 */
public class MyFragment extends Fragment{


    private static final String ARG_KEY = "ARG_KEY";

    public static Fragment newInstance(int key) {

        Bundle args = new Bundle();
        MyFragment fragment = new MyFragment();
        args.putInt(ARG_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, null);

        TextView textView = (TextView) rootView.findViewById(R.id.textView);

        Bundle arguments = getArguments();
        textView.setText("Fragment " + arguments.getInt(ARG_KEY));

        return rootView;
    }
}
