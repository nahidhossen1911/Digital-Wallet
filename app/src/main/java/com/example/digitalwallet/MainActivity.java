package com.example.digitalwallet;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView addin,addex,totaltx,totalin,totalex,seemore;
    LinearLayout layouth;
    HashMap<String,String> hashMap;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        listView = findViewById(R.id.listview);

        addin = findViewById(R.id.addincome);
        addex = findViewById(R.id.addexpense);

        totaltx = findViewById(R.id.totaltk);
        totalin = findViewById(R.id.inmony);
        totalex = findViewById(R.id.outmony);

        seemore = findViewById(R.id.seemore);
        layouth = findViewById(R.id.historylayout);


        SQLite sqLite = new SQLite(MainActivity.this);
        recevData(sqLite,listView);

        addin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog("Add Income","in","Income",sqLite);

            }
        });

        addex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("Add Expense","ex","Expense",sqLite);

            }
        });

        seemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, History.class));
            }
        });





    }
    public class Adapter extends BaseAdapter{

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

            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item,parent,false);

            TextView title = view.findViewById(R.id.title);
            TextView date = view.findViewById(R.id.date);
            TextView amount = view.findViewById(R.id.amount);
            ImageView type  = view.findViewById(R.id.type);

            HashMap <String,String> hashMap = arrayList.get(position);
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

    public void recevData(SQLite sqLite,ListView listView){

        arrayList = new ArrayList<>();
        Cursor cursor = sqLite.getShowData();
        if(cursor!=null && cursor.getCount()>0){

            layouth.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            int count = 0;

            while (cursor.moveToNext()){

                count = count+1;
                int id = cursor.getInt(0);
                String type = cursor.getString(1);
                String title = cursor.getString(2);
                String date = cursor.getString(3);
                String mony = cursor.getString(4);

                HashMap hashMap = new HashMap();
                hashMap.put("id",id);
                hashMap.put("type",type);
                hashMap.put("title",title);
                hashMap.put("date",date);
                hashMap.put("mony",mony);
                arrayList.add(hashMap);

                if(count >5){
                    break;
                }
            }

            TotalAmount totalAmount = new TotalAmount();
            totalAmount.income(MainActivity.this);
            totalAmount.expense(MainActivity.this);

            try {
                totaltx.setText(""+totalAmount.balance(MainActivity.this));
                totalin.setText(""+totalAmount.income(MainActivity.this));
                totalex.setText(""+totalAmount.expense(MainActivity.this));
            }catch (Exception e){

            }

            Adapter adapter = new Adapter();
            listView.setAdapter(adapter);

            if (cursor.getCount()>5){
                seemore.setVisibility(View.VISIBLE);

            }else {
                seemore.setVisibility(View.GONE);
            }

        }else {
            seemore.setVisibility(View.GONE);
            layouth.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }


    }

    private void showDialog(String Title, String type, String input,SQLite sqLite){

        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.addindialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        TextView title = dialog.findViewById(R.id.title);
        title.setText(""+Title);
        EditText setincome = dialog.findViewById(R.id.setincome);
        setincome.setHint(""+input);
        EditText setinmony = dialog.findViewById(R.id.setinmony);

        TextView inSubmit = dialog.findViewById(R.id.insubmit);
        TextView incancel = dialog.findViewById(R.id.incancel);

        inSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(setincome.length()<1){
                    setincome.setError("Title");
                } else if (setinmony.length()<1) {
                    setinmony.setError("amount");
                }

                else if(setincome.length()>0 && setinmony.length()>0) {
                    String title = setincome.getText().toString();
                    String mony = setinmony.getText().toString();
                    int imony = Integer.parseInt(mony);


                    String dateTime = datetime();

                    sqLite.getInsertData(""+type,""+title,""+dateTime,imony);
                    recevData(sqLite,listView);
                    dialog.dismiss();
                }
            }
        });

        incancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String datetime(){
        LocalDate date = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date = LocalDate.now();
        }
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("d");
        }
        int up = Integer.parseInt(""+date.format(formatter));
        int today = up+1;
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String currentTime = date.format(formatter1);

        return currentTime;

    }
}