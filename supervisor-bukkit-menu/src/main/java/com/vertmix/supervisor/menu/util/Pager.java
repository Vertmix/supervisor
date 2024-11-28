package com.vertmix.supervisor.menu.util;

import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.exception.MenuException;
import com.vertmix.supervisor.menu.service.AbstractMenu;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
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

        this.pageSize = menu.schema().getCharacterMap().getOrDefault(PAGED_KEY, List.of()).size();
        if (pageSize == 0)
            throw new MenuException("Page size must be greater than zero");

        this.items = new ArrayList<>(items);
    }

    public List<Icon> getPage(int pageNumber) {
        if (pageNumber < 1)
            throw new MenuException("Page number must be greater than zero");

        int fromIndex = pageNumber * pageSize;

        if (fromIndex >= items.size()) {
            return Collections.emptyList();
        }

        int toIndex = Math.min(fromIndex + pageSize, items.size());

        return items.subList(fromIndex, toIndex);

    }

    public void next() {
        this.page = clamped(getTotalPages(), page + 1);

        render();
    }

    public void previous() {
        this.page = clamped(getTotalPages(), page - 1);

        render();
    }

    public void render() {
        final List<Icon> pageContent = getPage(page);

        final List<Integer> slots = menu.schema().getCharacterMap().get(PAGED_KEY);

        for (int i = 0; i < slots.size(); i++) {

            final int slot = slots.get(i);
            final Icon icon = pageContent.get(i);

            menu.getInventory().setItem(slot, icon.getItemstack());
        }
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

    private int clamped(int max, int current) {
        return Math.max(0, Math.min(max, current));
    }
}