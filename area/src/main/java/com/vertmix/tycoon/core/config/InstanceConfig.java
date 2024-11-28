package com.vertmix.tycoon.core.config;

import com.vertmix.supervisor.configuration.Configuration;
import com.vertmix.supervisor.configuration.yml.YamlConfigService;

import java.util.HashMap;
import java.util.Map;

@Configuration(fileName = "instance.yml", service = YamlConfigService.class)
public class InstanceConfig {

    public String host = "0.0.0.0";
    public int port = 25565;

    public Map<String, Object> properties = new HashMap<>();
}
