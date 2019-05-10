package kmitl.s8070074.bawonsak.everycook.Controller.Activity;

import androidx.appcompat.app.AppCompatActivity;
import kmitl.s8070074.bawonsak.everycook.AnimateIntent;
import kmitl.s8070074.bawonsak.everycook.Model.Member;
import kmitl.s8070074.bawonsak.everycook.R;
import kmitl.s8070074.bawonsak.everycook.UserPreferences;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class SignUpActivity extends AnimateIntent {

    private EditText fullnameEt, nicknameEt, usernameEt, passwordEt;
    private DatabaseReference mRootRef, mUsersRef;
    boolean error = false, errorRequied = true;
    private String errorMessage = null;
    private String fullname, nickname, username, password;
    ProgressDialog progress;
    Button signUpBtn;
    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        fullnameEt = findViewById(R.id.new_fullname);
        nicknameEt = findViewById(R.id.new_nickname);
        usernameEt = findViewById(R.id.new_username);
        passwordEt = findViewById(R.id.new_password);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mUsersRef = mRootRef.child("Member");
        signUpBtn = findViewById(R.id.signUpBtn);
        progress = new ProgressDialog(SignUpActivity.this);
        progress.setMessage("loading..");
        signUpBtn.setOnClickListener(view -> signUp());

    }

    public void signUp(){
        fullname = fullnameEt.getText().toString();
        nickname = nicknameEt.getText().toString();
        username = usernameEt.getText().toString();
        password = passwordEt.getText().toString();

        if(username == null || username.equals("") || password == null || password.equals("") || fullname == null || fullname.equals("") ||
                nickname == null || nickname.equals("")){
            error = true;
            errorRequied = true;
            errorMessage = "Please complete informations";
            return;
        }
        errorRequied = false;
        mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> userList = (Map<String, Object>) dataSnapshot.getValue();
                Map<String, Object> user = (Map<String, Object>) userList.get(username);

                if(user != null){
                    error = true;
                    errorMessage = "Username is already used";
                } else {
                    error = false;
                    insert();
                    member = new Member(username, nickname, "0", fullname, "https://firebasestorage.googleapis.com/v0/b/trade-event.appspot.com/o/member%2Fprofile-icon-9%20(1).png?alt=media&token=2fd99d01-1b36-4377-b9ba-6113b0882a41");
                }
                check();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, databaseError.getMessage());
            }
        });
    }
    public void signIn(View view) {
        finish();
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
    }

    public void insert() {
        DatabaseReference member = mUsersRef.child(username);
        member.child("password").setValue(password);
        member.child("rating").setValue("0");
        member.child("fullname").setValue(fullname);
        member.child("name").setValue(nickname);
        member.child("url").setValue("https://firebasestorage.googleapis.com/v0/b/trade-event.appspot.com/o/member%2Fprofile-icon-9%20(1).png?alt=media&token=2fd99d01-1b36-4377-b9ba-6113b0882a41");
    }

    public void check(){
        if(errorRequied == true){
            Toast.makeText(this,errorMessage,Toast.LENGTH_SHORT).show();
        } else {
            if (error == true) {
                Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                progress.show();
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                UserPreferences.setUserName(getApplicationContext(), username);
                intent.putExtra("member", member);
                startActivity(intent);
                finish();
            }
        }
    }
}
