package com.galsie.gcs.continuousservice.listener.sockets;

import com.galsie.gcs.continuousservice.gcssockets.handler.TestSocketHandler;
import com.galsie.gcs.continuousservice.gcssockets.packet.packets.SendNamePacket;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionGroup;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.gcssockets.listener.OnPacket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
@Slf4j
public class TestListener {


    @OnPacket(handlerTypes = {TestSocketHandler.class}, packetTypes = {SendNamePacket.class})
    public void onSendNamePacket(WebSocketSession session, SendNamePacket packet){
        var principal = session.getPrincipal();
        if (principal != null){
            log.info("Principal is " + principal);
            if (principal instanceof GalSecurityAuthSessionGroup authSessionGroup){
                var sesOpt = authSessionGroup.getGalSecurityAuthSessionFor(GalSecurityAuthSessionType.TEST);
                if (!sesOpt.isEmpty()){
                    var ses = sesOpt.get();
                    log.info("Entity id: " + ses.getEntityIdentifier().toString());
                }

            }
        }
        log.info("Received name packet " + packet.getName());
    }
}
