package com.docker.technicalservices;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * - Settings
 * - Statistics
 * - Levels
 */
public class Persistence {

    private Preferences prefs;
    private String localDir;

    public Persistence() {
        prefs = Gdx.app.getPreferences("dockerPrefs");
        localDir = Gdx.files.getLocalStoragePath();
        //default prefs
        prefs.putBoolean("soundOn", true);
    }

    public Map<String, ?> getPreferenceMap() {
        return prefs.get();
    }

    public void setAllPreferences(Map<String,?> map) {
        prefs.put(map);
        prefs.flush();
    }

    public void setPreference(String key, Object value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
        prefs.put(map);
    }

    public Integer getHighscore() {
        return prefs.getInteger("highscore", 0);
    }

    public void setHighscore(Integer highscore) {
        prefs.putInteger("highscore", highscore);
        prefs.flush();
    }


    public FileHandle getLevelFile() {
        boolean isLocAvailable = Gdx.files.isLocalStorageAvailable();
        if (isLocAvailable) {
            return Gdx.files.internal("level/level.json");
        }
        return null;
    }

    public JsonValue getLevel(String id) {
        JsonReader r = new JsonReader();
        JsonValue value = r.parse(getLevelFile());
        JsonValue levels = value.child.child;
        for (int i = 0; i < 2; i++) {
            JsonValue level = levels.get(i);
            if (level.get("id").asString().equals(id)) {
                return level;
            }
        }
        return null;
    }

    public FileHandle getStatisticsFile() {
        boolean isLocAvailable = Gdx.files.isLocalStorageAvailable();
        if (isLocAvailable) {
            return Gdx.files.local("statistics.json");
        }
        return null;
    }

    public ObjectMap<String, Object> getStatisticsMap() {
        Json json = new Json();
        JsonStatistics stats = json.fromJson(JsonStatistics.class, getStatisticsFile().readString());
        return stats.data;
    }

    public void writeStatisticMap(ObjectMap<String, Object> map) {
        JsonStatistics s = new JsonStatistics();
        s.data = map;
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        FileHandle file = getStatisticsFile();
        file.writeString(json.prettyPrint(s), false);
    }

    public void saveStatisticValue(String key, Object object){
        ObjectMap<String, Object> statisticsMap = getStatisticsMap();
        statisticsMap.put(key, object);
        writeStatisticMap(statisticsMap);
    }

    private static class JsonStatistics {
        public ObjectMap<String, Object> data = new ObjectMap<String, Object>();
    }
}
