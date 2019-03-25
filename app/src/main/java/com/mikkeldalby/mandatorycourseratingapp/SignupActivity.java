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

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "SignupActivity";

    public EditText txtEmail, txtPassword, txtPasswordAgain;
    public Button btnSignup;

    private FirebaseAuth mAuth;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();

        txtEmail = findViewById(R.id.txt_email_signup);
        txtPassword = findViewById(R.id.txt_password_signup);
        txtPasswordAgain = findViewById(R.id.txt_password_again_signup);
        btnSignup = findViewById(R.id.btn_signup_signup);

        btnSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signup_signup:
                Log.d(TAG, "createAccount:" + txtEmail.getText().toString());
                if (!validateForm()) {
                    return;
                }

                showProgressDialog();

                mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //updateUI(user);
                                    Toast.makeText(SignupActivity.this, getString(R.string.alert_user_created),
                                            Toast.LENGTH_LONG).show();
                                    goToLogin();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignupActivity.this, getString(R.string.alert_auth_failed),
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                hideProgressDialog();
                            }
                        });
                break;
        }
    }

    public void goToLogin(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = txtEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            txtEmail.setError(getString(R.string.field_required));
            valid = false;
        } else {
            txtEmail.setError(null);
        }

        String password = txtPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            txtPassword.setError(getString(R.string.field_required));
            valid = false;
        } else {
            txtPassword.setError(null);
        }

        String pwd = txtPasswordAgain.getText().toString();
        if (TextUtils.isEmpty(password)) {
            txtPasswordAgain.setError(getString(R.string.field_required));
            valid = false;
        } else {
            txtPasswordAgain.setError(null);
        }

        if (!txtPassword.getText().toString().equals(txtPasswordAgain.getText().toString())){
            txtPasswordAgain.setError(getString(R.string.field_not_matching));
            valid = false;
        } else {
            txtPasswordAgain.setError(null);
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
