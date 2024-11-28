package com.vertmix.supervisor.menu;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.vertmix.supervisor.configuration.ConfigModule;
import com.vertmix.supervisor.configuration.ConfigService;
import com.vertmix.supervisor.configuration.yml.YamlConfigService;
import com.vertmix.supervisor.core.bukkit.item.Icon;
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
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.*;

public class SimpleMenu implements Menu, InventoryHolder {

    private final Map<Character, Icon> items = new HashMap<>();
    private final Map<Character, GuiAction<InventoryClickEvent>> actions = new HashMap<>();
    private final List<String> schema = new ArrayList<>();
    private final Map<String, Object> options = new HashMap<>();
    private final File file;
    private GuiAction<InventoryClickEvent> defaultClickAction;
    private GuiAction<InventoryClickEvent> defaultTopClickAction;
    private GuiAction<InventoryClickEvent> playerInventoryAction;
    private GuiAction<InventoryDragEvent> dragAction;
    private GuiAction<InventoryCloseEvent> closeGuiAction;
    private GuiAction<InventoryOpenEvent> openGuiAction;
    private GuiAction<InventoryClickEvent> outsideClickAction;
    private final Set<InteractionModifier> interactionModifiers = new HashSet<>();
    private static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory()
            .enable(YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR)
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
            .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
            .configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

    private Inventory inventory;

    private MenuData menuData = new MenuData();
    private final Class<Menu> clazz;

    public SimpleMenu(File file, Class<Menu> clazz) {
        this.file = file;
        this.clazz = clazz;
    }

    @Override
    public void init() {
        loadFile();

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
                    schema.addAll(data.schema);
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
            data.schema.addAll(schema);
            data.options.putAll(options);

            // Serialize MenuData to YAML and write to file
            yamlMapper.writeValue(file, data);
        } catch (IOException e) {
            throw new IllegalStateException("An error occurred while saving the file: " + file.getPath(), e);
        }
    }

    @Override
    public void render() {

        inventory = Bukkit.createInventory(this, 9, Component.text((String)options.getOrDefault("title", "Menu")));

        for (int row = 0; row < schema.size(); row++) {
            String rowSchema = schema.get(row);
            for (int col = 0; col < rowSchema.length(); col++) {
                char c = rowSchema.charAt(col);
                Icon icon = items.get(c);
                if (icon != null) {
                    int slot = row * 9 + col;
                    inventory.setItem(slot, new ItemStack(Material.GOLD_BLOCK, 1));
                }
            }
        }
    }



    @Override
    public void set(char c, Icon icon, GuiAction<InventoryClickEvent> action) {
        items.put(c, icon);
        actions.put(c, action);
        System.out.println("added item!");
    }

    @Override
    public List<String> schema() {
        return this.schema;
    }

    @Override
    public Map<String, Object> options() {
        return this.options;
    }

    @Override
    public void open(Player player) {
        System.out.println("called?");
        player.openInventory(this.inventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        if (inventory == null)
            throw new NullPointerException("Could not create menu");
        return this.inventory;
    }

    public GuiAction<InventoryClickEvent> getAction(int slot) {
        int row = slot / 9;
        int column = slot % 9;

        if (row < schema.size() && column < schema.get(row).length()) {
            char c = schema.get(row).charAt(column);
            return actions.get(c);
        }
        return null;
    }

    public Map<Character, Icon> getItems() {
        return items;
    }

    public Map<Character, GuiAction<InventoryClickEvent>> getActions() {
        return actions;
    }

    public List<String> getSchema() {
        return schema;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public GuiAction<InventoryClickEvent> getDefaultClickAction() {
        return defaultClickAction;
    }

    public GuiAction<InventoryClickEvent> getDefaultTopClickAction() {
        return defaultTopClickAction;
    }

    public GuiAction<InventoryClickEvent> getPlayerInventoryAction() {
        return playerInventoryAction;
    }

    public GuiAction<InventoryDragEvent> getDragAction() {
        return dragAction;
    }

    public GuiAction<InventoryCloseEvent> getCloseGuiAction() {
        return closeGuiAction;
    }

    public GuiAction<InventoryOpenEvent> getOpenGuiAction() {
        return openGuiAction;
    }

    public GuiAction<InventoryClickEvent> getOutsideClickAction() {
        return outsideClickAction;
    }

    public Set<InteractionModifier> getInteractionModifiers() {
        return interactionModifiers;
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
