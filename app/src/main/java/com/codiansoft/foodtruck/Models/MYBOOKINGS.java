package com.codiansoft.foodtruck.Models;


public class MYBOOKINGS {


    String ownername;
    String bookingdate;
    String bookingstatus;


    public MYBOOKINGS() {
    }

    public MYBOOKINGS(String ownername, String bookingdate, String bookingstatus) {
        this.ownername = ownername;
        this.bookingdate = bookingdate;
        this.bookingstatus = bookingstatus;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getBookingdate() {
        return bookingdate;
    }

    public void setBookingdate(String bookingdate) {
        this.bookingdate = bookingdate;
    }

    public String getBookingstatus() {
        return bookingstatus;
    }

    public void setBookingstatus(String bookingstatus) {
        this.bookingstatus = bookingstatus;
    }
}
