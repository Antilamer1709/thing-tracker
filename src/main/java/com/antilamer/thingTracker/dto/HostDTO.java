package com.antilamer.thingTracker.dto;

import lombok.Data;

import java.net.InetAddress;

@Data
public class HostDTO {

    private String hostName;

    public HostDTO() {
        hostName = "Unknown";
    }

    public HostDTO(InetAddress ip) {
        this.hostName = ip.getHostName();
    }
}
