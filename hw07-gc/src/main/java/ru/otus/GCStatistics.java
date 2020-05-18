package ru.otus;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.util.List;

public class GCStatistics {
    private String gcName;
    private long gcCount;
    private long durationMax;
    private long lastStartTime;
    private long durationTotal;

    GCStatistics(String gcName) {
        this.gcName = gcName;
    }

    public void addMeasure(long startTime, long duration) {
        gcCount += 1;
        lastStartTime = startTime;
        if (duration > durationMax) {
            durationMax = duration;
        }
        durationTotal += duration;
    }

    public String getStatus() {
        long durationAvg = durationTotal / gcCount;
        return String.format(
            "[%s] %s: max %d, avg %d, total %d, count %d",
            lastStartTime,
            gcName,
            durationMax,
            durationAvg,
            durationTotal,
            gcCount
        );
    }

    public static void startGCLogging() {
        List<GarbageCollectorMXBean> gcBeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            String gcBeanName = gcBean.getName();
            GCStatistics gcStatistics = new GCStatistics(gcBeanName);
            NotificationEmitter emitter = (NotificationEmitter) gcBean;
            NotificationListener listener = (notification, handback) -> {
                if (!notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    return;
                }
                CompositeData userData = (CompositeData) notification.getUserData();
                GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from(userData);
                long startTime = info.getGcInfo().getStartTime();
                long duration = info.getGcInfo().getDuration();
                gcStatistics.addMeasure(startTime, duration);
                System.out.println(gcStatistics.getStatus());
            };
            emitter.addNotificationListener(listener, null, null);
            System.out.printf("New listener for %s%n", gcBeanName);
        }
    }
}
