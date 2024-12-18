package com.vertmix.supervisor.configuration.yml;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.vertmix.supervisor.configuration.AbstractConfigService;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class YamlConfigService extends AbstractConfigService {

    private static final String EXT = ".yml";
    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory()
            .enable(YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR)
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
            .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
            .configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));


    @Override
    public String getExtension() {
        return EXT;
    }

    @Override
    public void save(Object obj, File file) {
        trySave(obj, file, f -> file.getName().endsWith(EXT), (o, f) -> {
            try {
                MAPPER.writeValue(f, o);
            } catch (IOException e) {
                throw new RuntimeException("Error writing to file: " + f, e);
            }
        });
    }

    @Override
    public <Type> Optional<Type> load(Class<Type> clazz, File file) {
        // Check if the file exists and is not empty
        if (!file.exists() || file.length() == 0) {
            return Optional.empty(); // Or handle this scenario as you see fit
        }

        return Optional.ofNullable(tryLoad(clazz, file, f -> f.getName().endsWith(EXT), ((f, instance) -> {
            try {
                return MAPPER.readValue(f, clazz);
            } catch (IOException e) {
                throw new RuntimeException("Error reading from file: " + f, e);
            }
        })));
    }
}
