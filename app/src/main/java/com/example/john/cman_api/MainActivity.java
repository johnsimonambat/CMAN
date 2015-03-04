package com.example.john.cman_api;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    Button login_button;
    EditText loginID;
    EditText loginPass;
    TextView tv;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_button = (Button)findViewById(R.id.login_button);
        loginID = (EditText)findViewById(R.id.loginID);
        loginPass = (EditText)findViewById(R.id.loginPass);
        //tv = (TextView)findViewById(R.id.tv);

        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if ( conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ){

            Toast.makeText(getApplicationContext(), "Internet is connected", Toast.LENGTH_LONG).show();

        }

        else if ( conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

            // notify user you are not online

            showAlert_internet();
            // Toast.makeText(getApplicationContext(), "No Network available.Connect to INTERNET", Toast.LENGTH_LONG).show();
        }

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



              //--------------------------------------------------//
                String loginidstrng = loginID.getText().toString();
                String loginpassstring = loginPass.getText().toString();
                 if (loginidstrng.matches("") || (loginpassstring.matches(""))) {
                    Toast.makeText(MainActivity.this, "enter  UserID/Password", Toast.LENGTH_SHORT).show();

                 }else {
//------------------------------------------------------------------------------//
                     dialog = ProgressDialog.show(MainActivity.this, "",
                             "Validating user...", true);
                     new Thread(new Runnable() {
                         public void run() {
                             try {


                                 login();


                             } catch (UnsupportedEncodingException e) {
                                 e.printStackTrace();
                             }
                         }
                     }).start();

                     //else
//-------------------------------------//
                 }
         //---------------------------------//


            }
        });


    }


    void login() throws UnsupportedEncodingException {
        try{

            httpclient=new DefaultHttpClient();

            httppost = new HttpPost("http://cman.avaninfotech.com/api/cman/v1/checkLogin/");
            nameValuePairs = new ArrayList<>(2);
            // Always use the same <span id="IL_AD12" class="IL_AD">variable</span> name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("loginID",loginID.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("loginPass",loginPass.getText().toString().trim()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //Execute HTTP Post Request
            response=httpclient.execute(httppost);

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            runOnUiThread(new Runnable() {
                public void run() {
                  //  tv.setText("Response from PHP : " + response);
                    dialog.dismiss();
                }
            });



            if(response.equals("0")){
                showAlert();



            }else{
              // showAlert();

                //-----------------------------//
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                       // Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this, HomePage.class);
                        i.putExtra("UserID", response);
                        startActivity(i);

                    }
                });
                //-------------------------------//




            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showAlert(){
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Login Error.");
                builder.setMessage("User not Found.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void showAlert_internet(){
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("NO Internet");
                builder.setMessage("Not connected to Internet.Please connect and Login")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
