package com.ToxicBakery.widget.calendarview.data;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

public interface ICalendarEntry extends Cloneable {

    /**
     * The title of the event.
     *
     * @return title
     */
    public
    @NonNull
    String getTitle();

    /**
     * The subtitle or description of the event. (Optional)
     *
     * @return sub title or description
     */
    public String getSubTitle();

    /**
     * The date that the event occurs.
     *
     * @return date the event starts
     */
    public
    @NonNull
    Date getDateStart();

    /**
     * An immutable copy of the event.
     *
     * @return immutable copy of the event.
     */
    public
    @NonNull
    ICalendarEntry clone() throws CloneNotSupportedException;

    public static final class Util {

        private static final Calendar calendarWeek = Calendar.getInstance();

        public static synchronized Date floorDateToStartOfWeek(Date date) {
            calendarWeek.setTime(date);
            calendarWeek.set(Calendar.DAY_OF_WEEK, calendarWeek.getFirstDayOfWeek());
            calendarWeek.set(Calendar.HOUR_OF_DAY, 0);
            calendarWeek.set(Calendar.MINUTE, 0);
            calendarWeek.set(Calendar.MILLISECOND, 0);
            return calendarWeek.getTime();
        }

        public static synchronized Date ceilDateToEndOfWeek(Date date) {
            calendarWeek.setTime(date);
            calendarWeek.add(Calendar.DAY_OF_YEAR, 6);
            return calendarWeek.getTime();
        }

    }

}
