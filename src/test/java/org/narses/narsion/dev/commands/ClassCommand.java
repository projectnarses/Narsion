package org.narses.narsion.dev.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import static net.minestom.server.command.builder.arguments.ArgumentType.*;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.classes.abilities.PlayerClass;
import org.narses.narsion.player.NarsionPlayer;

import java.util.Arrays;

public class ClassCommand extends Command {

    private final NarsionServer server;

    public ClassCommand(NarsionServer server) {
        super("class");

        this.server = server;

        // Get Array of class names for command completion
        String[] classNames = Arrays.stream(server.getPlayerClasses().values())
                .map(PlayerClass::className)
                .toArray(String[]::new);

        this.addSyntax(this::setUsage,
                Literal("set"),
                Word("class")
                        .from(classNames)
        );
    }

    private void setUsage(CommandSender sender, CommandContext context) {
        Player player = sender.asPlayer();
        NarsionPlayer narsionPlayer = server.getPlayerWrapper(player);

        // get player class
        String className = context.get("class");
        PlayerClass playerClass = server.getPlayerClasses().getPlayerClass(className);

        narsionPlayer.setPlayerClass(playerClass);
    }
}
