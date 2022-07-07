package pl.timsixth.vouchers.model.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

@RequiredArgsConstructor
@Getter
public class EmptySlotFilling {

    private final int emptySlotId;
    private final Material emptySlotMaterial;

}
