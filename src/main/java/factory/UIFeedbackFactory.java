package factory;

import enums.*;
import tickets.FeatureRequest;
import tickets.Ticket;
import tickets.UIFeedback;

import java.util.Map;

public class UIFeedbackFactory implements TicketFactory{
    @Override
    public Ticket createTicket(Map<String, Object> attributes, int id, String timestamp) {
        UIFeedback uiFeedback= new UIFeedback(timestamp);

        uiFeedback.setId(id);
        uiFeedback.setTitle((String) attributes.get("title"));
        uiFeedback.setDescription((String) attributes.get("description"));
        uiFeedback.setBusinessPriority(BusinessPriority.valueOf(
                (String) attributes.get("businessPriority")));
        uiFeedback.setExpertiseArea(ExpertiseArea.valueOf(
                (String) attributes.get("expertiseArea")));
        uiFeedback.setReportedBy((String) attributes.get("reportedBy"));
        uiFeedback.setCreatedAt((String) attributes.get("timestamp"));
        uiFeedback.setState(TicketStatus.OPEN);

        if(attributes.containsKey("uiElementId")) {
            uiFeedback.setUiElementId((String) attributes.get("uiElementId"));
        }
        uiFeedback.setBusinessValue(BusinessValue.valueOf(
                (String) attributes.get("businessValue")));
        uiFeedback.setUsabilityScore(Integer.parseInt
                (attributes.get("usabilityScore").toString()));
        if(attributes.containsKey("screenshotUrl")) {
            uiFeedback.setScreenshotUrl((String) attributes.get("screenshotUrl"));
        }
        if(attributes.containsKey("suggestedFix")) {
            uiFeedback.setSuggestedFix((String) attributes.get("suggestedFix"));
        }
        uiFeedback.setCreatedAt(timestamp);

        return uiFeedback;
    }
}
