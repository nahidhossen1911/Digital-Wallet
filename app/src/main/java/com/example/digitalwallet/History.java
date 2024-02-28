package com.example.digitalwallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class History extends AppCompatActivity {


    ListView listView;
    ImageView backbtn;
    TextView totalbtn,incomebtn,expensebtn;

    HashMap<String,String> hashMap;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        backbtn = findViewById(R.id.backbtn);
        totalbtn = findViewById(R.id.totalbtn);
        incomebtn = findViewById(R.id.incomebtn);
        expensebtn = findViewById(R.id.exensebtn);
        listView = findViewById(R.id.listview);

        SQLite sqLite = new SQLite(History.this);
        recevData(sqLite,listView,"all");

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        totalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                incomebtn.setTextColor(getResources().getColor(R.color.orange));
                incomebtn.setBackgroundResource(R.drawable.expenseback);

                expensebtn.setTextColor(getResources().getColor(R.color.orange));
                expensebtn.setBackgroundResource(R.drawable.expenseback);

                totalbtn.setTextColor(getResources().getColor(R.color.white));
                totalbtn.setBackgroundResource(R.drawable.incomeback);

                recevData(sqLite,listView,"all");

            }
        });

        incomebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                totalbtn.setTextColor(getResources().getColor(R.color.orange));
                totalbtn.setBackgroundResource(R.drawable.expenseback);

                expensebtn.setTextColor(getResources().getColor(R.color.orange));
                expensebtn.setBackgroundResource(R.drawable.expenseback);

                incomebtn.setTextColor(getResources().getColor(R.color.white));
                incomebtn.setBackgroundResource(R.drawable.incomeback);
                recevData(sqLite,listView,"in");

            }
        });

        expensebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                totalbtn.setTextColor(getResources().getColor(R.color.orange));
                totalbtn.setBackgroundResource(R.drawable.expenseback);

                incomebtn.setTextColor(getResources().getColor(R.color.orange));
                incomebtn.setBackgroundResource(R.drawable.expenseback);

                expensebtn.setTextColor(getResources().getColor(R.color.white));
                expensebtn.setBackgroundResource(R.drawable.incomeback);
                recevData(sqLite,listView,"ex");

            }
        });



    }
    public class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = LayoutInflater.from(History.this).inflate(R.layout.item,parent,false);

            TextView title = view.findViewById(R.id.title);
            TextView date = view.findViewById(R.id.date);
            TextView amount = view.findViewById(R.id.amount);
            ImageView type  = view.findViewById(R.id.type);

            HashMap<String,String> hashMap = arrayList.get(position);
            title.setText(""+hashMap.get("title"));
            date.setText(""+hashMap.get("date"));

            String stype = hashMap.get("type");
            if (stype.equals("in")){
                amount.setText("+ "+hashMap.get("mony")+" tk");
                amount.setTextColor(getResources().getColor(R.color.green));
                type.setImageResource(R.drawable.up);
            }else {
                amount.setText("- "+hashMap.get("mony")+" tk");
                amount.setTextColor(getResources().getColor(R.color.red));
                type.setImageResource(R.drawable.down);
            }

            return view;
        }
    }

    public void recevData(SQLite sqLite, ListView listView,String TYPE){

        arrayList = new ArrayList<>();
        Cursor cursor = sqLite.getShowData();
        if(cursor!=null && cursor.getCount()>0) {


            while (cursor.moveToNext()) {

                int id = cursor.getInt(0);
                String type = cursor.getString(1);
                String title = cursor.getString(2);
                String date = cursor.getString(3);
                String mony = cursor.getString(4);

                if(TYPE.equals("all")){
                    HashMap hashMap = new HashMap();
                    hashMap.put("id", id);
                    hashMap.put("type", type);
                    hashMap.put("title", title);
                    hashMap.put("date", date);
                    hashMap.put("mony", mony);
                    arrayList.add(hashMap);

                }
                else if (TYPE.equals("in")) {
                    if(type.equals("in")){
                        HashMap hashMap = new HashMap();
                        hashMap.put("id", id);
                        hashMap.put("type", type);
                        hashMap.put("title", title);
                        hashMap.put("date", date);
                        hashMap.put("mony", mony);
                        arrayList.add(hashMap);
                    }
                }
                else if (TYPE.equals("ex")) {
                    if(type.equals("ex")){
                        HashMap hashMap = new HashMap();
                        hashMap.put("id", id);
                        hashMap.put("type", type);
                        hashMap.put("title", title);
                        hashMap.put("date", date);
                        hashMap.put("mony", mony);
                        arrayList.add(hashMap);
                    }
                }


            }

            Adapter adapter = new Adapter();
            listView.setAdapter(adapter);

        }

    }




}