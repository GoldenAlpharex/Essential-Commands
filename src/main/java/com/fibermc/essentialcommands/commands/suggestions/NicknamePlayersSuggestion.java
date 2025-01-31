package com.fibermc.essentialcommands.commands.suggestions;

import com.fibermc.essentialcommands.PlayerData;
import com.fibermc.essentialcommands.PlayerDataManager;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;

import java.util.Objects;

public class NicknamePlayersSuggestion {
    //Brigader Suggestions
    public static SuggestionProvider<ServerCommandSource> suggestedStrings() {
        return (context, builder) -> ListSuggestion.getSuggestionsBuilder(builder,
            PlayerDataManager.getInstance().getAllPlayerData().stream()
                .map(PlayerData::getNickname)
                .filter(Objects::nonNull)
                .map(MutableText::getString)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList()
        );
    }
}
