package com.iitr.vishal.expensetracker.Model;

import java.util.Date;

/**
 * Created by Divya on 10-11-2018.
 */

public class CardBalanceModel {
    public long bank_id;
    public String bank_name;
    public String card_nbr;
    public Date last_transcation_date;
    public float balance;
    public float monthlySpent;
}
