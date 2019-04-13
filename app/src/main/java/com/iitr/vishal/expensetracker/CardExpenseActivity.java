package com.iitr.vishal.expensetracker;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iitr.vishal.expensetracker.Adapter.MonthlyCardAdapter;
import com.iitr.vishal.expensetracker.Common.Formatter;
import com.iitr.vishal.expensetracker.Task.CardPieChartTask;
import com.iitr.vishal.expensetracker.Task.MonthlyHorChartTask;
import com.iitr.vishal.expensetracker.db.AppDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CardExpenseActivity extends Activity {

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    public AppDatabase appDatabase;
    private Activity self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_expense);
        self = this;

        Bundle bundleIntent = getIntent().getExtras();
        long bank_id = bundleIntent.getLong("bank_id");
        String bank_name =  bundleIntent.getString("bank_name");

        String monthName = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.recent_expenses);
        Bundle bundle = fragment.getArguments();
        bundle.putString("monthName", monthName);
        bundle.putLong("bank_id", bank_id);

        ((TextView)findViewById(R.id.bankName_cardNbr)).setText(bank_name);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        appDatabase = AppDatabase.getAppDatabase(CardExpenseActivity.this);
        MonthlyCardAdapter viewPagerAdapter = new MonthlyCardAdapter(this, appDatabase, monthName, Long.toString( bank_id));
        viewPager.setAdapter(viewPagerAdapter);
        self.getWindow().setStatusBarColor(getResources().getColor(R.color.horizontalChartBackGround));
        //DisplayChart(monthName, bank_id);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];
        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.non_active_dot);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);
        }

        dots[0].setImageResource(R.drawable.active_dot);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }
            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageResource(R.drawable.non_active_dot);

                }
                dots[position].setImageResource(R.drawable.active_dot);
                LinearLayout cardView = (LinearLayout) viewPager.getParent();
                if(position == 0) {
                    cardView.setBackgroundColor(getResources().getColor(R.color.horizontalChartBackGround));
                    self.getWindow().setStatusBarColor(getResources().getColor(R.color.horizontalChartBackGround));
                }
                else {
                    cardView.setBackgroundColor(getResources().getColor(R.color.cardHorizontalChartBackGround));
                    self.getWindow().setStatusBarColor(getResources().getColor(R.color.cardHorizontalChartBackGround));
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void DisplayChart(String monthName, long bank_id) {

        //new CardPieChartTask(this).execute(monthName, Long.toString(bank_id));
    }
}
