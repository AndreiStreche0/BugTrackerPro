package factories;

import enums.Frequency;
import enums.Severity;
import enums.BusinessPriority;
import enums.ExpertiseArea;
import enums.TicketStatus;
import tickets.Bug;
import tickets.Ticket;

import java.util.Map;

/**
 * factory class to create a bug ticket
 */
public class BugFactory implements TicketFactory {
    /**
     * @param attributes all the ticket attributes given at input
     * @param id ticket id
     * @param timestamp timestamp of the command and ticket creation
     * @return a new Bug ticket based on given attributes
     */
    @Override
    public Ticket createTicket(final Map<String, Object> attributes, final int id,
                               final String timestamp) {
        Bug bug = new Bug(timestamp);

        bug.setId(id);
        bug.setTitle((String) attributes.get("title"));
        bug.setDescription((String) attributes.get("description"));
        bug.setBusinessPriority(BusinessPriority.valueOf(
                (String) attributes.get("businessPriority")));
        bug.setExpertiseArea(ExpertiseArea.valueOf(
                (String) attributes.get("expertiseArea")));
        bug.setReportedBy((String) attributes.get("reportedBy"));
        bug.setState(TicketStatus.OPEN);
        bug.setExpectedBehavior((String) attributes.get("expectedBehavior"));
        bug.setActualBehavior((String) attributes.get("actualBehavior"));
        bug.setFrequency(Frequency.valueOf((String) attributes.get("frequency")));
        bug.setSeverity(Severity.valueOf((String) attributes.get("severity")));
        bug.setCreatedAt(timestamp);

        if (attributes.containsKey("environment")) {
            bug.setEnvironment((String) attributes.get("environment"));
        }
        if (attributes.containsKey("errorCode")) {
            bug.setErrorCode(Integer.parseInt(attributes.get("errorCode").toString()));
        }

        return bug;
    }
}
