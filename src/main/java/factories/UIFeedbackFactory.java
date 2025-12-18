package factories;

import enums.BusinessValue;
import enums.BusinessPriority;
import enums.ExpertiseArea;
import enums.TicketStatus;
import tickets.Ticket;
import tickets.UIFeedback;

import java.util.Map;

/**
 * creates a new UI feedback ticket
 */
public final class UIFeedbackFactory implements TicketFactory {
    /**
     * @param attributes all the ticket attributes given at input
     * @param id ticket id
     * @param timestamp timestamp of the command and ticket creation
     * @return a new UI Feedback ticket based on given attributes
     */
    @Override
    public Ticket createTicket(final Map<String, Object> attributes, final int id,
                               final String timestamp) {
        UIFeedback uiFeedback = new UIFeedback(timestamp);

        uiFeedback.setId(id);
        uiFeedback.setTitle((String) attributes.get("title"));
        uiFeedback.setDescription((String) attributes.get("description"));
        uiFeedback.setBusinessPriority(BusinessPriority.valueOf(
                (String) attributes.get("businessPriority")));
        uiFeedback.setExpertiseArea(ExpertiseArea.getType(
                (String) attributes.get("expertiseArea")));
        uiFeedback.setReportedBy((String) attributes.get("reportedBy"));
        uiFeedback.setCreatedAt((String) attributes.get("timestamp"));
        uiFeedback.setState(TicketStatus.OPEN);

        if (attributes.containsKey("uiElementId")) {
            uiFeedback.setUiElementId((String) attributes.get("uiElementId"));
        }
        uiFeedback.setBusinessValue(BusinessValue.valueOf(
                (String) attributes.get("businessValue")));
        uiFeedback.setUsabilityScore(Integer.parseInt(
                attributes.get("usabilityScore").toString()));
        if (attributes.containsKey("screenshotUrl")) {
            uiFeedback.setScreenshotUrl((String) attributes.get("screenshotUrl"));
        }
        if (attributes.containsKey("suggestedFix")) {
            uiFeedback.setSuggestedFix((String) attributes.get("suggestedFix"));
        }
        uiFeedback.setCreatedAt(timestamp);
        return uiFeedback;
    }
}
