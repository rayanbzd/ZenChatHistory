package net.zencraft.velocity.chathistory;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSystemChatMessage;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class SystemChatPacketListener extends PacketListenerAbstract {

    public SystemChatPacketListener() {
        super(PacketListenerPriority.LOW);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        Player player = (Player) event.getPlayer();
        if(ChatHistory.getInstance().onRestore.contains(player)){
            return;
        }
        if(event.getConnectionState() != ConnectionState.PLAY)return;
        if(event.getUser().getClientVersion().isOlderThan(ClientVersion.V_1_20_2))return;
        if (event.getPacketType() == PacketType.Play.Server.SYSTEM_CHAT_MESSAGE) {
            try {
                WrapperPlayServerSystemChatMessage systemChatMessage = new WrapperPlayServerSystemChatMessage(event);

                Component component = systemChatMessage.getMessage();

                ChatHistory.getInstance().addMessage(player, component);
            }catch (IllegalStateException ex){
                throw new RuntimeException(ex);
            }
        }
    }

}
