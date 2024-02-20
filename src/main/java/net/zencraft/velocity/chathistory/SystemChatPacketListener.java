package net.zencraft.velocity.chathistory;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_19_3;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.protocol.packet.chat.ComponentHolder;
import io.netty.buffer.ByteBuf;
import net.kyori.adventure.text.Component;

public class SystemChatPacketListener extends PacketListenerAbstract {

    public SystemChatPacketListener() {
        super(PacketListenerPriority.MONITOR);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        Player player = (Player) event.getPlayer();
        if(event.getConnectionState() != ConnectionState.PLAY)return;
        if(event.getUser().getClientVersion().isOlderThan(ClientVersion.V_1_20_2))return;
        if(event.getPacketType() == PacketType.Play.Server.SYSTEM_CHAT_MESSAGE){
            ComponentHolder component = ComponentHolder.read((ByteBuf) event.getByteBuf(), ProtocolVersion.MINECRAFT_1_20_3);
            ChatHistory.getInstance().addMessage(player, component.getComponent());
        }else if(event.getPacketType() == PacketType.Play.Server.CHAT_MESSAGE){
            WrapperPlayServerChatMessage chatMessage = new WrapperPlayServerChatMessage(event);
            Component chatComponent = null;
            if(chatMessage.getMessage() instanceof ChatMessage_v1_19_3){
                chatComponent = ((ChatMessage_v1_19_3) chatMessage.getMessage()).getUnsignedChatContent().get();
            }
            if(chatComponent == null){
                chatComponent = chatMessage.getMessage().getChatContent();
            }
            ChatHistory.getInstance().addMessage(player, chatComponent);
        }
    }

}
