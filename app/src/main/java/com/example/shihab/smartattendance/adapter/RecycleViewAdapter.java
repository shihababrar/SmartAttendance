package com.example.shihab.smartattendance.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shihab.smartattendance.CoursesActivity;
import com.example.shihab.smartattendance.QRActivity;
import com.example.shihab.smartattendance.R;
import com.example.shihab.smartattendance.model.ListItem;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.zip.Inflater;

import static android.widget.Toast.LENGTH_LONG;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>{
    ArrayList<ListItem> arrayList;
    Context context;
    CardView cardView;
    String JsonUserId,JsonUserName;


    public RecycleViewAdapter(ArrayList<ListItem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
     ListItem listItem = arrayList.get(position);
     holder.textViewCourseId.setText(listItem.getCourseId());
     holder.textViewCourseName.setText(listItem.getCourseName());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewCourseId,textViewCourseName;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewCourseId = itemView.findViewById(R.id.textViewCourseId);
            textViewCourseName = itemView.findViewById(R.id.textViewCourseName);
            linearLayout = itemView.findViewById(R.id.idLinearLayout);

           // JsonUserId = intent.getStringExtra("StringJsonIdKey");
          //  JsonUserName = intent.getStringExtra("StringJsonUserNameKey");
          //  Toast.makeText(itemView.getContext(),"ID: "+ JsonUserId  +" Name: "+JsonUserName,Toast.LENGTH_LONG).show();
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v == linearLayout){
                      //  Toast.makeText(v.getContext(),"a",Toast.LENGTH_LONG).show();
                        Intent QRIntent = new Intent(v.getContext(),QRActivity.class);
                        v.getContext().startActivity(QRIntent);
                    }
                }
            });
        }
    }
}
