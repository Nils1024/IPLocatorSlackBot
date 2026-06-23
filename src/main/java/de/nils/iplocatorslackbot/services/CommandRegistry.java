package de.nils.iplocatorslackbot.services;

import com.slack.api.bolt.App;
import de.nils.iplocatorslackbot.commands.BotCommand;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private final App app;
    private final Map<String, BotCommand> commands;

    public CommandRegistry(App app) {
        this.app = app;
        this.commands = new HashMap<>();
    }

    public void register(BotCommand command) {
        commands.put(command.getCommand(), command);
        app.command(command.getCommand(), command::execute);
    }

    public Collection<BotCommand> getCommands() {
        return commands.values();
    }
}
