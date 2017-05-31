package com.codiansoft.foodtruck.expand;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codiansoft.foodtruck.Models.USERORDERMENUITEMS;
import com.codiansoft.foodtruck.Models.USERSORDERS;
import com.codiansoft.foodtruck.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class GenreAdapter extends ExpandableRecyclerViewAdapter<GenreViewHolder, ArtistViewHolder> {
    Context cc;

    public GenreAdapter(List<? extends ExpandableGroup> groups, Context c) {

        super(groups);
        cc = c;

    }

    @Override
    public GenreViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_genre, parent, false);
        return new GenreViewHolder(view, cc);
    }

    @Override
    public ArtistViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_artist, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(ArtistViewHolder holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {

        final USERORDERMENUITEMS uSERORDERMENUITEMS = ((USERSORDERS) group).getItems().get(childIndex);

        final String uSERORDEDetail = ((USERSORDERS) group).getOrderid();
        Log.d("ss", "onBindChildViewHolder: " + uSERORDEDetail.toString().trim());
        holder.setArtistName(uSERORDERMENUITEMS.getName());
        holder.setArtistQuantity(uSERORDERMENUITEMS.getQuantity());
        holder.setArtistPrice(uSERORDERMENUITEMS.getPrice());


        holder.dirctionlistner(cc, ((USERSORDERS) group).getLatitude().toString().trim(), ((USERSORDERS) group).getLongitude().toString().trim());
        if (childIndex == 0) {
            holder.setOrderId(((USERSORDERS) group).getOrderid().toString().trim());
            holder.setOrderName(((USERSORDERS) group).getName().toString().trim());
            holder.setOrderDatetime(((USERSORDERS) group).getDatetime().toString().trim());
            holder.setOrderDeliveryDateTime(((USERSORDERS) group).getOrderdatetime().toString().trim());
            holder.setOrderTotalAmount(((USERSORDERS) group).getTotalprice().toString().trim());

            holder.getOrderDetails().setVisibility(View.VISIBLE);
        } else {
            holder.getOrderDetails().setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindGroupViewHolder(GenreViewHolder holder, int flatPosition,
                                      ExpandableGroup group) {


        holder.setGenreTitle(group);
    }
}
