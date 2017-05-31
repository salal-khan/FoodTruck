package com.codiansoft.foodtruck.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codiansoft.foodtruck.FragmentEditMenu;
import com.codiansoft.foodtruck.Models.EDITMENU;
import com.codiansoft.foodtruck.Models.UserModel;
import com.codiansoft.foodtruck.R;
import com.codiansoft.foodtruck.Utils.AppConstants;
import com.codiansoft.foodtruck.Utils.MyApplication;
import com.codiansoft.foodtruck.Utils.utilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EditMenuListAdapter extends RecyclerView.Adapter<EditMenuListAdapter.NotiViewHolder> {
    List<EDITMENU> notifications;
    static Boolean checkboxval = false;
    static Context sContext;
    FragmentEditMenu f;
    Dialog add_dialog;

    public EditMenuListAdapter(Context context, List<EDITMENU> quizzes, FragmentEditMenu fragmentEditMenu) {
        this.notifications = quizzes;
        sContext = context;
        f = fragmentEditMenu;
    }

    @Override
    public NotiViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        final View v = layoutInflater.inflate(R.layout.edit_menu_item, viewGroup, false);
        return new NotiViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final NotiViewHolder holder, final int i) {
        Log.d("Size", String.valueOf(notifications.size()));
        holder.itemcode.setText(notifications.get(i).getItemcode());
        holder.itemname.setText(notifications.get(i).getItemtitle());
        holder.itemprice.setText(notifications.get(i).getItemprice());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_dialog = new Dialog(sContext, R.style.ThemeCustomDialog);
                add_dialog.setContentView(R.layout.add_menu_item);
                add_dialog.show();
                ImageButton cross = (ImageButton) add_dialog.findViewById(R.id.crossbtn);
                final EditText itemcode = (EditText) add_dialog.findViewById(R.id.itemcode);
                final EditText itemname = (EditText) add_dialog.findViewById(R.id.itemname);
                final EditText itemprice = (EditText) add_dialog.findViewById(R.id.itemprice);

                itemcode.setText(notifications.get(i).getItemcode());
                itemname.setText(notifications.get(i).getItemtitle());
                itemprice.setText(notifications.get(i).getItemprice());
                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        add_dialog.dismiss();

                    }
                });
                TextView submit = (TextView) add_dialog.findViewById(R.id.btn_submit);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validationChecker(itemcode, itemname, itemprice)) {
                            ADD_MENU(sContext, notifications.get(i).getItem_id(), itemcode.getText().toString(), itemname.getText().toString(), itemprice.getText().toString());

                        }


                    }
                });

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DELETE_MENU(sContext, notifications.get(i).getItem_id());


            }
        });
    }


    @Override
    public int getItemCount() {

        Log.d("Size", String.valueOf(notifications.size()));
        return notifications.size();
    }

    public class NotiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        LinearLayout noti_layout;
        TextView itemcode, itemname, itemprice;
        ImageButton edit, delete;


        NotiViewHolder(View itemView) {
            super(itemView);
            itemcode = (TextView) itemView.findViewById(R.id.itemcode);
            itemname = (TextView) itemView.findViewById(R.id.itemname);
            itemprice = (TextView) itemView.findViewById(R.id.itemprice);

            edit = (ImageButton) itemView.findViewById(R.id.edit);
            delete = (ImageButton) itemView.findViewById(R.id.delete);


        }


        @Override
        public void onClick(View v) {


        }

        @Override
        public boolean onLongClick(View v) {

            return false;
        }


    }

    public static Boolean validationChecker(EditText itemcode, EditText itemname, EditText itemprice) {
        boolean valid = true;
        if (itemcode.getText().toString().isEmpty()) {
            itemcode.setError("enter  Item Code");
            valid = false;
        } else {
            itemcode.setError(null);
        }

        if (itemname.getText().toString().isEmpty()) {
            itemname.setError("enter Item Name");
            valid = false;
        } else {
            itemname.setError(null);
        }

        if (itemprice.getText().toString().isEmpty()) {
            itemprice.setError("enter Item Price");
            valid = false;
        } else {
            itemprice.setError(null);
        }

        return valid;
    }

    void ADD_MENU(final Context c, final String id, final String itemcode, final String itemname, final String itemprice) {
        f.progressBar.setVisibility(View.VISIBLE);


        final StringRequest req = new StringRequest(Request.Method.POST, AppConstants.UPDATE_MENU,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserModel user;
                        try {
                            Log.d("USERID", response.toString());
                            add_dialog.dismiss();


                        } catch (Exception ee) {
                        }
                        f.GET_MENU();


                        //                        getActivity().finish();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 409:
                            utilities.dialog("Already Exist", c);
                            break;
                        case 400:
                            utilities.dialog("Connection Problem", c);
                            break;
                        default:
                            utilities.dialog("Connection Problem", c);
                            break;
                    }
                }
                if (f.progressBar.isShown()) f.progressBar.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //userId=XXX&routeId=XXX&selected=XXX
                Map<String, String> params = new HashMap<String, String>();

                params.put("api_secret", MyApplication.getInstance().getPrefManger().getUserProfile().getApi_secret());
                params.put("item_code", itemcode);
                params.put("item_name", itemname);
                params.put("price", itemprice);
                params.put("menu_id", id);


                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MyApplication.getInstance().addToRequestQueue(req);


    }

    void DELETE_MENU(final Context c, final String id) {

        f.progressBar.setVisibility(View.VISIBLE);

        final StringRequest req = new StringRequest(Request.Method.POST, AppConstants.DELETE_MENU,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserModel user;
                        try {
                            Log.d("USERID", response.toString());
                            add_dialog.dismiss();


                        } catch (Exception ee) {
                        }

                        f.GET_MENU();


                        //                        getActivity().finish();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 409:
                            utilities.dialog("Already Exist", c);
                            break;
                        case 400:
                            utilities.dialog("Connection Problem", c);
                            break;
                        default:
                            utilities.dialog("Connection Problem", c);
                            break;
                    }
                }
                if (f.progressBar.isShown()) f.progressBar.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //userId=XXX&routeId=XXX&selected=XXX
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_secret", MyApplication.getInstance().getPrefManger().getUserProfile().getApi_secret());
                params.put("menu_id", id);


                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MyApplication.getInstance().addToRequestQueue(req);


    }

}
