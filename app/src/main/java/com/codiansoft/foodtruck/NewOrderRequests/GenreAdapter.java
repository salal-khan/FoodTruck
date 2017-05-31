package com.codiansoft.foodtruck.NewOrderRequests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codiansoft.foodtruck.Models.USERORDERMENUITEMS;
import com.codiansoft.foodtruck.Models.USERSORDERS;
import com.codiansoft.foodtruck.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class GenreAdapter extends ExpandableRecyclerViewAdapter<GenreViewHolder, ArtistViewHolder> {
    Context cc;
    FragmentNewOrders f;

    public GenreAdapter(List<? extends ExpandableGroup> groups, Context c, FragmentNewOrders fragmentNewOrders) {

        super(groups);
        cc = c;
        f = fragmentNewOrders;

    }

    @Override
    public GenreViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_new_order, parent, false);
        return new GenreViewHolder(view, cc, f);
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

        holder.setArtistName(uSERORDERMENUITEMS.getName());
        holder.setArtistQuantity(uSERORDERMENUITEMS.getQuantity());
        holder.setArtistPrice(uSERORDERMENUITEMS.getPrice());
    }

    @Override
    public void onBindGroupViewHolder(GenreViewHolder holder, int flatPosition,
                                      ExpandableGroup group) {

        holder.setGenreTitle(group);
    }
}
