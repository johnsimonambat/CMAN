package com.example.john.cman_api;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by john on 30/1/15.
 */
public class HomePage extends Activity{

    Button post_ticket_button;
    Button view_ticket;
    Button view_all_ticket;
    String UserID1;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        post_ticket_button = (Button)findViewById(R.id.post_ticket_button);
        view_ticket = (Button)findViewById(R.id.view_ticket_button);
        view_all_ticket = (Button)findViewById(R.id.view_all_ticket_button);

        Intent intent = getIntent();
        final String UserID = intent.getExtras().getString("UserID");
       // Toast.makeText(getApplicationContext(), UserID, Toast.LENGTH_LONG).show();
        UserID1 = UserID;

        post_ticket_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(HomePage.this,
                       Post_ticket.class);
               myIntent.putExtra("UserID", UserID);
               // Toast.makeText(getApplicationContext(), "In home page", Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), UserID, Toast.LENGTH_LONG).show();
                startActivity(myIntent);

            }
        });


        view_ticket.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(HomePage.this,
                        View_ticket.class);
                myIntent.putExtra("UserID", UserID);
               // Toast.makeText(getApplicationContext(),"clicked view button",Toast.LENGTH_LONG).show();
                startActivity(myIntent);

            }
        });

    view_all_ticket.setOnClickListener(new View.OnClickListener() {
        public void onClick(View arg0) {

            // Start NewActivity.class
            Intent myIntent = new Intent(HomePage.this,
                    View_all_tickets.class);
            myIntent.putExtra("UserID", UserID);
            startActivity(myIntent);

        }
    });

    }


}
