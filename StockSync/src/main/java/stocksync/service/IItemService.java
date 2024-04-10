package stocksync.service;

import java.util.List;
import stocksync.model.Item;

public interface IItemService {
    public abstract void createItem(Item newIt);
    public abstract List<Item> getItems(int page, String sortBy, String sortMethod, String searchKey, String searchValue);
}
