package de.nils.iplocatorslackbot.commands;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import de.nils.iplocatorslackbot.common.Const;
import de.nils.iplocatorslackbot.services.CommandRegistry;

import java.io.IOException;

public class HelpCommand extends BotCommand {
    private final CommandRegistry commandRegistry;

    public HelpCommand(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
        super(Const.Commands.HELP, "Shows this help");
    }

    @Override
    protected Response execute(SlashCommandRequest request, SlashCommandContext context) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append("Commands:\n\n");

        for(BotCommand botCommand : commandRegistry.getCommands()) {
            sb.append("• ")
                    .append(botCommand.getCommand())
                    .append(" - ")
                    .append(botCommand.getDescription())
                    .append("\n");
        }

        context.respond(sb.toString());
        return context.ack();
    }
}
