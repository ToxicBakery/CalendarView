# Calendar Schedule View
Library for display a schedule similar to the Android Calendar Application.

#Getting Started (Gradle / Android Studio)

Add gradle dependency to your application.
```gradle
compile 'com.ToxicBakery.widget.calendarview:calendar-view:1.0.0'
```

## Features
This library was created for a very specific use case and as such is lacking what most would consider core features.

 1. Supports adding static entries which require two dates of start and end, a title, and a sub title or short discription.
 2. Notification that an entry was clicked.

If you are looking for more than that, please implement it and submit a pull request!

## Should I Use This, Matrix
 * Yes
  * My entries span only a short time period
  * My entries are static
 * No
  * I need an actual calendar
  * My entries can be in the hundreds
  * My entries can span multiple years
  * My entries are not static

## Exactly Like Google Calendar?
No. Due to the limited time I have to work on this and the very minor roll it plays in the intended target application, it is not feature rich. To clarify, my use case involves showing about 20-50 entries over the span of approximately 1-2 months. Given the limited data set some features did not make sense to add like forever scrolling view of a the normal calendar, the landscape mode full feature calendar, the day view, etc. The goals for this implementation are simply, light, fast, easy to use, and supports a small number of entries.