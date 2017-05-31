package com.codiansoft.foodtruck.Accepted;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codiansoft.foodtruck.Models.USERORDERMENUITEMS;
import com.codiansoft.foodtruck.Models.USERSORDERS;
import com.codiansoft.foodtruck.R;
import com.codiansoft.foodtruck.expand.ArtistViewHolder;
import com.codiansoft.foodtruck.expand.GenreViewHolder;
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
