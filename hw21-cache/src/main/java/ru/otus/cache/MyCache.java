package ru.otus.cache;

import lombok.extern.java.Log;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

@Log
public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, V> cache = new WeakHashMap<>();
    private final Set<HwListener<K, V>> listeners = new HashSet<>();

    private static final String GET_ACTION = "get";
    private static final String PUT_ACTION = "put";
    private static final String REMOVE_ACTION = "remove";

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, PUT_ACTION);
    }

    @Override
    public void remove(K key) {
        var value = cache.remove(key);
        notifyListeners(key, value, REMOVE_ACTION);
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        notifyListeners(key, value, GET_ACTION);
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String action) {
        for (var listener : listeners) {
            try {
                listener.notify(key, value, action);
            }
            catch (Exception e) {
                log.warning("Failed to notify listener");
            }
        }
    }
}
