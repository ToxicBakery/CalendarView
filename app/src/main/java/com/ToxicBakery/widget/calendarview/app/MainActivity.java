package com.ToxicBakery.widget.calendarview.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.ToxicBakery.widget.calendarview.CalendarScheduleView;
import com.ToxicBakery.widget.calendarview.OnEntryClickListener;
import com.ToxicBakery.widget.calendarview.data.ICalendarEntry;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class MainActivity extends ActionBarActivity implements OnEntryClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Calendar calendar = Calendar.getInstance();
        final CalendarScheduleView calendarScheduleView = (CalendarScheduleView) findViewById(R.id.calendar_view);
        calendarScheduleView.setOnEntryClickListener(this);

        calendar.add(Calendar.WEEK_OF_YEAR, -1);

        for (int i = 1; i <= 20; ++i) {
            List<ICalendarEntry> calendarEntries = new LinkedList<>();
            calendar.add(Calendar.WEEK_OF_YEAR, new Random().nextBoolean() ? 1 : -1);
            for (int j = 0; j < i; j++) {
                final Date start = calendar.getTime();
                calendarEntries.add(new CalendarEntry(start));

                calendar.add(Calendar.HOUR, 6);
            }
            calendarScheduleView.addAllEntries(calendarEntries);
        }

    }

    @Override
    public void OnEntryClick(ICalendarEntry calendarEntry) {
        if (calendarEntry != null) {
            Toast.makeText(this, calendarEntry.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }

    private static final class CalendarEntry implements ICalendarEntry {

        private static final String[] RANDOM_TITLES = {
                "Best Friend"
                , "A Big Day"
                , "Open Data"
                , "Android"
                , "Random Event"
        };

        private static final String[] RANDOM_SUB_TITLES = {
                "Birthday"
                , null
                , "Code for Orlando"
                , null
                , "Reminder"
        };

        private final String title, subTitle;
        private final Date start;

        private CalendarEntry(CalendarEntry calendarEntry) {
            start = calendarEntry.getDateStart();
            title = calendarEntry.getTitle();
            subTitle = calendarEntry.getSubTitle();
        }

        private CalendarEntry(Date start) {
            this.start = start;

            final int idx = new Random().nextInt(RANDOM_TITLES.length);
            title = RANDOM_TITLES[idx];
            subTitle = RANDOM_SUB_TITLES[idx];
        }

        @NonNull
        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public String getSubTitle() {
            return subTitle;
        }

        @NonNull
        @Override
        public Date getDateStart() {
            return start;
        }

        @SuppressWarnings("CloneDoesntCallSuperClone")
        @NonNull
        @Override
        public ICalendarEntry clone() throws CloneNotSupportedException {
            return new CalendarEntry(this);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o instanceof CalendarEntry) {
                CalendarEntry calendarEntry = (CalendarEntry) o;
                return calendarEntry.start.equals(start);
            }

            return false;
        }
    }

}
