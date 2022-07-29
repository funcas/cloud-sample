package com.github.funcas.spc.lb;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author Shane Fang
 * @since 1.0
 */
public class RibbonRequestContext {
    private final Map<String, String> attr = new HashMap<>();

    public String put(String key, String value) {
        return attr.put(key, value);
    }

    public String remove(String key) {
        return attr.remove(key);
    }

    public String get(String key) {
        return attr.get(key);
    }
}
