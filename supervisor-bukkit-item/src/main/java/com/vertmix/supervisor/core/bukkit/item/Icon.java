package com.vertmix.supervisor.core.bukkit.item;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
public class Icon {

    public Material material;
    public int amount = 0;
    public String name;
    public List<String> lore;
    public int customModelData = 0;

    private transient ItemStack itemStack;

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
