package com.eaglesakura.android.ui.spinner;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Spinner向けのAdapter
 */
public class BasicSpinnerAdapter extends ArrayAdapter<String> {
    boolean mTextRight = false;

    @ColorInt
    int mItemTextColor;

    public BasicSpinnerAdapter(Context context) {
        super(context, android.R.layout.simple_spinner_item);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void setTextRight(boolean textRight) {
        this.mTextRight = textRight;
    }

    /**
     * テキスト色を指定する
     *
     * @param itemTextColor ARGB色
     */
    public void setItemTextColor(@ColorInt int itemTextColor) {
        mItemTextColor = itemTextColor;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View result = super.getDropDownView(position, convertView, parent);
        if (mTextRight) {
            ((TextView) result.findViewById(android.R.id.text1)).setGravity(Gravity.END);
        }
        return result;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result = super.getView(position, convertView, parent);

        TextView view = ((TextView) result.findViewById(android.R.id.text1));
        if (mTextRight) {
            view.setGravity(Gravity.END);
        }
        if (mItemTextColor != 0) {
            view.setTextColor(mItemTextColor);
        }
        return result;
    }
}
