package de.nils.iplocatorslackbot.commands;

import com.google.common.net.InetAddresses;
import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import de.nils.iplocatorslackbot.common.Const;
import de.nils.iplocatorslackbot.services.ApiRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;

public class IpLookupCommand extends BotCommand {
    private final static Logger log = LoggerFactory.getLogger(IpLookupCommand.class);
    private final ApiRequestService apiRequestService;

    public IpLookupCommand(ApiRequestService apiRequestService) {
        this.apiRequestService = apiRequestService;
        super(Const.Commands.IP_LOOKUP, "Lookup an IP address");
    }

    @Override
    protected Response execute(SlashCommandRequest request, SlashCommandContext context) throws IOException {
        String ip = request.getPayload().getText();

        if(ip == null || ip.isBlank()) {
             context.respond("Please enter an IP");
             return context.ack();
        }

        ip = ip.trim();

        if(!InetAddresses.isInetAddress(ip)) {
            context.respond("Invalid IP: " + ip);
            return context.ack();
        }

        context.respond(apiRequestService.getIpData(ip));
        return context.ack();
    }
}
