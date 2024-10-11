package seng202.team3.models;

import java.sql.Date;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

public enum TimePeriod {

    DAYS,
    WEEKS,
    MONTHS,
    YEARS;

    public final static long dayMillis = 24 * 60 * 60 * 1000;

    public final static long weekMillis = 7 * dayMillis;

    /**
     * @param toSplit
     * @return
     * @param <T>
     */
    public <T extends Timed> HashMap<Integer, List<T>> splitIntoPeriods(List<T> toSplit) { // fuck this
        HashMap<Integer, List<T>> hashMap = new HashMap<>();
        toSplit.sort(Comparator.comparing(T::getDate));
        switch (this) {
            case DAYS:
                splitIntoDays(hashMap, toSplit);
                break;
            case WEEKS:
                splitIntoWeeks(hashMap, toSplit);
                break;
            case MONTHS:
                splitIntoMonths(hashMap, toSplit);
                break;
            case YEARS:
                splitIntoYears(hashMap, toSplit);
                break;
        }
        return hashMap;
    }

    private <T extends Timed> void splitIntoYears(HashMap<Integer, List<T>> hashMap, List<T> toSplit) {
        for (T entry : toSplit) {
            hashMap.merge(
                    getYearKey(entry.getDate()),
                    new ArrayList<T>(List.of(entry)),
                    getMergeFunc()
            );
        }
    }

    private <T extends Timed> void splitIntoMonths(HashMap<Integer, List<T>> hashMap, List<T> toSplit) {
        for (T entry : toSplit) {
            hashMap.merge(
                    getMonthKey(entry.getDate()),
                    new ArrayList<T>(List.of(entry)),
                    getMergeFunc()
            );
        }
    }

    private <T extends Timed> void splitIntoWeeks(HashMap<Integer, List<T>> hashMap, List<T> toSplit) {
        int weekNum = toSplit.getFirst().getDate().toLocalDate().get(WeekFields.ISO.weekOfWeekBasedYear());
        for (T entry : toSplit) {
            hashMap.merge(
                    getWeekKey(entry.getDate(), weekNum),
                    new ArrayList<T>(List.of(entry)),
                    getMergeFunc()
            );
        }
    }

    private <T extends Timed> BiFunction<List<T>, List<T>, List<T>> getMergeFunc() {
        return (existingList, newList) -> {
            existingList.addAll(newList);
            return existingList;
        };
    }

    private <T extends Timed> void splitIntoDays(HashMap<Integer, List<T>> hashMap, List<T> toSplit) {
        Date SOWeek = getSOWeek(toSplit.getFirst().getDate());
        for (T entry : toSplit) {
            hashMap.merge(
                    getDayKey(entry.getDate(), SOWeek),
                    new ArrayList<>(List.of(entry)),
                    getMergeFunc()
            );
        }
    }

    private int getWeekKey(Date date, int startWeek) {
        return date.toLocalDate().get(WeekFields.ISO.weekOfWeekBasedYear()) - startWeek;
    }

    private int getDayKey(Date date, Date SOWeek) {
        return date.toLocalDate().getDayOfWeek().getValue();
    }

    private Date getSOWeek(Date inWeek) {
        return new Date(Date.valueOf(inWeek.toLocalDate().atStartOfDay().toLocalDate()).getTime() -
                (dayMillis * (inWeek.toLocalDate().atStartOfDay().getDayOfWeek().getValue()-1)));
    }

    private int getMonthKey(Date date) {
        return date.toLocalDate().getMonthValue();
    }

    private int getYearKey(Date date) {
        return date.toLocalDate().getYear();
    }
}
