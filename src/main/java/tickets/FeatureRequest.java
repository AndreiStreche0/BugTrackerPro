package tickets;

import enums.BusinessValue;
import enums.CustomerDemand;
import enums.TicketType;

public class FeatureRequest extends Ticket {
    private BusinessValue businessValue;
    private CustomerDemand customerDemand;

    public FeatureRequest() {
        super();
        this.setType(TicketType.FEATURE_REQUEST);
    }

    public BusinessValue getBusinessValue() {
        return businessValue;
    }

    public void setBusinessValue(BusinessValue businessValue) {
        this.businessValue = businessValue;
    }

    public CustomerDemand getCustomerDemand() {
        return customerDemand;
    }

    public void setCustomerDemand(CustomerDemand customerDemand) {
        this.customerDemand = customerDemand;
    }
}