package com.mikkeldalby.mandatorycourseratingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikkeldalby.mandatorycourseratingapp.model.Rate;

public class RateLessonActivity extends AppCompatActivity {
    private static String TAG = "RateLessonActivity";

    public TextView txtHead, q1Txt, q2Txt, q3Txt, q4Txt, q5Txt, q6Txt;
    public RatingBar q1, q2, q3, q4, q5, q6;
    public Button btnSubmit, btnEmail;
    public String id;
    public String uId;
    public String email;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_lesson);

        init();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        txtHead = findViewById(R.id.txt_head);
        q1Txt = findViewById(R.id.q1_txt);
        q2Txt = findViewById(R.id.q2_txt);
        q3Txt = findViewById(R.id.q3_txt);
        q4Txt = findViewById(R.id.q4_txt);
        q5Txt = findViewById(R.id.q5_txt);
        q6Txt = findViewById(R.id.q6_txt);
        q1 = findViewById(R.id.q1);
        q2 = findViewById(R.id.q2);
        q3 = findViewById(R.id.q3);
        q4 = findViewById(R.id.q4);
        q5 = findViewById(R.id.q5);
        q6 = findViewById(R.id.q6);
        btnSubmit = findViewById(R.id.btn_submit);
        btnEmail = findViewById(R.id.btn_email);

        Intent i = getIntent();
        id = i.getStringExtra("id");
        txtHead.setText(i.getStringExtra("name"));
        email = i.getStringExtra("email");

        uId = mAuth.getUid();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rate r = getRate();

                db.collection("rates").document(uId+id).set(r).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "Updated");
                        goBack();
                    }
                });
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rate r = getRate();

                db.collection("rates").document(uId+id).set(r).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "Updated");
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
                        i.putExtra(Intent.EXTRA_SUBJECT, "New rating");
                        i.putExtra(Intent.EXTRA_TEXT   , createEmailString());
                        try {
                            startActivityForResult(Intent.createChooser(i, "Send mail..."),0);

                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(RateLessonActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        goBack();
    }

    public String createEmailString(){
        String s = "";
        Rate r = getRate();

        s += "Rate:\n";
        s += getString(R.string.q1) + ": " + r.getQ1() + "\n";
        s += getString(R.string.q2) + ": " + r.getQ2() + "\n";
        s += getString(R.string.q3) + ": " + r.getQ3() + "\n";
        s += getString(R.string.q4) + ": " + r.getQ4() + "\n";
        s += getString(R.string.q5) + ": " + r.getQ5() + "\n";
        s += getString(R.string.q6) + ": " + r.getQ6() + "\n";
        s += "Average: " + r.getAverage();

        return s;
    }

    public Rate getRate(){
        Float q1Val = q1.getRating()*20;
        Float q2Val = q2.getRating()*20;
        Float q3Val = q3.getRating()*20;
        Float q4Val = q4.getRating()*20;
        Float q5Val = q5.getRating()*20;
        Float q6Val = q6.getRating()*20;

        Rate r = new Rate(id, q1Val, q2Val, q3Val, q4Val, q5Val, q6Val);
        return r;
    }

    public void goBack(){
        Toast.makeText(RateLessonActivity.this, getString(R.string.alert_submitted),
                Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, ChooseLessonActivity.class);
        startActivity(i);
    }
}
