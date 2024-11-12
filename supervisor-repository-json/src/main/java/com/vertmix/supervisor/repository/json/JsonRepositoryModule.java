package com.vertmix.supervisor.repository.json;

import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.annotation.Navigation;
import com.vertmix.supervisor.core.module.Module;
import com.vertmix.supervisor.core.service.Services;

import java.io.File;

public class JsonRepositoryModule implements Module<Object> {

    private File folder = null;

    @Override
    public void onEnable(CoreProvider<Object> provider) {
        folder = provider.getPath().toFile();
        System.out.println("Enabled Json module");
        Services.register(JsonRepository.class, clazz -> {
            File file = folder;
            Navigation navigation = clazz.getAnnotation(Navigation.class);
            if (navigation != null) {
                file = new File(navigation.path());
            }
            return new JsonProxyHandler<>(clazz, file).getInstance();
        });


    }

    @Override
    public void onDisable() {

    }
}
