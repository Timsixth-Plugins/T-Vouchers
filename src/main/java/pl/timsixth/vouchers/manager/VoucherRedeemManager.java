package pl.timsixth.vouchers.manager;

import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.model.RedeemVoucher;
import pl.timsixth.vouchers.model.Voucher;
import pl.timsixth.vouchers.util.ConfigurationSectionUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VoucherRedeemManager {

    private final ConfigFile configFile;

    private final Map<UUID, List<RedeemVoucher>> redeemsVouchers = new HashMap<>();

    public void load() {
        YamlConfiguration ymlVoucherRedeems = configFile.getYmlVoucherRedeems();
        ConfigurationSection mainSection = ConfigurationSectionUtil.getSection(ymlVoucherRedeems, "vouchers_redeems");

        for (String uuid : mainSection.getKeys(false)) {
            ConfigurationSection uuidSection = ymlVoucherRedeems.getConfigurationSection(uuid);
            Map<String, Object> values = uuidSection.getValues(false);

            List<RedeemVoucher> redeemVouchers = values.entrySet().stream()
                    .map(entry -> new RedeemVoucher(entry.getKey(), (int) entry.getValue()))
                    .collect(Collectors.toList());

            redeemsVouchers.put(UUID.fromString(uuid), redeemVouchers);
        }
    }

    public void addRedeemVoucher(Player player, Voucher voucher) {
        if (voucher.canRedeemAllTheTime()) return;

        String voucherName = voucher.getName();

        List<RedeemVoucher> redeemVouchers = getOrCreateRedeemVouchers(player.getUniqueId());

        if (!redeemsVouchers.containsKey(player.getUniqueId())) {
            redeemVouchers.add(new RedeemVoucher(voucherName));
        } else {
            Optional<RedeemVoucher> redeemVoucherOptional = getRedeemVoucher(player, voucherName);

            if (redeemVoucherOptional.isPresent()) {
                RedeemVoucher redeemVoucher = redeemVoucherOptional.get();

                redeemVoucher.addRedeem();
            } else {
                redeemVouchers.add(new RedeemVoucher(voucherName));
            }
        }

        redeemsVouchers.put(player.getUniqueId(), redeemVouchers);

        saveVouchersRedeems(player.getUniqueId(), redeemVouchers);
    }

    private void saveVouchersRedeems(UUID playerUUID, List<RedeemVoucher> redeemsVouchers) {
        YamlConfiguration ymlVoucherRedeems = configFile.getYmlVoucherRedeems();

        ConfigurationSection mainSection = ConfigurationSectionUtil.getSection(ymlVoucherRedeems, "vouchers_redeems");
        ConfigurationSection uuidSection = ConfigurationSectionUtil.getSection(mainSection, playerUUID.toString());

        for (RedeemVoucher redeemVoucher : redeemsVouchers) {
            uuidSection.set(redeemVoucher.getVoucherName(), redeemVoucher.getRedeemTimes());
        }

        save(ymlVoucherRedeems);
    }

    public OptionalInt getRedeemTimesForVoucher(Player player, Voucher voucher) {
        if (voucher.canRedeemAllTheTime()) return OptionalInt.empty();

        List<RedeemVoucher> redeemVouchers = getOrCreateRedeemVouchers(player.getUniqueId());

        if (redeemVouchers.isEmpty()) return OptionalInt.empty();

        return redeemVouchers.stream()
                .filter(redeemVoucher -> redeemVoucher.getVoucherName().equalsIgnoreCase(voucher.getName()))
                .mapToInt(RedeemVoucher::getRedeemTimes)
                .findAny();
    }

    private Optional<RedeemVoucher> getRedeemVoucher(Player player, String voucherName) {
        List<RedeemVoucher> redeemVouchers = getOrCreateRedeemVouchers(player.getUniqueId());

        if (redeemVouchers.isEmpty()) return Optional.empty();

        return redeemVouchers.stream()
                .filter(redeemVoucher -> redeemVoucher.getVoucherName().equalsIgnoreCase(voucherName))
                .findAny();
    }

    private List<RedeemVoucher> getOrCreateRedeemVouchers(UUID playerUUID) {
        List<RedeemVoucher> redeemVouchers = redeemsVouchers.get(playerUUID);

        return redeemVouchers == null ? new ArrayList<>() : redeemVouchers;
    }

    private void save(YamlConfiguration yml) {
        try {
            yml.save(configFile.getVoucherRedeemsFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
