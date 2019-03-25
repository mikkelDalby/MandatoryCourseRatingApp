package com.mikkeldalby.mandatorycourseratingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikkeldalby.mandatorycourseratingapp.model.CombinedRating;
import com.mikkeldalby.mandatorycourseratingapp.model.Lesson;
import com.mikkeldalby.mandatorycourseratingapp.model.Rate;

import java.util.ArrayList;
import java.util.List;

public class ChooseLessonActivity extends AppCompatActivity {
    private static String TAG = "ChooseLessonActivity";

    public ListView listView;
    public Button btnSignout;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_lesson);

        init();
        setupLessons();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        listView = findViewById(R.id.list);
        btnSignout = findViewById(R.id.btn_signout);

        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(ChooseLessonActivity.this, getString(R.string.alert_signed_out),
                Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void setupLessons(){
        db.collection("lessons").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()){
                    List<Lesson> lessonList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Lesson l = document.toObject(Lesson.class);
                        l.setId(document.getId());
                        //Log.d(TAG, l.toString());
                        lessonList.add(l);
                    }
                    setupList(lessonList);
                } else {
                    Log.d(TAG, "Failed to load lessons");
                }
            }
        });
    }

    public void setupList(final List<Lesson> lessons){
        final List<CombinedRating> comb = new ArrayList<>();
        for (Lesson l: lessons){
            comb.add(new CombinedRating(l));
        }
        final ArrayAdapter<CombinedRating> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, comb);

        db.collection("rates")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                Rate r = document.toObject(Rate.class);
                                for (CombinedRating c: comb){
                                    if (r.getLessonId().equals(c.getLesson().getId())){
                                        c.addRating(r);
                                        break;
                                    }
                                }
                            }
                            for (CombinedRating i: comb){
                                Log.d(TAG, i.getLesson().getName()+" "+i.getRatings().size());
                            }

                            listView.setAdapter(adapter);
                            listView.setClickable(true);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    goToRating(comb.get(position).getLesson());
                                }
                            });
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void goToRating(Lesson lesson){
        //Log.d(TAG, lesson.getName());
        Intent i = new Intent(this, RateLessonActivity.class);
        i.putExtra("id", lesson.getId());
        i.putExtra("name", lesson.getName());
        i.putExtra("email", lesson.getEmail());
        startActivity(i);
    }
}