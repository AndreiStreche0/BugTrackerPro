package factory;

import enums.BusinessPriority;
import enums.BusinessValue;
import enums.CustomerDemand;
import enums.ExpertiseArea;
import tickets.FeatureRequest;
import tickets.Ticket;

import java.util.Map;

public class FeatureRequestFactory implements TicketFactory {

    @Override
    public Ticket createTicket(Map<String, Object> attributes, int id) {
        FeatureRequest featureRequest = new FeatureRequest();
        featureRequest.setId(id);
        featureRequest.setTitle((String) attributes.get("title"));
        featureRequest.setDescription((String) attributes.get("description"));
        featureRequest.setBusinessPriority(BusinessPriority.valueOf(
                (String) attributes.get("businessPriority")));
        featureRequest.setExpertiseArea(ExpertiseArea.valueOf(
                (String) attributes.get("expertiseArea")));
        featureRequest.setReportedBy((String) attributes.get("reportedBy"));
        featureRequest.setCreatedAt((String) attributes.get("timestamp"));

        featureRequest.setBusinessValue(BusinessValue.valueOf(
                (String) attributes.get("businessValue")));
        featureRequest.setCustomerDemand(CustomerDemand.valueOf(
                (String) attributes.get("customerDemand")));

        return featureRequest;
    }
}