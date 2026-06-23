package de.nils.iplocatorslackbot.commands;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import de.nils.iplocatorslackbot.common.Const;

public class LookupCommand extends BotCommand {
    public LookupCommand() {
        super(Const.Commands.LOOKUP, "Lookup an IP address");
    }

    @Override
    public Response execute(SlashCommandRequest request, SlashCommandContext context) {
        return context.ack();
    }
}
