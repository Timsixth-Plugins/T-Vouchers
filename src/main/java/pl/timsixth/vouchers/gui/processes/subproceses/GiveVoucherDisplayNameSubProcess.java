package pl.timsixth.vouchers.gui.processes.subproceses;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.timsixth.guilibrary.processes.manager.ProcessRunner;
import pl.timsixth.guilibrary.processes.model.impl.AbstractSubGuiProcess;
import pl.timsixth.guilibrary.processes.model.input.WriteableInput;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.config.Settings;

import java.util.Arrays;
import java.util.Collections;

public class GiveVoucherDisplayNameSubProcess extends AbstractSubGuiProcess implements WriteableInput {

    private final VouchersPlugin vouchersPlugin;
    private final Settings settings;

    public GiveVoucherDisplayNameSubProcess(VouchersPlugin vouchersPlugin) {
        super("GIVE_VOUCHER_DISPLAY_NAME");
        this.vouchersPlugin = vouchersPlugin;
        this.settings = vouchersPlugin.getSettings();
    }

    @Override
    public String getInventoryDisplayName() {
        return settings.getVoucherDisplayNameInputName();
    }

    @Override
    public AnvilGUI.Builder getAnvilInput() {
        return new AnvilGUI.Builder()
                .itemLeft(new ItemStack(Material.PAPER))
                .title(getInventoryDisplayName())
                .itemOutput(new ItemStack(Material.PAPER))
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    } else {
                        return Arrays.asList(
                                AnvilGUI.ResponseAction.close(),
                                AnvilGUI.ResponseAction.run(() -> this.transformData("displayName", stateSnapshot.getText())),
                                AnvilGUI.ResponseAction.run(() -> this.transformData("name", this.getDatum("name"))),
                                AnvilGUI.ResponseAction.run(() -> this.setEnded(true)),
                                AnvilGUI.ResponseAction.run(() -> ProcessRunner.runSubProcess(stateSnapshot.getPlayer(), this.nextProcess()))
                        );
                    }
                })
                .plugin(vouchersPlugin);
    }
}
