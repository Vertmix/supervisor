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

public class SimpleMenu implements Menu, InventoryHolder {

    private static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory()
            .enable(YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR)
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
            .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
            .configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

    private final File file;

    private final @Getter Map<Character, Icon> items = new HashMap<>();

    private final @Getter Map<Integer, GuiAction<InventoryClickEvent>> actions = new HashMap<>();
    private final @Getter Map<Character, GuiAction<InventoryClickEvent>> charActions = new HashMap<>();

    private final Schema schema = new Schema();
    private final MenuModifier menuModifier = new MenuModifier();

    private final @Getter Map<String, Object> options = new HashMap<>();

    private @Setter @Getter GuiAction<InventoryClickEvent> defaultClickAction;
    private @Setter @Getter GuiAction<InventoryClickEvent> defaultTopClickAction;
    private @Setter @Getter GuiAction<InventoryClickEvent> playerInventoryAction;
    private @Setter @Getter GuiAction<InventoryDragEvent> dragAction;
    private @Setter @Getter GuiAction<InventoryCloseEvent> closeGuiAction;
    private @Setter @Getter GuiAction<InventoryOpenEvent> openGuiAction;
    private @Setter @Getter GuiAction<InventoryClickEvent> outsideClickAction;

    private Inventory inventory;

    public SimpleMenu(File file) {
        this.file = file;
    }

    @Override
    public void init() {
        loadFile();
        schema().build();
        render();
    }


    public void loadFile() {
        try {
            if (file.exists() && file.length() > 0) {                // Deserialize the YAML file into MenuData
                MenuData data = yamlMapper.readValue(file, MenuData.class);

                // Load data into SimpleMenu
                if (data.items != null) {
                    items.putAll(data.items);
                }
                if (data.schema != null) {
                    schema.clear();
                    schema.add(data.schema);
                }
                if (data.options != null) {
                    options.clear();
                    options.putAll(data.options);
                }
            } else {
                // If file doesn't exist, create it with the default MenuData
                saveFile();
            }
        } catch (IOException e) {
            throw new IllegalStateException("An error occurred while loading the file: " + file.getPath(), e);
        }
    }

    // Custom saveFile method with YAML parsing using Jackson
    public void saveFile() {
        try {
            // Convert current state of SimpleMenu to MenuData
            MenuData data = new MenuData();
            data.items.putAll(items);
            data.schema.addAll(schema.getSchema());
            data.options.putAll(options);

            // Serialize MenuData to YAML and write to file
            yamlMapper.writeValue(file, data);
        } catch (IOException e) {
            throw new IllegalStateException("An error occurred while saving the file: " + file.getPath(), e);
        }
    }


    @Override
    public void render() {
        inventory = Bukkit.createInventory(this, 9, Component.text((String) options.getOrDefault("title", "Menu")));

        schema.getCharacterMap().forEach((key, value) -> {
            Icon icon = items.get(key);

            for (Integer slot : value) {
                inventory.setItem(slot, new ItemStack(Material.GOLD_BLOCK, 1));
            }
        });

    }

    @Override
    public void set(char c, Icon icon) {
        set(c, icon, null);
    }


    @Override
    public void set(char c, Icon icon, GuiAction<InventoryClickEvent> action) {
        items.put(c, icon);

        charActions.put(c, action);
    }

    @Override
    public void setSlotAction(char c, GuiAction<InventoryClickEvent> action) {
        for (Integer i : Optional.ofNullable(schema.getCharacterMap().get(c)).orElse(new HashSet<>())) {
            actions.put(i, action);
        }

    }

    @Override
    public void setSlotAction(int slot, GuiAction<InventoryClickEvent> action) {
        actions.put(slot, action);
    }

    @Override
    public Schema schema() {
        return this.schema;
    }

    @Override public MenuModifier modifiers() {
        return menuModifier;
    }

    @Override
    public Map<String, Object> options() {
        return this.options;
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

    public GuiAction<InventoryClickEvent> getSlotAction(int slot) {
        return actions.get(slot);
    }

    public GuiAction<InventoryClickEvent> getCharAction(int slot) {
        return charActions.get(schema.getCharacter(slot));
    }

    @Override
    public void close() throws IOException {

    }

    public static class MenuData {
        public Map<Character, Icon> items = new HashMap<>();
        public List<String> schema = new ArrayList<>();
        public Map<String, Object> options = new HashMap<>();

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
