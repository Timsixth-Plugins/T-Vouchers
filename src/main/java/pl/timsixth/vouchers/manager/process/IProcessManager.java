package pl.timsixth.vouchers.manager.process;

import org.bukkit.entity.Player;
import pl.timsixth.vouchers.model.process.IProcess;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IProcessManager<T extends IProcess> {

    void cancelProcess(T process);
    void startProcess(T process);
    List<T> getCurrentProcesses();

    T getProcessByUser(UUID userUUID);

    boolean isProcessedByUser(T process, Player player);

    void saveProcess(T process) throws IOException;
}
