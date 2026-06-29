package de.nils.iplocatorslackbot.services;

import com.google.gson.Gson;
import de.nils.iplocatorslackbot.common.Const;
import de.nils.iplocatorslackbot.daos.ASNData;
import de.nils.iplocatorslackbot.daos.DomainData;
import de.nils.iplocatorslackbot.daos.IPData;
import de.nils.iplocatorslackbot.daos.TLDData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiRequestService {
    private final static Logger log = LoggerFactory.getLogger(ApiRequestService.class);
    private final Gson gson;

    public ApiRequestService() {
        gson = new Gson();
    }

    public IPData getIpData(String ip) {
        String json = sendHTTPRequest(Const.API.IP_API_URL + ip);
        return gson.fromJson(json, IPData.class);
    }

    public ASNData getAsnData(int asn) {
        String json = sendHTTPRequest(Const.API.ASN_API_URL + asn);
        return gson.fromJson(json, ASNData.class);
    }

    public DomainData getDomainData(String domain) {
        String json = sendHTTPRequest(Const.API.DOMAIN_API_URL + domain);
        return gson.fromJson(json, DomainData.class);
    }

    public TLDData getTldData(String tld) {
        String json = sendHTTPRequest(Const.API.TLD_API_URL + tld);
        return gson.fromJson(json, TLDData.class);
    }

    private String sendHTTPRequest(String uri) {
        try(HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build()) {
            HttpRequest request = HttpRequest.newBuilder(new URI(uri)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
