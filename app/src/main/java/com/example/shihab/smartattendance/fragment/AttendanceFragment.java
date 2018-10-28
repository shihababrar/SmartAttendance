package com.example.shihab.smartattendance.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.shihab.smartattendance.R;

import java.util.zip.Inflater;

/**
 * Created by SHIHAB on 3/31/2018.
 */

public class AttendanceFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
     return inflater.inflate(R.layout.activity_attendance_fragment,null);
    }
}
