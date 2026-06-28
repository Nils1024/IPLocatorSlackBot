package de.nils.iplocatorslackbot.services;

import com.slack.api.bolt.App;
import de.nils.iplocatorslackbot.commands.BotCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private final static Logger log = LoggerFactory.getLogger(CommandRegistry.class);

    private final App app;
    private final Map<String, BotCommand> commands;

    public CommandRegistry(App app) {
        this.app = app;
        this.commands = new HashMap<>();

        log.info("Command registry created");
    }

    public void register(BotCommand command) {
        commands.put(command.getCommand(), command);
        app.command(command.getCommand(), command::executeBotCommand);

        log.info("Command <{}> registered", command.getCommand());
    }

    public Collection<BotCommand> getCommands() {
        return commands.values();
    }
}
