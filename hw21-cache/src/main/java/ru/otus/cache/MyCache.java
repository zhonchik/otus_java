package ru.otus.cache;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, V> cache = new WeakHashMap<>();
    private final Set<HwListener<K, V>> listeners = new HashSet<>();

    private final static String getAction = "get";
    private final static String putAction = "put";
    private final static String removeAction = "remove";

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, putAction);
    }

    @Override
    public void remove(K key) {
        var value = cache.remove(key);
        notifyListeners(key, value, removeAction);
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        notifyListeners(key, value, getAction);
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
            listener.notify(key, value, action);
        }
    }
}
