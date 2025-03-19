package cz.rohlik.service.stock.service;

import cz.rohlik.service.stock.domain.Order;
import cz.rohlik.service.stock.dto.CreateOrderRequest;

public interface OrderService {

    /**
     * Creates a new order.
     *
     * @param order the order to create
     * @return the created order
     */
    Order createOrder(CreateOrderRequest order);

    /**
     * Cancels an order.
     *
     * @param orderId the ID of the order to cancel
     * @return the canceled order
     */
    Order cancelOrder(Long orderId);

    /**
     * Pays an order.
     *
     * @param orderId the ID of the order to pay
     * @return the paid order
     */
    Order setOrderPaid(Long orderId);

    /**
     * Expires orders that are not paid in time.
     *
     * @return the number of expired orders
     */
    int expireOrders();

}
