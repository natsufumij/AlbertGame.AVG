package albertgame.avg.editor;

import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class LibAssetH implements LibAsset.Handler {

    protected final Map<String, LibAsset> libResourceMap;
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
        private int index;

        public ListH(String libName, String format, ListView<LibAsset> lib) {
            super(libName, format);
            this.lib = lib;
            init();
            index = -1;
        }

        void init() {
            lib.getSelectionModel().selectedIndexProperty().addListener((v, o, n) -> {
                if (n != null && n.intValue() != -1) {
                    index = n.intValue();
                    selectItem = lib.getItems().get(n.intValue());
                }
            });
        }

        @Override
        public void create() {
            Dialog<LibAsset> dialog = LibAssetHelp.createLibAsset(this.libName, this.format, true);
            Optional<LibAsset> asset = dialog.showAndWait();
            asset.ifPresent(asset1 -> {
                lib.getItems().add(asset1);
                libResourceMap.put(asset1.id, asset1);
                C.moveFileTo(asset1.path, this.libName, asset1.id);
            });
        }

        @Override
        public void edit() {
            if (selectItem != null) {
                LibAsset source = copySelectItem();
                Dialog<LibAsset> dialog = LibAssetHelp.editLibAsset(this.libName, source, true);
                Optional<LibAsset> asset = dialog.showAndWait();
                asset.ifPresent(asset1 -> {
                    lib.getItems().remove(index);
                    lib.getItems().add(index, asset1);
                    libResourceMap.replace(asset1.id, asset1);
                });
            }
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
        Map<String, TreeItem<LibAsset>> chapterItems;
        TreeItem<LibAsset> global;

        TreeItem<LibAsset> selectTreeItem;
        int index;

        public TreeH(String libName, String format, TreeView<LibAsset> lib) {
            super(libName, format);
            this.lib = lib;
            chapterItems = new HashMap<>();
            init();
        }

        void init() {
            global = new TreeItem<>(new LibAsset("global", "总配置", "global", "avg"));
            lib.setRoot(global);
            lib.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
                if (n != null) {
                    selectItem = n.getValue();
                    selectTreeItem = n;
                    if (selectTreeItem.getParent() != null) {
                        index = selectTreeItem.getParent().getChildren().indexOf(selectTreeItem);
                    }
                } else {
                    selectItem = null;
                    selectTreeItem = null;
                    index = -1;
                }
            });
        }

        @Override
        public void create() {
            Dialog<LibAsset> dialog = LibAssetHelp.createLibAsset(this.libName, this.format, false);
            Optional<LibAsset> asset = dialog.showAndWait();
            asset.ifPresent(asset1 -> {
                libResourceMap.put(asset1.id, asset1);
                addAsset(asset1);
            });
        }

        private void addAsset(LibAsset asset) {
            String id = asset.id;
            //如果包含这个，则是剧本
            if (id.contains("/")) {
                String[] s = id.split("/");
                String cId = s[0];
                if (chapterItems.containsKey(cId)) {
                    TreeItem<LibAsset> item = chapterItems.get(cId);
                    //选择的item的父item是需要插入的章节，则再选择的位置后插入
                    if (selectTreeItem != null && selectTreeItem.getParent() == item) {
                        item.getChildren().add(index + 1, new TreeItem<>(asset));
                    } else {
                        item.getChildren().add(new TreeItem<>(asset));
                    }
                }
            }
            //其他的，都是章节
            else if (!id.equals("global")) {
                TreeItem<LibAsset> chapter = new TreeItem<>(asset);
                if (selectTreeItem != null && selectTreeItem.getParent() == global) {
                    global.getChildren().add(index + 1, chapter);
                } else {
                    global.getChildren().add(chapter);
                }
                chapterItems.put(asset.id, chapter);
            }
        }

        @Override
        public void edit() {
            if (selectItem != null) {
                LibAsset source = copySelectItem();
                Dialog<LibAsset> dialog = LibAssetHelp.editLibAsset(this.libName, source, false);
                Optional<LibAsset> asset = dialog.showAndWait();
                asset.ifPresent(asset1 -> {
                    libResourceMap.replace(asset1.id, asset1);
                    editAsset(asset1);
                });
            }
        }

        private void editAsset(LibAsset asset) {
            String id = asset.id;
            //如果包含这个，则是剧本
            if (id.contains("/")) {
                String[] s = id.split("/");
                String cId = s[0];
                if (chapterItems.containsKey(cId)) {
                    TreeItem<LibAsset> item = chapterItems.get(cId);
                    //选择的位置上，删除原来的，用新的覆盖
                    item.getChildren().remove(index);
                    item.getChildren().add(index, new TreeItem<>(asset));
                }
            }
            //其他的，都是章节
            else if (!id.equals("global")) {
                TreeItem<LibAsset> item=chapterItems.get(asset.id);
                if(item!=null){
                    item.setValue(asset);
                }
            }
        }

        @Override
        public void remove() {
            super.remove();
            if (selectItem != null) {
                TreeItem<LibAsset> item = selectTreeItem;
                if (item != null && item.getParent() != null) {
                    TreeItem<LibAsset> parent = item.getParent();
                    parent.getChildren().remove(item);
                }
            }
            selectItem = null;
            selectTreeItem = null;
        }
    }
}
