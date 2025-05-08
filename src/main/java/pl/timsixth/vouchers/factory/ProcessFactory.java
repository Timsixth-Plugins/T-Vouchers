package pl.timsixth.vouchers.factory;

import pl.timsixth.vouchers.enums.ProcessType;
import pl.timsixth.vouchers.model.Process;

import java.util.UUID;

public final class ProcessFactory {

    private static Process createProcess(UUID userUUID, ProcessType type) {
        return new Process(userUUID, type);
    }

    public static Process createEditionProcess(UUID userUUID) {
        return createProcess(userUUID, ProcessType.EDIT);
    }

    public static Process createCreationProcess(UUID userUUID) {
        return createProcess(userUUID, ProcessType.CREATE);
    }

    public static Process createDelationProcess(UUID userUUID) {
        return createProcess(userUUID, ProcessType.DELETE);
    }
}
