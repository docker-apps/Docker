package com.docker.technicalservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private static Preferences prefs;
    private String localDir;

    public Persistence() {
        prefs = Gdx.app.getPreferences("dockerPrefs");
        localDir = Gdx.files.getLocalStoragePath();
        //default prefs
        prefs.putBoolean("soundOn", true);
        prefs.putBoolean("1", true);
    }

    public static Map<String, ?> getPreferenceMap() {
        return prefs.get();
    }

    public static void setAllPreferences(Map<String,?> map) {
        prefs.put(map);
        prefs.flush();
    }

    public static void setPreference(String key, Object value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
        prefs.put(map);
    }

    public static Boolean isSoundOn() {
        return prefs.getBoolean("soundOn");
    }

    public static Boolean isLevelOpen(String levelId) {
        return prefs.getBoolean(levelId);
    }

    public static void setLevelOpen(String levelId) {
        prefs.putBoolean(levelId, true);
        prefs.flush();
    }

    public static void setSound(Boolean soundOn) {
        prefs.putBoolean("soundOn", soundOn);
        prefs.flush();
    }

    public static Integer getHighscore() {
        return prefs.getInteger("highscore", 0);
    }

    public void setHighscore(Integer highscore) {
        prefs.putInteger("highscore", highscore);
        prefs.flush();
    }


    public static FileHandle getLevelFile() {
        FileHandle levelHandle = Gdx.files.internal("level/level.json");
        boolean fileExists = levelHandle.exists();
        if (fileExists) {
            return levelHandle;
        }
        return null;
    }

    public static List<JsonValue> getAllLevels() {
        List<JsonValue> jsonValues = new ArrayList<JsonValue>();
        JsonReader r = new JsonReader();
        FileHandle levelFile = getLevelFile();
        JsonValue value = r.parse(levelFile);
        JsonValue levels = value.child.child;
        for (int i = 0; i < levels.size; i++) {
            JsonValue level = levels.get(i);
            jsonValues.add(level);
        }
        return jsonValues;
    }

    public static JsonValue getLevel(String id) {
        JsonReader r = new JsonReader();
        FileHandle levelFile = getLevelFile();
        if (levelFile == null) {
            return null;
        }
        JsonValue value = r.parse(levelFile);
        JsonValue levels = value.child.child;
        for (int i = 0; i < levels.size; i++) {
            JsonValue level = levels.get(i);
            if (level.get("id").asString().equals(id)) {
                return level;
            }
        }
        return null;
    }

    public static FileHandle getStatisticsFile() {
        FileHandle local = Gdx.files.local("statistics.json");
        boolean isLocAvailable = local.exists();
        if (!isLocAvailable) {
            FileHandle internal = Gdx.files.internal("statistics.json");
            internal.copyTo(Gdx.files.local("statistics.json"));
            return internal;
        }
        return local;
    }

    public static ObjectMap<String, Object> getStatisticsMap() {
        Json json = new Json();
        JsonStatistics stats = json.fromJson(JsonStatistics.class, getStatisticsFile().readString());
        return stats.data;
    }

    public static void writeStatisticMap(ObjectMap<String, Object> map) {
        JsonStatistics s = new JsonStatistics();
        s.data = map;
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        FileHandle file = getStatisticsFile();
        file.writeString(json.prettyPrint(s), false);
    }

    public static void saveStatisticValue(String key, Object object){
        ObjectMap<String, Object> statisticsMap = getStatisticsMap();
        statisticsMap.put(key, object);
        writeStatisticMap(statisticsMap);
    }

    private static class JsonStatistics {
        public ObjectMap<String, Object> data = new ObjectMap<String, Object>();
    }
}
