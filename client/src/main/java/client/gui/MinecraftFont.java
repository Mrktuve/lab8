package client.gui;

import javafx.scene.text.Font;

import java.util.Objects;

public class MinecraftFont {

    private static final String FONT_PATH =
            "/fonts/minecraft.ttf";

    private static Font baseFont;

    static {

        try {

            baseFont = Font.loadFont(
                    Objects.requireNonNull(
                            MinecraftFont.class
                                    .getResourceAsStream(
                                            FONT_PATH
                                    )
                    ),
                    16
            );

        } catch (Exception e) {

            System.out.println(
                    "Minecraft font not loaded: "
                            + e.getMessage()
            );
        }
    }

    public static Font get(
            double size
    ) {

        if (baseFont == null) {
            return Font.font(size);
        }

        return Font.font(
                baseFont.getFamily(),
                size
        );
    }
}