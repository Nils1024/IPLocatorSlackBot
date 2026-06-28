package de.nils.iplocatorslackbot.commands;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import de.nils.iplocatorslackbot.common.Const;
import de.nils.iplocatorslackbot.services.ApiRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainLookupCommand extends BotCommand {
    private final static Logger log = LoggerFactory.getLogger(DomainLookupCommand.class);
    private final ApiRequestService apiRequestService;

    public DomainLookupCommand(ApiRequestService apiRequestService) {
        this.apiRequestService = apiRequestService;
        super(Const.Commands.DOMAIN_LOOKUP, "Lookup a domain");
    }

    @Override
    protected Response execute(SlashCommandRequest request, SlashCommandContext context) {
        return context.ack();
    }
}
