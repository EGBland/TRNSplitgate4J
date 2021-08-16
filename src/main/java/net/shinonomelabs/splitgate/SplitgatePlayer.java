package net.shinonomelabs.splitgate;

import org.json.JSONArray;
import org.json.JSONObject;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Duration;

public class SplitgatePlayer {
    private final Mono<JSONObject> data;
    private long cacheTime;

    public SplitgatePlayer(Mono<JSONObject> data, long cacheTime) {
        this.data = data;
        this.cacheTime = cacheTime;

        if(this.cacheTime > 0) {
            data.cache(Duration.ofMillis(this.cacheTime));
        }
    }

    public static boolean verifyJSON(JSONObject data) {
        // TODO better verification
        return data.has("platformInfo")
                && data.getJSONObject("platformInfo").has("platformUserId")
                && data.getJSONObject("platformInfo").has("platformUserHandle")
                && data.has("segments");
    }

    public Mono<Integer> getId() {
        return data.map(json -> json.getJSONObject("platformInfo"))
                .map(json -> json.getInt("platformUserId"));
    }

    public Mono<String> getName() {
        return data.map(json -> json.getJSONObject("platformInfo"))
                .map(json -> json.getString("platformUserHandle"));
    }

    public Mono<SplitgateSegment> getSegment(String segmentName) {
        return data.map(json -> json.getJSONArray("segments"))
                .flatMapMany(Flux::fromIterable)
                .filter(seg -> (seg instanceof JSONObject))
                .cast(JSONObject.class) // TODO change this?
                .map(SplitgateSegment::new)
                .next()
                .cache(Duration.ofMillis(this.cacheTime)); // TODO don't cache this?
    }
}
