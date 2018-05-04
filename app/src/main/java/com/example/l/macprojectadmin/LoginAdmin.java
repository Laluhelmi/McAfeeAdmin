package com.example.l.macprojectadmin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.l.macprojectadmin.App.AppSession;
import com.example.l.macprojectadmin.Interface.OnLoginRequested;
import com.example.l.macprojectadmin.Request.LoginRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginAdmin extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        ((Button)findViewById(R.id.email_sign_in_button)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEmailView.getText().toString().isEmpty()){
                    mEmailView.setError("Email Tidak Boleh Kosong");
                }
                else if(mPasswordView.getText().toString().isEmpty()){
                    mPasswordView.setError("Password Tidak Boleh kosong");
                }
                else{
                    LoginRequest request =
                            new LoginRequest(LoginAdmin.this);
                    request.loginVoid(mEmailView.getText().toString(),
                            mPasswordView.getText().toString()
                            , new OnLoginRequested() {
                                @Override
                                public void onLogin(String respnse) {
                                    finish();
                                    AppSession session = new AppSession(getApplicationContext());
                                    session.setUsernameAndPassword(mEmailView.getText().toString()
                                    ,mPasswordView.getText().toString(),respnse);
                                    session.setEmail(mEmailView.getText().toString());
                                }
                            });
                }
            }
        });
    }

}

