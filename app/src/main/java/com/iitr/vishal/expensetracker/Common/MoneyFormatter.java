package com.iitr.vishal.expensetracker.Common;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by Divya on 17-03-2018.
 */

public class MoneyFormatter implements IValueFormatter
{
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return Formatter.moneyFormatter((long)value);
    }
}
