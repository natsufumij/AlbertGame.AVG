package albertgame.avg.editor;

import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.HashMap;
import java.util.Map;

public abstract class LibAssetH implements LibAsset.Handler {

    private final Map<String, LibAsset> libResourceMap;
    protected LibAsset selectItem;
    String libName;
    String format;

    public LibAssetH(String libName, String format) {
        this.libName = libName;
        libResourceMap = new HashMap<>();
        this.format = format;
    }

    @Override
    public void remove() {
        if (selectItem != null) {
            libResourceMap.remove(selectItem.id);
            C.removeFile(libName, selectItem.id, selectItem.format);
        }
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
    public String format() {
        return this.format;
    }

    @Override
    public Map<String, LibAsset> allResources() {
        return libResourceMap;
    }

    public static class ListH extends LibAssetH {

        private final ListView<LibAsset> lib;

        public ListH(String libName, String format, ListView<LibAsset> lib) {
            super(libName, format);
            this.lib = lib;
            init();
        }

        void init() {
            lib.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
                selectItem = n;
            });
        }

        @Override
        public void create() {

        }

        @Override
        public void edit() {

        }

        @Override
        public void remove() {
            super.remove();
            if (selectItem != null) {
                lib.getItems().remove(selectItem);
            }
            selectItem = null;
        }
    }

    public static class TreeH extends LibAssetH {
        private final TreeView<LibAsset> lib;
        Map<String, TreeItem<LibAsset>> idItemMap;

        public TreeH(String libName, String format, TreeView<LibAsset> lib) {
            super(libName, format);
            this.lib = lib;
            idItemMap = new HashMap<>();
            init();
        }

        void init() {
            lib.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
                if (n != null) {
                    selectItem = n.getValue();
                }else {
                    selectItem=null;
                }
            });
        }

        @Override
        public void create() {

        }

        @Override
        public void edit() {

        }

        @Override
        public void remove() {
            super.remove();
            if (selectItem != null) {
                TreeItem<LibAsset> item = idItemMap.get(selectItem.id);
                if (item != null && item.getParent() != null) {
                    TreeItem<LibAsset> parent = item.getParent();
                    parent.getChildren().remove(item);
                }
            }
            selectItem = null;
        }
    }
}
