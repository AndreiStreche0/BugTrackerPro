package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import fileio.InputLoader;
import fileio.UserInput;
import processing.Brain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * main.App represents the main application logic that processes input commands,
 * generates outputs, and writes them to a file
 */
public class App {
    private static final String inputUserFile = "input/database/users.json";

    private static final ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();

    /**
     * Runs the application: reads commands from an input file,
     * processes them, generates results, and writes them to an output file
     *
     * @param inputPath path to the input file containing commands
     * @param outputPath path to the file where results should be written
     */
    public static void run(String inputPath, String outputPath) {
        // feel free to change this if needed (however keep 'outputs' variable name to be used for writing)

        List<ObjectNode> outputs = new ArrayList<>();
        processing.AppStorage.getInstance().reset();
        factory.FactoryService.getInstance().reset();

        /*
            TODO 1 :
            Load initial user data and commands. we strongly recommend using jackson library.
            you can use the reading from hw1 as a reference.
            however you can use some of the more advanced features of
            jackson library, available here: https://www.baeldung.com/jackson-annotations
        */
        InputLoader loader = new InputLoader(inputPath);
        List<UserInput> users = loader.getUsers();
        List<CommandInput> commands = loader.getCommands();
        Brain appBrain = new Brain(commands, users);
        // TODO 2: process commands.

        // TODO 3: create objectnodes for output, add them to outputs list.
        outputs = appBrain.parseCommands();
        // DO NOT CHANGE THIS SECTION IN ANY WAY
        try {
            File outputFile = new File(outputPath);
            outputFile.getParentFile().mkdirs();
            writer.withDefaultPrettyPrinter().writeValue(outputFile, outputs);
        } catch (IOException e) {
            System.out.println("error writing to output file: " + e.getMessage());
        }
    }
}
