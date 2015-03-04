package com.example.john.cman_api;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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
import java.util.List;

/**
 * Created by john on 3/3/15.
 */
public class View_ticket extends Activity{
    Button select_button;
    EditText ticket_id;
    String ticket;

    HttpPost httppost;

    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;


    InputStream is = null;

    String result = "";

    JSONObject jArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_ticket);

        //final TextView ticketview = (TextView)findViewById(R.id.ticketview);
        final TextView subjectvew = (TextView)findViewById(R.id.subjectview);
        final TextView messageview = (TextView)findViewById(R.id.mesageview);
        final TextView statusview = (TextView)findViewById(R.id.statusview);
        final TextView dateview = (TextView)findViewById(R.id.dateview);
         // final TextView productview = (TextView)findViewById(R.id.productview);
        final TextView subjectlabel = (TextView)findViewById(R.id.subjectlabel);
        final TextView messagelabel =(TextView)findViewById(R.id.messagelabel);
        final TextView statuslabel = (TextView)findViewById(R.id.statuslabel);
        final TextView datelabel = (TextView)findViewById(R.id.datelabel);
       // final TextView productlabel = (TextView)findViewById(R.id.productlabel);
        final ListView lisView1 = (ListView)findViewById(R.id.listView1);
        select_button = (Button)findViewById(R.id.ticket_button);

        select_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ticket_id = (EditText)findViewById(R.id.ticket_id);

                System.out.println("ticket" +ticket);
                try {

                    // String url = "http://192.168.1.101/API/get_cman.php?TicketID="+ticket;
                    String url = "http://cman.avaninfotech.com/api/cman/v1/ViewTicketByID";

                    // String url = "http://cman.avaninfotech.com/api/cman/v1/ViewTicket?UserID="+UserID;
                    System.out.println(url);


                    JSONArray data = new JSONArray(getJSONUrl(url));

                    final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<>();
                    HashMap<String, String> map;

                    for(int i = 0; i < data.length(); i++){
                        JSONObject c = data.getJSONObject(i);

                       String ticket =c.getString("TicketID");
                        String sub = c.getString("Subject");
                        String message = c.getString("Message");
                        String statusDetail = c.getString("StatusDetail");
                        String DateReport = c.getString("DateReport");
                        //ticketlabel.setText("TicketID :");
                        //  ticketview.setText(ticket);
                        subjectlabel.setText("Subject :");
                        subjectvew.setText(sub);
                        messagelabel.setText("Message :");
                        messageview.setText(message);
                       // productlabel.setText("Product :");
                       // productview.setText();
                        statuslabel.setText("Status :");
                        statusview.setText(statusDetail);
                        datelabel.setText("Date of Report :");
                        dateview.setText(DateReport);
                        Toast.makeText(getApplicationContext(), ticket, Toast.LENGTH_LONG).show();
                        map = new HashMap<>();
                        map.put("TicketID", c.getString("TicketID"));
                        map.put("Subject", c.getString("Subject"));

                        map.put("StatusDetail", c.getString("StatusDetail"));
                        MyArrList.add(map);

                    }

                    SimpleAdapter sAdap;
                    sAdap = new SimpleAdapter(View_ticket.this, MyArrList, R.layout.activity_column,
                            new String[] {"TicketID", "Subject", "StatusID"}, new int[] {R.id.ColMemberID, R.id.ColName, R.id.ColTel});
                    lisView1.setAdapter(sAdap);

                    final AlertDialog.Builder viewDetail = new AlertDialog.Builder(View_ticket.this);
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
        });





        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }



    }

    public String getJSONUrl(String url) throws UnsupportedEncodingException {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("TicketID",ticket_id.getText().toString()));
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
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
