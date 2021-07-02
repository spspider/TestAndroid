package com.example.filteredlistview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HighlightArrayAdapter extends ArrayAdapter<String> {
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final int mResource;
    private List<String> mObjects;
    private int mFieldId = 0;
    private ArrayList<String> mOriginalValues;
    private ArrayFilter mFilter;
    private final Object mLock = new Object();
    private String mSearchText; // this var for highlight
    ArrayList<Integer> startPos=new ArrayList<>();
    ArrayList<Integer> endPos=new ArrayList<>();

    public HighlightArrayAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        mObjects = Arrays.asList(objects);
        mFieldId = textViewResourceId;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public String getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public int getPosition(String item) {
        return mObjects.indexOf(item);
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            mSearchText = prefix.toString().toLowerCase();
            Log.d("my_logs","!prefix:"+mSearchText);
            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(mObjects);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                mSearchText = "";
                ArrayList<String> list;
                synchronized (mLock) {
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();
                //mSearchText = prefixString;
                ArrayList<String> values;
                synchronized (mLock) {
                    values = new ArrayList<>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<String> newValues = new ArrayList<>();
                //startPos.clear();endPos.clear();

                for (int i = 0; i < count; i++) {
                    final String value = values.get(i);
                    String valueText = value.toString().toLowerCase();

                    valueText = value.toString().toLowerCase().replace('\r', ' ').replace('\n', ' ');

// First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        // Break the prefix into "words"
                        final String[] prefixes = prefixString.split(" ");
                        final int prefixCount = prefixes.length;
                        // HERE CHANGE
                        if (prefixCount>0) {
                            int loc;
                            int end;
                            // Find the first "word" in prefix
                            if (valueText.contains(prefixes[0]) || (loc = valueText.indexOf(' ' + prefixes[0])) > -1)
                                loc = valueText.indexOf(prefixes[0]);
                                end=loc+prefixes.length;
                            // Find the following "words" in order
                            for (int j = 1; j < prefixCount && loc > -1; j++){
                                loc = valueText.indexOf(' ' + prefixes[j], loc + 2);  end=loc+prefixes[j].length();}

                            // If every "word" is in this row, add it to the results
                            if (loc > -1){
                                newValues.add(value);startPos.add(loc);endPos.add(end);}
                        }
                    }

                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<String>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        TextView text;

        if (convertView == null) {
            view = mInflater.inflate(mResource, parent, false);
        } else {
            view = convertView;
        }

        try {
            if (mFieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = (TextView) view.findViewById(mFieldId);
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }
        
        // HIGHLIGHT...
        //text.spa
        String fullText = getItem(position);

        //Log.d("my_logs","fulltext:"+fullText);
        //Log.d("my_logs","search:"+mSearchText);
        //Log.d("my_logs","startPos:"+startPos.toString()+"endPos:"+ endPos.toString());
        final SpannableStringBuilder sb = new SpannableStringBuilder(fullText);
        if (mSearchText != null && !mSearchText.isEmpty()) {
            String[] prefixes2 = mSearchText.split(" ");
            //Log.d("my_logs","prefixes.length:"+prefixes2.length+"text"+ Arrays.toString(prefixes2));

                for (int i = 0; i < prefixes2.length; i++) {
                    mSearchText = prefixes2[i];
                    int startPos = fullText.toLowerCase().indexOf(mSearchText);
                    int endPos = startPos + mSearchText.length();
                    //Spannable spannable = new SpannableString(fullText);
                    ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.BLUE});
                    TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                    sb.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //Log.d("my_logs", "set_span:" + i);
                }
                //Log.d("my_logs", "set_text:");
                text.setText(sb);

        } else {
            text.setText(sb);
        }

        return view;
    }

}
