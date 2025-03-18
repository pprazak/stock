package cz.rohlik.service.stock.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "stock.reservation")
public class StockReservationConfig {

    private int timeLimitInMinutes;

    public int getTimeLimitInMinutes() {
        return timeLimitInMinutes;
    }

    public void setTimeLimitInMinutes(int timeLimitInMinutes) {
        this.timeLimitInMinutes = timeLimitInMinutes;
    }
}
