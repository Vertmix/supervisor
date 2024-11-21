package com.vertmix.supervisor.menu.menu;

import com.vertmix.supervisor.menu.entity.InteractionModifier;
import lombok.Getter;
import org.jetbrains.annotations.Contract;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class MenuModifier {

    private final Set<InteractionModifier> modifiers = new HashSet<>();

    @Contract("_ -> this")
    public MenuModifier add(InteractionModifier modifier) {
        modifiers.add(modifier);
        return this;
    }

    @Contract("_ -> this")
    public MenuModifier add(InteractionModifier... modifier) {
        modifiers.addAll(List.of(modifier));
        return this;
    }

    @Contract("_ -> this")
    public MenuModifier remove(InteractionModifier modifier) {
        modifiers.remove(modifier);
        return this;
    }

    @Contract(" -> this")
    public MenuModifier disableAllInteractions() {
        modifiers.addAll(InteractionModifier.VALUES);
        return this;
    }

    @Contract(" -> this")
    public MenuModifier clear() {
        modifiers.clear();
        return this;
    }

    public boolean allInteractionsDisabled() {
        return modifiers.size() == InteractionModifier.VALUES.size();
    }

    public boolean canPlaceItems() {
        return !modifiers.contains(InteractionModifier.PREVENT_ITEM_PLACE);
    }

    public boolean canTakeItems() {
        return !modifiers.contains(InteractionModifier.PREVENT_ITEM_TAKE);
    }

    public boolean canSwapItems() {
        return !modifiers.contains(InteractionModifier.PREVENT_ITEM_SWAP);
    }

    public boolean canDropItems() {
        return !modifiers.contains(InteractionModifier.PREVENT_ITEM_DROP);
    }

    public boolean allowsOtherActions() {
        return !modifiers.contains(InteractionModifier.PREVENT_OTHER_ACTIONS);
    }


}
