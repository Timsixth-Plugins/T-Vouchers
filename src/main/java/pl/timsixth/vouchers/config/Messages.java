package pl.timsixth.vouchers.config;

import lombok.Getter;
import pl.timsixth.vouchers.VouchersPlugin;
import pl.timsixth.vouchers.util.ChatUtil;

@Getter
public class Messages {

    private final String noPermission;
    private final String correctUse;
    private final String addedVoucher;
    private final String voucherDoesntExists;
    private final String offlinePlayer;
    private final String addedVoucherToOtherPlayer;
    private final String createdVoucher;

    private final String updatedVoucher;

    private final String deletedVoucher;

    private final String typeVoucherName;

    private final String typeVoucherDisplayName;

    private final String cancelProcess;

    private final String voucherAlreadyExists;
    private final String typeVoucherLore;

    private final String typeVoucherCommand;
    private final String setVoucherEnchants;

    private final String invalidFormatOfName;
    private final String clearAllTodayLogs;

    public Messages(VouchersPlugin vouchersPlugin) {
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
    }
}
