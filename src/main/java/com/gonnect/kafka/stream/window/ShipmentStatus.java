package com.gonnect.kafka.stream.window;

import java.time.LocalTime;

public class ShipmentStatus {

    private Integer id;
    private long count;
    private LocalTime windowStart;
    private LocalTime windowEnd;

    public ShipmentStatus(Integer id, long count, LocalTime windowStart, LocalTime windowEnd) {
        this.id = id;
        this.count = count;
        this.windowStart = windowStart;
        this.windowEnd = windowEnd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public LocalTime getWindowStart() {
        return windowStart;
    }

    public void setWindowStart(LocalTime windowStart) {
        this.windowStart = windowStart;
    }

    public LocalTime getWindowEnd() {
        return windowEnd;
    }

    public void setWindowEnd(LocalTime windowEnd) {
        this.windowEnd = windowEnd;
    }
}
