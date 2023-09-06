package com.example.trainticket.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRoute {
    BaseRoute firstRoute;
    BaseRoute secondRoute;
    Long calcTimeWait(Time time1, Time time2) {
        long timeInMillis1 = time1.getTime();
        long timeInMillis2 = time2.getTime();
        long timeDifferenceInMillis = timeInMillis2 - timeInMillis1;
        return timeDifferenceInMillis / (1000 * 60);
    }
    public Long getTransTime() {
        return calcTimeWait(firstRoute.getArriveTime(), secondRoute.getStartTime());
    }

}
