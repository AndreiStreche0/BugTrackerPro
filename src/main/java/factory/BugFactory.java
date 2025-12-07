package factory;

import enums.BusinessPriority;
import enums.ExpertiseArea;
import enums.Frequency;
import enums.Severity;
import tickets.Bug;
import tickets.Ticket;

import java.util.Map;

public class BugFactory implements TicketFactory {

    @Override
    public Ticket createTicket(Map<String, Object> attributes, int id) {
        Bug bug = new Bug();

        bug.setId(id);
        bug.setTitle((String) attributes.get("title"));
        bug.setDescription((String) attributes.get("description"));
        bug.setBusinessPriority(BusinessPriority.valueOf(
                (String) attributes.get("businessPriority")));
        bug.setExpertiseArea(ExpertiseArea.valueOf(
                (String) attributes.get("expertiseArea")));
        bug.setReportedBy((String) attributes.get("reportedBy"));
        bug.setCreatedAt((String) attributes.get("timestamp"));
        bug.setExpectedBehavior((String) attributes.get("expectedBehavior"));
        bug.setActualBehavior((String) attributes.get("actualBehavior"));
        bug.setFrequency(Frequency.valueOf((String) attributes.get("frequency")));
        bug.setSeverity(Severity.valueOf((String) attributes.get("severity")));
        if ("".equals(attributes.get("reportedBy"))) {
            bug.setReportedBy("ANONYMOUS");
            bug.setBusinessPriority(BusinessPriority.LOW);
        }

        if (attributes.containsKey("environment")) {
            bug.setEnvironment((String) attributes.get("environment"));
        }
        if (attributes.containsKey("errorCode")) {
            bug.setErrorCode(Integer.parseInt(attributes.get("errorCode").toString()));
        }

        return bug;
    }
}
