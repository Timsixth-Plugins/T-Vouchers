package pl.timsixth.vouchers.config;

import lombok.AccessLevel;
import lombok.Getter;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.util.ChatUtil;

@Getter
public class Messages {

    private String noPermission;
    private String correctUse;
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

    @Getter(value = AccessLevel.NONE)
    private final VouchersPlugin vouchersPlugin;

    public Messages(VouchersPlugin vouchersPlugin) {
        this.vouchersPlugin = vouchersPlugin;
        load();
    }

    public void load() {
        noPermission = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.no_permission"));
        correctUse = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.correct_use"));
        addedVoucher = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.added_voucher"));
        voucherDoesntExists = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.doesnt_exists"));
        offlinePlayer = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.offline_player"));
        addedVoucherToOtherPlayer = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.added_voucher_other_player"));
        createdVoucher = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.created_voucher"));
        updatedVoucher = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.updated_voucher"));
        deletedVoucher = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.deleted_voucher"));
        typeVoucherName = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.type_voucher_name"));
        typeVoucherDisplayName = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.type_voucher_display_name"));
        cancelProcess = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.cancel_process"));
        voucherAlreadyExists = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.voucher_already_exits"));
        typeVoucherLore = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.type_voucher_lore"));
        typeVoucherCommand = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.type_voucher_command"));
        setVoucherEnchants = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.set_voucher_enchants"));
        invalidFormatOfName = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.invalid_name_format"));
        clearAllTodayLogs = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.clear_all_today_logs"));
        filesReloaded = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.files_reloaded"));
        typeVoucherMaterial = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.type_voucher_material"));
        addedVoucherEveryone = ChatUtil.chatColor(vouchersPlugin.getConfig().getString("messages.added_voucher_to_everyone"));
    }
}
