package com.ajain.securediary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class NewItemActivity extends AppCompatActivity {
    private EditText itemTitleTF;
    private EditText itemDataTF;
    private EditText customPassTF;
    private RadioGroup passRadioGroup;
    private int selID;
    private boolean isPassDefault = true;
    private boolean isEditPath = false;
    private String oldTitle, oldData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        itemTitleTF = (EditText) findViewById(R.id.itemTitleTF);
        itemDataTF = (EditText) findViewById(R.id.itemDataTF);
        customPassTF = (EditText) findViewById(R.id.customPassTF);
        Button saveButton = (Button) findViewById(R.id.saveItemBtn);
        RadioButton defaultPassRadioBtn = (RadioButton) findViewById(R.id.defaultPassRadioBtn);
        RadioButton customPassRadioBtn = (RadioButton) findViewById(R.id.customPassRadioBtn);
        passRadioGroup = (RadioGroup) findViewById(R.id.passSelRadioGrp);
        selID = passRadioGroup.getCheckedRadioButtonId();
        customPassTF.setVisibility(View.INVISIBLE);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioOnClickListener();
            }
        };
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });
        defaultPassRadioBtn.setOnClickListener(listener);
        customPassRadioBtn.setOnClickListener(listener);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            oldData = bundle.getString("data");
            oldTitle = bundle.getString("title");
            itemTitleTF.setText(oldTitle);
            itemDataTF.setText(oldData);
            isEditPath = true;
        }
    }

    private void radioOnClickListener() {
        if (selID != passRadioGroup.getCheckedRadioButtonId()) {
            selID = passRadioGroup.getCheckedRadioButtonId();
            switch (selID) {
                case R.id.defaultPassRadioBtn:
                    isPassDefault = true;
                    customPassTF.setVisibility(View.INVISIBLE);
                    break;
                case R.id.customPassRadioBtn:
                    isPassDefault = false;
                    customPassTF.setVisibility(View.VISIBLE);
                    break;
                default:
            }
        }
    }

    private void saveItem() {
        String password;
        if ("".equals(itemTitleTF.getText().toString()) || "".equals(itemDataTF.getText().toString())) {
            return;
        }
        if (isPassDefault) {
            password = StoreData.getInstance(this).getMasterPassword();
        } else {
            password = customPassTF.getText().toString();
            if ("".equals(password))
                return;
            Encrypt encrypt = new Encrypt(password);
            password = encrypt.getEncryptedPassword();
        }
        if (!isEditPath) {
            PasswordCard.getInstance().addNewEntry(itemTitleTF.getText().toString(), itemDataTF.getText().toString(), password);
        } else {
            PasswordCard.getInstance().editEntry(oldTitle, oldData, itemTitleTF.getText().toString(), itemDataTF.getText().toString(), password);
        }
        StoreData.getInstance(this).saveDataEntries(PasswordCard.getInstance().getDataEntries());
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
