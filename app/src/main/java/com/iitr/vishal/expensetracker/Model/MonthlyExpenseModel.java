package com.iitr.vishal.expensetracker.Model;

/**
 * Created by Divya on 17-03-2018.
 */

public class MonthlyExpenseModel {
    private String month_year;
    private int expenditure;

    public String getMonth_year(){return month_year;}
    public void setMonth_year(String month_year){this.month_year=month_year;}

    public int getExpenditure(){return expenditure;}
    public void setExpenditure(int expenditure){this.expenditure=expenditure;}
}
