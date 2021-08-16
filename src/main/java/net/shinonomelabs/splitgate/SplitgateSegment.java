package net.shinonomelabs.splitgate;

import org.json.JSONObject;

import java.util.Set;

public class SplitgateSegment {
    private final JSONObject segmentData;
    public SplitgateSegment(JSONObject segmentData) {
        this.segmentData = segmentData;
    }

    public String getName() {
        return this.segmentData.getJSONObject("metadata").optString("name", "");
    }

    public Set<String> getAvailableStats() {
        return this.segmentData.getJSONObject("stats").keySet();
    }

    public SplitgateStat getStat() {
        JSONObject stats = this.segmentData.getJSONObject("stats");
        String name = stats.getString("displayName");
        String category = stats.getString("displayCategory");
        int value = stats.getInt("value");
        return new SplitgateStat(name, category, value);
    }
}
