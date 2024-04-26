package pl.timsixth.vouchers.manager;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.enums.ProcessType;
import pl.timsixth.vouchers.model.Log;
import pl.timsixth.vouchers.model.Process;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static pl.timsixth.vouchers.model.Log.LOG_DATE_FORMATTER;
import static pl.timsixth.vouchers.model.Log.LOG_DATE_TIME_FORMATTER;

@Getter
@RequiredArgsConstructor
public class LogsManager {

    private final List<Log> logs = new ArrayList<>();

    private final ConfigFile configFile;

    public void load() {
        List<String> savedLogs = configFile.getYmlLogs().getStringList("logs");

        if (savedLogs.isEmpty()) return;

        savedLogs.forEach(savedLog -> {
            String[] logFromText = savedLog.split(";");
            Log log = new Log(UUID.fromString(logFromText[0]), logFromText[1], LocalDateTime.parse(logFromText[2], LOG_DATE_TIME_FORMATTER), ProcessType.valueOf(logFromText[3]));
            logs.add(log);
        });
    }

    private void saveLog(Log log) {
        YamlConfiguration ymlLogs = configFile.getYmlLogs();
        List<String> logsAsString = ymlLogs.getStringList("logs");

        logsAsString.add(log.toText());
        logs.add(log);

        setLogs(ymlLogs, logsAsString);
    }

    private void saveFile(YamlConfiguration ymlLogs) {
        try {
            ymlLogs.save(configFile.getLogsFile());
        } catch (IOException e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
    }

    public void clearAllTodayLogs() {
        YamlConfiguration ymlLogs = configFile.getYmlLogs();
        List<String> logsAsString = ymlLogs.getStringList("logs");
        List<LocalDate> dates = getCurrentLogsDates();

        dates.forEach(date -> {
            Optional<Log> optionalLog = getLog(date);
            if (!optionalLog.isPresent()) return;

            Log log = optionalLog.get();

            logsAsString.remove(log.toText());
            logs.remove(log);

            setLogs(ymlLogs, logsAsString);
        });
    }

    private void setLogs(YamlConfiguration ymlLogs, List<String> logs) {
        ymlLogs.set("logs", logs);
        saveFile(ymlLogs);
    }

    private List<LocalDate> getCurrentLogsDates() {
        return logs.stream()
                .map(log -> LocalDate.parse(log.getCreationDate().toLocalDate().format(Log.LOG_DATE_FORMATTER), LOG_DATE_FORMATTER))
                .filter(date -> date.equals(LocalDate.parse(LocalDate.now().format(Log.LOG_DATE_FORMATTER), LOG_DATE_FORMATTER)))
                .collect(Collectors.toList());
    }

    private Optional<Log> getLog(LocalDate date) {
        return logs.stream()
                .filter(log -> log.getCreationDate().toLocalDate().isEqual(date))
                .findAny();
    }

    public void log(Process process) {
        String message = String.format(Log.LogMessages.matchMessage(process.getType()), process.getCurrentVoucher().getName());
        Log log = new Log(process.getUserUUID(), message, process.getType());

        saveLog(log);
    }
}
