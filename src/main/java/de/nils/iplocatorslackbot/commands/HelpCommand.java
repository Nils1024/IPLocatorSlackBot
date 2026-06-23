package de.nils.iplocatorslackbot.commands;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import de.nils.iplocatorslackbot.common.Const;

public class HelpCommand extends BotCommand {
    public HelpCommand() {
        super(Const.Commands.HELP, "Shows this help");
    }

    @Override
    public Response execute(SlashCommandRequest request, SlashCommandContext context) {
        return context.ack();
    }
}
