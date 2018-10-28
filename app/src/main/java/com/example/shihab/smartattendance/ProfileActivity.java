package com.example.shihab.smartattendance;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shihab.smartattendance.adapter.RecycleViewAdapter;
import com.example.shihab.smartattendance.model.ListItem;
import com.github.clans.fab.FloatingActionButton;
import com.irozon.alertview.AlertActionStyle;
import com.irozon.alertview.AlertStyle;
import com.irozon.alertview.AlertView;
import com.irozon.alertview.objects.AlertAction;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    TextView tvShowWelcome,tvUserId,tvCourseNames;
    FloatingActionButton logOut,menuQR,menuViewAttendance,menuSendMail ;
    MaterialCalendarView materialCalendarView;
    ImageView imageView;
    String StringIdKey,StringJsonIdKey,StringJsonPasswordKey,StringJsonUserNameKey,StringJsonEmailKey;
    RequestQueue requestQueue;
    String studentId,studentName,course;
    Button buttonHD;
    final String URL = "http://jachaibd.com/lict_smart_attendance/select_courses.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvShowWelcome = findViewById(R.id.textViewShowWelcome);
        tvUserId=findViewById(R.id.textViewUserID);
        tvCourseNames=findViewById(R.id.textViewShowCourseNames);
        imageView = findViewById(R.id.imageViewProfileUser);
        buttonHD = findViewById(R.id.buttonHD2);
        imageView.setOnClickListener(this);
        buttonHD.setOnClickListener(this);
        View v = buttonHD;
        v.setVisibility(View.INVISIBLE);
        Intent intent=getIntent();
        StringIdKey=intent.getStringExtra("userIdKey");
        StringJsonIdKey=intent.getStringExtra("userIdJsonKey");
        StringJsonPasswordKey=intent.getStringExtra("userPasswordJsonKey");
        StringJsonUserNameKey=intent.getStringExtra("userNameJson");
        StringJsonEmailKey = intent.getStringExtra("userEmailJson");
        tvUserId.setText(StringIdKey);

      //  Toast.makeText(getApplicationContext(),"JsonUID: "+StringJsonIdKey+" JsonPass: "+StringJsonPasswordKey+" JsonName: "+StringJsonUserNameKey+" JsonEmail: "+StringJsonEmailKey,Toast.LENGTH_LONG).show();
        logOut=findViewById(R.id.fabLogOut);
        menuQR = findViewById(R.id.subFloatingMenuQR) ;
        menuViewAttendance = findViewById(R.id.subFloatingMenuViewAttendance) ;
        menuSendMail = findViewById(R.id.subFloatingMenuSendMail) ;
        materialCalendarView = findViewById(R.id.calendarView);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.WEDNESDAY)
                .setMinimumDate(CalendarDay.from(2018, 1, 1))
                .setMaximumDate(CalendarDay.from(2030, 1, 1))

                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        materialCalendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                Calendar cal1 = day.getCalendar();
                Calendar cal2 = Calendar.getInstance();

                return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                        && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                        && cal1.get(Calendar.DAY_OF_YEAR) ==
                        cal2.get(Calendar.DAY_OF_YEAR));
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.setBackgroundDrawable(ContextCompat.getDrawable(ProfileActivity.this,R.drawable.selector));
            }
        });
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
               // Toast.makeText(ProfileActivity.this, ""+date, Toast.LENGTH_SHORT).show();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
            }
        });
        menuQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ProfileActivity.this,CoursesActivity.class);
                intent1.putExtra("userIdKey",StringJsonIdKey);
                intent1.putExtra("userNameKey",StringJsonUserNameKey);
                startActivity(intent1);
            }
        });

        menuViewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),ViewAttendanceActivity.class));
                //fetchUserId();
            }
        });

        menuSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gmailURL = "http://www.gmail.com";
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(gmailURL)));

            }
        });
        buttonHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetchUserId();
                //finish();
                Intent intent1 = new Intent(ProfileActivity.this,CoursesActivity.class);
               startActivity(intent1);
              //  fet();
            }
        });

        currentDateTime();
        sendData();
    }



    private void sendData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Toast.makeText(getApplicationContext(),"Data Sent", Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("studentId",StringJsonIdKey);
                return  params;
            }
        };
        Volley.newRequestQueue(ProfileActivity.this).add(stringRequest);
    }
    private void fetchUserId(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("user_info");
                    for(int i=0; i<=jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        studentId = jsonObject.getString("STUDENT_ID");
                        studentName = jsonObject.getString("STUDENT_NAME");
                        course = jsonObject.getString("COURSE_NAME");
                        Toast.makeText(getApplicationContext(),"studentId: "+studentId+"\nStudentName: "+studentName+"\nCourseName: "+course, Toast.LENGTH_LONG).show();
                        //   tvCourseNames.setText(cou);
                    }
                    // Toast.makeText(getApplicationContext(),"studentId: "+studentId+"\nStudentName: "+studentName+"\nCourseName: "+course, Toast.LENGTH_LONG).show();
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
        Volley.newRequestQueue(ProfileActivity.this).add(jsonObjectRequest);
    }

    private void fet(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("user_info");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        studentId = jsonObject1.getString("STUDENT_ID");
                        studentName = jsonObject1.getString("STUDENT_NAME");
                        course = jsonObject1.getString("COURSE_NAME");
                        Toast.makeText(getApplicationContext(),"studentId: "+studentId+"\nStudentName: "+studentName+"\nCourseName: "+course, Toast.LENGTH_LONG).show();
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
        Volley.newRequestQueue(ProfileActivity.this).add(stringRequest);

    }


    private void currentDateTime() {
        DateFormat.getDateTimeInstance().format(new Date());
       // Toast.makeText(getApplicationContext(),currentDateTimeString,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
    }


    @Override
    public void onClick(View v) {
        if(v==imageView){
//            AlertStyle.BOTTOM_SHEET
//            AlertStyle.IOS
//            AlertStyle.DIALOG

            AlertView alert = new AlertView("User Details", "", AlertStyle.IOS);
            alert.addAction(new AlertAction("User ID: "+StringJsonIdKey, AlertActionStyle.DEFAULT, action -> {
// Action 1 callback
            }));
            alert.addAction(new AlertAction("User Name: "+StringJsonUserNameKey, AlertActionStyle.DEFAULT, action -> {
// Action 1 callback
            }));
            alert.addAction(new AlertAction("Email-Address: "+StringJsonEmailKey, AlertActionStyle.DEFAULT, action -> {
// Action 1 callback
            }));
          //  alert.addAction(new AlertAction("Action 2", AlertActionStyle.NEGATIVE, action -> {
// Action 2 callback
           // }));

            alert.show(this);
        }
    }
}
