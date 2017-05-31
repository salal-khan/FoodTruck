package com.codiansoft.foodtruck;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RecoverPasswordActivity extends Activity {


    EditText txtSecurityCode, txtNewPassword, txtConfirmPassword;
    Button btnclick;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_recover_password);
        txtSecurityCode = (EditText) findViewById(R.id.txtSecurityCode);
        txtNewPassword = (EditText) findViewById(R.id.txtNewPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        final String email = getIntent().getStringExtra("email");
        btnclick = (Button) findViewById(R.id.btnRecover);
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);


        btnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validationChecker(txtSecurityCode, txtNewPassword, txtConfirmPassword)) {
                    RequestData(txtSecurityCode.getText().toString().trim(), txtNewPassword.getText().toString().trim(), email);
                }

            }
        });
    }


    public void RequestData(final String SCode, final String NPassword, final String email) {
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest req = new StringRequest(Request.Method.POST, AppConstants.RECOVERPASSWORD,
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

//                                Toast.makeText(getApplicationContext(),"sucess",Toast.LENGTH_LONG).show();
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(RecoverPasswordActivity.this);
                                builder1.setTitle("SuccessFully");
                                builder1.setMessage(Data);
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                finish();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();

//                                        startActivity(new Intent(ResetPasswordActivity.this, DrawerActivity.class));
//                                        finish();

                            } else {

//                                Toast.makeText(getApplicationContext(),"sucess wrong",Toast.LENGTH_LONG).show();
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(RecoverPasswordActivity.this);
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
                            utilities.dialog("Already Exist", RecoverPasswordActivity.this);
                            break;
                        case 400:
                            utilities.dialog("Invalid Credentials", RecoverPasswordActivity.this);
                            break;
                        case 411:
                            utilities.dialog("Account not Verified yet.", RecoverPasswordActivity.this);
                            break;
                        default:
                            utilities.dialog("Connection Problem", RecoverPasswordActivity.this);
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
                params.put("Code", SCode);
                params.put("NewPassword", NPassword);

                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MyApplication.getInstance().addToRequestQueue(req);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplication(), MapsActivity.class));
    }


    public static Boolean validationChecker(EditText code, EditText Password, EditText rePassword) {
        boolean valid = true;

        if (code.getText().toString().isEmpty()) {
            code.setError("enter  Username");
            valid = false;
        } else {
            code.setError(null);
        }

        if (Password.getText().toString().isEmpty()) {
            Password.setError("enter Password");
            valid = false;
        } else {
            Password.setError(null);
        }


        if (rePassword.getText().toString().isEmpty() || !rePassword.getText().toString().equals(Password.getText().toString())) {
            rePassword.setError("Password dosn't match");
            valid = false;
        } else {
            rePassword.setError(null);
        }


        if (rePassword.getText().length() < 6) {
            rePassword.setError("Password Minimum 6 character");
            valid = false;
        } else {
            rePassword.setError(null);
        }

        return valid;
    }

}
