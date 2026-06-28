package de.nils.iplocatorslackbot.services;

import de.nils.iplocatorslackbot.common.Const;
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

    public String getIpData(String ip) {
        return sendHTTPRequest(Const.API.IP_API_URL + ip);
    }

    public String getAsnData(int asn) {
        return sendHTTPRequest(Const.API.ASN_API_URL + asn);
    }

    public String getDomainData(String domain) {
        return sendHTTPRequest(Const.API.DOMAIN_API_URL + domain);
    }

    public String getTldData(String tld) {
        return sendHTTPRequest(Const.API.TLD_API_URL + tld);
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
