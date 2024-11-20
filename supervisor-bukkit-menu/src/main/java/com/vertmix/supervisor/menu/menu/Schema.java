package com.vertmix.supervisor.menu.menu;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Schema {

    private final @Getter List<String> schema = new ArrayList<>();

    private final @Getter Map<Character, Set<Integer>> characterMap = new HashMap<>();

    public Schema add(String row) {
        schema.add(row);
        return this;
    }

    public Schema add(String... row) {
        schema.addAll(List.of(row));
        return this;
    }

    public Schema add(Collection<String> rows) {
        schema.addAll(rows);
        return this;
    }

    public Schema add(String row, int times) {

        for (int i = 0; i < times; i++) {
            schema.add(row);
        }
        return this;
    }

    public Schema addEmpty() {
        schema.add("");
        return this;
    }

    public Schema addEmpty(int times) {
        for (int i = 0; i < times; i++) {
            addEmpty();
        }
        return this;
    }

    public Schema clear() {
        schema.clear();
        return this;
    }

    public void build() {
        characterMap.clear();
        characterMap.putAll(getSchemaSlots());
    }

    public @Nullable String get(int row) {
        return getSchema().get(row);
    }

    public int size() {
        return Math.min(6, getSchema().size());
    }

    private Map<Character, Set<Integer>> getSchemaSlots() {
        Map<Character, Set<Integer>> toReturn = new HashMap<>();

        int inventorySize = (schema.size() * 9) - 1;

        for (int i = 0; i < schema.size(); i++) {
            String line = schema.get(i);
            char[] chars = line.toCharArray();

            for (int column = 0; column < chars.length; column++) {
                char character = chars[column];

                int slot = column + (i * 9);
                if (slot > inventorySize)
                    break;

                toReturn.putIfAbsent(character, new HashSet<>());
                toReturn.get(character).add(slot);
            }
        }

        return toReturn;
    }

}
