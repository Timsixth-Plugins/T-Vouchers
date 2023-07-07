package pl.timsixth.vouchers.manager;

import lombok.Getter;
import pl.timsixth.vouchers.model.PrepareProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PrepareProcessManager {
    @Getter
    private final List<PrepareProcess> prepareToProcessList = new ArrayList<>();

    public void addNewLocalizedName(PrepareProcess prepareToProcess) {
        prepareToProcessList.add(prepareToProcess);
    }

    public void removeLocalizedName(PrepareProcess prepareToProcess) {
        prepareToProcessList.remove(prepareToProcess);
    }

    public PrepareProcess getPrepareToProcess(UUID uuid) {
        return prepareToProcessList
                .stream().filter(prepareToProcess -> prepareToProcess.getPlayerUuid().equals(uuid))
                .findAny()
                .orElse(null);
    }
}
