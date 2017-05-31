package com.codiansoft.foodtruck.NewOrderRequests;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codiansoft.foodtruck.Models.USERSORDERS;
import com.codiansoft.foodtruck.Models.UserModel;
import com.codiansoft.foodtruck.R;
import com.codiansoft.foodtruck.Utils.AppConstants;
import com.codiansoft.foodtruck.Utils.MyApplication;
import com.codiansoft.foodtruck.Utils.utilities;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class GenreViewHolder extends GroupViewHolder {

    private TextView timetake, totalcost, datetime;
    private ImageView arrow;
    Button btn_accept, btn_reject;
    Context c;
    FragmentNewOrders fragmentNewOrders;

    public GenreViewHolder(View itemView, Context c, FragmentNewOrders ff) {

        super(itemView);
        this.c = c;
        fragmentNewOrders = ff;
        timetake = (TextView) itemView.findViewById(R.id.time);
        totalcost = (TextView) itemView.findViewById(R.id.totalcost);
        btn_accept = (Button) itemView.findViewById(R.id.btn_accept);
        btn_reject = (Button) itemView.findViewById(R.id.btn_reject);
        arrow = (ImageView) itemView.findViewById(R.id.list_item_genre_arrow);

    }

    public void setGenreTitle(final ExpandableGroup genre) {
        if (genre instanceof USERSORDERS) {
            timetake.setText("");
            totalcost.setText(((USERSORDERS) genre).getTotalprice());
            btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!timetake.getText().toString().equals("")) {
                        new MaterialDialog.Builder(c)
                                .title("Accept")
                                .content("Are You Sure, You Want Accept Order")
                                .positiveText("Yes, Proceed")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {

                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        long selected_time = (long) timetake.getTag() + MyApplication.getInstance().getPrefManger().getLasttime();
                                        ACCEPT_REJECT(((USERSORDERS) genre).getOrderid(), "1", convert(selected_time));
                                    }
                                })
                                .negativeText("Cancel").onNegative(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                                .show();

                    } else {

                        Toast.makeText(c, "Put time for this order", Toast.LENGTH_LONG).show();
                    }

                }
            });
            btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new MaterialDialog.Builder(c)
                            .title("Reject")
                            .content("Are You Sure, You Want Reject Order")
                            .positiveText("Yes, Proceed")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {

                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    ACCEPT_REJECT(((USERSORDERS) genre).getOrderid(), "2", "");
                                }
                            })
                            .negativeText("Cancel").onNegative(new MaterialDialog.SingleButtonCallback() {

                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                            .show();

                }
            });
            timetake.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    long total = (hour * 60 * 60 * 1000) + (minute * 60 * 1000);
                    timetake.setTag(total);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(c, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            timetake.setText(selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Time Required For this Order");
                    mTimePicker.show();
                }
            });
        }

    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
        arrow.setImageDrawable(c.getResources().getDrawable(R.drawable.colaps_btn));
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
        arrow.setImageDrawable(c.getResources().getDrawable(R.drawable.plus));
    }


    void ACCEPT_REJECT(final String orderId, final String updateIndex, final String Datetime) {

        fragmentNewOrders.progressBar.setVisibility(View.VISIBLE);
        final StringRequest req = new StringRequest(Request.Method.POST, AppConstants.ACCEPT_ORDERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserModel user;
                        try {
                            Log.d("USERID", response.toString());

                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");

                            fragmentNewOrders.GETORDERS();

                        } catch (Exception ee) {
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {

                        default:
                            utilities.dialog("Connection Problem", c);
                            break;
                    }
                }


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //userId=XXX&routeId=XXX&selected=XXX
                Map<String, String> params = new HashMap<String, String>();


                params.put("api_secret", MyApplication.getInstance().getPrefManger().getUserProfile().getApi_secret());


                params.put("order_id", orderId);
                params.put("update_type", updateIndex);
                params.put("delivery_time", Datetime);

                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MyApplication.getInstance().addToRequestQueue(req);


    }

    public static String convert(long milliSeconds) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
