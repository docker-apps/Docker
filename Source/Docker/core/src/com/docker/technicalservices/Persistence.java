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
        prefs.putBoolean("1", true);
    }

    /**
     * @return the whole preference map
     */
    public static Map<String, ?> getPreferenceMap() {
        return prefs.get();
    }

    /**
     * sets all prefrences (adds new ones, overwrite existing)
     * @param map preference map
     */
    public static void setAllPreferences(Map<String,?> map) {
        prefs.put(map);
        prefs.flush();
    }

    /**
     * set single preference
     * example: setPreference("soundOn", true)
     * @param key key of the preference
     * @param value new value for the preference
     */
    public static void setPreference(String key, Object value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
        prefs.put(map);
        prefs.flush();
    }

    public static Boolean isSoundOn() {
        return prefs.getBoolean("soundOn", true);
    }

    public static void setSoundOn(Boolean soundOn) {
        prefs.putBoolean("soundOn", soundOn);
        prefs.flush();
    }

    public static Boolean isMusicOn() {
        return prefs.getBoolean("musicOn", true);
    }

    public static void setMusicOn(Boolean soundOn) {
        prefs.putBoolean("musicOn", soundOn);
        prefs.flush();
    }

    public static Boolean isLevelLocked(String levelId) {
        return !prefs.getBoolean(levelId);
    }

    public static void unlockLevel(String levelId) {
        prefs.putBoolean(levelId, true);
        prefs.flush();
    }

    public static Integer getLevelScore(String levelId) {
        return prefs.getInteger(levelId+"Score");
    }

    public static void setLevelScore(String levelId, Integer score) {
        prefs.putInteger(levelId + "Score", score);
        prefs.flush();
    }

    /**
     * @return highest score of the quick game
     */
    public static Integer getQuickHighscore() {
        return prefs.getInteger("quickHighscore", 0);
    }

    /**
     * set the new quick game highscore
     * @param highscore new high score
     */
    public static void setQuickHighscore(Integer highscore) {
        prefs.putInteger("quickHighscore", highscore);
        prefs.flush();
    }
    /**
     * @return highest score of the infinite game
     */
    public static Integer getInfiniteHighscore() {
        return prefs.getInteger("infiniteHighscore", 0);
    }

    /**
     * set the new infinite game highscore
     * @param highscore new high score
     */
    public static void setInfiniteHighscore(Integer highscore) {
        prefs.putInteger("infiniteHighscore", highscore);
        prefs.flush();
    }

    private static FileHandle getLevelFile() {
        FileHandle levelHandle = Gdx.files.internal("level/level.json");
        boolean fileExists = levelHandle.exists();
        if (fileExists) {
            return levelHandle;
        }
        return null;
    }

    /**
     * get all Levels as JsonValue
     * usage: for (JsonValue level : allLevels)
     *
     * @return levels
     */
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

    /**
     * Get Level by Id
     * @param id the level id
     * @return the level as JsonValue
     */
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

    private static FileHandle getStatisticsFile() {
        FileHandle local = Gdx.files.local("statistics.json");
        boolean isLocAvailable = local.exists();
        if (!isLocAvailable) {
            FileHandle internal = Gdx.files.internal("statistics.json");
            internal.copyTo(Gdx.files.local("statistics.json"));
            return internal;
        }
        return local;
    }

    /**
     * returns all Statistics from the file in a map
     * @return statistics ObjectMap
     */
    public static ObjectMap<String, Object> getStatisticsMap() {
        Json json = new Json();
        JsonStatistics stats = json.fromJson(JsonStatistics.class, getStatisticsFile().readString());
        return stats.data;
    }

    /**
     * writes map in statistics file
     * - overwrite whole file with new values
     * @param map statistics map
     */
    public static void writeStatisticMap(ObjectMap<String, Object> map) {
        JsonStatistics s = new JsonStatistics();
        s.data = map;
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        FileHandle file = getStatisticsFile();
        file.writeString(json.prettyPrint(s), false);
    }

    /**
     * save a value to the statistics file
     * - overwrite existing or add new
     * @param key the key/id of the statistic
     * @param value the statistic value
     */
    public static void saveStatisticValue(String key, Object value){
        ObjectMap<String, Object> statisticsMap = getStatisticsMap();
        statisticsMap.put(key, value);
        writeStatisticMap(statisticsMap);
    }

    public static float getVolume() {
        return prefs.getFloat("volume");
    }

    public static void setVolume(float volume) {
        prefs.putFloat("volume", volume);
        prefs.flush();
    }

    private static class JsonStatistics {
        public ObjectMap<String, Object> data = new ObjectMap<String, Object>();
    }
}
