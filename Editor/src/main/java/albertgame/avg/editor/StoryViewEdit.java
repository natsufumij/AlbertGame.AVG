package albertgame.avg.editor;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import albertgame.avg.editor.FormController.StoryView;

import java.util.Set;


public interface StoryViewEdit {
    GridPane create(StoryView view, ReadOnlyObjectProperty<String> typeSelectItem);

    String[] names = new String[]{"Person", "Storage", "Audio", "Select", "View", "Dialog"};
    StoryViewEdit[] views = new StoryViewEdit[]{
            new PersonViewEdit(), new StorageViewEdit(), new AudioViewEdit(),
            new SelectViewEdit(), new ViewViewEdit(), new DialogViewEdit()};

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
                    switch (typeSelectItem.get()) {
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

            if (FormController.get().isEditMode()) {
                StoryView newS = FormController.get().getNewExpression();
                switch (newS.name) {
                    case "In":
                    case "Out":
                        String personName = newS.data[0];
                        personSelect.setValue(personName);
                        break;
                    case "No.Show":
                    case "Hide":
                        String loc = newS.data[0];
                        if (loc.equals("L")) {
                            posC.setValue("Left");
                        } else if (loc.equals("C")) {
                            posC.setValue("Center");
                        } else {
                            posC.setValue("Right");
                        }
                        break;
                    case "Show":
                        String loc2 = newS.data[0];
                        String dataName = newS.data[1];
                        if (loc2.equals("L")) {
                            posC.setValue("Left");
                        } else if (loc2.equals("C")) {
                            posC.setValue("Center");
                        } else {
                            posC.setValue("Right");
                        }
                        personSelect.setValue(dataName);
                        disable(new int[]{4, 5}, list);
                        break;
                    case "Change.State":
                        String loc3 = newS.data[0];
                        String stateName = newS.data[1];
                        if (loc3.equals("L")) {
                            posC.setValue("Left");
                        } else if (loc3.equals("C")) {
                            posC.setValue("Center");
                        } else {
                            posC.setValue("Right");
                        }
                        stateF.setText(stateName);
                        disable(new int[]{0, 1}, list);
                        break;
                }
            }
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
                    view.data[0] = n;
                }
            });
            textField1.textProperty().addListener((v, o, n) -> {
                if (n != null) {
                    view.data[1] = n;
                }
            });

            if (FormController.get().isEditMode()) {
                StoryView view1 = FormController.get().getNewExpression();
                textField.setText(view1.data[0]);
                textField1.setText(view1.data[1]);
            }

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
            pane.add(nameL, 0, 0);
            pane.add(choice, 1, 0);
            choice.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
                if (n != null && !n.isBlank()) {
                    view.data[0] = n;
                    System.out.println("Select Audio:"+n);
                }
            });

            typeSelectItem.addListener((v, o, n) -> {
                if (n != null) {
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

            if (FormController.get().isEditMode()) {
                StoryView view1 = FormController.get().getNewExpression();
                if (view1.name.equals("Sound.Play") || view1.name.equals("Bgm.Play")) {
                    String name = view1.data[0];
                    choice.setValue(name);
                }
            }
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

            pane.add(selectL, 0, 0);
            pane.add(selectId, 1, 0);
            pane.add(sizeL, 0, 1);
            pane.add(sizeC, 1, 1);

            TextField[] fields = new TextField[5];
            for (int i = 0; i != fields.length; ++i) {
                TextField textField = new TextField();
                textField.setUserData(i);
                fields[i] = textField;
                pane.add(textField, 0, 2 + i, 2, 1);
                textField.textProperty().addListener((v, o, n) -> {
                    if (n != null) {
                        int index = (int) textField.getUserData();
                        view.data[1 + index] = n;
                    }
                });
            }
            sizeC.getItems().addAll(1, 2, 3, 4, 5);
            sizeC.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
                if (n != null) {
                    enable(new int[]{}, fields);
                    for (int i = 0; i < n; ++i) {
                        fields[i].setVisible(true);
                    }
                    for (int i = n; i != 5; ++i) {
                        fields[i].setText("");
                    }
                    String[] olds = view.data;
                    String[] newS = new String[n + 1];
                    if (olds.length > newS.length) {
                        System.arraycopy(olds, 0, newS, 0, newS.length);
                    } else {
                        System.arraycopy(olds, 0, newS, 0, olds.length);
                    }
                    view.data = newS;
                }
            });

            if (FormController.get().isEditMode()) {
                StoryView view1 = FormController.get().getNewExpression();
                selectId.setText(view1.data[0]);
                for (int i = 1; i != view1.data.length; ++i) {
                    fields[i - 1].setText(view1.data[i]);
                }
                sizeC.setValue(view1.data.length - 1);
            }
            return pane;
        }
    }

    class ViewViewEdit implements StoryViewEdit {

        @Override
        public GridPane create(StoryView view, ReadOnlyObjectProperty<String> typeSelectItem) {
            GridPane pane = new GridPane();
            pane.setHgap(5.0);
            pane.setVgap(5.0);

            Label labelL = new Label("Name");
            Label locationL = new Label("Location");
            Label timeL = new Label("Time");
            ChoiceBox<String> sceneChoice = new ChoiceBox<>();
            ChoiceBox<String> locationChoice = new ChoiceBox<>();
            ChoiceBox<String> timeChoice = new ChoiceBox<>();

            pane.add(labelL, 0, 0);
            pane.add(sceneChoice, 1, 0);
            pane.add(locationL, 0, 1);
            pane.add(locationChoice, 1, 1);
            pane.add(timeL, 0, 2);
            pane.add(timeChoice, 1, 2);

            Node[] list = new Node[]{labelL, sceneChoice, locationL, locationChoice, timeL, timeChoice};

            sceneChoice.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
                if (n != null && view.name.equals("Scene")) {
                    view.data[0] = n;
                }
            });
            locationChoice.getItems().addAll("Left", "Center", "Right");
            locationChoice.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
                if (n != null && typeSelectItem.get().equals("Shake")) {
                    view.data[0] = n;
                }
            });
            timeChoice.getItems().addAll("500", "1000", "1500", "2000", "2500", "3000");
            timeChoice.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
                if (n != null) {
                    if (typeSelectItem.get().equals("Darking") || typeSelectItem.get().equals("Lighting")) {
                        view.data[0] = n;
                        System.out.println("Set View Data:" + n);
                    }
                }
            });

            typeSelectItem.addListener((v, o, n) -> {
                if (n != null) {
                    switch (typeSelectItem.get()) {
                        case "Scene":
                            sceneChoice.getItems().clear();
                            sceneChoice.getItems().addAll(FormController.get().getSceneMap().keySet());
                            enable(new int[]{0, 1}, list);
                            break;
                        case "Shake":
                            enable(new int[]{2, 3}, list);
                            break;
                        case "Darking":
                        case "Lighting":
                            enable(new int[]{4, 5}, list);
                            break;
                    }
                }
            });

            if (FormController.get().isEditMode()) {
                StoryView view1 = FormController.get().getNewExpression();
                switch (view1.name) {
                    case "Scene":
                        sceneChoice.setValue(view1.data[0]);
                        break;
                    case "Shake":
                        locationChoice.setValue(view1.data[0]);
                        break;
                    case "Darking":
                    case "Lighting":
                        timeChoice.setValue(view1.data[0]);
                        break;
                }
            }
            return pane;
        }
    }

    //Dialog  Word  Dest  WordContent
    //Dialog  Pound  WordContent
    class DialogViewEdit implements StoryViewEdit {
        @Override
        public GridPane create(StoryView view, ReadOnlyObjectProperty<String> typeSelectItem) {
            GridPane pane = new GridPane();
            Label label = new Label("Person");
            ChoiceBox<String> pBox = new ChoiceBox<>();
            TextArea textArea = new TextArea();
            textArea.setPrefRowCount(3);
            textArea.textProperty().addListener((v, o, n) -> {
                if (n != null) {
                    if (view.name.equals("Pound")) {
                        view.data[0] = n;
                    } else if (view.name.equals("Word")) {
                        view.data[1] = n;
                    }
                }
            });
            pBox.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
                if (n != null && view.name.equals("Word")) {
                    view.data[0] = n;
                }
            });

            pane.add(label, 0, 0);
            pane.add(pBox, 1, 0);
            pane.add(textArea, 0, 1, 2, 3);

            typeSelectItem.addListener((v, o, n) -> {
                if (n != null) {
                    if (n.equals("Word")) {
                        pBox.getItems().clear();
                        pBox.getItems().addAll("Me", "Secret");
                        pBox.getItems().addAll(FormController.get().getPersonMap().keySet());
                    }
                    if (n.equals("Word") || n.equals("Pound")) {
                        textArea.setVisible(true);
                        pBox.setVisible(true);
                        label.setVisible(true);
                    } else {
                        label.setVisible(false);
                        textArea.setVisible(false);
                        pBox.setVisible(false);
                    }
                }
            });

            if (FormController.get().isEditMode()) {
                StoryView view1 = FormController.get().getNewExpression();
                if (view1.name.equals("Pound")) {
                    pBox.setValue("Pound");
                    textArea.setText(view1.data[0]);
                } else if (view1.name.equals("Word")) {
                    if (view1.data[0].equals("M")) {
                        pBox.setValue("Me");
                    } else if (view1.data[0].equals("S")) {
                        pBox.setValue("Secret");
                    } else {
                        pBox.setValue(view1.data[0]);
                    }
                    textArea.setText(view1.data[1]);
                }
            }
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
