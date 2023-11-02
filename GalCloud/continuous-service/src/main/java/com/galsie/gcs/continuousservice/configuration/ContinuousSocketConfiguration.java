package com.galsie.gcs.continuousservice.configuration;

import com.galsie.gcs.continuousservice.gcssockets.handler.GalDevicesSubhubSocketHandler;
import com.galsie.gcs.continuousservice.gcssockets.handler.MobileSubhubSocketHandler;
import com.galsie.gcs.continuousservice.gcssockets.handler.TestSocketHandler;
import com.galsie.gcs.continuousservice.gcssockets.packet.PacketType;
import com.galsie.gcs.microservicecommon.lib.gcssockets.GCSWebSocketsConfiguration;
import com.galsie.gcs.microservicecommon.lib.gcssockets.handler.GCSSocketHandlerConfiguration;
import com.galsie.gcs.microservicecommon.lib.gcssockets.packet.AbstractPacketType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.util.Arrays;
import java.util.List;

@EnableWebSocket
@Configuration
public class ContinuousSocketConfiguration extends GCSWebSocketsConfiguration {

    @Autowired
    ContinuousSocketsLocalProperties continuousSocketsLocalProperties;

    @Override
    public <T extends AbstractPacketType> T[] getPacketTypes() {
        return (T[]) PacketType.values();
    }

    @Override
    public List<GCSSocketHandlerConfiguration> getSocketHandlersConfiguration() {
        var galDeviceSubHubConfig = GCSSocketHandlerConfiguration
                .builder()
                .gcsSocketHandler(new GalDevicesSubhubSocketHandler())
                .path(continuousSocketsLocalProperties.getGalDeviceSocketPath()).build();

        var mobileSubHubConfig = GCSSocketHandlerConfiguration
                .builder()
                .gcsSocketHandler(new MobileSubhubSocketHandler())
                .path(continuousSocketsLocalProperties.getMobileSocketPath()).build();

        var testConfig = GCSSocketHandlerConfiguration
                .builder()
                .gcsSocketHandler(new TestSocketHandler())
                .path(continuousSocketsLocalProperties.getTestSocketPath()).build();

        return Arrays.asList(galDeviceSubHubConfig, mobileSubHubConfig, testConfig);
    }

}


