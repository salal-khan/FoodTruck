package com.codiansoft.foodtruck;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codiansoft.foodtruck.Models.UserModel;
import com.codiansoft.foodtruck.Utils.AppConstants;
import com.codiansoft.foodtruck.Utils.MyApplication;
import com.codiansoft.foodtruck.Utils.utilities;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ResetPasswordActivity extends Activity {


    EditText txtResetEmail;
    Button btnclick;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_reset_password);
        txtResetEmail = (EditText) findViewById(R.id.txtResetEmail);
        btnclick = (Button) findViewById(R.id.btnReset);
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);

        btnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = txtResetEmail.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                final StringRequest req = new StringRequest(Request.Method.POST, AppConstants.RESET_PASSWORD,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                UserModel user;
                                try {
                                    Log.d("USERID", response.toString());

                                    JSONObject Jobject = new JSONObject(response);
                                    JSONObject result = Jobject.getJSONObject("result");
                                    JSONObject Data1 = Jobject.getJSONObject("data");
                                    String Data = Data1.getString("user_data");
                                    if (result.get("status").equals("success")) {


                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ResetPasswordActivity.this);
                                        builder1.setTitle("SuccessFully");
                                        builder1.setMessage(Data);
                                        builder1.setCancelable(true);

                                        builder1.setPositiveButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                        startActivity(new Intent(ResetPasswordActivity.this, RecoverPasswordActivity.class).putExtra("email", email));
                                                        finish();
                                                    }
                                                });

                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();

//                                        startActivity(new Intent(ResetPasswordActivity.this, DrawerActivity.class));
//                                        finish();

                                    } else {


                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ResetPasswordActivity.this);
                                        builder1.setTitle("Failed");
                                        builder1.setMessage(Data);
                                        builder1.setCancelable(true);

                                        builder1.setPositiveButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();
                                    }
                                } catch (Exception ee) {
                                    Toast.makeText(getApplicationContext(), "error" + ee.getMessage(), Toast.LENGTH_LONG).show();
                                }


                                if (progressBar.isShown()) progressBar.setVisibility(View.GONE);

                                //                        getActivity().finish();


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 409:
                                    utilities.dialog("Already Exist", ResetPasswordActivity.this);
                                    break;
                                case 400:
                                    utilities.dialog("Invalid Credentials", ResetPasswordActivity.this);
                                    break;
                                case 411:
                                    utilities.dialog("Account not Verified yet.", ResetPasswordActivity.this);
                                    break;
                                default:
                                    utilities.dialog("Connection Problem", ResetPasswordActivity.this);
                                    break;
                            }

                        }
                        if (progressBar.isShown()) progressBar.setVisibility(View.GONE);

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        //userId=XXX&routeId=XXX&selected=XXX
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("Email", email);
                        return params;
                    }
                };

                req.setRetryPolicy(new DefaultRetryPolicy(3000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                // TODO Auto-generated method stub
                MyApplication.getInstance().addToRequestQueue(req);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplication(), MapsActivity.class));
    }
}
