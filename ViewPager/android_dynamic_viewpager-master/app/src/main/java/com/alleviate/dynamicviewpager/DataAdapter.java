package com.alleviate.dynamicviewpager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by felix on 9/6/16.
 * Created at Alleviate.
 * shirishkadam.com
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    ArrayList<MovieInfo> mcu_movies;
    Context context;

    public DataAdapter(Context context, ArrayList<MovieInfo> mcu_movies) {
        this.mcu_movies = mcu_movies;
        this.context = context;

    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataAdapter.ViewHolder holder, final int position) {

        ((RelativeLayout) holder.itemView.findViewById(R.id.relative_layout_update)).setVisibility(View.GONE);

        ((TextView) holder.itemView.findViewById(R.id.title)).setText(mcu_movies.get(position).mname);
        ((TextView) holder.itemView.findViewById(R.id.phase)).setText(mcu_movies.get(position).mphase);

        /*((RelativeLayout) holder.itemView.findViewById(R.id.relative_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(mcu_movies.get(position).toString());
            }
        });*/

        ((RelativeLayout) holder.itemView.findViewById(R.id.relative_layout)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                ((RelativeLayout) holder.itemView.findViewById(R.id.relative_layout_update)).setVisibility(View.VISIBLE);
                //((RelativeLayout) holder.itemView.findViewById(R.id.relative_layout_update)).setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                ((RelativeLayout) holder.itemView.findViewById(R.id.relative_layout)).setVisibility(View.GONE);

                ((TextView) holder.itemView.findViewById(R.id.p1)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        update(mcu_movies.get(position).mid,"Phase 1");
                        remove(position);
                    }
                });

                ((TextView) holder.itemView.findViewById(R.id.p2)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        update(mcu_movies.get(position).mid,"Phase 2");
                        remove(position);
                    }
                });

                ((TextView) holder.itemView.findViewById(R.id.p3)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        update(mcu_movies.get(position).mid,"Phase 3");
                        remove(position);
                    }
                });

                ((TextView) holder.itemView.findViewById(R.id.p4)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        update(mcu_movies.get(position).mid,"Phase 4");
                        remove(position);
                    }
                });

                //remove(mcu_movies.get(position).toString());

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mcu_movies.size();
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(int position) {

        notifyItemRemoved(position);

        MainActivity.mSectionsPagerAdapter.notifyDataSetChanged();

    }

    /*
    public void insert(int position) {
        mcu_movies.add(position, "New Movie");
        notifyItemInserted(position);
        //getItemId(position);
    }*/

    public void update(int mid, String dphase) {
        SQLiteHelper db = new SQLiteHelper(context);
        SQLiteDatabase dbw = db.getWritableDatabase();

        ContentValues update_values = new ContentValues();
        update_values.put(db.db_mcu_phase, dphase);

        Toast.makeText(context,"Status changed to "+dphase,Toast.LENGTH_LONG).show();

        int res = dbw.update( db.db_mcu, update_values, db.db_mcu_id+ " = ? ", new String[]{String.valueOf(mid)});
        Log.d("=Database","Data Updated Id "+res+" Phase: "+dphase);

        dbw.close();
        db.close();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
