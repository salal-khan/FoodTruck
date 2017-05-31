package com.codiansoft.foodtruck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignupActivity extends Activity {
    EditText txtEmailAddress;
    EditText txtPassword;
    EditText txtusername;
    EditText txtenterpassword;
    EditText txtFullname;
    EditText txtphone;
    EditText txtplatenumber;
    EditText txtCategory;
    static Boolean is_user = true;
    Button Signup;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_signup);
        Init();
        Clickevent();
    }

    void Init() {
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);

        Signup = (Button) findViewById(R.id.btnsignup);
        txtEmailAddress = (EditText) findViewById(R.id.email);
        txtPassword = (EditText) findViewById(R.id.pass);
        txtFullname = (EditText) findViewById(R.id.fullname);
        txtenterpassword = (EditText) findViewById(R.id.repass);
        txtphone = (EditText) findViewById(R.id.mobile);
        txtusername = (EditText) findViewById(R.id.username);
        txtplatenumber = (EditText) findViewById(R.id.platenumber);
        txtCategory = (EditText) findViewById(R.id.edit_Cate);
        is_user = getIntent().getBooleanExtra(AppConstants.IS_CUSTOMER, false);
        if (is_user) {
            txtplatenumber.setVisibility(View.GONE);
            txtCategory.setVisibility(View.GONE);
        }


    }

    void Clickevent() {
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationChecker(txtusername, txtPassword, txtenterpassword, txtFullname, txtEmailAddress, txtphone, txtplatenumber, txtCategory)) {
                    if (is_user) {
                        SignupFun(txtEmailAddress.getText().toString(), txtPassword.getText().toString(), txtusername.getText().toString(), txtFullname.getText().toString(), txtphone.getText().toString(), txtplatenumber.getText().toString(), "3", txtCategory.getText().toString());
                    } else {
                        SignupFun(txtEmailAddress.getText().toString(), txtPassword.getText().toString(), txtusername.getText().toString(), txtFullname.getText().toString(), txtphone.getText().toString(), txtplatenumber.getText().toString(), "2", txtCategory.getText().toString());

                    }
                }


            }
        });


    }


    void SignupFun(final String email, final String Password, final String Username, final String name, final String contact, final String plateno, final String usertype, final String Category) {
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest req = new StringRequest(Request.Method.POST, AppConstants.USER_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserModel user;
                        try {


                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                JSONObject Data1 = Jobject.getJSONObject("data");
                                JSONObject Data = Data1.getJSONObject("user_data");
                                UserModel userModel = new UserModel();
                                userModel.setApi_secret(Data1.getString("api_secret"));
                                userModel.setUsername(Data.getString("name"));
                                userModel.setId(Data.getString("user_id"));
                                userModel.setEmail(Data.getString("email"));
                                userModel.setName(Data.getString("name"));
                                userModel.setProfilepic(Data.getString("image"));
                                userModel.setPhonenumber(Data.getString("contact_no"));
                                userModel.setUsertype(Data.getString("user_type"));
                                userModel.setIstrackon(Data.getString("track_location"));
                                if (Data.getString("user_type").equals("3")) {
                                    MyApplication.getInstance().getPrefManger().setUserProfile(userModel);
                                    startActivity(new Intent(SignupActivity.this, DrawerActivity.class).putExtra(AppConstants.IS_CUSTOMER, getIntent().getBooleanExtra(AppConstants.IS_CUSTOMER, false)));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Email will send after your account verification", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class).putExtra(AppConstants.IS_CUSTOMER, getIntent().getBooleanExtra(AppConstants.IS_CUSTOMER, false)));
                                    finish();
                                }
                            }
                        } catch (Exception ee) {
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
                            utilities.dialog("Username Already Exists", SignupActivity.this);
                            break;

                        case 410:
                            utilities.dialog("Email Already Exists", SignupActivity.this);
                            break;


                        case 411:
                            utilities.dialog("Minimum password lenght should be 6", SignupActivity.this);
                            break;

                        case 400:
                            utilities.dialog("Invalid User Type", SignupActivity.this);
                            break;

                        case 401:
                            utilities.dialog("Invalid Email Address.", SignupActivity.this);
                            break;


                        default:
                            utilities.dialog("Connection Problem", SignupActivity.this);
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
                params.put("name", name);
                params.put("email", email);
                params.put("password", Password);
                params.put("contact_no", contact);
                params.put("truck_no", plateno);
                params.put("user_type", usertype);
                params.put("username", Username);
                params.put("device_token", FirebaseInstanceId.getInstance().getToken());
                params.put("truckCate", Category);
                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MyApplication.getInstance().addToRequestQueue(req);


    }


    public static Boolean validationChecker(EditText username, EditText Password, EditText rePassword, EditText Fullname, EditText Email, EditText Phone, EditText Plate, EditText Category) {
        boolean valid = true;
        if (username.getText().toString().isEmpty()) {
            username.setError("enter  Username");
            valid = false;
        } else {
            username.setError(null);
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
        if (Fullname.getText().toString().isEmpty()) {
            Fullname.setError("enter Full Name");
            valid = false;
        } else {
            Fullname.setError(null);
        }
        if (Email.getText().toString().isEmpty()) {
            Email.setError("enter Email");
            valid = false;
        } else {
            Email.setError(null);
        }


        if (Phone.getText().toString().isEmpty()) {
            Phone.setError("enter Phone");
            valid = false;
        } else {
            Phone.setError(null);
        }

        if (!is_user && Plate.getText().toString().isEmpty()) {
            Plate.setError("enter Plate No");
            valid = false;
        } else {
            Plate.setError(null);
        }


        if (!is_user && Category.getText().toString().isEmpty()) {
            Category.setError("enter Category");
            valid = false;
        } else {
            Category.setError(null);
        }

        return valid;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
