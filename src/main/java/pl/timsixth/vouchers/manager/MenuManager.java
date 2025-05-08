package pl.timsixth.vouchers.manager;

import pl.timsixth.guilibrary.core.manager.YAMLMenuManager;
import pl.timsixth.guilibrary.core.manager.registration.ActionRegistration;
import pl.timsixth.vouchers.config.ConfigFile;

public class MenuManager extends YAMLMenuManager implements Reloadable {
    private final ConfigFile configFile;

    public MenuManager(ActionRegistration actionRegistration, ConfigFile configFile) {
        super(actionRegistration);
        this.configFile = configFile;

    }

    public void load() {
        load(configFile.getYmlGuis());
    }

    @Override
    public void reload() {
        menus.clear();
        load();
    }
}
