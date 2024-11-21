package com.vertmix.supervisor.menu.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.vertmix.supervisor.core.bukkit.item.Icon;
import com.vertmix.supervisor.menu.entity.GuiAction;
import com.vertmix.supervisor.menu.menu.Menu;
import com.vertmix.supervisor.menu.menu.MenuModifier;
import com.vertmix.supervisor.menu.menu.Schema;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SimpleMenu extends AbstractMenu<Object> implements Menu<Object>, InventoryHolder {

    private static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory()
            .enable(YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR)
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
            .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
            .configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

    protected final @Getter Map<Character, Icon> items = new HashMap<>();

    protected final @Getter Map<Integer, GuiAction<InventoryClickEvent>> actions = new HashMap<>();
    protected final @Getter Map<Character, GuiAction<InventoryClickEvent>> charActions = new HashMap<>();

    protected final Schema schema = new Schema();
    protected final MenuModifier menuModifier = new MenuModifier();

    protected final @Getter Map<String, Object> options = new HashMap<>();

    protected @Setter @Getter GuiAction<InventoryClickEvent> defaultClickAction;
    protected @Setter @Getter GuiAction<InventoryClickEvent> defaultTopClickAction;
    protected @Setter @Getter GuiAction<InventoryClickEvent> playerInventoryAction;
    protected @Setter @Getter GuiAction<InventoryDragEvent> dragAction;
    protected @Setter @Getter GuiAction<InventoryCloseEvent> closeGuiAction;
    protected @Setter @Getter GuiAction<InventoryOpenEvent> openGuiAction;
    private @Setter @Getter GuiAction<InventoryClickEvent> outsideClickAction;

    protected Inventory inventory;

    public SimpleMenu(File file) {
        super(file);
    }

    @Override
    public void init() {
        loadFile();
        schema().build();
        render(null);
    }
    @Override
    public void render(Object o) {
        inventory = Bukkit.createInventory(this, 9, Component.text((String) options.getOrDefault("title", "Menu")));

        schema.getCharacterMap().forEach((key, value) -> {
            Icon icon = items.get(key);

            for (Integer slot : value) {
                inventory.setItem(slot, new ItemStack(Material.GOLD_BLOCK, 1));
            }
        });

    }

    @Override
    public void open(Player player) {
        player.openInventory(this.inventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        if (inventory == null)
            throw new NullPointerException("Could not create menu");
        return this.inventory;
    }

    @Override
    public void close() throws IOException {

    }
}
