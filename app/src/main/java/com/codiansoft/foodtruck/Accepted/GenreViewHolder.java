package com.codiansoft.foodtruck.Accepted;

import android.content.Context;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.codiansoft.foodtruck.Models.USERSORDERS;
import com.codiansoft.foodtruck.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class GenreViewHolder extends GroupViewHolder {

    private TextView status, totalcost, datetime;
    private ImageView arrow;
    Context c;

    public GenreViewHolder(View itemView, Context c) {

        super(itemView);
        this.c = c;
        status = (TextView) itemView.findViewById(R.id.status);
        totalcost = (TextView) itemView.findViewById(R.id.totalcost);
        datetime = (TextView) itemView.findViewById(R.id.datetime);
        arrow = (ImageView) itemView.findViewById(R.id.list_item_genre_arrow);

    }

    public void setGenreTitle(ExpandableGroup genre) {
        if (genre instanceof USERSORDERS) {
            status.setText(genre.getTitle());
            totalcost.setText(((USERSORDERS) genre).getTotalprice());
            datetime.setText(((USERSORDERS) genre).getDatetime());
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
}
