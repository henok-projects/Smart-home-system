package com.galsie.gcs.continuousservice.gcssockets.packet.packets;

import com.galsie.gcs.continuousservice.gcssockets.packet.PacketType;
import com.galsie.gcs.microservicecommon.lib.gcssockets.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class SendNamePacket extends Packet {

    private final String name;

    public SendNamePacket(String name) {
        super(PacketType.SEND_NAME);
        this.name = name;
    }

    public SendNamePacket() {
        this(null);
    }
}
