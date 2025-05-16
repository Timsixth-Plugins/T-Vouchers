package pl.timsixth.vouchers.model.discord;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URL;

@AllArgsConstructor
@Getter
public class Webhook {

    private final URL url;
    private final String name;
    private URL avatarUrl;
}
