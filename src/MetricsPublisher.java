import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;

/**
 * Contains operations for publishing metrics.
 */
public class MetricsPublisher {

    private AmazonCloudWatch cloudWatch = AmazonCloudWatchClientBuilder.standard().withRegion(Regions.US_WEST_2).build();


    /**
     * Publishes the given metric to CloudWatch.
     *
     * @param metricName name of metric to publish.
     * @param value      value of metric.
     * @param unit       unit of metric.
     */
    public void addMetric(final String metricName, final double value, final StandardUnit unit) {
        final PutMetricDataRequest request = buildMetricDataRequest(metricName, value, unit);
        cloudWatch.putMetricData(request);
    }

    /**
     * Helper method that builds the PutMetricDataRequest object used to publish to CloudWatch.
     *
     * @param metricName name of metric
     * @param value      value of metric
     * @param unit       unit of metric.
     * @return PutMetricDataRequest
     */
    public PutMetricDataRequest buildMetricDataRequest(final String metricName, final double value,
                                                       final StandardUnit unit) {

        // TODO: implement
        Dimension dimension = new Dimension()
                .withName("ENVIRONMENT")
                .withValue("PRODUCTION");

        MetricDatum datum = new MetricDatum()
                .withMetricName(metricName)
                .withUnit(unit)
                .withValue(value)
                .withDimensions(dimension);

        PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace("EXAMPLE/ORDERS")
                .withMetricData(datum);

        return request;

        //return null;
    }
}
