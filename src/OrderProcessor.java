import com.amazonaws.services.cloudwatch.model.StandardUnit;

/**
 * Class representing final state of the coding activity.
 */
public class OrderProcessor {

    private CustomerManager customerManager = new CustomerManager();
    private InventoryManager inventoryManager = new InventoryManager();
    private CreditProcessor creditProcessor = new CreditProcessor();
    private MetricsPublisher metricsPublisher;

    /**
     * Constructs a OrderProcessor object.
     *
     * @param metricsPublisher Used to publish metrics to CloudWatch.
     */
    public OrderProcessor(MetricsPublisher metricsPublisher) {
        this.metricsPublisher = metricsPublisher;
    }

    /**
     *  Processes orders by verifying the customer data, checking against the inventory, processing payment,
     *  and then allowing the order to be shipped.
     *
     * @param newOrder The order to be processed
     * @return True if the order was successfully processed, false otherwise.
     */
    public boolean processOrder(Order newOrder) {
        final double startTime = System.currentTimeMillis();
        boolean success = false;

        try {
            customerManager.verifyCustomerInfo(newOrder);
            int pickListNumber = inventoryManager.createPickList(newOrder);
            creditProcessor.processPayment(newOrder);
            inventoryManager.processPickList(pickListNumber);
            success = true;
        } catch (Exception e) {
            System.out.println("Error processing order " + newOrder.getOrderNumber());
        }
        final double endTime = System.currentTimeMillis();
        metricsPublisher.addMetric("ORDER_PROCESSING_TIMES", endTime - startTime, StandardUnit.Milliseconds);
        return success;
    }
}
