package com.iitr.vishal.expensetracker;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpinnerFragment extends Fragment {

    PopupWindow popupWindowDogs;

    public SpinnerFragment() {
        super();
        setArguments(new Bundle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        popupWindowDogs = popupWindowDogs();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_spinner, container, false);

        Button btn = rootView.findViewById(R.id.buttonShowDropDown);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindowDogs.showAsDropDown(view, -5, 0);
            }
        });

        return  rootView;
    }

    public PopupWindow popupWindowDogs() {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(getActivity());

        // the drop down list is a list view
        ListView listViewDogs = new ListView(getActivity());

        // set our adapter and pass our pop up window contents
        //listViewDogs.setAdapter(dogsAdapter(getArguments().getStringArray("a")));
        listViewDogs.setAdapter(dogsAdapter(new String[]{"6 Months", "1 Year"}));

        // set the item click listener
        listViewDogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemClick(parent,view,position,id);
            }
        });

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(250);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);

        return popupWindow;
    }

    private ArrayAdapter<String> dogsAdapter(String dogsArray[]) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dogsArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                // setting the ID and text for every items in the list
                String item = getItem(position);
                String[] itemArr = item.split("::");
                String text = itemArr[0];
                String id = itemArr[1];

                // visual settings for the list item
                TextView listItem = new TextView(getContext());

                listItem.setText(text);
                listItem.setTag(id);
                listItem.setTextSize(22);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(Color.WHITE);

                return listItem;
            }
        };

        return adapter;
    }

    public void itemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {

        // add some animation when a list item was clicked
        Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
        fadeInAnimation.setDuration(10);
        v.startAnimation(fadeInAnimation);

        popupWindowDogs().dismiss();

        // get the text and set it as the button text
        String selectedItemText = ((TextView) v).getText().toString();

        Button btn = (Button)(getActivity().findViewById(R.id.buttonShowDropDown));
        btn.setText(selectedItemText);
        // get the id
        //String selectedItemTag = ((TextView) v).getTag().toString();
        //Toast.makeText(mContext, "Dog ID is: " + selectedItemTag, Toast.LENGTH_SHORT).show();

    }
}
