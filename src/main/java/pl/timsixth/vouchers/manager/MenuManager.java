package pl.timsixth.vouchers.manager;

import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.manager.registration.ActionRegistration;

public class MenuManager extends AbstractMenuManager {
    private final ConfigFile configFile;

    public MenuManager(ActionRegistration actionRegistration,ConfigFile configFile) {
        super(actionRegistration);
        this.configFile = configFile;

    }

    public void load() {
        load(configFile.getYmlGuis());
    }
}
