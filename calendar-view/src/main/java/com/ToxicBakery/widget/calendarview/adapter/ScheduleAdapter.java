package com.ToxicBakery.widget.calendarview.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.widget.calendarview.OnEntryClickListener;
import com.ToxicBakery.widget.calendarview.R;
import com.ToxicBakery.widget.calendarview.data.ICalendarEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class ScheduleAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private final TreeMap<Date, Week> calendarWeekMap;
    private final ArrayList<Date> weekKeyList;

    private OnEntryClickListener onEntryClickListener;

    public ScheduleAdapter() {
        calendarWeekMap = new TreeMap<>();
        weekKeyList = new ArrayList<>();
    }

    public void setOnEntryClickListener(@Nullable OnEntryClickListener onEntryClickListener) {
        this.onEntryClickListener = onEntryClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onEntryClickListener != null) {
            onEntryClickListener.OnEntryClick((ICalendarEntry) v.getTag(R.id.entry_value));
        }
    }

    public static final class Week {

        private static final Comparator<ICalendarEntry> CALENDAR_ENTRY_COMPARATOR = new Comparator<ICalendarEntry>() {
            @Override
            public int compare(ICalendarEntry lhs, ICalendarEntry rhs) {
                return lhs.getDateStart().compareTo(rhs.getDateStart());
            }
        };

        private final List<ICalendarEntry> entries;
        private final Date weekDate;

        private Week(Date weekDate) {
            entries = new LinkedList<>();
            this.weekDate = weekDate;
        }

        void addEntry(ICalendarEntry calendarEntry) {
            entries.add(calendarEntry);
            Collections.sort(entries, CALENDAR_ENTRY_COMPARATOR);
        }

        public Date getWeekDate() {
            return weekDate;
        }

        public List<ICalendarEntry> getEntries() {
            return entries;
        }

    }

    protected final Week getWeekForEntry(ICalendarEntry calendarEntry) {
        // Find or create the week for the given entry
        final Date weekDate = ICalendarEntry.Util.floorDateToStartOfWeek(calendarEntry.getDateStart());
        Week week = calendarWeekMap.get(weekDate);
        if (week == null) {
            week = new Week(weekDate);
            calendarWeekMap.put(weekDate, week);
        }

        // Determine if the week is already present in the week list, if not present, add it.
        final int weekDatePosition = Collections.binarySearch(weekKeyList, weekDate);
        if (weekDatePosition < 0) {
            weekKeyList.add(-weekDatePosition - 1, weekDate);
        }

        return week;
    }

    /**
     * Add an entry to the calendar.
     *
     * @param calendarEntry entry to be added.
     */
    public void addEntry(ICalendarEntry calendarEntry) {
        addAllEntries(new ICalendarEntry[]{calendarEntry});
    }

    /**
     * Add an array of entries to the calendar.
     *
     * @param calendarEntry entries to be added
     */
    public void addAllEntries(ICalendarEntry[] calendarEntry) {
        addAllEntries(Arrays.asList(calendarEntry));
    }

    /**
     * Add a collection of entries to the calendar.
     *
     * @param calendarEntries entries to be added
     */
    public void addAllEntries(Collection<ICalendarEntry> calendarEntries) {
        for (ICalendarEntry calendarEntry : calendarEntries) {
            // Copy the entry
            try {
                calendarEntry = calendarEntry.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException(e);
            }

            // Add the event to the week container
            final Week week = getWeekForEntry(calendarEntry);
            week.addEntry(calendarEntry);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        final View view = layoutInflater.inflate(R.layout.week_view, viewGroup, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolder) viewHolder).setWeek(getWeekAt(i));
    }

    public Week getWeekAt(int position) {
        return calendarWeekMap.get(getDateAt(position));
    }

    public Date getDateAt(int position) {
        return weekKeyList.get(position);
    }

    @Override
    public int getItemCount() {
        return calendarWeekMap.size();
    }

}
