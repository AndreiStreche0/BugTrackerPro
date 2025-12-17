package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.TicketStatus;
import enums.UserRole;
import factory.FilterFactory;
import filters.Filter;
import milestones.Milestone;
import processing.AppStorage;
import tickets.Ticket;
import users.Developer;
import users.Manager;
import users.User;

import java.util.*;

/**
 * search command that returns all the developers or tickets filtered by given requirements
 */
public final class SearchCommand extends Command{
    private final Map<String, Object> filters;
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;

    public SearchCommand(final String username, final String timestamp,
                         final Map<String, Object> filters) {
        super("search", username, timestamp);
        this.filters = filters;
        appStorage = AppStorage.getInstance();
    }
    /**
     * creates error node for json output
     * @param message the string used for the node
     * @return the ObjectNode output
     */
    private ObjectNode createErrorNode(final String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "search");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }
    /**
     * searches the tickets or developers by given filters
     * @param correctPhase says if the command can be done in this phase
     * @return the output node
     */
    public ObjectNode execute(final boolean correctPhase) {
        User user = appStorage.findUser(super.getUsername());
        List<Ticket> tickets = null;
        List<Developer> developers = null;
        boolean isDeveloper = true;
        if (user == null) {
            return createErrorNode("The user " + super.getUsername() + " does not exist.");
        }
        switch(user.getRole()) {
            case UserRole.DEVELOPER:
                if (filters.get("searchType").equals("DEVELOPER")) {
                    return createErrorNode("The user does not have permission to execute this" +
                            " command: required role MANAGER; user role " + user.getRole() + ".");
                }
                tickets = this.availableTicketList((Developer) user);
                break;
            case UserRole.MANAGER:
                isDeveloper = false;
                if (filters.get("searchType").equals("TICKET")) {
                    tickets = appStorage.getTickets();
                } else if (filters.get("searchType").equals("DEVELOPER")) {
                    developers = this.developerList((Manager) user);
                }
                break;
            default:
                return createErrorNode("The user does not have permission to execute this" +
                        " command: required role DEVELOPER, MANAGER; user role " +
                        user.getRole() + ".");
        }
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("command", "search");
        outputNode.put("username", user.getUsername());
        outputNode.put("timestamp", super.getTimestamp());
        outputNode.put("searchType", (String) filters.get("searchType"));
        if (filters.get("searchType").equals("TICKET")) {
            List<Ticket> result = new ArrayList<>();
            Developer developer = isDeveloper ? (Developer)user : null;
            List<Filter<Ticket>> ticketFilters = FilterFactory.createTicketFilters(filters, developer);
            ArrayNode filteredTickets = mapper.createArrayNode();
            if (tickets == null) {
                outputNode.set("results", filteredTickets);
                return outputNode;
            }
            for (Ticket ticket : tickets) {
                boolean matchesAll = true;
                for (Filter<Ticket> filter : ticketFilters) {
                    if (!filter.filter(ticket)) {
                        matchesAll = false;
                    }
                }
                if (matchesAll) {
                    result.add(ticket);
                }
            }
            result.sort((t1, t2) -> {
                int dateComparator = t1.getCreatedAt().compareTo(t2.getCreatedAt());
                if (dateComparator != 0) {
                    return dateComparator;
                }
                return t1.getId() - t2.getId();
            });

            for (Ticket ticket : result) {
                ObjectNode ticketNode = mapper.createObjectNode();
                ticketNode.put("id", ticket.getId());
                ticketNode.put("type", ticket.getType().toString());
                ticketNode.put("title", ticket.getTitle());
                ticketNode.put("businessPriority", ticket.getBusinessPriority().toString());
                ticketNode.put("status", ticket.getState().toString());
                ticketNode.put("createdAt", ticket.getCreatedAt());
                ticketNode.put("solvedAt", ticket.getSolvedAt() == null ? "" :
                        ticket.getSolvedAt());
                ticketNode.put("reportedBy", ticket.getReportedBy());
                List<String> matches = new ArrayList<>();
                if (filters.get("keywords") != null) {
                    for (String keyword : (List<String>) filters.get("keywords")) {
                        if (ticket.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                                ticket.getDescription() != null && ticket.getDescription().contains(keyword.toLowerCase())) {
                            matches.add(keyword.toLowerCase());
                        }
                    }
                    Collections.sort(matches);
                }
                if (user.getRole() == UserRole.MANAGER) {
                    ticketNode.set("matchingWords", mapper.valueToTree(matches));
                }
                filteredTickets.add(ticketNode);
            }
            outputNode.set("results", filteredTickets);
        } else if (filters.get("searchType").equals("DEVELOPER")) {
            List<Filter<Developer>> developerFilters = FilterFactory.createDeveloperFilters(filters);
            List<Developer> result = new ArrayList<>();
            ArrayNode filteredDevelopers = mapper.createArrayNode();

            if (developers == null) {
                outputNode.set("results", filteredDevelopers);
                return outputNode;
            }
            for (Developer developer : developers) {
                boolean matchesAll = true;
                for (Filter<Developer> filter : developerFilters) {
                    if (!filter.filter(developer)) {
                        matchesAll = false;
                    }
                }
                if (matchesAll) {
                    result.add(developer);
                }
            }
            result.sort((d1, d2) -> d1.getUsername().compareTo(d2.getUsername()));

            for (Developer developer : result) {
                ObjectNode developerNode = mapper.createObjectNode();
                developerNode.put("username", developer.getUsername());
                developerNode.put("expertiseArea", developer.getExpertise().toString());
                developerNode.put("seniority", developer.getSeniority().toString());
                developer.calculatePerformanceScore();
                developerNode.put("performanceScore", developer.getPerformanceScore());
                developerNode.put("hireDate", developer.getHireDate());
                filteredDevelopers.add(developerNode);
            }
            outputNode.set("results", filteredDevelopers);
        }
        return outputNode;
    }

    /**
     * @param developer the developer that has the tickets
     * @return all the available tickets for the developer
     */
    private List<Ticket> availableTicketList(Developer developer) {
        List <Ticket> tickets = new ArrayList<>();
        for (Milestone milestone : appStorage.getMilestones()) {
            if (milestone.getAssignedDevs().contains(developer.getUsername())) {
                for (int ticketId : milestone.getTicketIds()) {
                    Ticket ticket = appStorage.getTicket(ticketId);
                    if (ticket.getState() == TicketStatus.OPEN) {
                        tickets.add(ticket);
                    }
                }
            }
        }
        return tickets;
    }

    /**
     * @param manager the manager who gives the command
     * @return all the developers that are subordinates of the manager
     */
    private List<Developer> developerList(Manager manager) {
        List <Developer> developerList = new ArrayList<>();
        for (String developerName : manager.getSubordinates()) {
            developerList.add((Developer)appStorage.findUser(developerName));
        }
        return developerList;
    }
}
