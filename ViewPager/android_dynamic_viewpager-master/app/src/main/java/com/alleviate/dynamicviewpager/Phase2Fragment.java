package com.alleviate.dynamicviewpager;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Phase2Fragment extends Fragment {


    public Phase2Fragment() {
        // Required empty public constructor
    }


    RecyclerView.Adapter rvadpter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View frag_view = inflater.inflate(R.layout.fragment_phase3, container, false);

        RecyclerView rv = (RecyclerView)frag_view.findViewById(R.id.recycler_view);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager rvlayoutmanager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(rvlayoutmanager);

        final ArrayList<MovieInfo> mcu_movies = new ArrayList<MovieInfo>();

        SQLiteHelper db = new SQLiteHelper(getActivity());
        SQLiteDatabase dbr = db.getReadableDatabase();

        String[] columns = {
                db.db_mcu_id,
                db.db_mcu_name,
                db.db_mcu_phase,
        };

        String[] selection_args = {"Phase 2"};

        Cursor cur = dbr.query(db.db_mcu, columns, db.db_mcu_phase +" =? ", selection_args, null, null, null);

        if(cur != null){
            while (cur.moveToNext()){

                int mid = cur.getInt(cur.getColumnIndex(db.db_mcu_id));
                String mname = cur.getString(cur.getColumnIndex(db.db_mcu_name));
                String mphase = cur.getString(cur.getColumnIndex(db.db_mcu_phase));


                mcu_movies.add(new MovieInfo(mid, mname, mphase));

            }cur.close();
        }

        rvadpter = new DataAdapter(getActivity(), mcu_movies);
        rv.setAdapter(rvadpter);

        rv.setItemAnimator(new DefaultItemAnimator());

        return frag_view;
    }

}
