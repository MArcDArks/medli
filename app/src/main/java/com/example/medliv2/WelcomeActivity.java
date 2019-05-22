package com.example.medliv2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.medliv2.Adapter.AllUserAdapter;
import com.example.medliv2.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    SinchClient sinchClient;
    Call call;
    ArrayList<User>userArrayList;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        recyclerView=(RecyclerView)findViewById(R.id.recyclevie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userArrayList=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference().child("Users");
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();


        sinchClient= Sinch.getSinchClientBuilder()
                .context(this)
                .userId(firebaseUser.getUid())
                .applicationKey("5408d1a5-b2ad-4928-a751-223b1a357b2c")
                .applicationSecret("J3smHuWDhUGnctGkZjv3oA==")
                .environmentHost("clientapi.sinch.com")
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();

        sinchClient.getCallClient().addCallClientListener(new sinchCallClientListener(){

        });

        sinchClient.start();
        fechtAllUser();
    }

    private void fechtAllUser() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dss:dataSnapshot.getChildren()){
                    User user=dss.getValue(User.class);
                    userArrayList.add(user);

                }


                AllUserAdapter adapter=new AllUserAdapter(WelcomeActivity.this,userArrayList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"error"+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private class sinchCallListener implements CallListener{

        @Override
        public void onCallProgressing(Call call) {
            Toast.makeText(getApplicationContext(),"Llamando...",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCallEstablished(Call call) {
            Toast.makeText(getApplicationContext(),"Llamada Establecida",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCallEnded(Call endcall) {
            Toast.makeText(getApplicationContext(),"Llamada Finalizada",Toast.LENGTH_LONG).show();
            call =null;
            endcall.hangup();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_logout){
            if (firebaseUser!=null){
                auth.signOut();
                finish();
                Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private class sinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(final CallClient callClient, final Call incomingcall) {
            AlertDialog alertDialog=new AlertDialog.Builder(WelcomeActivity.this).create();
            alertDialog.setTitle("Llamando");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Reject", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    call.hangup();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Pick", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    call=incomingcall;
                    call.answer();
                    call.addCallListener(new sinchCallListener());
                    Toast.makeText(getApplicationContext(),"Llamada Comenzada",Toast.LENGTH_LONG).show();

                }
            });
            alertDialog.show();
        }
    }
    public void callUser(User user){
        if (call==null){
            call=sinchClient.getCallClient().callUser(user.getUserID());
            call.addCallListener(new sinchCallListener());

            openCallerDialog(call);
        }
    }

    private void openCallerDialog(final Call call) {
        AlertDialog alertDialog=new AlertDialog.Builder(WelcomeActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Llamando");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ring Up", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                call.hangup();
            }
        });
        alertDialog.show();
    }
}
