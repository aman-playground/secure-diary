package com.ajain.securediary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PasswordListActivity extends AppCompatActivity {
    private ConstraintLayout noListView;
    private ConstraintLayout displayListView;
    private int secureItemCount;
    private ArrayList<Entry> dataEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_password_list);
        noListView = (ConstraintLayout) findViewById(R.id.noListView);
        displayListView = (ConstraintLayout) findViewById(R.id.displayListView);
        ListView secureListView = (ListView) findViewById(R.id.secureListView);
        FloatingActionButton addMoreBtn = (FloatingActionButton) findViewById(R.id.addItemBtn);
        addMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMoreItems();
            }
        });

        dataEntries = StoreData.getInstance(this).getDataEntries();
        if (dataEntries != null) {
            PasswordListAdapter passwordListAdapter = new PasswordListAdapter(this, R.layout.list_item, dataEntries);
            secureListView.setAdapter(passwordListAdapter);
            setSecureItemCount(dataEntries.size());
        } else {
            setSecureItemCount(0);
        }

        secureListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                confirmPasswordFor(dataEntries.get(i), "show");
            }
        });
    }

    public void setSecureItemCount(int secureItemCount) {
        this.secureItemCount = secureItemCount;
        modifyLayout();
    }

    private void modifyLayout() {
        if (secureItemCount > 0) {
            noListView.setVisibility(View.INVISIBLE);
            displayListView.setVisibility(View.VISIBLE);
        } else {
            noListView.setVisibility(View.VISIBLE);
            displayListView.setVisibility(View.INVISIBLE);
        }
    }

    private void addMoreItems() {
        Intent intent = new Intent(this, NewItemActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manage, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.defaultPassMenuItem:
                openChangePassDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openChangePassDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Change Password");
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.change_pass_layout, null);
        alert.setView(view);
        final ViewHolder viewHolder = new ViewHolder(view);
        alert.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveNewPassword(viewHolder.oldPassTF.getText().toString()
                        , viewHolder.newPassTF.getText().toString()
                        , viewHolder.chkPassTF.getText().toString());
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private void saveNewPassword(String oldPass, String newPass, String chkPass) {
        if ("".equals(oldPass) || "".equals(newPass) || "".equals(chkPass)) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(chkPass)) {
            Toast.makeText(this, "New and Confirm passwords must be same", Toast.LENGTH_LONG).show();
            return;
        }

        if (!StoreData.getInstance(this).getMasterPassword().equals(new Encrypt(oldPass).getEncryptedPassword())) {
            Toast.makeText(this, "Old password is wrong", Toast.LENGTH_SHORT).show();
            return;
        }

        Encrypt encrypt = new Encrypt(newPass);
        StoreData.getInstance(this).saveMasterPassword(encrypt.getEncryptedPassword());
        Toast.makeText(this, "Default password successfully updated", Toast.LENGTH_LONG).show();
    }

    public void confirmPasswordFor(final Entry entry, final String task) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter password to proceed:");
        LayoutInflater inflater = LayoutInflater.from(this);
        final EditText input = (EditText) inflater.inflate(R.layout.password_et, null);
        alert.setView(input);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Encrypt encrypt = new Encrypt(input.getText().toString());
                if (encrypt.getEncryptedPassword().equals(entry.getPassword())) {
                    switch (task) {
                        case "edit":
                            editEntry(entry);
                            break;
                        case "delete":
                            deleteEntry(entry);
                            break;
                        case "show":
                            dialog.cancel();
                            displayEntry(entry);
                        default:
                    }
                } else {
                    Toast.makeText(PasswordListActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private void displayEntry(Entry entry) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(entry.getTitle());
        alert.setMessage(entry.getData());
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    public void editEntry(Entry e) {
        Intent intent = new Intent(this, NewItemActivity.class);
        intent.putExtra("title", e.getTitle());
        intent.putExtra("data", e.getData());
        startActivity(intent);
    }

    public boolean deleteEntry(Entry e) {
        boolean status = PasswordCard.getInstance().deleteEntry(e);
        if (status) {
            dataEntries = PasswordCard.getInstance().getDataEntries();
            StoreData.getInstance(this).saveDataEntries(dataEntries);

            if (dataEntries.size() == 0) {
                setSecureItemCount(0);
            }
        }
        return status;
    }

    private class ViewHolder {
        EditText oldPassTF;
        EditText newPassTF;
        EditText chkPassTF;

        ViewHolder(View view) {
            oldPassTF = (EditText) view.findViewById(R.id.oldPassTF);
            newPassTF = (EditText) view.findViewById(R.id.newPassTF);
            chkPassTF = (EditText) view.findViewById(R.id.chkPassTF);
        }
    }
}


