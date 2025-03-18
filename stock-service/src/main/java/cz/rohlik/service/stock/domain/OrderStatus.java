package cz.rohlik.service.stock.domain;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {

    PENDING,
    CANCELED,
    EXPIRED,
    PAID;

    public static Set<OrderStatus> getInactiveStatuses() {
        return EnumSet.of(CANCELED, EXPIRED);
    }
}
