package com.iitr.vishal.expensetracker.Common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MerchantFormatter {
    public static String getFormattedName(String value)
    {
        value = value.toUpperCase().replace("WALLET","");

        Pattern p = Pattern.compile("WWW\\.(.*?)\\.COM");
        Matcher m = p.matcher(value);
        if(m.find())
            value = m.group(1);

        value = value.replace(" - ","-");

         p = Pattern.compile("[a-zA-Z]{2}-(.*?)-[a-zA-Z]{2}");
         m = p.matcher(value);
        if(m.find())
            value = m.group(1);

        value = value.replace(" COM", "");
        value = value.replace(" LTD", "");
        value = value.replace(" PVT", "");
        value = value.replace(" PTMPAYTM", "");
        value = value.replace(" PRIVATE", "");
        return value.trim();
    }
}
