package pl.timsixth.vouchers.manager;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.enums.ProcessType;
import pl.timsixth.vouchers.model.Log;
import pl.timsixth.vouchers.model.process.IProcess;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static pl.timsixth.vouchers.model.Log.LOG_DATE_TIME_FORMATTER;

@Getter
@RequiredArgsConstructor
public class LogsManager {

    private final List<Log> logs = new ArrayList<>();

    private final ConfigFile configFile;
    private final SimpleDateFormat logDateFormatter = new SimpleDateFormat("dd-M-yyyy", Locale.ENGLISH);

    public void load() {
        List<String> savedLogs = configFile.getYmlLogs().getStringList("logs");
        if (savedLogs.isEmpty()) {
            return;
        }
        savedLogs.forEach(savedLog -> {
            String[] logFromText = savedLog.split(";");
            try {
                Log log = new Log(UUID.fromString(logFromText[0]), logFromText[1], LOG_DATE_TIME_FORMATTER.parse(logFromText[2]), ProcessType.valueOf(logFromText[3]));
                logs.add(log);
            } catch (ParseException e) {
                Bukkit.getLogger().severe(e.getMessage());
            }
        });
    }

    private void saveLog(Log log) {
        YamlConfiguration ymlLogs = configFile.getYmlLogs();
        List<String> logsFromConfig = ymlLogs.getStringList("logs");
        logsFromConfig.add(log.toText());
        ymlLogs.set("logs", logsFromConfig);
        logs.add(log);
        saveFile(ymlLogs);
    }

    private void saveFile(YamlConfiguration ymlLogs) {
        try {
            ymlLogs.save(configFile.getLogsFile());
        } catch (IOException e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
    }

    public void clearAllLogsByCurrentDate() throws ParseException {
        YamlConfiguration ymlLogs = configFile.getYmlLogs();
        List<String> logsFromConfig = ymlLogs.getStringList("logs");
        List<Date> dates = getCurrentLogsDates();
        if (dates.isEmpty()) {
            return;
        }
        dates.forEach(date -> {
            Optional<Log> optionalLog = getLogByDate(date);
            if (!optionalLog.isPresent()) {
                return;
            }
            Log log = optionalLog.get();
            String textToRemove = log.toText();
            logsFromConfig.remove(textToRemove);
            logs.remove(log);
            ymlLogs.set("logs", logsFromConfig);
            saveFile(ymlLogs);
        });
    }

    private List<Date> getCurrentLogsDates() throws ParseException {
        Date currentDate = logDateFormatter.parse(logDateFormatter.format(new Date()));
        return logs.stream()
                .map(log -> {
                    try {
                        return logDateFormatter.parse(logDateFormatter.format(log.getCreationDate()));
                    } catch (ParseException e) {
                        Bukkit.getLogger().severe(e.getMessage());
                    }
                    return new Date();
                }).filter(date -> date.equals(currentDate))
                .collect(Collectors.toList());
    }

    private Optional<Log> getLogByDate(Date date) {
        return logs.stream()
                .filter(log -> {
                    try {
                        return logDateFormatter.parse(logDateFormatter.format(log.getCreationDate())).equals(date);
                    } catch (ParseException e) {
                        Bukkit.getLogger().severe(e.getMessage());
                    }
                    return false;
                })
                .findAny();
    }

    public List<Log> getLogsToShowInGui() {
        return logs.stream()
                .limit(53)
                .collect(Collectors.toList());

    }

    public void log(IProcess process, ProcessType processType) {
        String message = String.format(Log.LogMessages.matchMessage(processType), process.getCurrentVoucher().getName());
        Log log = new Log(process.getUserUuid(), message, processType);

        saveLog(log);
    }
}
