package de.nils.iplocatorslackbot.commands;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import de.nils.iplocatorslackbot.common.Const;
import de.nils.iplocatorslackbot.services.ApiRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsnLookupCommand extends BotCommand {
    private final static Logger log = LoggerFactory.getLogger(AsnLookupCommand.class);
    private final ApiRequestService apiRequestService;

    public AsnLookupCommand(ApiRequestService apiRequestService) {
        this.apiRequestService = apiRequestService;
        super(Const.Commands.ASN_LOOKUP, "Lookup an ASN");
    }

    @Override
    protected Response execute(SlashCommandRequest request, SlashCommandContext context) {
        return context.ack();
    }
}
