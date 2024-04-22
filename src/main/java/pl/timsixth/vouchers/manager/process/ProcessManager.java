package pl.timsixth.vouchers.manager.process;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.manager.LogsManager;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.Process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
public abstract class ProcessManager {

    private final List<Process> processes = new ArrayList<>();

    private final ConfigFile configFile;
    private final VoucherManager voucherManager;
    private final LogsManager logsManager;

    public void cancelProcess(Process process) {
        processes.remove(process);
    }

    public void startProcess(Process process) {
        processes.add(process);
    }

    public Optional<Process> getProcessByUser(UUID userUUID) {
        return processes.stream()
                .filter(process -> process.getUserUUID().equals(userUUID))
                .findAny();
    }

    public abstract void saveProcess(Process process);

    protected void saveVouchersFile() {
        try {
            configFile.getYmlVouchers().save(getConfigFile().getVouchersFile());
        } catch (IOException e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
    }
}
