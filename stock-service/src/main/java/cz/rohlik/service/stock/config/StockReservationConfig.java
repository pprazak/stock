package cz.rohlik.service.stock.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "stock.reservation")
public class StockReservationConfig {

    private int orderExpirationInMinutes;

    public int getOrderExpirationInMinutes() {
        return orderExpirationInMinutes;
    }

    public void setOrderExpirationInMinutes(int orderExpirationInMinutes) {
        this.orderExpirationInMinutes = orderExpirationInMinutes;
    }
}
