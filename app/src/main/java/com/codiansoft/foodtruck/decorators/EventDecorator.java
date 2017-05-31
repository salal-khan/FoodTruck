package com.codiansoft.foodtruck.decorators;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.style.StyleSpan;

import com.codiansoft.foodtruck.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {


    private HashSet<CalendarDay> dates;
    Context cc;

    public EventDecorator(Collection<CalendarDay> dates, Context c) {

        cc = c;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(10));
        view.addSpan(new StyleSpan(Color.WHITE));
        view.setBackgroundDrawable(ContextCompat.getDrawable(cc, R.color.screen_color));

    }
}
