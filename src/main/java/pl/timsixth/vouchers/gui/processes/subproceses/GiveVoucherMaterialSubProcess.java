package pl.timsixth.vouchers.gui.processes.subproceses;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.timsixth.guilibrary.core.util.ChatUtil;
import pl.timsixth.guilibrary.processes.model.impl.AbstractSubGuiProcess;
import pl.timsixth.guilibrary.processes.model.input.WriteableInput;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.config.Messages;
import pl.timsixth.vouchers.manager.process.ProcessManager;
import pl.timsixth.vouchers.model.Process;
import pl.timsixth.vouchers.model.Voucher;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class GiveVoucherMaterialSubProcess extends AbstractSubGuiProcess implements WriteableInput {

    private final VouchersPlugin vouchersPlugin;
    private final Messages messages;
    private final ProcessManager processManager;

    public GiveVoucherMaterialSubProcess(VouchersPlugin vouchersPlugin, Messages messages, ProcessManager processManager) {
        super("GIVE_VOUCHER_MATERIAL");
        this.vouchersPlugin = vouchersPlugin;
        this.messages = messages;
        this.processManager = processManager;
    }

    @Override
    public String getInventoryDisplayName() {
        return ChatUtil.chatColor("&a&lType material");
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
                                AnvilGUI.ResponseAction.run(() -> {
                                    Player player = stateSnapshot.getPlayer();
                                    Optional<Process> processOptional = processManager.getProcessByUser(player.getUniqueId());

                                    if (!processOptional.isPresent()) return;

                                    Process process = processOptional.get();

                                    Material materialFromInput = Material.getMaterial(stateSnapshot.getText().toUpperCase());
                                    Material material = materialFromInput == null ? Material.PAPER : materialFromInput;

                                    Voucher voucher = getOrCreateVoucher(process);
                                    if (voucher.getName() == null) voucher.setName((String) this.getDatum("name"));
                                    voucher.setDisplayName((String) this.getDatum("displayName"));
                                    voucher.setMaterial(material);

                                    processManager.startProcess(process);
                                }),
                                AnvilGUI.ResponseAction.run(() -> this.setEnded(true)),
                                AnvilGUI.ResponseAction.run(() -> stateSnapshot.getPlayer().sendMessage(messages.getTypeVoucherCommand()))
                        );
                    }
                })
                .plugin(vouchersPlugin);
    }

    private Voucher getOrCreateVoucher(Process process) {
        Voucher currentVoucher = process.getCurrentVoucher();

        if (currentVoucher == null) process.setCurrentVoucher(new Voucher());

        return currentVoucher;
    }
}
