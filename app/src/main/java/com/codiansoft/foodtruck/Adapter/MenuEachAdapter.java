package com.codiansoft.foodtruck.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codiansoft.foodtruck.Models.MENU;
import com.codiansoft.foodtruck.R;

import java.util.List;


public class MenuEachAdapter extends RecyclerView.Adapter<MenuEachAdapter.NotiViewHolder> {
    List<MENU> notifications;
    static Boolean checkboxval = false;
    static Context sContext;

    public MenuEachAdapter(Context context, List<MENU> quizzes) {
        this.notifications = quizzes;
        sContext = context;
    }

    @Override
    public NotiViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        final View v = layoutInflater.inflate(R.layout.menu_item, viewGroup, false);
        return new NotiViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final NotiViewHolder holder, final int i) {
        Log.d("Size", String.valueOf(notifications.size()));
        holder.itemname.setText(notifications.get(i).getItemtitle());
        holder.price.setText(notifications.get(i).getItemprice());
//        holder.noti_title.setText(notifications.get(i).getNoti_name());
        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notifications.get(i).getSeleccted()) {
                    notifications.get(i).setSeleccted(false);
                    notifications.get(i).save();
                    holder.main_layout.setBackgroundColor(Color.parseColor("#e7fae7"));
                } else {
                    notifications.get(i).setSeleccted(true);
                    notifications.get(i).save();
                    holder.main_layout.setBackgroundColor(Color.parseColor("#52d64f"));
                }
            }
        });


    }


    @Override
    public int getItemCount() {

        Log.d("Size", String.valueOf(notifications.size()));
        return notifications.size();
    }

    public class NotiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        LinearLayout main_layout;
        TextView itemname, price;


        NotiViewHolder(View itemView) {
            super(itemView);
            itemname = (TextView) itemView.findViewById(R.id.menuitem);
            price = (TextView) itemView.findViewById(R.id.price);
            main_layout = (LinearLayout) itemView.findViewById(R.id.mainlayout);


        }


        @Override
        public void onClick(View v) {


        }

        @Override
        public boolean onLongClick(View v) {

            return false;
        }


    }
}
