package com.example.john.cman_api;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 30/1/15.
 */
public class Post_ticket extends Activity {


    private RadioGroup radioGroup;
    private RadioButton low, medium, high, priority;
    private Button button;
    private TextView textView;
    String radio_text;
    int sid;
    int radioId;
    String product_text;
    Spinner spinnerDropDown;
    String[] product = {

            "SAMP",
            "OpenLypsaa",
            "C-MAN",
            "Hardware",
            "Kiosk",

    };

    EditText subject, message;

    ProgressDialog Dialog = null;

    HttpPost httppost;

    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
        int userid;
    String UserID;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_ticket);


        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
             UserID = extras.getString("UserID");


           // Toast.makeText(getApplicationContext(),"Inside post ticket", Toast.LENGTH_LONG).show();

          //  Toast.makeText(getApplicationContext(), UserID, Toast.LENGTH_LONG).show();
        }




        subject = (EditText)findViewById(R.id.subject);
        message = (EditText)findViewById(R.id.message);
//-------------------------------spinner---------------------//

        spinnerDropDown =(Spinner)findViewById(R.id.spinner1);

        ArrayAdapter<String> adapter= new ArrayAdapter<>(this,android.
                R.layout.simple_spinner_dropdown_item ,product);

        spinnerDropDown.setAdapter(adapter);

        spinnerDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // Get select item
                 sid=spinnerDropDown.getSelectedItemPosition();
                System.out.println("the sid" +sid);

                //Toast.makeText(getBaseContext(), "You have selected  : " + product[sid],
                //      Toast.LENGTH_SHORT).show();


                product_text = spinnerDropDown.getSelectedItem().toString();
                Toast.makeText(getBaseContext(), "Selected product : " + product_text,
                       Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        //------------------------------------------------------------//

//----------------------------------radio group-------------------------------------//
        radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected

                int selected =
                        radioGroup.getCheckedRadioButtonId();
                priority = (RadioButton) findViewById(selected);
                //&&&&&&&&&&&&&&&&&&&&&&&&&//

                 radioId = radioGroup.indexOfChild(priority);
                RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
                radio_text = (String) btn.getText();
                Toast.makeText(getBaseContext(), "You have selected  : " + radio_text,
                        Toast.LENGTH_SHORT).show();

                //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&//


                System.out.println("priority"+priority);
               // Toast.makeText(Post_ticket.this,
                 //       priority.getText(), Toast.LENGTH_SHORT).show();



                if(checkedId == R.id.low) {
                   // Toast.makeText(getApplicationContext(), "choice: low",
                       //     Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.medium) {
                   // Toast.makeText(getApplicationContext(), "choice: medium",
                   //         Toast.LENGTH_SHORT).show();
                } else {
                   // Toast.makeText(getApplicationContext(), "choice: high",
                   //         Toast.LENGTH_SHORT).show();
                }
            }

        });

        low = (RadioButton) findViewById(R.id.low);
        medium = (RadioButton) findViewById(R.id.medium);
        high = (RadioButton) findViewById(R.id.high);
       // textView = (TextView) findViewById(R.id.text);

        button = (Button)findViewById(R.id.insertBtn);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find which radioButton is checked by id
                if(selectedId == low.getId()) {
                    textView.setText("You chose 'low priority' option");
                } else if(selectedId == medium.getId()) {
                    textView.setText("You chose 'medium prority' option");

                } else {
                    textView.setText("You chose 'high priority' option");
                }
            }
        });


//------------------------------------------------------------------------//

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sub =  subject.getText().toString();
                String mes = message.getText().toString();
                if (sub.matches("") || (mes.matches(""))) {
                    Toast.makeText(Post_ticket.this, "Please enter  Subject/Meaasge", Toast.LENGTH_SHORT).show();

                }
                //----------------------------------else
                else {
                    //----------------------------------else
                    Dialog = ProgressDialog.show(Post_ticket.this, "",
                            "inserting...", true);
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                login();
                                showAlert_Ticketpost();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
//----------------------------------else
                }
//----------------------------------
            }
        });

    }


    void login() throws UnsupportedEncodingException {
        try{


           // userid = Integer.parseInt(UserID);

            httpclient=new DefaultHttpClient();


            httppost = new HttpPost("http://cman.avaninfotech.com/api/cman/v1/insertTicket/");
            nameValuePairs = new ArrayList<>(2);
            // Always use the same <span id="IL_AD12" class="IL_AD">variable</span> name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("UserID",(UserID)));
           // nameValuePairs.add(new BasicNameValuePair("UserID",Integer.toString(Integer.parseInt(UserID))));
            nameValuePairs.add(new BasicNameValuePair("product",Integer.toString(sid)));
            nameValuePairs.add(new BasicNameValuePair("subject",subject.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("message",message.getText().toString().trim()));
            nameValuePairs.add(new BasicNameValuePair("priority",Integer.toString(radioId)));


            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            //Execute HTTP Post Request
            response=httpclient.execute(httppost);

           // ResponseHandler<String> responseHandler = new BasicResponseHandler();
            // final String response = httpclient.execute(httppost, responseHandler);
           // System.out.println("Response : " + response);






            runOnUiThread(new Runnable() {
                public void run() {

                    Dialog.dismiss();
                  ///  Toast.makeText(Post_ticket.this, "inserted ", Toast.LENGTH_SHORT).show();





                }
            });

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showAlert_Ticketpost(){
        Post_ticket.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Post_ticket.this);
                builder.setTitle("Posted");
                builder.setMessage("Ticket Posted. Wait for the status update")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }




}
