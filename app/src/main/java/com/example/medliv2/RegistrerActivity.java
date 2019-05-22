package com.example.medliv2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medliv2.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrerActivity extends AppCompatActivity {
    EditText edname,edemail,edpassword;
    FirebaseAuth auth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrer);
        edname=(EditText)findViewById(R.id.regnombre);
        edemail=(EditText)findViewById(R.id.regemail);
        edpassword=(EditText)findViewById(R.id.regpassword);
        auth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference().child("Users");


    }
    public void registrar(View v){
        final String name=edname.getText().toString();
        final String email=edemail.getText().toString();
        final String password=edpassword.getText().toString();

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser=auth.getCurrentUser();
                    User user=new User(name,email,password,firebaseUser.getUid());
                    reference.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                finish();
                                Intent intent =new Intent(RegistrerActivity.this,WelcomeActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(),"Usuario Creado",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"No se Pudo Crear Usuario En Firebase",Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
            }
        });
    }
    private void writeNewUser(String userId,String name,String email,String password){
        User user=new User(name,email,password,userId);
        reference.child("Users").child(userId).setValue(user);
    }
}

