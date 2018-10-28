package com.example.shihab.smartattendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class QRActivity extends AppCompatActivity implements  ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private String userId, userName, currentDateTime, QRResult;
    RequestQueue requestQueue;
    private final String URL = "http://jachaibd.com/lict_smart_attendance/give_attendance.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        userId = intent.getStringExtra("userIdKey");
        userName = intent.getStringExtra("userNameKey");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(QRActivity.this);
        userId =  sharedPreferences.getString("userIdKey","userId");
        userName =  sharedPreferences.getString("userNameKey","userName");
      //  Toast.makeText(getApplicationContext(), "ID: " + userId + "\nName: " + userName, Toast.LENGTH_LONG).show();
      //  Toast.makeText(getApplicationContext(), "ID: " + userId  + "\nName: " + userName, Toast.LENGTH_LONG).show();
        currentDateTime = DateFormat.getDateTimeInstance().format(new Date());

      //  Toast.makeText(getApplicationContext(), currentDateTime, Toast.LENGTH_SHORT).show();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        int currentApiVersion = Build.VERSION.SDK_INT;

        if (currentApiVersion >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
           //     Toast.makeText(getApplicationContext(), "Permission already granted!", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }
        }


    }

    private void sendData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QRActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("QRResult", QRResult);
                params.put("studentId", userId);
                params.put("studentName", userName);
                params.put("currentDateTime", currentDateTime);
                return params;
            }
        };
//        requestQueue.getCache().clear();
        Volley.newRequestQueue(QRActivity.this).add(stringRequest);
    }


    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                       Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(QRActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    public void handleResult(Result result) {
        QRResult = result.getText();
      //  Toast.makeText(getApplicationContext(), "Key: " + QRResult, Toast.LENGTH_LONG).show();
        sendData();
        Log.d("QRCodeScanner", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());
      //  showAlertDialog();
        //showSuccessDialog();
        makeBeepSound();
        successDialog();
//        finish();
//        startActivity(new Intent(QRActivity.this,LoginActivity.class));

    }

    private void successDialog() {
            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(QRActivity.this);
            builder.setTitle("          Successful ");
            builder.setIcon(getResources().getDrawable(R.drawable.check_attendance));
            builder.setMessage("               Attendance Submitted");
            final android.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                    finish();
                    startActivity(new Intent(QRActivity.this,LoginActivity.class));
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(runnable,8000);
        }


    private void makeBeepSound() {
        MediaPlayer sound = MediaPlayer.create(QRActivity.this,R.raw.beepsound);
        AudioManager audioManager = (AudioManager) getSystemService(QRActivity.this.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,10,0);
        sound.start();
    }

//    private void showSuccessDialog() {
//        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
//                .setTitleText("Good job!")
//                .setContentText("You clicked the button!")
//                .show();
//    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(QRActivity.this);
            }
        });
        builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(QRResult));
                startActivity(browserIntent);
            }
        });
        // builder.setMessage(result.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    }

