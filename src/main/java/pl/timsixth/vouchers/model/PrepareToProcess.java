package pl.timsixth.vouchers.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
@ToString
public class PrepareToProcess {

    private final UUID playerUuid;
    private final String localizeName;
}
