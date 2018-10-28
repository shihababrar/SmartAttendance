package com.example.shihab.smartattendance.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shihab.smartattendance.R;

/**
 * Created by SHIHAB on 3/31/2018.
 */

public class ProfileFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle){
        return layoutInflater.inflate(R.layout.activity_profile_fragment,null);
    }
}
