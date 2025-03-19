package cz.rohlik.service.stock.job;

import cz.rohlik.service.stock.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobExecutionContext;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderExpirationJobTest {

    @Mock
    private OrderService orderService;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @InjectMocks
    private OrderExpirationJob orderExpirationJob;

    @Test
    void execute_expireOrdersSuccessfully() throws Exception {
        when(orderService.expireOrders()).thenReturn(5);

        orderExpirationJob.execute(jobExecutionContext);

        verify(orderService, times(1)).expireOrders();
    }
}
