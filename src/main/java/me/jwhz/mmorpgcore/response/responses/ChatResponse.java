package me.jwhz.mmorpgcore.response.responses;

import me.jwhz.mmorpgcore.response.Response;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public abstract class ChatResponse extends Response<String> {

    public ChatResponse(Player player) {

        listener = new Listener() {

            @EventHandler
            public void onPlayerChat(AsyncPlayerChatEvent e) {

                if (e.getPlayer().equals(player)) {

                    e.setCancelled(true);

                    if (onResponse(e.getMessage()))
                        unreigster();

                }

            }

        };

    }

}
