package com.iitr.vishal.expensetracker.Common;

/**
 * Created by Divya on 17-03-2018.
 */

public class Constants {
    public final static String BANKNAMECITI = "Citi";
    public final static String BANKNAMESBI = "Sbi";
    public final static String BANKNAMEINDUS = "Indus";
    public final static String BANKNAMEHSBC = "Hsbc";
    public final static String BANKNAMEICICI = "Icici";
    public final static String BANKNAMESC = "SC";

    public final static String BANKSMSNAMECITI = "-citi";
    public final static String BANKSMSNAMESBI = "-";
    public final static String BANKSMSNAMEINDUS = "-indus";
    public final static String BANKSMSNAMEICICI = "-icici";
    public final static String BANKSMSNAMEHSBC = "-hsbc";
    public final static String BANKSMSNAMESC = "-fromsc";

    public static class RegexConstants {
        public final static String Number = "([,\\d]+[\\.\\d{2}]{0,3})";
        public final static String Money = "([Rr]s\\.?[ ]?" + Number + "|" + Number + "[ ]?INR|INR[ ]?" + Number + ")";
        public final static String Card = "[xX]{2}(\\d{4})";
        public final static String Merchant = "([a-zA-Z0-9 -]*)\\.{0,1}";
        public final static String DateWithName = "([a-zA-Z\\d\\-]{9,11})";
        public final static String DateWithNumber = "([\\d\\/]+)";
    }
}
