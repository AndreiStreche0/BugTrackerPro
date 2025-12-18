package factories;

import enums.TicketStatus;
import enums.CustomerDemand;
import enums.BusinessValue;
import enums.BusinessPriority;
import enums.ExpertiseArea;
import tickets.FeatureRequest;
import tickets.Ticket;

import java.util.Map;

/**
 * creates a new feature request ticket
 */
public class FeatureRequestFactory implements TicketFactory {
    /**
     * @param attributes all the ticket attributes given at input
     * @param id ticket id
     * @param timestamp timestamp of the command and ticket creation
     * @return a new Feature Request ticket based on given attributes
     */
    @Override
    public Ticket createTicket(final Map<String, Object> attributes, final int id,
                               final String timestamp) {
        FeatureRequest featureRequest = new FeatureRequest(timestamp);
        featureRequest.setId(id);
        featureRequest.setTitle((String) attributes.get("title"));
        featureRequest.setDescription((String) attributes.get("description"));
        featureRequest.setBusinessPriority(BusinessPriority.valueOf(
                (String) attributes.get("businessPriority")));
        featureRequest.setExpertiseArea(ExpertiseArea.valueOf(
                (String) attributes.get("expertiseArea")));
        featureRequest.setReportedBy((String) attributes.get("reportedBy"));
        featureRequest.setCreatedAt((String) attributes.get("timestamp"));
        featureRequest.setState(TicketStatus.OPEN);

        featureRequest.setBusinessValue(BusinessValue.valueOf(
                (String) attributes.get("businessValue")));
        featureRequest.setCustomerDemand(CustomerDemand.valueOf(
                (String) attributes.get("customerDemand")));
        featureRequest.setCreatedAt(timestamp);

        return featureRequest;
    }
}
