package com.santosh.creditmanagementapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    private static final String URL_DATA = "https://thecodont-tears.000webhostapp.com/sparks/user_list.php";
    private static final String URL_DATA_TRANSFER = "https://thecodont-tears.000webhostapp.com/sparks/transfer.php";
    ArrayList<String> listItems;
    String id;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        id = getIntent().getStringExtra("id");
        final String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");
        final String credits = getIntent().getStringExtra("credits");
        listView = findViewById(R.id.transfer);

        listItems = new ArrayList<>();

        TextView tv1, tv2, tv3, tv4, tv5;
        final TextInputEditText e1;
        final Button button;

        tv1 = findViewById(R.id.user_name);
        tv2 = findViewById(R.id.user_id);
        tv3 = findViewById(R.id.user_email);
        tv4 = findViewById(R.id.user_phone);
        tv5 = findViewById(R.id.user_credits);
        e1 = findViewById(R.id.user_credit_et);
        button = findViewById(R.id.button);

        tv1.setText("Name : " + name);
        tv2.setText("Id : " + id);
        tv3.setText("Email : " + email);
        tv4.setText("Phone : " + phone);
        tv5.setText(credits);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(e1.getText())) {
                    e1.setError("Enter Credits!!!");
                    return;
                }
                if (TextUtils.isDigitsOnly(e1.getText())) {
                    if (Integer.parseInt(e1.getText().toString()) <= Integer.parseInt(credits)) {
                        //startTransaction();
                        loadRecyclerViewData();
//                        Toast.makeText(UserActivity.this,new MainActivity()..size()+"",Toast.LENGTH_LONG).show();
                    } else {
                        e1.setError("No enough credits to transfer !");
                        return;
                    }
                } else {
                    e1.setError("Enter numeric values!!!");
                    return;
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                e1.setVisibility(View.INVISIBLE);
                button.setVisibility(View.INVISIBLE);
                transferStart(e1.getText().toString(), name, listItems.get(i));
            }
        });

    }

    private void transferStart(final String credit, final String from, final String to) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading  . . .");
        progressDialog.show();
        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATA_TRANSFER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();
                        // Showing response message coming from server.
                        Toast.makeText(getApplicationContext(), "Transaction Success!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UserActivity.this, MainActivity.class));
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();
                        // Showing error message if something goes wrong.
                        Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<>();

                // Adding All values to Params.
                params.put("from", from);
                params.put("to", to);
                params.put("credit", credit);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        progressDialog.setMessage("Adding User. . .");
        requestQueue.add(stringRequest);

    }

    private void loadRecyclerViewData() {
        listItems.clear();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data . . .");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject o = jsonArray.getJSONObject(i);
                                if (!o.getString("user_id").equals(id))
                                    listItems.add(o.getString("user_name"));
                            }
                            listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listItems));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
}
