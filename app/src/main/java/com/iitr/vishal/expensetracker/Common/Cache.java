package com.iitr.vishal.expensetracker.Common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Divya on 17-03-2018.
 */

public class Cache {
    public static Map<Pair<String, String>,Long> BanksNCards; //BankName, CardNbr, BankId

    static {
        BanksNCards = new HashMap<>();
    }
}
