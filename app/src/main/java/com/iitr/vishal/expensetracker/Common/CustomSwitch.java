package com.iitr.vishal.expensetracker.Common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.iitr.vishal.expensetracker.R;

/**
 * https://stackoverflow.com/questions/27641705/oncheckedchanged-called-automatically
 */

public class CustomSwitch extends Switch {

    private CompoundButton.OnCheckedChangeListener listener;

    public CustomSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        this.listener = listener;
        super.setOnCheckedChangeListener(listener);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.setOnCheckedChangeListener(null);
        super.onRestoreInstanceState(state);
        super.setOnCheckedChangeListener(listener);
    }

    public void setCheckedSilent(boolean checked) {
        super.setOnCheckedChangeListener(null);
        super.setChecked(checked);
        super.setOnCheckedChangeListener(listener);
    }
}
