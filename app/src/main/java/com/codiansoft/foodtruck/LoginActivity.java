package com.codiansoft.foodtruck;

import android.app.Activity;
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


public class LoginActivity extends Activity {
    EditText txtEmailAddress;
    EditText txtPassword;
    TextView signup;
    TextView txtReset;
    ProgressBar progressBar;
    Button login;
    static Boolean is_user = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_login);
        Init();
        Clickevent();
    }

    void Init() {
        is_user = getIntent().getBooleanExtra(AppConstants.IS_CUSTOMER, false);
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        setupUI(findViewById(R.id.parent));
        login = (Button) findViewById(R.id.btnLogin);
        txtEmailAddress = (EditText) findViewById(R.id.useremail);
        txtPassword = (EditText) findViewById(R.id.pass);
        signup = (TextView) findViewById(R.id.newSignup);
        txtReset = (TextView) findViewById(R.id.txtReset);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(LoginActivity.this);
                    return false;
                }

            });
        }

         /*-- layout container, iterate over children and seed recursion.--*/
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    void Clickevent() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationChecker(txtEmailAddress, txtPassword)) {
                    if (is_user) {
                        LoginFun(txtEmailAddress.getText().toString(), txtPassword.getText().toString(), "3");
                    } else {
                        LoginFun(txtEmailAddress.getText().toString(), txtPassword.getText().toString(), "2");
                    }
                }

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this, SignupActivity.class).putExtra(AppConstants.IS_CUSTOMER, getIntent().getBooleanExtra(AppConstants.IS_CUSTOMER, false)));


            }
        });

        txtReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });


    }


    void LoginFun(final String email, final String Password, final String Usertype) {


        progressBar.setVisibility(View.VISIBLE);
        final StringRequest req = new StringRequest(Request.Method.POST, AppConstants.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserModel user;
                        try {
                            Log.d("USERID", response.toString());

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
                                userModel.setDescription(Data.getString("food_title"));
                                userModel.setProfilepic(Data.getString("image"));
                                userModel.setPhonenumber(Data.getString("contact_no"));
                                userModel.setUsertype(Data.getString("user_type"));
                                userModel.setIstrackon(Data.getString("track_location"));
                                MyApplication.getInstance().getPrefManger().setUserProfile(userModel);


                                startActivity(new Intent(LoginActivity.this, DrawerActivity.class));
                                finish();

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
                            utilities.dialog("Already Exist", LoginActivity.this);
                            break;
                        case 400:
                            utilities.dialog("Invalid Credentials", LoginActivity.this);
                            break;
                        case 411:
                            utilities.dialog("Account not Verified yet.", LoginActivity.this);
                            break;
                        default:
                            utilities.dialog("Connection Problem", LoginActivity.this);
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

                params.put("username", email);
                params.put("password", Password);

                params.put("user_type", Usertype);
                params.put("device_token", FirebaseInstanceId.getInstance().getToken());
                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MyApplication.getInstance().addToRequestQueue(req);


    }


    public static Boolean validationChecker(EditText username, EditText Password) {
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

        return valid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplication(), MapsActivity.class));
    }
}
