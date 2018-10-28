package com.example.shihab.smartattendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shihab.smartattendance.adapter.RecycleViewAdapter;
import com.example.shihab.smartattendance.model.ListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CoursesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<ListItem> arrayList;
    final String URL = "http://jachaibd.com/lict_smart_attendance/select_courses.php";
    String JsonUserId,JsonUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        recyclerView = findViewById(R.id.recycleViewCourses);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CoursesActivity.this));
        arrayList = new ArrayList<>();
        Intent intent = getIntent();
        JsonUserId = intent.getStringExtra("userIdKey");
        JsonUserName = intent.getStringExtra("userNameKey");
      //  Toast.makeText(CoursesActivity.this,"CA_ID: "+ JsonUserId  +" CA_Name: "+JsonUserName,Toast.LENGTH_LONG).show();
        fetchUserId();
       // sendKeyValues();
        sendValueSharePrefer();

        
    }

    private void sendValueSharePrefer() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CoursesActivity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userIdKey",JsonUserId);
        editor.putString("userNameKey",JsonUserName);
        editor.commit();

    }

    private void sendKeyValues() {
        Intent intent = new Intent(CoursesActivity.this,QRActivity.class);
        intent.putExtra("userIdKey",JsonUserId);
        intent.putExtra("userNameKey",JsonUserName);
        startActivity(intent);
    }
    private void fetchUserId() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                   JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("user_info");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        ListItem listItem = new ListItem(jsonObject1.getString("COURSE_ID"),jsonObject1.getString("COURSE_NAME"));
                        arrayList.add(listItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new RecycleViewAdapter(arrayList,CoursesActivity.this);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        Volley.newRequestQueue(CoursesActivity.this).add(stringRequest);
    }




}
