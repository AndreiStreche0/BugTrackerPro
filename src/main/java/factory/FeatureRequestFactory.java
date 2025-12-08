package factory;

import enums.*;
import tickets.FeatureRequest;
import tickets.Ticket;

import java.util.Map;

public class FeatureRequestFactory implements TicketFactory {

    @Override
    public Ticket createTicket(Map<String, Object> attributes, int id, String timestamp) {
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