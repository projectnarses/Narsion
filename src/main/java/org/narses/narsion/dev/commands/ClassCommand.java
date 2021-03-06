package org.narses.narsion.dev.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.suggestion.Suggestion;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.classes.PlayerClass;
import org.narses.narsion.classes.abilities.Ability;
import org.narses.narsion.commands.NarsionCommand;
import org.narses.narsion.player.NarsionPlayer;

import java.util.Arrays;

import static net.minestom.server.command.builder.arguments.ArgumentType.Literal;
import static net.minestom.server.command.builder.arguments.ArgumentType.Word;

public class ClassCommand extends NarsionCommand<NarsionServer> {

    public ClassCommand(NarsionServer server) {
        super(server, "class");

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
        NarsionPlayer narsionPlayer = server.wrap(player);

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
        NarsionPlayer narsionPlayer = server.wrap(player);

        sender.sendMessage("Your class is: " + narsionPlayer.getPlayerClass().className());
    }

    private void setUsage(CommandSender sender, CommandContext context) {
        Player player = sender.asPlayer();
        NarsionPlayer narsionPlayer = server.wrap(player);

        // get player class
        String className = context.get("class");
        PlayerClass playerClass = server.getPlayerClasses().getPlayerClass(className);

        assert playerClass != null;

        // Set class and send message to player
        narsionPlayer.setPlayerClass(playerClass);
        sender.sendMessage("Your class is: " + narsionPlayer.getPlayerClass().className());
    }

    private void abilityUsage(CommandSender sender, CommandContext context) {
        Player player = sender.asPlayer();
        NarsionPlayer narsionPlayer = server.wrap(player);

        // Get ability name
        String abilityName = context.get("ability");

        // Get ability and trigger
        Ability.valueOf(abilityName)
                .activate(server, player);
    }
}
