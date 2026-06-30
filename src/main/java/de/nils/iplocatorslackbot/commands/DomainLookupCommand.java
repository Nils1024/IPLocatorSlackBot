package de.nils.iplocatorslackbot.commands;

import com.google.common.net.InetAddresses;
import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import de.nils.iplocatorslackbot.common.Const;
import de.nils.iplocatorslackbot.daos.DomainData;
import de.nils.iplocatorslackbot.daos.IPData;
import de.nils.iplocatorslackbot.services.ApiRequestService;
import de.nils.iplocatorslackbot.services.ApiResponse;
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
            context.respond("Please enter a Domain");
            return context.ack();
        }

        domain = domain.trim();
        String finalDomain = domain;

        CompletableFuture.runAsync(() -> {
            try {
                context.respond("Fetching domain data for `" + finalDomain + "`...");

                ApiResponse<DomainData> domainDataRequest = apiRequestService.getDomainData(finalDomain);

                if(domainDataRequest.statusCode() != 200) {
                    context.respond("Failed to fetch data: HTTP " + domainDataRequest.statusCode());
                    return;
                }

                DomainData domainData = domainDataRequest.body();
                IPData ipData = domainData.getIpData();

                context.respond(r -> r.blocks(asBlocks(
                        section(s -> s.text(markdownText("*Domain Lookup*\n`" + domainData.getDomain() + "`"))),
                        section(s -> s.fields(Arrays.asList(
                                markdownText("*Resolved IP*\n" + TextUtils.na(ipData.getIp())),
                                markdownText("*Network (CIDR)*\n" + TextUtils.na(ipData.getNetwork())),
                                markdownText("*ASN*\n" + TextUtils.na(ipData.getAsn())),
                                markdownText("*Organization*\n" + TextUtils.na(ipData.getOrganization()))
                        ))),
                        section(s -> s.fields(Arrays.asList(
                                markdownText("*Country*\n" + TextUtils.na(ipData.getCountry())),
                                markdownText("*Region*\n" + TextUtils.na(ipData.getRegion())),
                                markdownText("*City*\n" + TextUtils.na(ipData.getCity())),
                                markdownText("*Timezone*\n" + TextUtils.na(ipData.getTimezone()))
                        ))),
                        section(s -> s.fields(Arrays.asList(
                                markdownText("*Coordinates*\n" + TextUtils.coords(ipData.getLatitude(), ipData.getLongitude())),
                                markdownText("*Hostnames*\n" + TextUtils.hostnames(ipData.getHostnames()))
                        )))

                        //section(s -> s.text(markdownText("*DNS Records*\n" + TextUtils.hostnames(domainData.getRecords()))))
                )));

            } catch (IOException e) {
                log.error("Failed to respond", e);
            }
        }, getExecutor());

        return context.ack();
    }
}
