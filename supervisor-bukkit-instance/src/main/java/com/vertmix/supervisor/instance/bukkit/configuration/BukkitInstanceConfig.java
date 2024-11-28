package com.vertmix.supervisor.instance.bukkit.configuration;

import com.vertmix.supervisor.configuration.Configuration;
import com.vertmix.supervisor.configuration.yml.YamlConfigService;

import java.util.HashMap;
import java.util.Map;

@Configuration(fileName = "instance.yml", service = YamlConfigService.class)
public class BukkitInstanceConfig {

    public String id = "server-1";
    public String host = "0.0.0.0";
    public int port = 25565;
    public Map<String, Object> options = new HashMap<>() {{
        put("inventory-sync", true);
        put("health-sync", false);
        put("exp-sync", false);
    }};
}
