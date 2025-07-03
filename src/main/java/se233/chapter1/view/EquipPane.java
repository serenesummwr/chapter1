package se233.chapter1.view;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import se233.chapter1.Launcher;
import se233.chapter1.controller.AllCustomHandler.GenCharacterHandler;
import se233.chapter1.model.item.Armor;
import se233.chapter1.model.item.Weapon;

public class EquipPane extends ScrollPane {
    private Weapon equippedWeapon;
    private Armor equippedArmor;

    public EquipPane() {
    }

    private Pane getDetailsPane() {
        VBox equipmentInfoPane = new VBox(10);
        equipmentInfoPane.setAlignment(Pos.CENTER);
        equipmentInfoPane.setPadding(new Insets(25));

        Label weaponLbl, armorLbl;
        StackPane weaponImgGroup = new StackPane();
        StackPane armorImgGroup = new StackPane();
        ImageView bg1 = new ImageView();
        ImageView bg2 = new ImageView();
        ImageView weaponImg = new ImageView();
        ImageView armorImg = new ImageView();

        bg1.setImage(new Image(Launcher.class.getResource("assets/blank.png").toString()));
        bg2.setImage(new Image(Launcher.class.getResource("assets/blank.png").toString()));
        weaponImgGroup.getChildren().add(bg1);
        armorImgGroup.getChildren().add(bg2);

        if (equippedWeapon != null) {
            weaponLbl = new Label("Weapon:\n" + equippedWeapon.getName());
            weaponImg.setImage(new Image(Launcher.class.getResource(equippedWeapon.getImagepath()).toString()));
            weaponImgGroup.getChildren().add(weaponImg);
        } else {
            weaponLbl = new Label("Weapon:");
            weaponImg.setImage(new Image(Launcher.class.getResource("assets/blank.png").toString()));
        }

        if (equippedArmor != null) {
            armorLbl = new Label("Armor:\n" + equippedArmor.getName());
            armorImg.setImage(new Image(Launcher.class.getResource(equippedArmor.getImagepath()).toString()));
            armorImgGroup.getChildren().add(armorImg); // <-- fixed: add armorImg if equipped
        } else {
            armorLbl = new Label("Armor:");
            armorImg.setImage(new Image(Launcher.class.getResource("assets/blank.png").toString()));
        }

        // Drag event handlers
        weaponImgGroup.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent e) {
                GenCharacterHandler.onDragOver(e, "Weapon");
            }
        });

        armorImgGroup.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent e) {
                GenCharacterHandler.onDragOver(e, "Armor");
            }
        });

        weaponImgGroup.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent e) {
                GenCharacterHandler.onDragDropped(e, weaponLbl, weaponImgGroup);
            }
        });

        armorImgGroup.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent e) {
                GenCharacterHandler.onDragDropped(e, armorLbl, armorImgGroup);
            }
        });

        equipmentInfoPane.getChildren().addAll(weaponLbl, weaponImgGroup, armorLbl, armorImgGroup);
        return equipmentInfoPane;
    }

    public void drawPane(Weapon equippedWeapon, Armor equippedArmor) {
        this.equippedWeapon = equippedWeapon;
        this.equippedArmor = equippedArmor;
        Pane equipmentInfoPane = getDetailsPane();
        this.setStyle("-fx-background-color:Red;");
        this.setContent(equipmentInfoPane);
    }
}
