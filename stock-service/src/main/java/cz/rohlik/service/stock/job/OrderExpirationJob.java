package cz.rohlik.service.stock.job;

import cz.rohlik.service.stock.service.OrderService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderExpirationJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(OrderExpirationJob.class);

    private final OrderService orderService;

    public OrderExpirationJob(final OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("job=OrderExpirationJob status=start");

        int expiredOrdersCount = orderService.expireOrders();

        logger.info("job=OrderExpirationJob status=stop expiredOrders={}", expiredOrdersCount);
    }
}
