package com.example.mvryan.belajarphpconn;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private AlertDialog alertDialog;
    ListView lvData;
    ArrayList<HashMap<String,String>> dataList;

    static String url = "http://10.107.201.22/Mopro/getData.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvData = findViewById(R.id.lv_data);
        dataList = new ArrayList<>();

        new GetData().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class GetData extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String JsonStr = sh.makeServiceCall(url);

            Log.e("Condition","Respon From URL : "+JsonStr);

            if (JsonStr!= null){
                try {
                    JSONObject jsonObject = new JSONObject(JsonStr);

                    //get json array
                    JSONArray dataPool = jsonObject.getJSONArray("data");//change parameter with your own

                    //looping dataPool
                    for (int i = 0; i<dataPool.length();i++){
                        JSONObject d = dataPool.getJSONObject(i);

                        //get every data in db
                        String nama = d.getString("nama");
                        String nim = d.getString("nim");
                        String jurusan = d.getString("jurusan");

                        HashMap<String,String> sampelData = new HashMap<>();

                        sampelData.put("nama",nama);
                        sampelData.put("nim",nim);
                        sampelData.put("jurusan",jurusan);

                        dataList.add(sampelData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Log.e("Information :","\nCouldn't get json from server");
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setMessage("Please Wait...");
            alertDialog.setCancelable(false);
            alertDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (alertDialog.isShowing()){
                alertDialog.dismiss();
                ListAdapter adapter = new SimpleAdapter(
                        MainActivity.this,dataList,
                        R.layout.getdatapull, new String[]{"nama","nim","jurusan"},
                        new int[]{R.id.tv_name,R.id.tv_nim,R.id.tv_jurusan}
                );
                lvData.setAdapter(adapter);
            }
        }
    }
}
