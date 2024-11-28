package com.vertmix.supervisor.menu.util;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.exception.MenuException;
import com.vertmix.supervisor.menu.service.AbstractMenu;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Pager {

    private @Getter final char PAGED_KEY = '@';
    private @Getter final char NEXT_KEY = '>';
    private @Getter final char BACK_KEY = '<';

    private final AbstractMenu menu;
    private final List<Icon> items;

    private @Getter int pageSize;

    private @Getter int page = 0;

    public Pager(AbstractMenu menu, List<Icon> items) {
        this.menu = menu;

        if (items == null)
            throw new MenuException("Items list cannot be null");

        this.pageSize = menu.schema().getCharacterMap().getOrDefault(PAGED_KEY, new HashSet<>()).size();
        if (pageSize == 0)
            throw new MenuException("Page size must be greater than zero");

        this.items = new ArrayList<>(items);
    }

    public List<Icon> getPage(int pageNumber) {
        if (pageNumber < 1)
            throw new MenuException("Page number must be greater than zero");

        int fromIndex = (pageNumber - 1) * pageSize;

        if (fromIndex >= items.size()) {
            return Collections.emptyList();
        }

        int toIndex = Math.min(fromIndex + pageSize, items.size());
        return items.subList(fromIndex, toIndex);

    }

    public void next() {
        page++;
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) items.size() / pageSize);
    }

    public boolean hasNextPage() {
        return page < getTotalPages();
    }

    public boolean hasPreviousPage() {
        return page > 0 && page <= getTotalPages();
    }
}