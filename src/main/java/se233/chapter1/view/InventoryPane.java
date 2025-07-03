package se233.chapter1.view;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;

import se233.chapter1.Launcher;
import se233.chapter1.model.item.BasedEquipment;
import se233.chapter1.controller.AllCustomHandler;

import java.util.ArrayList;

import static se233.chapter1.controller.AllCustomHandler.GenCharacterHandler.onEquipDone;

public class InventoryPane extends ScrollPane {
    private ArrayList<BasedEquipment> equipmentsArray;

    public InventoryPane() {
        this.setContent(getDetailsPane());
    }

    private Pane getDetailsPane() {
        Pane inventoryInfoPane = new HBox(10);
        inventoryInfoPane.setPadding(new Insets(25, 25, 25, 25));

        if (equipmentsArray != null) {
            ImageView[] imageViewList = new ImageView[equipmentsArray.size()];
            for (int i = 0; i < equipmentsArray.size(); i++) {
                imageViewList[i] = new ImageView();
                imageViewList[i].setImage(
                        new Image(Launcher.class.getResource(
                                equipmentsArray.get(i).getImagepath()).toString())
                );

                int finalI = i;
                imageViewList[i].setOnDragDetected(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        AllCustomHandler.GenCharacterHandler.onDragDetected(
                                event,
                                equipmentsArray.get(finalI),
                                imageViewList[finalI]
                        );
                    }
                });
                imageViewList[i].setOnDragDone(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        onEquipDone(event);
                    }
                });
            }
            inventoryInfoPane.getChildren().addAll(imageViewList);
        }

        return inventoryInfoPane;
    }

    public void drawPane(ArrayList<BasedEquipment> equipmentsArray) {
        this.equipmentsArray = equipmentsArray;
        Pane inventoryInfoPane = getDetailsPane();
        this.setStyle("-fx-background-color:Red;");
        this.setContent(inventoryInfoPane);
    }
}
