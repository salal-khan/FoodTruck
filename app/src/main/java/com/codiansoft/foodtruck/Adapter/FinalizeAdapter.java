package com.codiansoft.foodtruck.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codiansoft.foodtruck.FragmentOrderFinalize;
import com.codiansoft.foodtruck.Models.MENUFINAL;
import com.codiansoft.foodtruck.R;

import java.util.List;


public class FinalizeAdapter extends RecyclerView.Adapter<FinalizeAdapter.NotiViewHolder> {
    List<MENUFINAL> notifications;
    static Boolean checkboxval = false;
    static Context sContext;
    TextView total_field;
    FragmentOrderFinalize f;

    public FinalizeAdapter(Context context, List<MENUFINAL> quizzes, TextView ee, FragmentOrderFinalize fragmentOrderFinalize) {
        this.notifications = quizzes;
        sContext = context;
        total_field = ee;
        f = fragmentOrderFinalize;
    }

    @Override
    public NotiViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        final View v = layoutInflater.inflate(R.layout.selectd_item, viewGroup, false);
        return new NotiViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final NotiViewHolder holder, final int i) {
        Log.d("Size", String.valueOf(notifications.size()));
        holder.itemname.setText(notifications.get(i).getItemtitle());
        holder.price.setText(notifications.get(i).getItemprice());
        holder.qty.setText(notifications.get(i).getQty());
        holder.plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int count = Integer.parseInt(notifications.get(i).getQty()) + 1;

                holder.qty.setText(String.valueOf(count));
            }
        });


        holder.minusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(notifications.get(i).getQty()) - 1;
                if (count > 0) {
                    holder.qty.setText(String.valueOf(count));
                }
            }
        });


        holder.qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!holder.qty.getText().toString().equals("")) {
                    holder.price.setText("" + (Integer.parseInt(holder.qty.getText().toString()) * Double.parseDouble(notifications.get(i).getItemprice())));
                    notifications.get(i).setQty(holder.qty.getText().toString());
                    f.FinalOrderList(notifications);
                    setToatl(total_field, notifications);
                } else {
                    holder.qty.setText("1");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        if (i == notifications.size() - 1)
            setToatl(total_field, notifications);

    }


    @Override
    public int getItemCount() {

        Log.d("Size", String.valueOf(notifications.size()));
        return notifications.size();
    }

    public class NotiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        LinearLayout main_layout;
        TextView itemname, price;
        TextView qty;
        ImageView plusbtn;
        ImageView minusbtn;


        NotiViewHolder(View itemView) {
            super(itemView);
            itemname = (TextView) itemView.findViewById(R.id.menuitem);
            price = (TextView) itemView.findViewById(R.id.price);
            main_layout = (LinearLayout) itemView.findViewById(R.id.mainlayout);
            qty = (TextView) itemView.findViewById(R.id.qty);
            plusbtn = (ImageView) itemView.findViewById(R.id.plusbtn);
            minusbtn = (ImageView) itemView.findViewById(R.id.minusbtn);

        }


        @Override
        public void onClick(View v) {


        }

        @Override
        public boolean onLongClick(View v) {

            return false;
        }


    }

    void setToatl(TextView e, List<MENUFINAL> m) {

        double total = 0;
        for (MENUFINAL mm : m) {
            total = total + (Double.parseDouble(mm.getItemprice()) * Integer.parseInt(mm.getQty()));

        }
        e.setText("Total Bil: " + total);

    }
}
