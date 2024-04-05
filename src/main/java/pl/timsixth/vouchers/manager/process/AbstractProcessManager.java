package pl.timsixth.vouchers.manager.process;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.manager.LogsManager;
import pl.timsixth.vouchers.manager.VoucherManager;
import pl.timsixth.vouchers.model.process.IProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
public abstract class AbstractProcessManager<T extends IProcess> implements IProcessManager<T> {

    private final List<T> processes = new ArrayList<>();

    private final ConfigFile configFile;
    private final VoucherManager voucherManager;
    private final LogsManager logsManager;

    @Override
    public void cancelProcess(T process) {
        processes.remove(process);
    }

    @Override
    public void startProcess(T process) {
        processes.add(process);
    }

    @Override
    public T getProcessByUser(UUID userUUID) {
        return processes.stream()
                .filter(process -> process.getUserUuid().equals(userUUID))
                .findAny().orElse(null);
    }

    @Override
    public boolean isProcessedByUser(T process, Player player) {
        return getProcessByUser(player.getUniqueId()) != null;
    }
}
