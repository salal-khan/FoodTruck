package com.codiansoft.foodtruck.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codiansoft.foodtruck.DrawerActivity;
import com.codiansoft.foodtruck.FragmentTruckMenu;
import com.codiansoft.foodtruck.Models.TRUCKOWNERS;
import com.codiansoft.foodtruck.R;

import java.util.List;


public class OwnersListAdapter extends RecyclerView.Adapter<OwnersListAdapter.NotiViewHolder> {
    List<TRUCKOWNERS> notifications;
    static Boolean checkboxval = false;
    static Context sContext;

    public OwnersListAdapter(Context context, List<TRUCKOWNERS> quizzes) {
        this.notifications = quizzes;
        sContext = context;
    }

    @Override
    public NotiViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        final View v = layoutInflater.inflate(R.layout.owner_item, viewGroup, false);
        return new NotiViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final NotiViewHolder holder, final int i) {
        Log.d("Size", String.valueOf(notifications.size()));
        holder.owner.setText(notifications.get(i).getName());

//        holder.noti_title.setText(notifications.get(i).getNoti_name());
        holder.noti_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTruckMenu f1 = new FragmentTruckMenu();
                Bundle bundle = new Bundle();
                bundle.putString("owner_id", notifications.get(i).getId() + "");
                f1.setArguments(bundle);
                ((DrawerActivity) sContext).getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_placeholder, f1, "FragmentTruckMenu").addToBackStack("FragmentTruckMenu").commit();

            }
        });
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
        TextView owner, price;


        NotiViewHolder(View itemView) {
            super(itemView);
            owner = (TextView) itemView.findViewById(R.id.ownername);
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
