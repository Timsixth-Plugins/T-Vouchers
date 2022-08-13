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

    public void load() {
        List<String> savedLogs = configFile.getYmlLogs().getStringList("logs");
        if (savedLogs.isEmpty()) {
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm a", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        savedLogs.forEach(savedLog -> {
            String[] logFromText = savedLog.split(";");
            try {
                Log log = new Log(UUID.fromString(logFromText[0]), logFromText[1], formatter.parse(logFromText[2]), ProcessType.valueOf(logFromText[3]));
                logs.add(log);
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public void addLog(Log log) {
        YamlConfiguration ymlLogs = configFile.getYmlLogs();
        List<String> logsFromConfig = ymlLogs.getStringList("logs");
        logsFromConfig.add(log.getSenderUuid() + ";" + log.getContent() + ";" + log.getCreationDate() + ";" + log.getProcessType()+";"
                + Bukkit.getPlayer(log.getSenderUuid()).getName());
        ymlLogs.set("logs",logsFromConfig);
        logs.add(log);
        try {
            ymlLogs.save(configFile.getLogsFile());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Log> getLogsByUserUuid(UUID uuid) {
        return logs.stream()
                .filter(log -> log.getSenderUuid().equals(uuid))
                .collect(Collectors.toList());
    }
}
