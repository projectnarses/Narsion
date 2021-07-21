package org.narses.narsion.dev.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import static net.minestom.server.command.builder.arguments.ArgumentType.*;

import net.minestom.server.command.builder.suggestion.Suggestion;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.classes.PlayerClass;
import org.narses.narsion.classes.abilities.Ability;
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

        this.addSyntax(this::abilityUsage,
                Word("Ability").from("ability"),
                Word("ability")
                    .setSuggestionCallback(this::abilitySuggestion)
        );

        this.setDefaultExecutor(this::defaultUsage);
    }

    private void abilitySuggestion(CommandSender sender, CommandContext context, Suggestion suggestion) {
        Player player = sender.asPlayer();
        NarsionPlayer narsionPlayer = server.getPlayerWrapper(player);

        // Get all class abilities
        Ability[] abilities = narsionPlayer.getPlayerClass().abilities();

        // Convert + add to suggestion
        Arrays.stream(abilities)
                .map(Enum::name)
                .map(SuggestionEntry::new)
                .forEach(suggestion::addEntry);
    }


    private void defaultUsage(CommandSender sender, CommandContext context) {
        Player player = sender.asPlayer();
        NarsionPlayer narsionPlayer = server.getPlayerWrapper(player);

        sender.sendMessage("Your class is: " + narsionPlayer.getPlayerClass().className());
    }

    private void setUsage(CommandSender sender, CommandContext context) {
        Player player = sender.asPlayer();
        NarsionPlayer narsionPlayer = server.getPlayerWrapper(player);

        // get player class
        String className = context.get("class");
        PlayerClass playerClass = server.getPlayerClasses().getPlayerClass(className);

        narsionPlayer.setPlayerClass(playerClass);
    }

    private void abilityUsage(CommandSender sender, CommandContext context) {
        Player player = sender.asPlayer();
        NarsionPlayer narsionPlayer = server.getPlayerWrapper(player);

        // Get ability name
        String abilityName = context.get("ability");

        // Get ability and trigger
        Ability.valueOf(abilityName)
                .activate(server, player);
    }
}
