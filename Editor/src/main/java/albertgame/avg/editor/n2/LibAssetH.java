package albertgame.avg.editor.n2;

import javafx.scene.control.ListView;

import java.util.HashMap;
import java.util.Map;

public class LibAssetH implements LibAsset.Handler {
    final Map<String, LibAsset> libResourceMap;
    final ListView<LibAsset> lib;
    LibAsset selectItem;
    String libName;
    String format;

    public LibAssetH(ListView<LibAsset> lib, String libName, String format) {
        this.lib = lib;
        this.libName = libName;
        libResourceMap = new HashMap<>();
        this.format = format;
        init();
    }

    @Override
    public void init() {
        lib.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
            if (n != null) {
                selectItem = n;
            }
        });
    }

    @Override
    public void create() {
        //TODO: 创建一个资源
    }

    @Override
    public void edit() {
        //TODO: 编辑选中的资源
    }

    @Override
    public void remove() {
        //TODO: 移除选中的资源
    }

    @Override
    public LibAsset copySelectItem() {
        if (selectItem == null) {
            return null;
        } else {
            return new LibAsset(selectItem.id, selectItem.name,
                    selectItem.path, selectItem.format);
        }
    }

    @Override
    public Map<String, LibAsset> allResources() {
        return libResourceMap;
    }
}
