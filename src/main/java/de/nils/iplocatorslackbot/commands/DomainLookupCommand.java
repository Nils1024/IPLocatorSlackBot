package de.nils.iplocatorslackbot.commands;

import com.google.common.net.InetAddresses;
import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import de.nils.iplocatorslackbot.common.Const;
import de.nils.iplocatorslackbot.daos.IPData;
import de.nils.iplocatorslackbot.services.ApiRequestService;
import de.nils.iplocatorslackbot.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static com.slack.api.model.block.Blocks.asBlocks;
import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;

public class DomainLookupCommand extends BotCommand {
    private final static Logger log = LoggerFactory.getLogger(DomainLookupCommand.class);
    private final ApiRequestService apiRequestService;

    public DomainLookupCommand(ApiRequestService apiRequestService) {
        this.apiRequestService = apiRequestService;
        super(Const.Commands.DOMAIN_LOOKUP, "Lookup a domain");
    }

    @Override
    protected Response execute(SlashCommandRequest request, SlashCommandContext context) throws IOException {
        String domain = request.getPayload().getText();

        if(domain == null || domain.isBlank()) {
            context.respond("Please enter an IP");
            return context.ack();
        }

        domain = domain.trim();

        if(!InetAddresses.isInetAddress(domain)) {
            context.respond("Invalid IP: " + domain);
            return context.ack();
        }

        String finalIp = domain;

        CompletableFuture.runAsync(() -> {
            try {
                context.respond("Fetching IP data for `" + finalIp + "`...");

                IPData ipData = apiRequestService.getIpData(finalIp);

                context.respond(r -> r
                        .blocks(asBlocks(
                                section(s -> s.text(markdownText("*IP Lookup*\n`" + ipData.getIp() + "`"))),
                                section(s -> s.fields(Arrays.asList(
                                        markdownText("*Network (CIDR)*\n" + TextUtils.na(ipData.getNetwork())),
                                        markdownText("*ASN*\n" + TextUtils.na(ipData.getAsn())),
                                        markdownText("*Organization*\n" + TextUtils.na(ipData.getOrganization())),
                                        markdownText("*Continent*\n" + TextUtils.na(ipData.getContinent())),
                                        markdownText("*Country*\n" + TextUtils.na(ipData.getCountry())),
                                        markdownText("*Region*\n" + TextUtils.na(ipData.getRegion()))
                                ))),
                                section(s -> s.fields(Arrays.asList(
                                        markdownText("*City*\n" + TextUtils.na(ipData.getCity())),
                                        markdownText("*Postal Code*\n" + TextUtils.na(ipData.getPostalCode())),
                                        markdownText("*Timezone*\n" + TextUtils.na(ipData.getTimezone())),
                                        markdownText("*Coordinates*\n" + TextUtils.coords(ipData.getLatitude(), ipData.getLongitude())),
                                        markdownText("*Accuracy*\n" + TextUtils.na(ipData.getAccuracy()))
                                ))),
                                section(s -> s.fields(Arrays.asList(
                                        markdownText("*Hostnames*\n" + TextUtils.hostnames(ipData.getHostnames()))
                                )))
                        ))
                );

            } catch (IOException e) {
                log.error("Failed to respond: ", e);
            }
        }, getExecutor());

        return context.ack();
    }
}
