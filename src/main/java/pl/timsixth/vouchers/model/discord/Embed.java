package pl.timsixth.vouchers.model.discord;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URL;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Embed {

    private String title;
    private String description;
    private EmbedAuthor author;
    private LocalDateTime timestamp;
    private int color; //int color representation, converted from HEX
    private URL imageURL;
    private URL thumbnailURL;
    private URL footerIconURL;

    public String getDescription() {
        return description == null ? "" : description;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }
}
