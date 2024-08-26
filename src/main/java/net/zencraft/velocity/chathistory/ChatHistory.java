package net.zencraft.velocity.chathistory;

import com.github.retrooper.packetevents.PacketEvents;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Plugin(
        id = "zenchathistory",
        name = "ZenChatHistory",
        version = "1.0.2-SNAPSHOT",
        dependencies = {
            @Dependency(id = "packetevents")
        }
)
public class ChatHistory {

    private final ConcurrentHashMap<Player, Deque<Component>> messagesCache = new ConcurrentHashMap<>();
    public ConcurrentHashMap<Player,  Deque<Component>> getMessagesCache() {
        return messagesCache;
    }

    private static ChatHistory instance;
    public static ChatHistory getInstance() {
        return instance;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        instance = this;

        PacketEvents.getAPI().getSettings().reEncodeByDefault(false)
                .checkForUpdates(true)
                .bStats(true);
        PacketEvents.getAPI().load();

        PacketEvents.getAPI().getEventManager().registerListener(new SystemChatPacketListener());
        PacketEvents.getAPI().init();
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event){
        messagesCache.remove(event.getPlayer());
    }

    @Subscribe
    public void onServerConnected(ServerConnectedEvent event){
        Player player = event.getPlayer();
        Deque<Component> componentJsonList = getMessagesCache().remove(event.getPlayer());
        if(componentJsonList != null){
            for(Component component : componentJsonList){
                player.sendMessage(component);
            }
        }
    }

    public void addMessage(Player player, Component component) {
        Deque<Component> messages = messagesCache.getOrDefault(player, new ArrayDeque<>());

        messages.addLast(component);

        while (messages.size() > 50) {
            messages.removeFirst();
        }

        messagesCache.put(player, messages);
    }

}
