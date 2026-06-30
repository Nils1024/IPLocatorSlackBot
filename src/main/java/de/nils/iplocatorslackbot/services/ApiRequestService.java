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

    public ApiResponse<IPData> getIpData(String ip) {
        ApiResponse<String> apiResponse = sendHTTPRequest(Const.API.IP_API_URL + ip);
        return new ApiResponse<>(apiResponse.statusCode(), gson.fromJson(apiResponse.body(), IPData.class));
    }

    public ApiResponse<ASNData> getAsnData(String asn) {
        ApiResponse<String> apiResponse = sendHTTPRequest(Const.API.ASN_API_URL + asn);
        return new ApiResponse<>(apiResponse.statusCode(), gson.fromJson(apiResponse.body(), ASNData.class));
    }

    public ApiResponse<DomainData> getDomainData(String domain) {
        ApiResponse<String> apiResponse = sendHTTPRequest(Const.API.DOMAIN_API_URL + domain);
        return new ApiResponse<>(apiResponse.statusCode(), gson.fromJson(apiResponse.body(), DomainData.class));
    }

    public ApiResponse<TLDData> getTldData(String tld) {
        ApiResponse<String> apiResponse = sendHTTPRequest(Const.API.TLD_API_URL + tld);
        return new ApiResponse<>(apiResponse.statusCode(), gson.fromJson(apiResponse.body(), TLDData.class));
    }

    private ApiResponse<String> sendHTTPRequest(String uri) {
        try(HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build()) {
            HttpRequest request = HttpRequest.newBuilder(new URI(uri)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return new ApiResponse<>(response.statusCode(), response.body());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            log.error("Error while making HTTP Request to <{}>: ", uri, e);
            return new ApiResponse<>(0, "{}");
        }
    }
}
