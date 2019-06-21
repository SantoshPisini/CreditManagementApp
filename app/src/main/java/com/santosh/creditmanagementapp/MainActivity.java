package com.santosh.creditmanagementapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private static final String URL_DATA_ADD = "https://thecodont-tears.000webhostapp.com/sparks/newuser.php";
    private static final String URL_DATA = "https://thecodont-tears.000webhostapp.com/sparks/user_list.php";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    ArrayList<User> listItems;
    Button button, button_submit;

    TextInputLayout t1, t2, t3, t4, t5;
    TextInputEditText e1, e2, e3, e4, e5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.list_view);
        recyclerView.setHasFixedSize(true);
        button = findViewById(R.id.button_add_user);
        button_submit = findViewById(R.id.button_submit);
        t1 = findViewById(R.id.text_input_layout_1);
        t2 = findViewById(R.id.text_input_layout_2);
        t3 = findViewById(R.id.text_input_layout_3);
        t4 = findViewById(R.id.text_input_layout_4);
        t5 = findViewById(R.id.text_input_layout_credit);
        e1 = findViewById(R.id.editText_user_id);
        e2 = findViewById(R.id.editText_user_name);
        e3 = findViewById(R.id.editText_user_email);
        e4 = findViewById(R.id.editText_user_phone);
        e5 = findViewById(R.id.editText_user_credit);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        listItems = new ArrayList<>();
        setHome();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAdd();
            }
        });
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(e1.getText())) {
                    Toast.makeText(MainActivity.this, "Empty fields!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    uploadToDatabase();
                    Toast.makeText(getApplicationContext(), "User Added Successfully", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }
        });
    }

    private void uploadToDatabase() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading  . . .");
        progressDialog.show();
        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATA_ADD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();
                        // Showing response message coming from server.
                        resetFields();
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
                params.put("user_id", String.valueOf(e1.getText()));
                params.put("user_name", String.valueOf(e2.getText()));
                params.put("user_email", String.valueOf(e3.getText()));
                params.put("user_phone", String.valueOf(e4.getText()));
                params.put("user_credits", String.valueOf(e5.getText()));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        progressDialog.setMessage("Adding User. . .");
        requestQueue.add(stringRequest);
    }

    private void resetFields() {
        e1.setText("");
        e2.setText("");
        e3.setText("");
        e5.setText("");
        e4.setText("");
    }

    private void setHome() {
        recyclerView.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
        button_submit.setVisibility(View.INVISIBLE);
        t1.setVisibility(View.INVISIBLE);
        t2.setVisibility(View.INVISIBLE);
        t3.setVisibility(View.INVISIBLE);
        t4.setVisibility(View.INVISIBLE);
        t5.setVisibility(View.INVISIBLE);
        e1.setVisibility(View.INVISIBLE);
        e2.setVisibility(View.INVISIBLE);
        e3.setVisibility(View.INVISIBLE);
        e4.setVisibility(View.INVISIBLE);
        e5.setVisibility(View.INVISIBLE);
        loadRecyclerViewData();
    }

    private void setAdd() {
        recyclerView.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);
        button_submit.setVisibility(View.VISIBLE);
        t1.setVisibility(View.VISIBLE);
        t2.setVisibility(View.VISIBLE);
        t3.setVisibility(View.VISIBLE);
        t4.setVisibility(View.VISIBLE);
        t5.setVisibility(View.VISIBLE);
        e1.setVisibility(View.VISIBLE);
        e2.setVisibility(View.VISIBLE);
        e3.setVisibility(View.VISIBLE);
        e4.setVisibility(View.VISIBLE);
        e5.setVisibility(View.VISIBLE);
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
                                User item = new User(
                                        o.getString("user_id"),
                                        o.getString("user_name"),
                                        o.getString("user_email"),
                                        o.getString("user_phone"),
                                        o.getString("user_credits"));
                                listItems.add(item);
                            }
                            adapter = new UserAdapter(getApplicationContext(), listItems);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

}
