package com.ToxicBakery.widget.calendarview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.ToxicBakery.widget.calendarview.adapter.ScheduleAdapter;
import com.ToxicBakery.widget.calendarview.data.ICalendarEntry;

import java.util.Collection;

public class CalendarScheduleView extends RecyclerView {

    private final ScheduleAdapter adapter;

    public CalendarScheduleView(Context context) {
        this(context, null);
    }

    public CalendarScheduleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarScheduleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        adapter = createAdapter();

        setLayoutManager(createLayoutManager(context));
        setAdapter(adapter);
    }

    public void setOnEntryClickListener(@Nullable OnEntryClickListener onEntryClickListener) {
        adapter.setOnEntryClickListener(onEntryClickListener);
    }

    protected ScheduleAdapter createAdapter() {
        return new ScheduleAdapter();
    }

    protected LayoutManager createLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    public void addEntry(ICalendarEntry calendarEntry) {
        adapter.addEntry(calendarEntry);
    }

    public void addAllEntries(Collection<ICalendarEntry> calendarEntries) {
        adapter.addAllEntries(calendarEntries);
    }

    public void addAllEntries(ICalendarEntry[] calendarEntries) {
        adapter.addAllEntries(calendarEntries);
    }

}
