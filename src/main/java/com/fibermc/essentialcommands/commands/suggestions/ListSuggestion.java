package com.fibermc.essentialcommands.commands.suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class ListSuggestion {
    public static CompletableFuture<Suggestions> getSuggestionsBuilder(SuggestionsBuilder builder, List<String> list) {
        String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);

        if(list.isEmpty()) { // If the list is empty then return no suggestions
            return Suggestions.empty(); // No suggestions
        }

        for (String str : list) { // Iterate through the supplied list
            if (str.toLowerCase(Locale.ROOT).startsWith(remaining)) {
                builder.suggest(str); // Add every single entry to suggestions list.
            }
        }
        return builder.buildFuture(); // Create the CompletableFuture containing all the suggestions
    }

    public static SuggestionProvider<ServerCommandSource> of(Collection<String> suggestionCollection) {
        return (CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) -> {
            String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);
            for (String str : suggestionCollection) { // Iterate through the supplied list
                if (str.toLowerCase(Locale.ROOT).startsWith(remaining)) {
                    builder.suggest(str); // Add every single entry to suggestions list.
                }
            }
            return builder.buildFuture();
        };
    }
}
