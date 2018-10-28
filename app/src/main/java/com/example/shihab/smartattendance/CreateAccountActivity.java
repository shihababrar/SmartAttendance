package com.example.shihab.smartattendance;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String REGISTER_URL = "http://jachaibd.com/lict_smart_attendance/insert_user.php";


    private EditText editTextID;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ImageButton imageButton;
    public String id, name, email, password;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        editTextID = findViewById(R.id.editTextUserId);
        editTextName = findViewById(R.id.editTextUserName);
        editTextEmail = findViewById(R.id.editTextStudentEmailAddress);
        editTextPassword = findViewById(R.id.editTextUserPassword);
        imageButton = findViewById(R.id.idImageButtonCreateAccount);
        imageButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        id = editTextID.getText().toString().trim();
        name = editTextName.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        if (v == imageButton) {
            boolean flag = false;
            if (id.isEmpty()) {
                editTextID.setError("ID is required");
                flag = true;
            } else if (id.length() < 10) {
                editTextID.setError("Wrong ID!");
                flag = true;
            }
            if (name.isEmpty()) {
                editTextName.setError("Enter Username");
                flag = true;
            }
            if (email.isEmpty()) {
                editTextEmail.setError("Enter Email-Address");
                flag = true;
            } else if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                editTextEmail.setError("Enter Valid Email-Address");
                flag = true;
            }
            if (password.isEmpty()) {
                editTextPassword.setError("Enter Password");
                flag = true;
            } else if (password.length() > 13) {
                editTextPassword.setError("Password is too large!");
                flag = true;
            }

            if (flag == true) {
              //   Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                 animatedAlertDialog();
            } else {
                showProgressBar();
                registerUser();
            }
        }
        }

    private void animatedAlertDialog() {
      final AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccountActivity.this);
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

    @Override
    protected void onResume() {
        super.onResume();
        editTextID.setText("");
        editTextName.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }

    private void showProgressBar() {
      progressDialog = new ProgressDialog(CreateAccountActivity.this);
        progressDialog.setMessage("Creating....");
        progressDialog.setTitle("Create Account");
        progressDialog.show();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
            progressDialog.cancel();
                startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));


            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable,4000);
    }


    private void registerUser () {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(CreateAccountActivity.this, "Account Created!", Toast.LENGTH_LONG).show();
                            showProgressBar();
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
                    params.put("userName", name);
                    params.put("userEmail", email);
                    params.put("userPassword", password);
                    return params;
                }

            };
            Volley.newRequestQueue(this).add(stringRequest);


        }
    }
