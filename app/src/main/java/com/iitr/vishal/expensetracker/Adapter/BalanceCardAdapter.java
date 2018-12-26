package com.iitr.vishal.expensetracker.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iitr.vishal.expensetracker.CardExpenseActivity;
import com.iitr.vishal.expensetracker.Common.Constants;
import com.iitr.vishal.expensetracker.MainActivity;
import com.iitr.vishal.expensetracker.Model.CardBalanceModel;
import com.iitr.vishal.expensetracker.MonthlyexpenseActivity;
import com.iitr.vishal.expensetracker.R;
import com.iitr.vishal.expensetracker.db.entity.CardBalanceEntity;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Divya on 28-10-2018.
 */

public class BalanceCardAdapter extends RecyclerView.Adapter<BalanceCardAdapter.ViewHolder> {

    List<CardBalanceModel> cardBalanceEntities;
    static Activity context;
    SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MMM-yyyy");

    public BalanceCardAdapter(Activity  context, List<CardBalanceModel> alName) {
        super();
        this.context = context;
        this.cardBalanceEntities = alName;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_balance_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.cardNbr.setText(cardBalanceEntities.get(i).card_nbr);
        viewHolder.month_spent.setText("₹ " + Float.toString(cardBalanceEntities.get(i).monthlySpent));
        viewHolder.cardNbr.setText(cardBalanceEntities.get(i).card_nbr);
        viewHolder.bank_id = cardBalanceEntities.get(i).bank_id;
        viewHolder.bank_name = cardBalanceEntities.get(i).bank_name + " - xxxx" + cardBalanceEntities.get(i).card_nbr;
        if(cardBalanceEntities.get(i).balance==0)
        {
            viewHolder.balance.setVisibility(View.GONE);
            viewHolder.last_updatedDate.setVisibility(View.GONE);
            viewHolder.horizontal_line.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);

            params.addRule(RelativeLayout.CENTER_VERTICAL);

            viewHolder.month_spent.setLayoutParams(params);
            viewHolder.month_spent.setTextSize(18);
        }
        else
        {
            viewHolder.balance.setVisibility(View.VISIBLE);
            viewHolder.last_updatedDate.setVisibility(View.VISIBLE);
            viewHolder.horizontal_line.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);

            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.setMargins(0,15,0,9);
            viewHolder.month_spent.setLayoutParams(params);
            viewHolder.month_spent.setTextSize(16);

            viewHolder.balance.setText("Balance ₹ " + Float.toString(cardBalanceEntities.get(i).balance));
            viewHolder.last_updatedDate.setText("Updated on " + simpleDate.format(cardBalanceEntities.get(i).last_transcation_date));
        }

        viewHolder.imgThumbnail.setImageResource(getBankImageId(cardBalanceEntities.get(i).bank_name));


    }

    @Override
    public int getItemCount() {
        return cardBalanceEntities.size();
    }

    private int getBankImageId(String bankName) {
        switch (bankName) {
            case Constants.BANKNAMECITI:
                return R.drawable.bank_logo_citi;
            case Constants.BANKNAMEHSBC:
                return R.drawable.bank_logo_hsbc;
            case Constants.BANKNAMEICICI:
                return R.drawable.bank_logo_icici;
            case Constants.BANKNAMEINDUS:
                return R.drawable.bank_logo_indusind;
            case Constants.BANKNAMESBI:
                return R.drawable.bank_logo_sbi;
            case Constants.BANKNAMESC:
                return R.drawable.bank_logo_sc;
            default:
                return R.drawable.bank_logo_sc;

        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView imgThumbnail;
        public TextView cardNbr;
        public TextView month_spent;
        public TextView balance;
        public TextView last_updatedDate;
        public View horizontal_line;
        public long bank_id;
        public String bank_name;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            cardNbr = (TextView) itemView.findViewById(R.id.cardNbr);
            month_spent = (TextView) itemView.findViewById(R.id.month_spent);
            balance = (TextView) itemView.findViewById(R.id.balance);
            last_updatedDate = (TextView) itemView.findViewById(R.id.last_updated);
            horizontal_line = (View)itemView.findViewById(R.id.horizontal_line);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View view) {
            //Toast.makeText(context,"Hello Javatpoint",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, CardExpenseActivity.class);
            Bundle bundle = new Bundle();

            //Add your data to bundle
            bundle.putLong("bank_id", bank_id);
            bundle.putString("bank_name",bank_name);

            //Add the bundle to the intent
            intent.putExtras(bundle);
            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            return true;
        }
    }

}
