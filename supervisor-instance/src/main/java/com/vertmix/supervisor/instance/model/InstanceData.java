package com.vertmix.supervisor.instance.model;

import lombok.Data;

import java.util.Map;

@Data
public class InstanceData {

    private final String id;
    private final String host;
    private final int port;
    private final Map<String, Object> options;


}
