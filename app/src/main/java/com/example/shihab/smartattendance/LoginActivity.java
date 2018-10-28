package com.example.shihab.smartattendance;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shihab.smartattendance.model.MySingleton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOGIN_URL = "";
    private EditText editTextID;
    private EditText editTextPassword;
   private Button buttonCreate,buttonLogin,buttonHD;
   RequestQueue requestQueue;
     String id,password;
     ProgressDialog progressDialog;
    String userIdJson,userPasswordJson,userNameJson,userEmailJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextID = findViewById(R.id.editTextStudentId);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogIn);
        buttonCreate= findViewById(R.id.buttonCreateAccount);
        buttonHD = findViewById(R.id.buttonHD);
        View b = findViewById(R.id.buttonHD);
        b.setVisibility(View.GONE);

        buttonLogin.setOnClickListener(this);
        buttonCreate.setOnClickListener(this);
        buttonHD.setOnClickListener(this);
        requestQueue = Volley.newRequestQueue(this);


    }

    public void onClick(View v) {
        id = editTextID.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        if(v == buttonLogin){
            boolean flag = false;
            if(id.isEmpty()){
                editTextID.setError("ID is required");
                flag=true;
            }
             if(password.isEmpty()){

                editTextPassword.setError("Enter Password");
                flag=true;
            }
            if(flag == true){
               // Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                animatedAlertDialog();

            }
            else {
               sendData();
                }

        }
        if(v == buttonCreate){
              finish();
            startActivity(new Intent(getApplicationContext(),CreateAccountActivity.class));

        }
        if(v == buttonHD){
            viewUserLogInInfo();
        }
    }




    private void animatedAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("            Error! ");
        builder.setIcon(getResources().getDrawable(R.drawable.error96e2));
        builder.setMessage("   Required Fields are Empty!!");
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable,2000);
    }
    private void animatedAlertDialogErrorPassword() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("            Error!! ");
        builder.setIcon(getResources().getDrawable(R.drawable.error96e2));
        builder.setMessage("   Username or Password is Wrong!!");
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable,3000);
    }


    private void sendData () {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                     //   Toast.makeText(LoginActivity.this, "Account Created!", Toast.LENGTH_LONG).show();
                      buttonHD.performClick();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userId", id);
                params.put("userPassword", password);
                return params;
            }

        };
        requestQueue.getCache().clear();
        Volley.newRequestQueue(this).add(stringRequest);



    }
    private void viewUserLogInInfo() {
         JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, null, new Response.Listener<JSONObject>() {
             @Override
             public void onResponse(JSONObject response) {
                 try {
                     JSONArray jsonArray = response.getJSONArray("user_info");
                     for (int i=0; i<jsonArray.length();i++) {
                         JSONObject jsonObject = jsonArray.getJSONObject(i);
                         userIdJson = jsonObject.getString("userId");
                         userNameJson = jsonObject.getString("userName");
                         userPasswordJson = jsonObject.getString("userPassword");
                         userEmailJson = jsonObject.getString("userEmail");

                     }
                     if((id.equals(userIdJson) && password.equals(userPasswordJson))){
                     //    Toast.makeText(getApplicationContext(), "Username or Password is 100%correct!", Toast.LENGTH_SHORT).show();
                         showProgressDialog();
                     } else {
                       //  Toast.makeText(getApplicationContext(), "Username or Password is Incorrect!", Toast.LENGTH_SHORT).show();
                         animatedAlertDialogErrorPassword();
                     }
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }

             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();

             }
         });

        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextID.setText("");
        editTextPassword.setText("");
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Quit");
        builder.setIcon(getResources().getDrawable(R.drawable.exit2));
        builder.setMessage("Do you want to quit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showProgressDialog() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Signing in....");
            progressDialog.setTitle("Sign In");
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.show();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                    Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                    intent.putExtra("userIdKey",id);
                    intent.putExtra("userIdJsonKey",userIdJson);
                    intent.putExtra("userPasswordJsonKey",userPasswordJson);
                    intent.putExtra("userNameJson",userNameJson);
                    intent.putExtra("userEmailJson",userEmailJson);
                    finish();
                    startActivity(intent);
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(runnable,3000);
        }

}
