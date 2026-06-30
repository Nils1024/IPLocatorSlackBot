package de.nils.iplocatorslackbot.commands;

import com.google.common.net.InetAddresses;
import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import de.nils.iplocatorslackbot.common.Const;
import de.nils.iplocatorslackbot.daos.ASNData;
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

public class AsnLookupCommand extends BotCommand {
    private final static Logger log = LoggerFactory.getLogger(AsnLookupCommand.class);
    private final ApiRequestService apiRequestService;

    public AsnLookupCommand(ApiRequestService apiRequestService) {
        this.apiRequestService = apiRequestService;
        super(Const.Commands.ASN_LOOKUP, "Lookup an ASN");
    }

    @Override
    protected Response execute(SlashCommandRequest request, SlashCommandContext context) throws IOException {
        String asn = request.getPayload().getText();

        if(asn == null || asn.isBlank()) {
            context.respond("Please enter an ASN");
            return context.ack();
        }

        asn = asn.trim();
        String finalAsn = asn;

        CompletableFuture.runAsync(() -> {
            try {
                context.respond("Fetching ASN data for `" + finalAsn + "`...");

                ApiResponse<ASNData> asnDataRequest = apiRequestService.getAsnData(finalAsn);

                if(asnDataRequest.statusCode() != 200) {
                    context.respond("Failed to fetch data: HTTP " + asnDataRequest.statusCode());
                    return;
                }

                ASNData asnData = asnDataRequest.body();

                context.respond(r -> r.blocks(asBlocks(
                        section(s -> s.text(markdownText("*ASN Lookup*\n`AS" + asnData.getAsn() + "`"))),
                        section(s -> s.fields(Arrays.asList(
                                markdownText("*Name*\n" + TextUtils.na(asnData.getName())),
                                markdownText("*Organization*\n" + TextUtils.na(asnData.getOrganization())),
                                markdownText("*RIR*\n" + TextUtils.na(asnData.getRir()))
                        ))),
                        section(s -> s.fields(Arrays.asList(
                                markdownText("*Networks*\n" + TextUtils.hostnames(asnData.getNetworks()))
                        )))
                )));

            } catch (IOException e) {
                log.error("ASN lookup failed", e);
            }
        }, getExecutor());

        return context.ack();
    }
}
