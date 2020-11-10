package com.iitr.vishal.expensetracker.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.iitr.vishal.expensetracker.ExpenseFragment;
import com.iitr.vishal.expensetracker.MainActivity;
import com.iitr.vishal.expensetracker.R;
import com.iitr.vishal.expensetracker.Task.ExpenseChartTask;
import com.iitr.vishal.expensetracker.Task.RecentExpenseTask;
import com.iitr.vishal.expensetracker.db.entity.TransactionEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Divya on 18-03-2018.
 */

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    //String[] Colors = {"#64dd17", "#00b8d4", "#304ffe", "#fea904", "#01c653", "#d30100"};
    String[] Colors = {"#e71473", "#ffdb0f", "#b415ff", "#00e863", "#00b8d4", "#304ffe", "#01c653"};
    int[] ShoppingIcons = {R.drawable.ic_groceries_white,R.drawable.ic_shopping_white};
    public ArrayList<TransactionEntity> listData;
    private LayoutInflater layoutInflater;
    SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MMM-yyyy");
    Random rand = new Random();
    private ItemDeleteEvent mItemDeleteEvent;
    //Swipe control
    private final ViewBinderHelper swipeControl = new ViewBinderHelper();

    public ExpenseAdapter(Context aContext, ArrayList<TransactionEntity> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
        swipeControl.setOpenOnlyOne(true);
    }
    public interface ItemDeleteEvent {
        public void onItemDelete(long id);
    }
    public void setOnStopTrackEventListener(ItemDeleteEvent eventListener)
    {
        mItemDeleteEvent = eventListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder;
        // if (convertView == null) {
        View convertView = layoutInflater.inflate(R.layout.layout_expense_row, null);
        holder = new ViewHolder(convertView);
        holder.amountView = (TextView) convertView.findViewById(R.id.amount);
        holder.spentAtView = (TextView) convertView.findViewById(R.id.spentAt);
        holder.dateView = (TextView) convertView.findViewById(R.id.date);
        holder.shoppingView = (ImageView) convertView.findViewById(R.id.shoppingIcon);
        holder.swipeLayout = (SwipeRevealLayout) convertView.findViewById(R.id.swipe_layout);
        holder.deleteLayout = convertView.findViewById(R.id.delete_layout);
        //  convertView.setTag(holder);
        //} else {
        //  holder = (ViewHolder) convertView.getTag();
        //}


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int p = position;
        holder.deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long smsId = listData.get(p).getId();
                    String layouTHide = Integer.toString(v.getId());
                    listData.remove(p);
                    if (mItemDeleteEvent != null) {
                        mItemDeleteEvent.onItemDelete(smsId);
                    }
                    notifyItemRemoved(p);
                    notifyDataSetChanged();
                   //((SwipeRevealLayout) v.getParent()).close(true);
                }
            });
        swipeControl.bind(holder.swipeLayout, Long.toString(listData.get(position).getId()));
        holder.amountView.setText("₹ " + Float.toString(listData.get(position).getAmount()));
        holder.spentAtView.setText(listData.get(position).getSpentAt());
        holder.dateView.setText(simpleDate.format(listData.get(position).getSpentDate()));
        holder.shoppingView.setImageResource(ShoppingIcons[rand.nextInt(2)]);
        holder.shoppingView.setBackgroundColor(Color.parseColor(Colors[rand.nextInt(7)]));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

/*    public void deleteItem(int position) {
        TransactionEntity mRecentlyDeletedItem = listData.get(position);
        int mRecentlyDeletedItemPosition = position;
        listData.remove(position);
        notifyItemRemoved(position);

        notifyDataSetChanged();
        //showUndoSnackbar();
    }*/



    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView amountView;
        TextView spentAtView;
        TextView dateView;
        ImageView shoppingView;
        SwipeRevealLayout swipeLayout;
        private View deleteLayout;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}