package fileio;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public final class InputLoader {
    private final List<CommandInput> commands;
    private final List<UserInput> users;

    public InputLoader(final String inputPath) {
        ObjectMapper mapper = new ObjectMapper();
        List<CommandInput> commandsList = new ArrayList<>();
        List<UserInput> usersList = new ArrayList<>();

        try {
            CommandInput[] commandArray = mapper.readValue(new File(inputPath), CommandInput[].class);
            commandsList = Arrays.asList(commandArray);

            File usersFile = new File("input/database/users.json");
            UserInput[] userArray = mapper.readValue(usersFile, UserInput[].class);
            usersList = Arrays.asList(userArray);
        } catch (IOException e) {
            System.out.println("error while reading json input");
        }

        this.commands = commandsList;
        this.users = usersList;
    }

    public List<CommandInput> getCommands() {
        return commands;
    }

    public List<UserInput> getUsers() {
        return users;
    }
}