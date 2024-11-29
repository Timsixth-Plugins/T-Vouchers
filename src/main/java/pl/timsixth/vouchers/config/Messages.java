package pl.timsixth.vouchers.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import pl.timsixth.guilibrary.core.util.ChatUtil;
import pl.timsixth.vouchers.VouchersPlugin;

import java.util.List;

@Getter
public final class Messages {

    private String noPermission;
    private String addedVoucher;
    private String voucherDoesntExists;
    private String offlinePlayer;
    private String addedVoucherToOtherPlayer;
    private String createdVoucher;
    private String addedVoucherEveryone;

    private String updatedVoucher;

    private String deletedVoucher;

    private String typeVoucherName;

    private String typeVoucherDisplayName;
    private String typeVoucherMaterial;

    private String cancelProcess;

    private String voucherAlreadyExists;
    private String typeVoucherLore;

    private String typeVoucherCommand;
    private String setVoucherEnchants;

    private String invalidFormatOfName;
    private String clearAllTodayLogs;
    private String filesReloaded;

    private String usedVoucher;
    private String notNumber;
    private String voucherRedeemRejected;
    private List<String> commandsList;

    @Getter(value = AccessLevel.NONE)
    private final VouchersPlugin vouchersPlugin;

    public Messages(VouchersPlugin vouchersPlugin) {
        this.vouchersPlugin = vouchersPlugin;
        load();
    }

    public void load() {
        FileConfiguration config = vouchersPlugin.getConfig();
        noPermission = ChatUtil.hexColor(config.getString("messages.no_permission"));
        addedVoucher = ChatUtil.hexColor(config.getString("messages.added_voucher"));
        voucherDoesntExists = ChatUtil.hexColor(config.getString("messages.doesnt_exists"));
        offlinePlayer = ChatUtil.hexColor(config.getString("messages.offline_player"));
        addedVoucherToOtherPlayer = ChatUtil.hexColor(config.getString("messages.added_voucher_other_player"));
        createdVoucher = ChatUtil.hexColor(config.getString("messages.created_voucher"));
        updatedVoucher = ChatUtil.hexColor(config.getString("messages.updated_voucher"));
        deletedVoucher = ChatUtil.hexColor(config.getString("messages.deleted_voucher"));
        typeVoucherName = ChatUtil.hexColor(config.getString("messages.type_voucher_name"));
        typeVoucherDisplayName = ChatUtil.hexColor(config.getString("messages.type_voucher_display_name"));
        cancelProcess = ChatUtil.hexColor(config.getString("messages.cancel_process"));
        voucherAlreadyExists = ChatUtil.hexColor(config.getString("messages.voucher_already_exits"));
        typeVoucherLore = ChatUtil.hexColor(config.getString("messages.type_voucher_lore"));
        typeVoucherCommand = ChatUtil.hexColor(config.getString("messages.type_voucher_command"));
        setVoucherEnchants = ChatUtil.hexColor(config.getString("messages.set_voucher_enchants"));
        invalidFormatOfName = ChatUtil.hexColor(config.getString("messages.invalid_name_format"));
        clearAllTodayLogs = ChatUtil.hexColor(config.getString("messages.clear_all_today_logs"));
        filesReloaded = ChatUtil.hexColor(config.getString("messages.files_reloaded"));
        typeVoucherMaterial = ChatUtil.hexColor(config.getString("messages.type_voucher_material"));
        addedVoucherEveryone = ChatUtil.hexColor(config.getString("messages.added_voucher_to_everyone"));
        usedVoucher = ChatUtil.hexColor(config.getString("messages.used_voucher"));
        notNumber = ChatUtil.hexColor(config.getString("messages.not_number"));
        voucherRedeemRejected = ChatUtil.hexColor(config.getString("messages.voucher_redeem_rejected"));
        commandsList = ChatUtil.hexColor(config.getStringList("messages.commands_list"));
    }
}
