package com.vertmix.supervisor.core.bukkit.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;
import java.util.regex.Pattern;

//todo temporary
public class Text {
    private static final Pattern PATTERN = Pattern.compile("([^\\\\]?)&([0-9a-fk-r])");
    private static final Map<String, String> COLOR_CODES = new HashMap<String, String>() {
        {
            this.put("0", "<reset><color:black>");
            this.put("1", "<reset><color:dark_blue>");
            this.put("2", "<reset><color:dark_green>");
            this.put("3", "<reset><color:dark_aqua>");
            this.put("4", "<reset><color:dark_red>");
            this.put("5", "<reset><color:dark_purple>");
            this.put("6", "<reset><color:gold>");
            this.put("7", "<reset><color:gray>");
            this.put("8", "<reset><color:dark_gray>");
            this.put("9", "<reset><color:blue>");
            this.put("a", "<reset><color:green>");
            this.put("b", "<reset><color:aqua>");
            this.put("c", "<reset><color:red>");
            this.put("d", "<reset><color:light_purple>");
            this.put("e", "<reset><color:yellow>");
            this.put("f", "<reset><color:white>");
            this.put("k", "<obfuscated>");
            this.put("l", "<bold>");
            this.put("m", "<strikethrough>");
            this.put("n", "<underlined>");
            this.put("o", "<italic>");
            this.put("r", "<reset>");
        }
    };

    public Text() {
    }

    public static Component translate(String text) {
        return MiniMessage.miniMessage().deserialize(replacePrimitiveWithMiniMessage(text.replaceAll(String.valueOf('§'), "&"))).decoration(TextDecoration.ITALIC, false);
    }

    public static Component[] translate(String... text) {
        return (Component[]) Arrays.stream(text).map(Text::translate).toArray((x$0) -> {
            return new Component[x$0];
        });
    }

    public static List<Component> translate(List<String> list) {
        List<Component> toReturn = new ArrayList();
        Iterator var2 = list.iterator();

        while (var2.hasNext()) {
            String text = (String) var2.next();
            toReturn.add(translate(text.replaceAll(String.valueOf('§'), "&")));
        }

        return toReturn;
    }

    private static String replacePrimitiveWithMiniMessage(String string) {
        return PATTERN.matcher(string).replaceAll((matchResult) -> {
            String var10000 = matchResult.group(1);
            return var10000 + (String) COLOR_CODES.get(matchResult.group(2));
        });
    }

    public static String translateToPrimitive(Component component) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(component);
    }

    public static List<String> translateToPrimitive(List<Component> component) {
        return component.stream().map(Text::translateToPrimitive).toList();
    }

    public static String translateToMiniMessage(Component component) {
        return (String) MiniMessage.miniMessage().serialize(component);
    }

    public static List<String> translateToMiniMessage(List<Component> component) {
        return component.stream().map(Text::translateToMiniMessage).toList();
    }

    public static String capitalize(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] words = s.split(" +");
        String[] var3 = words;
        int var4 = words.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            String word = var3[var5];
            stringBuilder.append(word.substring(0, 1).toUpperCase());
            stringBuilder.append(word.substring(1).toLowerCase());
            stringBuilder.append(" ");
        }

        return stringBuilder.toString().trim();
    }
}
