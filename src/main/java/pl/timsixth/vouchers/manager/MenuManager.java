package pl.timsixth.vouchers.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.manager.registration.ActionRegistration;

public class MenuManager extends AbstractMenuManager{
    private final YamlConfiguration ymlGuis;

    public MenuManager(ActionRegistration actionRegistration,ConfigFile configFile) {
        super(actionRegistration);
        ymlGuis = YamlConfiguration.loadConfiguration(configFile.getGuisFile());
    }

    public void load() {
        load(ymlGuis);
    }

    @Override
    protected void addActionsWhichIncludingSection(ConfigurationSection clickActionSection) {

    }

    @Override
    protected boolean useActionWhichIncludingSection() {
        return false;
    }

}
