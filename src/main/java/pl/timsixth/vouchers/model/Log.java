package pl.timsixth.vouchers.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import pl.timsixth.vouchers.enums.ProcessType;

import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ToString
public class Log {

    private final UUID senderUuid;
    private final String content;
    private final Date creationDate;
    private final ProcessType processType;
}
