package com.example.medliv2;

import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText edEmail,edPassword;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edEmail=(EditText)findViewById(R.id.edEmail);
        edPassword=(EditText)findViewById(R.id.edPassword);
        auth=FirebaseAuth.getInstance();
    }


    public void LoginUser(View v){
        String email=edEmail.getText().toString();
        String pass=edPassword.getText().toString();
        if (!email.equals("")&&!pass.equals("")){
            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        //open other activity
                        Intent intent=new Intent(LoginActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"El Usuario No Esta Logeado",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    public void goRegistrer(View v){
        Intent intent=new Intent(LoginActivity.this,RegistrerActivity.class);
        startActivity(intent);
    }
}
