package com.example.infox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.infox.Adapters.UserDetailsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    FloatingActionButton fab;
    DatabaseHelper mydb;
    List<Contacts> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DatabaseHelper(this);

        rv=findViewById(R.id.rv);
        fab=findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getuserdetails();
            }
        });

        setup_recyclerview();
    }

    private void setup_recyclerview() {

        contacts = new ArrayList<>(mydb.getalldatas());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        UserDetailsAdapter userDetailsAdapter = new UserDetailsAdapter(contacts,this);
        rv.setAdapter(userDetailsAdapter);
    }

    private void getuserdetails()
    {
        LayoutInflater layout = getLayoutInflater();
        View view = layout.inflate(R.layout.create_user_details, null, false);

        EditText name=view.findViewById(R.id.name);
        EditText contactno = view.findViewById(R.id.contactno);
        EditText emailid = view.findViewById(R.id.emailid);
        Button cancel = view.findViewById(R.id.cancel);
        Button done = view.findViewById(R.id.done);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        add_data(done,alertDialog,name,contactno,emailid);

    }

    private void add_data(Button done, AlertDialog alertDialog, EditText name, EditText contactno, EditText emailid) {
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean res=mydb.insertdata(name.getText().toString(),contactno.getText().toString(),emailid.getText().toString());

                if(res)
                {
                    Toast.makeText(getApplicationContext(), "DATA ADDED", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    setup_recyclerview();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "DATA NOT ADDED", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}