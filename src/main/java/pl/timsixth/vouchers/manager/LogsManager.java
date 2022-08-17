package pl.timsixth.vouchers.manager;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.timsixth.vouchers.config.ConfigFile;
import pl.timsixth.vouchers.enums.ProcessType;
import pl.timsixth.vouchers.model.Log;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LogsManager {

    @Getter
    private final List<Log> logs = new ArrayList<>();

    private final ConfigFile configFile;
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm a", Locale.ENGLISH);
    private final SimpleDateFormat onlyDateFormatter = new SimpleDateFormat("dd-M-yyyy", Locale.ENGLISH);

    public void load() {
        List<String> savedLogs = configFile.getYmlLogs().getStringList("logs");
        if (savedLogs.isEmpty()) {
            return;
        }
        formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        savedLogs.forEach(savedLog -> {
            String[] logFromText = savedLog.split(";");
            try {
                Log log = new Log(UUID.fromString(logFromText[0]), logFromText[1], formatter.parse(logFromText[2]), ProcessType.valueOf(logFromText[3]));
                logs.add(log);
            } catch (ParseException e) {
                Bukkit.getLogger().severe(e.getMessage());
            }
        });
    }

    public void addLog(Log log) {
        YamlConfiguration ymlLogs = configFile.getYmlLogs();
        List<String> logsFromConfig = ymlLogs.getStringList("logs");
        formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        logsFromConfig.add(log.getSenderUuid() + ";" + log.getContent() + ";" + formatter.format(log.getCreationDate()) + ";" + log.getProcessType() + ";"
                + Bukkit.getPlayer(log.getSenderUuid()).getName());
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
            String textToRemove = getTextToRemove(log);
            logsFromConfig.remove(textToRemove);
            logs.remove(log);
            ymlLogs.set("logs", logsFromConfig);
            saveFile(ymlLogs);
        });
    }

    private String getTextToRemove(Log log) {
        return log.getSenderUuid() +
                ";" +
                log.getContent() +
                ";" +
                formatter.format(log.getCreationDate()) +
                ";" +
                log.getProcessType().name() +
                ";" +
                Bukkit.getOfflinePlayer(log.getSenderUuid()).getName();
    }

    private List<Date> getCurrentLogsDates() throws ParseException {
        Date currentDate = onlyDateFormatter.parse(onlyDateFormatter.format(new Date()));
        return logs.stream()
                .map(log -> {
                    try {
                        return onlyDateFormatter.parse(onlyDateFormatter.format(log.getCreationDate()));
                    } catch (ParseException e) {
                        Bukkit.getLogger().severe(e.getMessage());
                    }
                    return new Date();
                }).filter(date -> date.equals(currentDate))
                .collect(Collectors.toList());
    }

    public Optional<Log> getLogByDate(Date date) {
        return logs.stream()
                .filter(log -> {
                    try {
                        return onlyDateFormatter.parse(onlyDateFormatter.format(log.getCreationDate())).equals(date);
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
}
