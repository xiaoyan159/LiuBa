package com.navinfo.liuba;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = View.inflate(LoginActivity.this, R.layout.activity_login, null);
        setContentView(mView);
        loginButton = (Button) mView.findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent secretIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(secretIntent);
            }
        });
    }


}
