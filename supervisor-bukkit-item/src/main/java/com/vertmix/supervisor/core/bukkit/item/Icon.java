package com.vertmix.supervisor.core.bukkit.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Icon {

    public Material material;
    public int amount = 1;
    public String name;
    public List<String> lore;
    public int customModelData = 0;

    public Icon(Material material, int amount, String name, List<String> lore, int customModelData) {
        this.material = material;
        this.amount = amount;
        this.name = name;
        this.lore = lore;
        this.customModelData = customModelData;
    }

    @JsonIgnore
    private transient ItemStack itemStack;

    public Icon()  {}

    public Icon(Material material) {
        this.material = material;
    }

    public Icon(Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    public Icon(Material material, int amount, String name, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.name = name;
        this.lore = lore;
    }

    @JsonIgnore
    public ItemStack getItemstack() {

        if (itemStack == null) {
            itemStack = new ItemStack(material, amount);

            itemStack.editMeta(meta -> {

                if (name != null && !name.isEmpty())
                    meta.displayName(Text.translate(name));

                if (lore != null && !lore.isEmpty())
                    meta.lore(Text.translate(lore));

                meta.setCustomModelData(0);
            });

        }

        return itemStack.clone();

    }

}
