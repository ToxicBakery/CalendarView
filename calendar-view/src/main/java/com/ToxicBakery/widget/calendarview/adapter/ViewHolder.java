package com.ToxicBakery.widget.calendarview.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ToxicBakery.widget.calendarview.R;
import com.ToxicBakery.widget.calendarview.data.ICalendarEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.TimeZone;

class ViewHolder extends RecyclerView.ViewHolder {

    private static final Calendar calendarA = Calendar.getInstance();
    private static final Calendar calendarB = Calendar.getInstance();
    private static final SimpleDateFormat dateFormatWithMonth = new SimpleDateFormat("MMM d", Locale.US);
    private static final SimpleDateFormat dateFormatDayOfMonth = new SimpleDateFormat("d", Locale.US);
    private static final SimpleDateFormat dateFormatDayOfWeek = new SimpleDateFormat("E", Locale.US);

    private final LinkedList<View> recycledDays;
    private final LinkedList<View> recycledEntries;
    private final TextView textViewWeekTitle;
    private final ViewGroup viewGroupDayContainer;
    private final Resources resources;
    private final LayoutInflater inflater;
    private final int[] entryDrawables;
    private final View.OnClickListener onClickListener;

    private ScheduleAdapter.Week week;

    public ViewHolder(View itemView, View.OnClickListener onClickListener) {
        super(itemView);

        this.onClickListener = onClickListener;

        // Convert the xml array to resource ids to drawables
        final TypedArray typedArray = itemView.getResources().obtainTypedArray(R.array.entry_drawables);
        entryDrawables = new int[typedArray.length()];
        for (int i = 0; i < entryDrawables.length; ++i) {
            entryDrawables[i] = typedArray.getResourceId(i, -1);
        }

        recycledDays = new LinkedList<>();
        recycledEntries = new LinkedList<>();
        inflater = LayoutInflater.from(itemView.getContext());
        resources = itemView.getContext().getResources();
        textViewWeekTitle = (TextView) itemView.findViewById(R.id.week_title);
        viewGroupDayContainer = (ViewGroup) itemView.findViewById(R.id.day_container);
    }

    void setWeek(ScheduleAdapter.Week week) {
        this.week = week;

        // Determine the dates of this, last, and next week
        calendarA.setTimeZone(TimeZone.getDefault());
        calendarA.setTimeInMillis(System.currentTimeMillis());
        final Date weekThis = ICalendarEntry.Util.floorDateToStartOfWeek(calendarA.getTime());
        calendarA.setTime(weekThis);
        calendarA.add(Calendar.WEEK_OF_YEAR, -1);
        final Date weekLast = calendarA.getTime();
        calendarA.add(Calendar.WEEK_OF_YEAR, 2);
        final Date weekNext = calendarA.getTime();

        // Determine the appropriate week text
        final Date weekDate = week.getWeekDate();
        final String formatString;
        if (weekDate.equals(weekThis)) {
            formatString = resources.getString(R.string.week_this);
        } else if (weekDate.equals(weekLast)) {
            formatString = resources.getString(R.string.week_last);
        } else if (weekDate.equals(weekNext)) {
            formatString = resources.getString(R.string.week_next);
        } else {
            formatString = resources.getString(R.string.week_of);
        }

        // Get the formatted start and end of the week
        final String formattedDateWeekStart = dateFormatWithMonth.format(weekDate);
        final String formattedDateWeekEnd;
        calendarA.setTime(weekDate);
        calendarB.setTime(ICalendarEntry.Util.ceilDateToEndOfWeek(weekDate));
        if (calendarB.get(Calendar.MONTH) == calendarA.get(Calendar.MONTH)) {
            formattedDateWeekEnd = dateFormatDayOfMonth.format(calendarB.getTime());
        } else {
            formattedDateWeekEnd = dateFormatWithMonth.format(calendarB.getTime());
        }

        // Format the date
        textViewWeekTitle.setText(String.format(formatString, formattedDateWeekStart, formattedDateWeekEnd));

        setDays();
    }

    private void setDays() {

        // Recycle any currently added days
        for (int i = viewGroupDayContainer.getChildCount() - 1; i >= 0; --i) {
            recycleDayView(viewGroupDayContainer.getChildAt(i));
            viewGroupDayContainer.removeViewAt(i);
        }

        // Add each day and its entries
        Date currentDay = null;
        View dayView = null;
        int entryId = 0;
        for (ICalendarEntry entry : week.getEntries()) {
            final Date entryStartDate = entry.getDateStart();

            if (currentDay == null) {
                currentDay = entryStartDate;
            } else {
                // Create a new day if the entry start is not the same day as the previous
                calendarA.setTime(entry.getDateStart());
                calendarB.setTime(currentDay);

                if (calendarA.get(Calendar.DAY_OF_YEAR) != calendarB.get(Calendar.DAY_OF_YEAR)) {
                    currentDay = entryStartDate;
                    dayView = getDayView();
                    entryId = 0;
                } else {
                    ++entryId;
                }
            }

            if (dayView == null) {
                // Create the first day container
                dayView = getDayView();
            }

            // Update the days date
            setDayDisplay(dayView, currentDay);

            // Add entry
            addEntry((ViewGroup) dayView.findViewById(R.id.entries_container), entry, entryId);
        }

    }

    private int getColorForDayLabel(Context context, Date day) {
        calendarA.setTimeInMillis(System.currentTimeMillis());
        calendarB.setTime(day);
        final int today = calendarA.get(Calendar.DAY_OF_YEAR);
        final int incomingDay = calendarB.get(Calendar.DAY_OF_YEAR);
        final int color;
        if (incomingDay > today) {
            color = R.color.day_future;
        } else if (incomingDay == today) {
            color = R.color.day_present;
        } else {
            color = R.color.day_past;
        }

        return context.getResources().getColor(color);
    }

    /**
     * Set the day of week and month and color.
     *
     * @param dayView the day view container
     * @param day     the date for the day
     */
    private void setDayDisplay(View dayView, Date day) {
        final int dayColor = getColorForDayLabel(dayView.getContext(), day);

        final TextView textViewDayOfMonth = (TextView) dayView.findViewById(R.id.day_of_month);
        textViewDayOfMonth.setText(dateFormatDayOfMonth.format(day));
        textViewDayOfMonth.setTextColor(dayColor);

        final TextView textViewDayOfWeek = (TextView) dayView.findViewById(R.id.day_of_week);
        textViewDayOfWeek.setText(dateFormatDayOfWeek.format(day));
        textViewDayOfWeek.setTextColor(dayColor);
    }

    private void addEntry(ViewGroup entriesContainer, ICalendarEntry calendarEntry, int entryId) {
        View view = getEntryView(entriesContainer);
        view.setTag(R.id.entry_value, calendarEntry);
        view.setBackgroundResource(entryDrawables[entryId % entryDrawables.length]);

        TextView title = (TextView) view.findViewById(R.id.entry_title);
        TextView subTitle = (TextView) view.findViewById(R.id.entry_sub_title);

        final String subTitleText = calendarEntry.getSubTitle();

        title.setText(calendarEntry.getTitle());
        if (subTitleText == null) {
            subTitle.setVisibility(View.GONE);
        } else {
            subTitle.setVisibility(View.VISIBLE);
            subTitle.setText(subTitleText);
        }
    }

    /**
     * Get a new, or recycled, day view instance and add it to the day container.
     *
     * @return a day view
     */
    private View getDayView() {
        View view;
        if (recycledDays.size() > 0) {
            view = recycledDays.pop();
        } else {
            view = inflater.inflate(R.layout.day_view, viewGroupDayContainer, false);
        }

        viewGroupDayContainer.addView(view);
        return view;
    }

    /**
     * Recycle a day view instance.
     *
     * @param view the day to recycle
     */
    private void recycleDayView(View view) {
        recycledDays.add(view);

        // Recycle the entries
        ViewGroup entriesContainer = (ViewGroup) view.findViewById(R.id.entries_container);
        for (int i = entriesContainer.getChildCount() - 1; i >= 0; --i) {
            recycleEntryView(entriesContainer.getChildAt(i));
            entriesContainer.removeViewAt(i);
        }
    }

    /**
     * Get a new, or recycled, entry view instance and add it to the entries container.
     *
     * @param viewGroupEntryContainer container to place the entry view
     * @return a entry view
     */
    private View getEntryView(ViewGroup viewGroupEntryContainer) {
        View view;
        if (recycledEntries.size() > 0) {
            view = recycledEntries.pop();
        } else {
            view = inflater.inflate(R.layout.entry_view, viewGroupEntryContainer, false);
            view.setOnClickListener(onClickListener);
        }

        viewGroupEntryContainer.addView(view);
        return view;
    }

    /**
     * Recycle a entry view instance.
     *
     * @param view the entry to recycle
     */
    private void recycleEntryView(View view) {
        recycledEntries.add(view);
    }

}
