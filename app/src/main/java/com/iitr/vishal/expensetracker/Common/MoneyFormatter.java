package com.iitr.vishal.expensetracker.Common;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by Divya on 17-03-2018.
 */

public class MoneyFormatter extends LargeValueFormatter
{
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return Formatter.moneyFormatter((long)value);
        //return super.getFormattedValue(value, entry, dataSetIndex, viewPortHandler);
    }

}
