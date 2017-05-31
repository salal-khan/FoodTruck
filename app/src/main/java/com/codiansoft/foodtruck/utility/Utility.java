package com.codiansoft.foodtruck.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alirizwan on 08/08/15.
 */
public class Utility {

    private static Pattern pattern;
    private static Matcher matcher;
    //Email Pattern
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Validate Email with regular expression
     *
     * @param email
     * @return true for Valid Email and false for Invalid Email
     */
    public static boolean validate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }

    /**
     * Checks for Null String object
     *
     * @param txt
     * @return true for not null and false for null String object
     */
    public static boolean isNotNull(String txt) {
        return txt != null && txt.trim().length() > 0 ? true : false;
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String getTime(String timeString) throws ParseException {
        if (timeString != null && timeString.length() > 0) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

            SimpleDateFormat timedFormat = new SimpleDateFormat("hh:mm a");
            Date timedDate = timeFormat.parse(timeString);
            return timedFormat.format(timedDate);

        } else {
            return "";
        }
    }

    public static final String getDate(String timeString) throws ParseException {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd/yy");
        SimpleDateFormat timedFormat = new SimpleDateFormat("hh:mm a");
        Date timedDate = timeFormat.parse(timeString);
        return dateFormat.format(timedDate);
    }

    public static String convertCurrency(String priceStr) {
        Double price = Double.parseDouble(priceStr);
        Locale locale = Locale.getDefault();
        //Currency currency = Currency.getInstance(locale);
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) format).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) format).setDecimalFormatSymbols(decimalFormatSymbols);
        String currencyPrice = "";
        try {
            currencyPrice = format.format(price);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return currencyPrice;
    }

    public static String convertCurrencyWithSmbl(String priceStr) {
        Double price = Double.parseDouble(priceStr);
        Locale locale = Locale.getDefault();
        //Currency currency = Currency.getInstance(locale);
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());

        String currencyPrice = "";
        try {
            currencyPrice = format.format(price);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return currencyPrice;
    }

    public static final String getNotificationDate(String timeString) throws ParseException {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-d'T'hh:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("d");
        SimpleDateFormat timedFormat = new SimpleDateFormat("hh:mm a");
        Date timedDate = timeFormat.parse(timeFormat.format(timeFormat.parse(timeString)));
        String[] suffixes =
                //    0     1     2     3     4     5     6     7     8     9
                {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                        //    10    11    12    13    14    15    16    17    18    19
                        "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                        //    20    21    22    23    24    25    26    27    28    29
                        "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                        //    30    31
                        "th", "st"};
        String day = dateFormat.format(timedDate);
        int dayInt = Integer.parseInt(day.replace("0", ""));
        day = day + suffixes[dayInt];

        dateFormat = new SimpleDateFormat("MMMM ");
        String month = dateFormat.format(timedDate);
        return day + " " + month;

    }


}
