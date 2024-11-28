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
import com.vertmix.supervisor.menu.menu.MenuModifier;
import com.vertmix.supervisor.menu.menu.Schema;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class AbstractMenu<T> {

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

    protected final @Getter Map<Character, Icon> items = new HashMap<>();

    protected final @Getter Map<Integer, GuiAction<InventoryClickEvent>> actions = new HashMap<>();
    protected final @Getter Map<Character, GuiAction<InventoryClickEvent>> charActions = new HashMap<>();

    protected final Schema schema = new Schema();
    protected final MenuModifier menuModifier = new MenuModifier();

    protected final @Getter Map<String, Object> options = new HashMap<>();

    protected @Setter
    @Getter GuiAction<InventoryClickEvent> defaultClickAction;
    protected @Setter @Getter GuiAction<InventoryClickEvent> defaultTopClickAction;
    protected @Setter @Getter GuiAction<InventoryClickEvent> playerInventoryAction;
    protected @Setter @Getter GuiAction<InventoryDragEvent> dragAction;
    protected @Setter @Getter GuiAction<InventoryCloseEvent> closeGuiAction;
    protected @Setter @Getter GuiAction<InventoryOpenEvent> openGuiAction;
    private @Setter @Getter GuiAction<InventoryClickEvent> outsideClickAction;


    public AbstractMenu(File file) {
        this.file = file;
    }

    public abstract void init();


    public void loadFile() {
        try {
            if (file.exists() && file.length() > 0) {                // Deserialize the YAML file into MenuData
                SimpleMenu.MenuData data = yamlMapper.readValue(file, SimpleMenu.MenuData.class);

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
            SimpleMenu.MenuData data = new SimpleMenu.MenuData();
            data.items.putAll(items);
            data.schema.addAll(schema.getSchema());
            data.options.putAll(options);

            // Serialize MenuData to YAML and write to file
            yamlMapper.writeValue(file, data);
        } catch (IOException e) {
            throw new IllegalStateException("An error occurred while saving the file: " + file.getPath(), e);
        }
    }



    public abstract void render(T o);

    public void set(char c, Icon icon) {
        set(c, icon, null);
    }


    public void set(char c, Icon icon, GuiAction<InventoryClickEvent> action) {
        items.put(c, icon);

        charActions.put(c, action);
    }

    public void setSlotAction(char c, GuiAction<InventoryClickEvent> action) {
        for (Integer i : Optional.ofNullable(schema.getCharacterMap().get(c)).orElse(new HashSet<>())) {
            actions.put(i, action);
        }

    }

    public void setSlotAction(int slot, GuiAction<InventoryClickEvent> action) {
        actions.put(slot, action);
    }

    public Schema schema() {
        return this.schema;
    }

    public MenuModifier modifiers() {
        return menuModifier;
    }

    public Map<String, Object> options() {
        return this.options;
    }

    public abstract void open(Player player);


    public GuiAction<InventoryClickEvent> getSlotAction(int slot) {
        return actions.get(slot);
    }

    public GuiAction<InventoryClickEvent> getCharAction(int slot) {
        return charActions.get(schema.getCharacter(slot));
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

