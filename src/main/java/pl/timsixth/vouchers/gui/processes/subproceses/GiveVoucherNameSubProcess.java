package pl.timsixth.vouchers.gui.processes.subproceses;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.timsixth.guilibrary.processes.manager.ProcessRunner;
import pl.timsixth.guilibrary.processes.model.impl.AbstractSubGuiProcess;
import pl.timsixth.guilibrary.processes.model.input.WriteableInput;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.config.Messages;
import pl.timsixth.vouchers.config.Settings;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Voucher;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;

public class GiveVoucherNameSubProcess extends AbstractSubGuiProcess implements WriteableInput {

    private final VouchersPlugin vouchersPlugin;
    private final VoucherManager voucherManager;
    private final Messages messages;
    private final Settings settings;

    public GiveVoucherNameSubProcess(VouchersPlugin vouchersPlugin, VoucherManager voucherManager, Messages messages) {
        super("GIVE_VOUCHER_NAME");
        this.vouchersPlugin = vouchersPlugin;
        this.voucherManager = voucherManager;
        this.messages = messages;
        this.settings = vouchersPlugin.getSettings();
    }

    @Override
    public AnvilGUI.Builder getAnvilInput() {
        return new AnvilGUI.Builder()
                .itemLeft(new ItemStack(Material.PAPER))
                .title(settings.getVoucherNameInputName())
                .itemOutput(new ItemStack(Material.PAPER))
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String name = stateSnapshot.getText();
                    Player player = stateSnapshot.getPlayer();
                    Optional<Voucher> optionalVoucher = voucherManager.getVoucher(name);

                    if (optionalVoucher.isPresent()) {
                        player.sendMessage(messages.getVoucherAlreadyExists());
                        return Collections.singletonList(AnvilGUI.ResponseAction.close());
                    }

                    Matcher matcher = Voucher.VOUCHER_NAME_PATTERN.matcher(name);

                    if (!matcher.matches()) {
                        player.sendMessage(messages.getInvalidFormatOfName());
                        return Collections.singletonList(AnvilGUI.ResponseAction.close());
                    }

                    return Arrays.asList(
                            AnvilGUI.ResponseAction.close(),
                            AnvilGUI.ResponseAction.run(() -> this.transformData("name", name)),
                            AnvilGUI.ResponseAction.run(() -> this.setEnded(true)),
                            AnvilGUI.ResponseAction.run(() -> ProcessRunner.runSubProcess(stateSnapshot.getPlayer(), this.nextProcess()))
                    );

                }).plugin(vouchersPlugin);
    }
}
