package albertgame.avg.editor.n2;

import albertgame.avg.editor.ConfigCenter;
import javafx.scene.control.ListView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LibRH implements LibResource.Handler {
    final Map<String, LibResource> libResourceMap;
    final ListView<LibResource> lib;
    LibResource selectItem;
    String libName;
    String format;

    public LibRH(ListView<LibResource> lib, String libName, String format) {
        this.lib = lib;
        this.libName = libName;
        libResourceMap = new HashMap<>();
        this.format = format;
        initHandler();
    }

    @Override
    public void initHandler() {
        lib.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
            if (n != null) {
                selectItem = n;
            }
        });
    }

    @Override
    public void addResource(LibResource resource) {
        String id = resource.id;
        libResourceMap.put(id, resource);
        lib.getItems().add(resource);
        //从源位置复制到项目中
        File sourceF = new File(resource.path);
        if (sourceF.exists() && sourceF.isFile()) {
            ConfigCenter.moveFileTo(sourceF, this.libName, resource.id);
        }
    }

    @Override
    public void removeResource(String id) {
        if(id==null)return;

        LibResource resource = libResourceMap.get(id);
        if (resource != null) {
            libResourceMap.remove(id);
            lib.getItems().remove(resource);
        }
        //从项目资源库中移除
        ConfigCenter.removeFile(this.libName, id, this.format);
    }

    @Override
    public void removeSelect() {
        if(selectItem!=null){
            removeResource(selectItem.id);
        }
    }

    @Override
    public void editResource(LibResource resource) {
        if (selectItem != null) {
            if (selectItem.id.equals(resource.id)) {
                selectItem.name = resource.name;
                selectItem.path = resource.path;
            } else {
                System.out.println("You can only edit the Select Item!");
            }
        } else {
            System.err.println("You must Select One Resource!");
        }
    }

    @Override
    public LibResource copySelectItem() {
        if (selectItem == null) {
            return null;
        } else {
            return new LibResource(selectItem.id, selectItem.name,
                    selectItem.path, selectItem.format);
        }
    }

    @Override
    public Map<String, LibResource> allResources() {
        return libResourceMap;
    }
}
