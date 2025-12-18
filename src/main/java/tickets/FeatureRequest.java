package tickets;

import enums.BusinessValue;
import enums.CustomerDemand;
import enums.TicketType;
/**
 * FeatureRequest class that holds all the specific parameters for this ticket type
 */
public final class FeatureRequest extends Ticket {
    private BusinessValue businessValue;
    private CustomerDemand customerDemand;

    public FeatureRequest(final String createdAt) {
        super(createdAt, createdAt);
        this.setType(TicketType.FEATURE_REQUEST);
    }

    public BusinessValue getBusinessValue() {
        return businessValue;
    }

    public void setBusinessValue(final BusinessValue businessValue) {
        this.businessValue = businessValue;
    }

    public CustomerDemand getCustomerDemand() {
        return customerDemand;
    }

    public void setCustomerDemand(final CustomerDemand customerDemand) {
        this.customerDemand = customerDemand;
    }
}
