package de.nils.iplocatorslackbot.commands;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class BotCommand {
    private static final Logger log = LoggerFactory.getLogger(BotCommand.class);
    private final String command;
    private final String description;

    protected BotCommand(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public Response executeBotCommand(SlashCommandRequest request, SlashCommandContext context) {
        log.info("Command <{}> executed", command);
        try {
            return execute(request, context);
        } catch (IOException e) {
            log.error("Error while running <{}>: ", command, e);
            return context.ack();
        }
    }

    protected abstract Response execute(SlashCommandRequest request, SlashCommandContext context) throws IOException;
}
