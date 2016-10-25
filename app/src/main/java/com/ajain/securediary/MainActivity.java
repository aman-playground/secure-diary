package com.ajain.securediary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText passwordTF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        passwordTF = (EditText) findViewById(R.id.passwordTF);
        Button saveBtn = (Button) findViewById(R.id.savePassBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encryptPassword();
            }
        });
        if (null != StoreData.getInstance(this).getMasterPassword()) {
            showListActivity();
        }
    }

    private void encryptPassword() {
        String pass = passwordTF.getText().toString();
        if ("".equals(pass))
            return;
        Encrypt encrypt = new Encrypt(pass);
        StoreData.getInstance(this).saveMasterPassword(encrypt.getEncryptedPassword());
        showListActivity();
    }

    private void showListActivity() {
        Intent intent = new Intent(this, PasswordListActivity.class);
        startActivity(intent);
        finish();
    }
}
