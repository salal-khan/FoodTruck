package com.codiansoft.foodtruck.Models;


import com.prolificinteractive.materialcalendarview.CalendarDay;

public class Bookings {
    String bookingid;
    CalendarDay bookingdate;
    String customerid;

    public Bookings() {
    }

    public String getBookingid() {
        return bookingid;
    }

    public void setBookingid(String bookingid) {
        this.bookingid = bookingid;
    }

    public CalendarDay getBookingdate() {
        return bookingdate;
    }

    public void setBookingdate(CalendarDay bookingdate) {
        this.bookingdate = bookingdate;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }
}
