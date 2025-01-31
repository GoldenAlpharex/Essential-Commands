package com.fibermc.essentialcommands.commands;

import com.fibermc.essentialcommands.*;
import com.fibermc.essentialcommands.access.ServerPlayerEntityAccess;
import com.fibermc.essentialcommands.config.Config;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Util;

public class TeleportAcceptCommand extends TeleportResponseCommand {

    public TeleportAcceptCommand() {}

    protected int exec(CommandContext<ServerCommandSource> context, ServerPlayerEntity senderPlayer, ServerPlayerEntity targetPlayer) {
        ServerCommandSource source = context.getSource();
        PlayerData targetPlayerData = ((ServerPlayerEntityAccess)targetPlayer).getEcPlayerData();

        //identify if target player did indeed request to teleport. Continue if so, otherwise throw exception.
        TeleportRequest teleportRequest = targetPlayerData.getSentTeleportRequest();
        if (teleportRequest != null && teleportRequest.getTargetPlayer().equals(senderPlayer)) {

            //inform target player that teleport has been accepted via chat
            targetPlayer.sendSystemMessage(
                    ECText.getInstance().getText("cmd.tpaccept.feedback").setStyle(Config.FORMATTING_DEFAULT)
                    , Util.NIL_UUID);

            //Conduct teleportation
            teleportRequest.queue();

            //Send message to command sender confirming that request has been accepted
            source.sendFeedback(
                    ECText.getInstance().getText("cmd.tpaccept.feedback").setStyle(Config.FORMATTING_DEFAULT)
                    , Config.BROADCAST_TO_OPS);

            //Clean up TPAsk
            targetPlayerData.setTpTimer(-1);
            // Remove the tp request, as it has been completed.
            TeleportRequestManager.getInstance().endTpRequest(teleportRequest);

            return 1;
        } else {
            source.sendError(
                    ECText.getInstance().getText("cmd.tpa_reply.error.no_request_from_target").setStyle(Config.FORMATTING_ERROR)
            );
            return -1;
        }

    }

}
