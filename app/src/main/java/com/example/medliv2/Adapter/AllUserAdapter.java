package com.example.medliv2.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.medliv2.Model.User;
import com.example.medliv2.R;
import com.example.medliv2.WelcomeActivity;

import java.util.ArrayList;

public class AllUserAdapter  extends RecyclerView.Adapter<AllUserAdapter.AllUserViewHolder> {


    Activity context;
    ArrayList<User> userArrayList;
    public AllUserAdapter(Activity context,ArrayList<User> userArrayList){
        this.context=context;
        this.userArrayList=userArrayList;
    }
    @Override
    public AllUserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alluser,viewGroup,false);
        AllUserViewHolder allUserViewHolder=new AllUserViewHolder(view);

        return allUserViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllUserViewHolder allUserViewHolder, int i) {
        User user=userArrayList.get(i);
        allUserViewHolder.textView.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class AllUserViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        Button button;

        public AllUserViewHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.itemname);
            button=itemView.findViewById(R.id.callbuton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user=userArrayList.get(getAdapterPosition());
                    ((WelcomeActivity)context).callUser(user);
                }
            });
        }
    }
}
