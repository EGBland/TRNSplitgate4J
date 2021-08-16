package net.shinonomelabs.splitgate;

import org.json.JSONObject;
import reactor.core.publisher.Mono;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Splitgate {
    private static final String BASE_URL = "https://public-api.tracker.gg/v2/splitgate/standard/profile/steam/";

    private final String agentName;
    private final String apiKey;

    public Splitgate(String apiKey, String agentName) {
        this.apiKey = apiKey;
        this.agentName = agentName;
    }

    public SplitgatePlayer getPlayer(String steamId, long playerDataCacheTime) {
        return new SplitgatePlayer(getData(steamId), playerDataCacheTime);
    }

    public SplitgatePlayer getPlayer(String steamId) {
        return getPlayer(steamId, 15*60*1000L);
    }

    private Mono<JSONObject> getData(String steamId) {
        return Mono.create(sink -> {
            try {
                URL url = new URL(BASE_URL + steamId);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                if(this.agentName != null) {
                    connection.setRequestProperty("User-Agent", this.agentName);
                }
                connection.setRequestProperty("TRN-Api-Key", this.apiKey);
                int responseCode = connection.getResponseCode();
                if(responseCode != HttpsURLConnection.HTTP_OK) {
                    sink.error(new Exception("Response from " + url + " was " + responseCode + "."));
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while((line = br.readLine()) != null) {
                    response.append(line);
                }

                // TODO check json
                JSONObject data = new JSONObject(response.toString()).getJSONObject("data");
                if(SplitgatePlayer.verifyJSON(data)) {
                    sink.success(data);
                }
                else {
                    sink.error(new Exception("400 response received, but response lacks some or all necessary data."));
                }
            } catch (Exception e) {
                sink.error(e);
            }
        });
    }
}
