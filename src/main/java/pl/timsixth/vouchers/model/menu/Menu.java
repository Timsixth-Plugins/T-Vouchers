package pl.timsixth.vouchers.model.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@RequiredArgsConstructor
@Getter
@ToString
public class Menu {

    private final int size;
    private final String name;
    private final String displayName;
    private EmptySlotFilling emptySlotFilling;
    @Setter
    private Set<MenuItem> items;

    public Menu(int size, String name, String displayName, EmptySlotFilling emptySlotFilling) {
        this.size = size;
        this.name = name;
        this.displayName = displayName;
        this.emptySlotFilling = emptySlotFilling;
    }
}
