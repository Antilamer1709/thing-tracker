package com.antilamer.thingTracker.dto;

import lombok.Data;

import java.net.InetAddress;

@Data
public class HostDTO {

    private String hostName;

    private String redirectOAuthRUri;


    public HostDTO() {
        hostName = "Unknown";
    }

    public HostDTO(InetAddress ip) {
        this.hostName = ip.getHostName();
    }
}
