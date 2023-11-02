package com.galsie.gcs.continuousservice.gcssockets.packet;

import com.galsie.gcs.continuousservice.gcssockets.packet.packets.SendNamePacket;
import com.galsie.gcs.microservicecommon.lib.gcssockets.packet.AbstractPacketType;
import com.galsie.gcs.microservicecommon.lib.gcssockets.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PacketType implements AbstractPacketType {
    SEND_NAME(0L, SendNamePacket.class);

    Long id;
    Class<? extends Packet> packetClass;
}
