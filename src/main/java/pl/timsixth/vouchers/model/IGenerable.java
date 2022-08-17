package pl.timsixth.vouchers.model;

import pl.timsixth.vouchers.model.menu.MenuItem;

public interface IGenerable {

    MenuItem getGeneratedItem(int slot);
}
