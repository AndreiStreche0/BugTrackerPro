package tickets;

import enums.BusinessValue;
import enums.TicketType;

public class UIFeedback extends Ticket {
    private String uiElementId;
    private BusinessValue businessValue;
    private int usabilityScore;
    private String screenshotUrl;
    private String suggestedFix;

    public UIFeedback() {
        super();
        this.setType(TicketType.UI_FEEDBACK);
    }

    public String getUiElementId() {
        return uiElementId;
    }

    public void setUiElementId(String uiElementId) {
        this.uiElementId = uiElementId;
    }

    public BusinessValue getBusinessValue() {
        return businessValue;
    }

    public void setBusinessValue(BusinessValue businessValue) {
        this.businessValue = businessValue;
    }

    public int getUsabilityScore() {
        return usabilityScore;
    }

    public void setUsabilityScore(int usabilityScore) {
        this.usabilityScore = usabilityScore;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }

    public String getSuggestedFix() {
        return suggestedFix;
    }

    public void setSuggestedFix(String suggestedFix) {
        this.suggestedFix = suggestedFix;
    }
}