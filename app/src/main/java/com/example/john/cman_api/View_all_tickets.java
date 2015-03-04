package com.example.john.cman_api;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by john on 13/2/15.
 */
public class View_all_tickets extends Activity {
   String UserID;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_tickets);


        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            UserID = extras.getString("UserID");


           Toast.makeText(getApplicationContext(), "ALL tickets posted", Toast.LENGTH_LONG).show();

         //   Toast.makeText(getApplicationContext(), UserID, Toast.LENGTH_LONG).show();
        }



        final ListView lisView1 = (ListView)findViewById(R.id.listView1);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // listView1
        // final ListView lisView1 = (ListView)findViewById(R.id.listView1);

        // String url = "http://192.168.1.102/API/get_cman.php";

        try {



            String url = "http://cman.avaninfotech.com/api/cman/v1/ViewTicket";
            JSONArray data = new JSONArray(getJSONUrl(url));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<>();
                map.put("TicketID", c.getString("TicketID"));
                map.put("Subject", c.getString("Subject"));

                map.put("StatusDetail", c.getString("StatusDetail"));
                MyArrList.add(map);

            }

            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(View_all_tickets.this, MyArrList, R.layout.activity_column,
                    new String[] {"TicketID", "Subject", "StatusID"}, new int[] {R.id.ColMemberID, R.id.ColName, R.id.ColTel});
            lisView1.setAdapter(sAdap);

            final AlertDialog.Builder viewDetail = new AlertDialog.Builder(this);
// OnClick Item
            lisView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> myAdapter, View myView,
                                        int position, long mylng) {

                    String sTicketID = MyArrList.get(position).get("TicketID").toString();
                    String sSubject = MyArrList.get(position).get("Subject").toString();
                    String sStatusDetail = MyArrList.get(position).get("StatusDetail").toString();

//String sMemberID = ((TextView) myView.findViewById(R.id.ColMemberID)).getText().toString();
// String sName = ((TextView) myView.findViewById(R.id.ColName)).getText().toString();
// String sTel = ((TextView) myView.findViewById(R.id.ColTel)).getText().toString();

                    viewDetail.setIcon(android.R.drawable.btn_star_big_on);
                    viewDetail.setTitle("ticket Details");
                    viewDetail.setMessage("TicketID : " + sTicketID + "\n"
                            + "Subject : " + sSubject + "\n" + "StatusDetail : " + sStatusDetail);
                    viewDetail.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
// TODO Auto-generated method stub
                                    dialog.dismiss();


                                }
                            });
                    viewDetail.show();

                }
            });


        } catch (JSONException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    public String getJSONUrl(String url) throws UnsupportedEncodingException {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();

        nameValuePairs.add(new BasicNameValuePair("UserID",UserID));

        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        //HttpGet httpGet = new HttpGet(url);
        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        try {
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Download OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

}