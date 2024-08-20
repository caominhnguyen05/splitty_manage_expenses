package client.utils;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.prefs.Preferences;

public class PreferenceUtils {

    public ObservableList<Long> savedEventIDs;
//    public String savedLanguage;

    private static final String KEY = "saved_data";
    private static final String LANGUAGE_KEY = "selected_language";
    private static final String CURRENCY_KEY = "selected_currency";

    /**
     * Initializes a new PreferenceUtils object.
     */
    public PreferenceUtils() {
        savedEventIDs = FXCollections.observableList(getAllSavedEvents());

        savedEventIDs.addListener((ListChangeListener<? super Long>) (listener) -> {
            saveEvents();
        });
//        savedLanguage = getSavedLanguage();
    }

    /**
     * Bubbles passed id to the top of the list and saves it.
     *
     * @param id to be bubbled to the top
     */
    public void putOnTop(long id) {
        savedEventIDs.remove(id);
        savedEventIDs.addFirst(id);
        saveEvents();
    }

    /**
     * Returns all event IDs saved in the preferences.
     * @return list of all IDs
     */
    private List<Long> getAllSavedEvents() {
        Preferences preferences = Preferences.userRoot();

        String savedJson = preferences.get(KEY, "");

        Gson gson = new Gson();

        var result = gson.fromJson(savedJson, Long[].class);

        return result == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(result));
    }

    /**
     * Saves all events from the observable list.
     */
    private void saveEvents() {
        Preferences preferences = Preferences.userRoot();

        Gson gson = new Gson();

        preferences.put(KEY, gson.toJson(savedEventIDs));
    }

    /**
     * Get the saved language.
     * @return the language code of the saved language.
     */
    public String getSavedLanguage() {
        Preferences preferences = Preferences.userRoot();
        var result = preferences.get(LANGUAGE_KEY, null);

        return result == null ? "en_US" : result;
    }

    /**
     * Save the language choice.
     * @param language the language code to save
     */
    public void saveLanguage(String language) {
        Preferences preferences = Preferences.userRoot();
        preferences.put(LANGUAGE_KEY, language);
    }

    /**
     * gets the currency chosen by the user
     * @return saved currency
     */
    public String getSavedCurrency() {
        Preferences preferences = Preferences.userRoot();
        var result = preferences.get(CURRENCY_KEY, null);

        return result == null ? "Default" : result;
    }

    /**
     * saves the currency chosen by the user
     * @param currency chosen currency
     */
    public void saveCurrency(String currency) {
        Preferences preferences = Preferences.userRoot();
        preferences.put(CURRENCY_KEY, currency);
    }
}
