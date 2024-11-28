package com.vertmix.supervisor.menu.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pager<T> {
    private final List<T> items;
    private final int pageSize;

    public Pager(List<T> items, int pageSize) {
        if (items == null) {
            throw new IllegalArgumentException("Items list cannot be null");
        }
        if (pageSize <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }
        this.items = new ArrayList<>(items);
        this.pageSize = pageSize;
    }

    public List<T> getPage(int pageNumber) {
        if (pageNumber < 1) {
            throw new IllegalArgumentException("Page number must be greater than zero");
        }

        int fromIndex = (pageNumber - 1) * pageSize;
        if (fromIndex >= items.size()) {
            return Collections.emptyList();
        }

        int toIndex = Math.min(fromIndex + pageSize, items.size());
        return items.subList(fromIndex, toIndex);
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) items.size() / pageSize);
    }

    public boolean hasNextPage(int pageNumber) {
        return pageNumber < getTotalPages();
    }

    public boolean hasPreviousPage(int pageNumber) {
        return pageNumber > 1 && pageNumber <= getTotalPages();
    }
}