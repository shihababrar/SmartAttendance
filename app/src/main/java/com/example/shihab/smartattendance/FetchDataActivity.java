package com.example.shihab.smartattendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FetchDataActivity extends AppCompatActivity {

    TextView showId, showName,showEmail,showPassword;
  String REGISTER_URL2 = "http://jachaibd.com/lict_smart_attendance/user_informations.php";
    String userId,userName,userEmail,userPassword;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue= Volley.newRequestQueue(this);
        setContentView(R.layout.activity_fetch_data);
        showId=findViewById(R.id.textViewShowId);
//        showName=findViewById(R.id.textViewShowName);
//        showEmail=findViewById(R.id.textViewShowEmail);
//        showPassword=findViewById(R.id.textViewShowPassword);
        userLogIn2();


    }

    private void userLogIn2() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REGISTER_URL2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("user_info");
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        userId = jsonObject.getString("userId");
                        userName = jsonObject.getString("userName");
                        userEmail = jsonObject.getString("userEmail");
                        userPassword = jsonObject.getString("userPassword");

//                        showId.setText(userId);
//                        showName.setText(userName);
//                        showEmail.setText(userEmail);
//                        showPassword.setText(userPassword);
                        showId.append("ID: "+userId+"\n"+"Name: "+ userName+"\n"+"Email: "+ userEmail+"\n" +"Password: "+ userPassword+"\n"+"\n");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
