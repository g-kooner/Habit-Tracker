package com.example.agilesprinters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This class provides a custom layout for items in the habit list on the home page.
 *
 * @author Hannah Desmarais
 */
public class habitListAdapter extends ArrayAdapter<Habit> {
    private final Context mContext;
    private final ArrayList<Habit> habitArrayList;

    /**
     * This function initializes the array list of habits and the context of the screen.
     *
     * @param context The current screen.
     * @param objects The arraylist of object we are using our custom adapter on.
     */
    public habitListAdapter(Context context, ArrayList<Habit> objects) {
        super(context, 0, objects);
        this.habitArrayList = objects;
        this.mContext = context;
    }

    /**
     * This method converts the view for the list into a custom view.
     *
     * @param position    Position of the habit.
     * @param convertView The converted view.
     * @param parent      The parent of the view we are changing.
     * @return Returns the view after it's been converted and it's values set.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.home_list_content, parent, false);
        }

        Habit habit = habitArrayList.get(position);

        //attach variables to the textViews in the list
        TextView dateText = view.findViewById(R.id.habit_date_textView);
        TextView privacyText = view.findViewById(R.id.privacy_setting_list);
        TextView titleText = view.findViewById(R.id.habit_list_title_textView);
        TextView reasonText = view.findViewById(R.id.habit_list_reason_textView);

        //pass values to variables
        dateText.setText(mContext.getString(R.string.dateStarted) + habit.getDateToStart());
        privacyText.setText(habit.getTitle());
        titleText.setText(habit.getPrivacySetting());
        reasonText.setText(habit.getReason());

        return view;
    }
}
