package pl.timsixth.vouchers.model.discord;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URL;
@AllArgsConstructor
@Getter
public class EmbedAuthor {

    private String name;
    private URL iconURL;

    public String getName() {
        return name == null ? "" : name;
    }
}
