package com.nirav.retrofit;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nirav.retrofit.utils.InternetConnection;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private View parentView;

    private ArrayList<Contact> contactList;
    private MyContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactList = new ArrayList<>();

        parentView = findViewById(R.id.parentLayout);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(parentView, contactList.get(position).getName() + " => " + contactList.get(position).getPhone().getHome(), Snackbar.LENGTH_LONG).show();
            }
        });

        /**
         * Just to know onClick and Printing Hello Toast in Center.
         */
       /* Toast toast = Toast.makeText(getApplicationContext(), "Click on FloatingActionButton to Load JSON", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();*/

        /**
         * Checking Internet Connection
         */
        if (InternetConnection.checkConnection(getApplicationContext())) {
            final ProgressDialog dialog;
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Hey Wait Please...");
            dialog.setMessage("I am getting your JSON");
            dialog.show();

            //Creating an object of our api interface
            ApiService api = RetroClient.getApiService();

            /**
             * Calling JSON
             */
            Call<ContactList> call = api.getMyJSON();

            /**
             * Enqueue Callback will be call when get response...
             */
            call.enqueue(new Callback<ContactList>() {
                @Override
                public void onResponse(Call<ContactList> call, Response<ContactList> response) {
                    //Dismiss Dialog
                    dialog.dismiss();

                    if (response.isSuccessful()) {
                        /**
                         * Got Successfully
                         */
                        contactList = response.body().getContacts();

                        /**
                         * Binding that List to Adapter
                         */
                        adapter = new MyContactAdapter(MainActivity.this, contactList);
                        listView.setAdapter(adapter);

                    } else {
                        Snackbar.make(parentView, "Something went wrong.", Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ContactList> call, Throwable t) {
                    dialog.dismiss();
                }
            });

        } else {
            Snackbar.make(parentView, "Internet Connection Not Available", Snackbar.LENGTH_LONG).show();
        }
    }
}