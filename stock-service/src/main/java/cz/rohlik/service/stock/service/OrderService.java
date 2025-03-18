package cz.rohlik.service.stock.service;

import cz.rohlik.service.stock.domain.Order;

public interface OrderService {

    /**
     * Creates an order.
     *
     * @param order the order to create
     * @return the created order
     */
    Order createOrder(Order order);

    /**
     * Cancels an order.
     *
     * @param orderId the ID of the order to cancel
     * @return the cancelled order
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
