package org.narses.narsion.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TextUtils {

    /**
     * Converts the array of strings into an array of components with the specified style.
     * <br><br>
     * Each converted line has a max value
     *
     * @param style the style to apply
     * @param lines the lines to convert
     * @return array of components
     */
    public static List<Component> prepareLore(@Nullable Style style, String... lines) {
        List<Component> componentList = new ArrayList<>();

        for (String line : lines) {
            for (String split : splitIntoLine(line, 50)) {
                ComponentBuilder<TextComponent, TextComponent.Builder> builder = Component.text();

                if (style != null) {
                    builder.style(style);
                }

                builder.append(Component.text(split));

                componentList.add(builder.build());
            }
        }

        return componentList;
    }

    public static String[] splitIntoLine(String input, int maxCharInLine) {

        StringTokenizer tok = new StringTokenizer(input, " ");
        StringBuilder output = new StringBuilder(input.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();

            while(word.length() > maxCharInLine){
                output.append(word.substring(0, maxCharInLine-lineLen) + "\n");
                word = word.substring(maxCharInLine-lineLen);
                lineLen = 0;
            }

            if (lineLen + word.length() > maxCharInLine) {
                output.append("\n");
                lineLen = 0;
            }
            output.append(word + " ");

            lineLen += word.length() + 1;
        }
        // output.split();
        // return output.toString();
        return output.toString().split("\n");
    }

}
