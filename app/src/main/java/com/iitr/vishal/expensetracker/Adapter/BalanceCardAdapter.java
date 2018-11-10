package com.iitr.vishal.expensetracker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iitr.vishal.expensetracker.Common.Constants;
import com.iitr.vishal.expensetracker.MainActivity;
import com.iitr.vishal.expensetracker.Model.CardBalanceModel;
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
    Context context;
    SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MMM-yyyy");

    public BalanceCardAdapter(Context context, List<CardBalanceModel> alName) {
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
        viewHolder.balance.setText("Balance ₹ " + Float.toString(cardBalanceEntities.get(i).balance));
        viewHolder.last_updatedDate.setText("Updated on " + simpleDate.format(cardBalanceEntities.get(i).last_transcation_date));

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

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            cardNbr = (TextView) itemView.findViewById(R.id.cardNbr);
            month_spent = (TextView) itemView.findViewById(R.id.month_spent);
            balance = (TextView) itemView.findViewById(R.id.balance);
            last_updatedDate = (TextView) itemView.findViewById(R.id.last_updated);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View view) {
        }

        @Override
        public boolean onLongClick(View view) {
            return true;
        }
    }

}
