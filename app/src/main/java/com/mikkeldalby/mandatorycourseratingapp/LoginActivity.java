package com.mikkeldalby.mandatorycourseratingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "LoginActivity";

    public EditText txtUsername, txtPassword;
    public Button btnLogin, btnSignup;

    private FirebaseAuth mAuth;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();

        txtUsername = findViewById(R.id.txt_username);
        txtPassword = findViewById(R.id.txt_password_signup);
        btnLogin = findViewById(R.id.btn_login);
        btnSignup = findViewById(R.id.btn_signup);

        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                Log.d(TAG, "signIn:" + txtUsername.getText());
                if (!validateForm()) {
                    return;
                }

                showProgressDialog();

                mAuth.signInWithEmailAndPassword(txtUsername.getText().toString(), txtPassword.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(LoginActivity.this, getString(R.string.alert_logged_in_as) + " " + user.getEmail(),
                                            Toast.LENGTH_LONG).show();
                                    goToChooseLesson();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, getString(R.string.alert_something_went_wrong),
                                            Toast.LENGTH_SHORT).show();
                                }

                                if (!task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, getString(R.string.alert_wrong_credentials),
                                            Toast.LENGTH_SHORT).show();
                                }
                                hideProgressDialog();
                            }
                        });
                break;
            case R.id.btn_signup:
                Intent i = new Intent(this, SignupActivity.class);
                startActivity(i);
                break;
        }
    }

    public void goToChooseLesson(){
        if (mAuth.getUid().equals("C1melbxOnLZULC8XLXoCvZ5fQO03")){
            Intent i = new Intent(this, ChooseLessonActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(this, ChooseLessonActivity.class);
            startActivity(i);
        }
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = txtUsername.getText().toString();
        if (TextUtils.isEmpty(email)) {
            txtUsername.setError(getString(R.string.field_required));
            valid = false;
        } else {
            txtUsername.setError(null);
        }

        String password = txtPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            txtPassword.setError(getString(R.string.field_required));
            valid = false;
        } else {
            txtPassword.setError(null);
        }

        return valid;
    }
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}