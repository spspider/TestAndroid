package com.example.sp_1.iotmymanager.util;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.sp_1.iotmymanager.Activitys.BaseActivity;
import com.example.sp_1.iotmymanager.Activitys.MainDevicesPager;
import com.example.sp_1.iotmymanager.Fragments.FragmentActivityA;
import com.example.sp_1.iotmymanager.NotInPackage.MainActivity;
import com.example.sp_1.iotmymanager.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import static android.R.attr.fragment;
import static android.R.attr.id;
import static android.media.CamcorderProfile.get;

import static com.example.sp_1.iotmymanager.Activitys.MainDevicesPager.adapter;
import static com.example.sp_1.iotmymanager.R.id.myLiLa;

import static java.lang.Integer.parseInt;


/**
 * Created by sp_1 on 12.01.2017.
 */

public class CreateListItem {
    private GlobalClass app;
    private Activity activity;
    private MainActivity getMainactivity2;
    //private FragmentActivityA fragmentActivityA;
    private Fragment fragmentMy;
    private Fragment myFragment;
    static HashMap<Integer,LayoutInflater> ltInflater = new HashMap<>();
    LinearLayout linLayout;
    private static Button btnNew[] = new Button[20];
    private ArrayList<HashMap<String, String>> contactList;
    private static HashMap<Integer, String> AllTopic=new HashMap<>();
    private static HashMap<Integer, String> AllDescr=new HashMap<>();
    private static HashMap<Integer, Button> BtnList=new HashMap<>();
    private static HashMap<Integer,SeekBar> SeekList=new HashMap<>();
    public static HashMap<Integer,ToggleButton> TglList=new HashMap<>();

    //HashMap<String, String> AllTopic = new HashMap<>();

    //private MainActivity getMainactivity;

    //////////////////////////////////////////////////
    private static String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + ":" + position;
    }
    /*
public Fragment getFragment(int page){
    app = GlobalClass.getInstance();
    fragmentMy = (FragmentActivityA) app.getFragment();
    if (fragmentMy==null){ Log.d(MainActivity.TAG,"viewPagerFragmentNULL!!!:");return null;}
        String name = makeFragmentName(R.id.pager, page);
    //Log.d(MainActivity.TAG,"found:"+name);

        Fragment viewPagerFragment = fragmentMy.getFragmentManager().findFragmentByTag(name);
        if(viewPagerFragment != null) {
            return viewPagerFragment;
        }
        else{
            Log.d(MainActivity.TAG,"viewPagerFragmentNULL!!!:");
        }

    return null;
}
    */
    public Fragment getFragmentStatic(int page){

        Fragment myFragment = MainDevicesPager.mFragments.get(page);
        return myFragment;
    }
///////////////////////////////////////////////

    public void createInflater(int Page){



    }

    public void printAllHashMapMap(HashMap<String, String> map) {
        ///создать страницу

        if(!MainDevicesPager.mFragmentsName.contains(map.get("page"))) {
            //if (MyPagerAdapter_imp.mFragmentsName.)
            int NewPage = MainDevicesPager.mFragments.size();
            MainDevicesPager.mFragmentsName.add(map.get("page"));//добавим имя фрагмента
            Fragment AddFragment = FragmentActivityA.newInstance(NewPage);
            MainDevicesPager.mFragments.add(AddFragment);//добавляем фрагмент
            //ViewPager Vpager = (ViewPager) findViewById(R.id.pager);
            //adapter = new MyPagerAdapter_imp( getActivtiy().getSupportFragmentManager());
            adapter.setItems(MainDevicesPager.mFragments);
            adapter.notifyDataSetChanged();
            //adapter.set
            //создаем новый вид
            //fragmentMy = (FragmentActivityA) getFragmentStatic(page);
            //fragmentMy = (FragmentActivityA) getFragmentStatic(NewPage);
            ltInflater.put(NewPage,AddFragment.getActivity().getLayoutInflater());

        }

        ////////////////////////

        //if (page==null){;}

        int page = MainDevicesPager.mFragmentsName.indexOf(map.get("page"));
        fragmentMy = MainDevicesPager.mFragments.get(page);
        //fragmentMy = (FragmentActivityA) getFragmentStatic(page);
        linLayout = (LinearLayout) fragmentMy.getView().findViewById(myLiLa);
        ////////////////////////////////////////////////////////////
        //Log.d(BaseActivity.TAG,"page:"+map.get("page")+" found:"+page+"InflaterSize:"+ltInflater.size());

        View item = ltInflater.get(page).inflate(R.layout.item, linLayout, false);

        //for(Map<String, String> map : values) {

            String widget = map.get("widget");
            int id = parseInt(map.get("id"));
            String topic = map.get("topic");
            //AllTopic.add(map.get("id"),topic);
            AllTopic.put(id,topic);
            AllDescr.put(id,map.get("descr"));
        if (widget!=null) {
            switch (widget) {
                case "button":

                    //String topic =map.get("topic");
                    //Map<String, String>widjects ;

                    Button btnNew = new Button(fragmentMy.getActivity());
                    //Button Btn = new Button(fragmentMy.getActivity());
                    btnNew.setText(AllDescr.get(id));
                    btnNew.setOnClickListener(new ClickListener(btnNew.getId(), map.get("id")));
                    btnNew.setId(id);

                    linLayout.addView(btnNew, item.getLayoutParams());
                    BtnList.put(id, btnNew);
                    break;
                case "toggle":

                    //String topic =map.get("topic");
                    //Map<String, String>widjects ;

                    ToggleButton tglBtn = new ToggleButton(fragmentMy.getActivity());
                    //Button Btn = new Button(fragmentMy.getActivity());
                    tglBtn.setText(AllDescr.get(id));
                    //tglBtn.isChecked();
                    tglBtn.setOnClickListener(new ClickListener(tglBtn.getId(), map.get("id")));
                    tglBtn.setId(id);
                    tglBtn.setTextOn(AllDescr.get(id) + " Включен");
                    tglBtn.setTextOff(AllDescr.get(id) + " Выключен");
                    linLayout.addView(tglBtn, item.getLayoutParams());
                    TglList.put(id, tglBtn);
                    break;
                case "range":
                    SeekBar seekBar = new SeekBar(fragmentMy.getActivity());
                    //seekBar.set
                    //seekBar.
                    seekBar.setProgress(0);
                    seekBar.setMax(1024);
                    seekBar.setOnSeekBarChangeListener(new ClickListener(seekBar.getId(), map.get("id")));

                    linLayout.addView(seekBar, item.getLayoutParams());
                    SeekList.put(id, seekBar);
                    //Log.d(MainActivity.TAG, "new seekBar");
                    break;
            }
        }

            TextView tvName = (TextView) item.findViewById(R.id.tvName);
            tvName.setText(map.get("descr"));
            TextView tvPosition = (TextView) item.findViewById(R.id.tvPosition);
            tvPosition.setText("page: " + (map.get("page")));
            TextView tvSalary = (TextView) item.findViewById(R.id.tvSalary);
            tvSalary.setText("widget: " + (map.get("widget")));
            item.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            //item.setBackgroundColor(colors[i % 2]);
            linLayout.addView(item);

        ////////////////////////////////////////////////////////////

    }


    public void setStatusWSMap(HashMap<String,String> map) {

        fragmentMy = (FragmentActivityA) getFragmentStatic(0);//используется только для лога
        //fragmentMy.myLogB("sTopic"+map.get("sTopic"),0);
        //fragmentMy.myLogB("AllTopic:"+AllTopic.get(0),0);
        //fragmentMy.myLogB("newstatus:"+map.get("status"),0);
        for(int id=0;id<AllTopic.size();id++) {
            if (!AllTopic.get(id).equals(null)) {
                //Log.d(MainActivity.TAG,"AllTopic.get:"+AllTopic.get(id)+" map.get:"+map.get("sTopic"));
                if ((AllTopic.get(id)+"/status").equals(map.get("sTopic"))) {
                    //k=i;
                    if (BtnList.containsKey(id)) {
                        Button thisButton = BtnList.get(id);
                        if (!thisButton.getText().equals("")) {
                            thisButton.setText(AllDescr.get(id) + map.get("status").toString());
                            //Log.d(MainActivity.TAG, "new text setted");
                            //fragmentMy.myLogB("new text:" + AllDescr.get(id) + map.get("status").toString(), 0);
                        }
                        break;
                    }

                    if (SeekList.containsKey(id)) {
                        SeekBar thisSeekList = SeekList.get(id);
                        thisSeekList.setProgress(parseInt(map.get("status")));
                        break;
                    }
                    if (TglList.containsKey(id)) {
                        ToggleButton thisTglBtn = TglList.get(id);
                        if (parseInt(map.get("status")) == 1) {
                            thisTglBtn.setChecked(true);
                        }
                        if (parseInt(map.get("status")) == 0) {
                            thisTglBtn.setChecked(false);
                        }
                        //thisTglBtn.
                        thisTglBtn.setText(AllDescr.get(id) + map.get("status").toString());
                        Log.d(MainActivity.TAG, AllDescr.get(id) + "new text setted");
                        //fragmentMy.myLogB("new text:" + AllDescr.get(id) + map.get("status").toString(), 0);

                        break;
                    }
                }
            }
        }


            /*
            for (int i=0;i<AllTopic.length;i++){
                if (map.get("sTopic")==AllTopic[i]){
                    if (btnNew[i]!=null){
                        btnNew[i].setText(AllDescr[i]+map.get("status").toString());}
                    else{
                        Log.d(MainActivity.TAG,"button is null");
                    }
                    return;
                }

            }
*/
        //if (statusWS.get)
    }



}
