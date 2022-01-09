package albertgame.avg.editor;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import albertgame.avg.editor.FormController.StoryView;

import java.util.Set;


public interface StoryViewEdit {
    GridPane create(StoryView view, ReadOnlyObjectProperty<String> typeSelectItem);

    class PersonViewEdit implements StoryViewEdit {
        @Override
        public GridPane create(StoryView view, ReadOnlyObjectProperty<String> typeSelectItem) {
            GridPane pane = new GridPane();
            pane.setHgap(5.0);
            pane.setVgap(5.0);
            Label dataIdL = new Label("Person");
            ChoiceBox<String> personSelect = new ChoiceBox<>();
            Label pos = new Label("Position");
            ChoiceBox<String> posC = new ChoiceBox<>();
            posC.getItems().addAll("Left", "Center", "Right");
            Label state = new Label("New State");
            TextField stateF = new TextField();

            pane.add(dataIdL, 0, 0);
            pane.add(personSelect, 1, 0);
            pane.add(pos, 0, 1);
            pane.add(posC, 1, 1);
            pane.add(state, 0, 2);
            pane.add(stateF, 1, 2);

            Node[] list = new Node[]{dataIdL, personSelect, pos, posC, state, stateF};

            personSelect.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
                //Person  In  Data
                if (typeSelectItem.get().equals("In") && n != null) {
                    FormController.PersonC c = FormController.get().getPersonMap().get(n);
                    view.data[0] = c.id;
                }
            });

            stateF.textProperty().addListener((v, o, n) -> {
                if (typeSelectItem.get().equals("Change.State") && n != null) {
                    view.data[1] = n;
                }
            });

            posC.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
                if (n != null) {
                    if (!typeSelectItem.get().equals("In") && !typeSelectItem.get().equals("Out")) {
                        view.data[0] = n;
                    }
                }
            });

            typeSelectItem.addListener((v, o, n) -> {
                if (n != null) {
                    view.name = n;
                    switch (view.name) {
                        case "In":
                        case "Out":
                            enable(new int[]{0, 1}, list);
                            break;
                        case "No.Show":
                        case "Hide":
                            enable(new int[]{2, 3}, list);
                            break;
                        case "Show":
                            disable(new int[]{4, 5}, list);
                            break;
                        case "Change.State":
                            disable(new int[]{0, 1}, list);
                            break;
                    }
                }
            });

            return pane;
        }
    }

    class StorageViewEdit implements StoryViewEdit {

        @Override
        public GridPane create(StoryView view, ReadOnlyObjectProperty<String> typeSelectItem) {
            GridPane pane = new GridPane();
            pane.setHgap(5.0);
            pane.setVgap(5.0);
            Label nameL = new Label("Key");
            TextField textField = new TextField();
            Label value = new Label("Value");
            TextField textField1 = new TextField();
            pane.add(nameL, 0, 0);
            pane.add(textField, 1, 0);
            pane.add(value, 0, 1);
            pane.add(textField1, 1, 1);
            textField.textProperty().addListener((v, o, n) -> {
                if (n != null) {
                    StoryView view1 = FormController.get().getNowEditExpression();
                    view1.data[0] = n;
                }
            });
            textField1.textProperty().addListener((v, o, n) -> {
                if (n != null) {
                    StoryView view1 = FormController.get().getNowEditExpression();
                    view1.data[1] = n;
                }
            });

            return pane;
        }
    }

    class AudioViewEdit implements StoryViewEdit {

        @Override
        public GridPane create(StoryView view, ReadOnlyObjectProperty<String> typeSelectItem) {
            GridPane pane = new GridPane();
            pane.setHgap(5.0);
            pane.setVgap(5.0);
            Label nameL = new Label("Name");
            ChoiceBox<String> choice = new ChoiceBox<>();
            pane.add(nameL,0,0);
            pane.add(choice,1,0);
            choice.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
                if (n != null) {
                    view.data[0] = n;
                }
            });

            typeSelectItem.addListener((v, o, n) -> {
                if (n != null) {
                    view.name = n;
                    if (n.equals("Bgm.Play")) {
                        Set<String> bgmS = FormController.get().getBgmMap().keySet();
                        choice.getItems().clear();
                        choice.getItems().addAll(bgmS);
                        nameL.setVisible(true);
                        choice.setVisible(true);
                    } else if (n.equals("Sound.Play")) {
                        Set<String> soundS = FormController.get().getAudioMap().keySet();
                        choice.getItems().clear();
                        choice.getItems().addAll(soundS);
                        nameL.setVisible(true);
                        choice.setVisible(true);
                    }
                }
            });

            return pane;
        }
    }

    class SelectViewEdit implements StoryViewEdit {
        @Override
        public GridPane create(StoryView view, ReadOnlyObjectProperty<String> typeSelectItem) {
            GridPane pane = new GridPane();
            pane.setHgap(5.0);
            pane.setVgap(5.0);

            Label selectL = new Label("Select Id");
            TextField selectId = new TextField();
            Label sizeL = new Label("Size");
            ChoiceBox<Integer> sizeC = new ChoiceBox<>();

            pane.add(selectId,0,0);
            pane.add(selectId,1,0);
            pane.add(sizeL,0,1);
            pane.add(sizeC,1,1);

            TextField[] fields = new TextField[5];
            for (int i = 0; i != fields.length; ++i) {
                TextField textField = new TextField();
                textField.setUserData(i);
                fields[i] = textField;
                pane.add(textField,0,2+i,2,1);
                textField.textProperty().addListener((v,o,n)->{
                    if(n!=null){
                        int index= (int) textField.getUserData();
                        view.data[index]=n;
                    }
                });
            }
            sizeC.getItems().addAll(1, 2, 3, 4, 5);
            sizeC.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
                if (n != null) {
                    enable(new int[]{},fields);
                    for (int i = 0; i < n; ++i) {
                        fields[i].setVisible(true);
                    }
                }
            });

            return pane;
        }
    }


    static void enable(int[] enables, Node[] list) {
        for (Node node : list) {
            node.setVisible(false);
        }
        for (int index : enables) {
            list[index].setVisible(true);
        }
    }

    static void disable(int[] disables, Node[] list) {
        for (Node node : list) {
            node.setVisible(true);
        }
        for (int index : disables) {
            list[index].setVisible(false);
        }
    }
}
