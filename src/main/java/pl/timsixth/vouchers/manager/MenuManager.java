package pl.timsixth.vouchers.manager;

import org.bukkit.configuration.ConfigurationSection;
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

    @Override
    protected void addActionsWhichIncludingSection(ConfigurationSection clickActionSection) {

    }

    @Override
    protected boolean useActionWhichIncludingSection() {
        return false;
    }

}
