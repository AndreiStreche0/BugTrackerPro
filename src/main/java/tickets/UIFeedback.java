package tickets;

import enums.BusinessValue;
import enums.TicketType;
/**
 * UIFeedback class that holds all the specific parameters for this ticket type
 */
public final class UIFeedback extends Ticket {
    private String uiElementId;
    private BusinessValue businessValue;
    private int usabilityScore;
    private String screenshotUrl;
    private String suggestedFix;

    public UIFeedback(final String createdAt) {
        super(createdAt, createdAt);
        this.setType(TicketType.UI_FEEDBACK);
    }

    public String getUiElementId() {
        return uiElementId;
    }

    public void setUiElementId(final String uiElementId) {
        this.uiElementId = uiElementId;
    }

    public BusinessValue getBusinessValue() {
        return businessValue;
    }

    public void setBusinessValue(final BusinessValue businessValue) {
        this.businessValue = businessValue;
    }

    public int getUsabilityScore() {
        return usabilityScore;
    }

    public void setUsabilityScore(final int usabilityScore) {
        this.usabilityScore = usabilityScore;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setScreenshotUrl(final String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }

    public String getSuggestedFix() {
        return suggestedFix;
    }

    public void setSuggestedFix(final String suggestedFix) {
        this.suggestedFix = suggestedFix;
    }
}