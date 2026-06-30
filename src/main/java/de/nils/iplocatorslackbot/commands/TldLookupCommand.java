package de.nils.iplocatorslackbot.commands;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import de.nils.iplocatorslackbot.common.Const;
import de.nils.iplocatorslackbot.daos.TLDData;
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

public class TldLookupCommand extends BotCommand {
    private final static Logger log = LoggerFactory.getLogger(TldLookupCommand.class);
    private final ApiRequestService apiRequestService;

    public TldLookupCommand(ApiRequestService apiRequestService) {
        this.apiRequestService = apiRequestService;
        super(Const.Commands.TLD_LOOKUP, "Lookup a TLD");
    }

    @Override
    protected Response execute(SlashCommandRequest request, SlashCommandContext context) throws IOException {
        String tld = request.getPayload().getText();

        if (tld == null || tld.isBlank()) {
            context.respond("Please enter a TLD");
            return context.ack();
        }

        tld = tld.trim();
        String finalTld = tld;

        CompletableFuture.runAsync(() -> {
            try {
                context.respond("Fetching TLD data for `" + finalTld + "`...");

                ApiResponse<TLDData> tldDataRequest = apiRequestService.getTldData(finalTld);

                if(tldDataRequest.statusCode() != 200) {
                    context.respond("Failed to fetch data: HTTP " + tldDataRequest.statusCode());
                    return;
                }

                TLDData tldData = tldDataRequest.body();

                context.respond(r -> r.blocks(asBlocks(
                        section(s -> s.text(markdownText("*TLD Lookup*\n`." + tldData.getTld() + "`"))),
                        section(s -> s.fields(Arrays.asList(
                                markdownText("*Type*\n" + TextUtils.na(tldData.getType())),
                                markdownText("*Registry*\n" + TextUtils.na(tldData.getRegistry())),
                                markdownText("*Country*\n" + TextUtils.na(tldData.getCountry())),
                                markdownText("*Country Code*\n" + TextUtils.na(tldData.getCountryCode()))
                        ))),
                        section(s -> s.fields(Arrays.asList(
                                markdownText("*WHOIS Server*\n" + TextUtils.na(tldData.getWhoisServer())),
                                markdownText("*RDAP Server*\n" + TextUtils.na(tldData.getRdapServerUrl())),
                                markdownText("*IANA ID*\n" + TextUtils.na(tldData.getIanaId())),
                                markdownText("*Status*\n" + TextUtils.na(tldData.getStatus()))
                        ))),
                        section(s -> s.fields(Arrays.asList(
                                markdownText("*Created*\n" + TextUtils.na(tldData.getCreatedDate())),
                                markdownText("*Updated*\n" + TextUtils.na(tldData.getUpdatedDate())),
                                markdownText("*DNSSEC Signed*\n" + (tldData.isDnssecSigned() ? "Yes" : "No")),
                                markdownText("*Punycode*\n" + TextUtils.na(tldData.getPunycode()))
                        ))),
                        section(s -> s.text(markdownText(
                                "*Description*\n" + TextUtils.na(tldData.getDescription())
                        ))),
                        section(s -> s.fields(Arrays.asList(
                                markdownText("*Nameservers*\n" +
                                        (tldData.getNameservers() == null || tldData.getNameservers().isEmpty()
                                                ? "N/A"
                                                : String.join("\n", tldData.getNameservers()))),
                                markdownText("*DNSSEC Keys*\n" +
                                        (tldData.getDnssecKeys() == null || tldData.getDnssecKeys().isEmpty()
                                                ? "N/A"
                                                : String.join("\n", tldData.getDnssecKeys())))
                        )))
                )));
            } catch (IOException e) {
                log.error("Failed to respond", e);
            }
        }, getExecutor());

        return context.ack();
    }
}
