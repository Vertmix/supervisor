package gg.cookie.demo;

import com.vertmix.supervisor.core.CoreProvider;
import com.vertmix.supervisor.core.annotation.ModuleInfo;
import com.vertmix.supervisor.core.module.Module;
import org.bukkit.plugin.Plugin;

@ModuleInfo(name = "Demo", version = "0.0.2")
public class DemoModule implements Module<Plugin> {
    @Override
    public void onEnable(CoreProvider<Plugin> provider) {
        System.out.println("Logging " + provider.getSource().getName());
    }

    @Override
    public void onDisable() {

    }
}
