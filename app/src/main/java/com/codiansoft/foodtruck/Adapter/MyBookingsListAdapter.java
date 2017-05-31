package com.codiansoft.foodtruck.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codiansoft.foodtruck.Models.MYBOOKINGS;
import com.codiansoft.foodtruck.R;

import java.util.List;


public class MyBookingsListAdapter extends RecyclerView.Adapter<MyBookingsListAdapter.NotiViewHolder> {
    List<MYBOOKINGS> notifications;
    static Boolean checkboxval = false;
    static Context sContext;

    public MyBookingsListAdapter(Context context, List<MYBOOKINGS> quizzes) {
        this.notifications = quizzes;
        sContext = context;
    }

    @Override
    public NotiViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        final View v = layoutInflater.inflate(R.layout.mybooking_item, viewGroup, false);
        return new NotiViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final NotiViewHolder holder, final int i) {
        Log.d("Size", String.valueOf(notifications.size()));
        holder.owner.setText(notifications.get(i).getOwnername());
        holder.booking_date.setText(notifications.get(i).getBookingdate());
        holder.booking_status.setText(notifications.get(i).getBookingstatus());

//
//        if (!notifications.get(i).getNoti_pic().equals("")) {
//          byte[] decodedString = Base64.decode(notifications.get(i).getNoti_pic(), Base64.DEFAULT);
//           holder.noti_layout.setBackground(new BitmapDrawable(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length)));
//        }


    }


    @Override
    public int getItemCount() {

        Log.d("Size", String.valueOf(notifications.size()));
        return notifications.size();
    }

    public class NotiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        LinearLayout noti_layout;
        TextView owner, booking_date, booking_status;


        NotiViewHolder(View itemView) {
            super(itemView);
            owner = (TextView) itemView.findViewById(R.id.ownername);
            booking_date = (TextView) itemView.findViewById(R.id.bookingdate);
            booking_status = (TextView) itemView.findViewById(R.id.bookingstatus);
            noti_layout = (LinearLayout) itemView.findViewById(R.id.mainlayout);


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
