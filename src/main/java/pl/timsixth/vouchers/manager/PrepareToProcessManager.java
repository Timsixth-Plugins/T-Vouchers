package pl.timsixth.vouchers.manager;

import lombok.Getter;
import pl.timsixth.vouchers.model.PrepareToProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PrepareToProcessManager {
    @Getter
    private final List<PrepareToProcess> prepareToProcessList = new ArrayList<>();

    public void addNewLocalizedName(PrepareToProcess prepareToProcess) {
        prepareToProcessList.add(prepareToProcess);
    }

    public void removeLocalizedName(PrepareToProcess prepareToProcess) {
        prepareToProcessList.remove(prepareToProcess);
    }

    public PrepareToProcess getPrepareToProcess(UUID uuid) {
        return prepareToProcessList
                .stream().filter(prepareToProcess -> prepareToProcess.getPlayerUuid().equals(uuid))
                .findAny()
                .orElse(null);
    }
}
