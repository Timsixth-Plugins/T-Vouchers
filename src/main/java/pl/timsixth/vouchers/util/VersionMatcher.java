package pl.timsixth.vouchers.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

@Getter
public final class VersionMatcher {

    private static VersionMatcher instance;

    private final Version currentVersion;

    private VersionMatcher() {
        String version = Bukkit.getBukkitVersion().split("-")[0];

        currentVersion = getVersion(version);
    }

    public static VersionMatcher getInstance() {
        if (instance == null) instance = new VersionMatcher();

        return instance;
    }

    public boolean isSupported(String versionAsString) {
        Version version = getVersion(versionAsString);

        return (version.major >= currentVersion.major && version.mirror >= currentVersion.mirror && version.patch >= currentVersion.patch) ||
                (version.major >= currentVersion.major && version.mirror >= currentVersion.mirror)
                || (version.major >= currentVersion.major);
    }

    public Version getVersion(String version) {
        String[] versionAsArray = version.split("\\.");

        if (versionAsArray.length == 2) {
            return new Version(Integer.parseInt(versionAsArray[0]), Integer.parseInt(versionAsArray[1]));
        }

        return new Version(Integer.parseInt(versionAsArray[0]), Integer.parseInt(versionAsArray[1], Integer.parseInt(versionAsArray[2])));
    }

    @RequiredArgsConstructor
    @Getter
    public static class Version {
        private final int major;
        private final int mirror;
        private int patch;

        public Version(int major, int mirror, int patch) {
            this.major = major;
            this.mirror = mirror;
            this.patch = patch;
        }
    }

}
