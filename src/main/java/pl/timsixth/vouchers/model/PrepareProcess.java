package pl.timsixth.vouchers.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
@ToString
public class PrepareProcess {

    private final UUID playerUuid;
    private final String localizeName;
}
