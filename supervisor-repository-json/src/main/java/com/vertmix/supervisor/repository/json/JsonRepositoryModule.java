package com.vertmix.supervisor.repository.json;

import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.annotation.Navigation;
import com.vertmix.supervisor.core.module.Module;
import com.vertmix.supervisor.core.service.Services;

import java.io.File;

public class JsonRepositoryModule implements Module<Object> {

    private File pluginDataFolder = null;

    @Override
    public void onEnable(CoreProvider<Object> provider) {
        // Retrieve the plugin's data folder
        pluginDataFolder = provider.getPath().toFile();
        ensureFolderExists(pluginDataFolder);

        Services.register(JsonRepository.class, clazz -> {
            // Determine the subfolder name
            String subfolderName = clazz.getSimpleName();
            Navigation navigation = clazz.getAnnotation(Navigation.class);
            if (navigation != null) {
                subfolderName = navigation.path();
            }

            // Create subfolder within the plugin's data folder
            File subFolder = new File(pluginDataFolder, subfolderName);
            ensureFolderExists(subFolder);

            // Specify a data.json file within the subfolder
            File jsonFile = new File(subFolder, "data.json");

            return new JsonProxyHandler<>(clazz, jsonFile).getInstance();
        });
    }

    @Override
    public void onDisable() {
        // No specific disable logic for now
    }

    /**
     * Ensures that the specified folder exists; if it does not, creates it.
     *
     * @param folder The folder to verify or create.
     */
    private void ensureFolderExists(File folder) {
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new RuntimeException("Could not create folder: " + folder.getAbsolutePath());
            }
        }
    }
}
