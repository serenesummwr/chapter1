package se233.chapter1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import se233.chapter1.controller.GenCharacter;
import se233.chapter1.controller.GenItemList;
import se233.chapter1.model.character.BasedCharacter;
import se233.chapter1.model.item.Armor;
import se233.chapter1.model.item.BasedEquipment;
import se233.chapter1.model.item.Weapon;
import se233.chapter1.view.CharacterPane;
import se233.chapter1.view.EquipPane;
import se233.chapter1.view.InventoryPane;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Launcher extends Application {
    private static final Logger logger = LogManager.getLogger(Launcher.class);
    private static Scene mainScene;
    private static BasedCharacter mainCharacter = null;

    public static ArrayList<BasedEquipment> getAllEquipments() {
        return allEquipments;
    }

    public static void setAllEquipments(ArrayList<BasedEquipment> allEquipments) {
        Launcher.allEquipments = allEquipments;
    }

    private static ArrayList<BasedEquipment> allEquipments = null;

    public static Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    public static void setEquippedWeapon(Weapon equippedWeapon) {
        Launcher.equippedWeapon = equippedWeapon;
    }

    private static Weapon equippedWeapon = null;

    public static Armor getEquippedArmor() {
        return equippedArmor;
    }

    public static void setEquippedArmor(Armor equippedArmor) {
        Launcher.equippedArmor = equippedArmor;
    }

    private static Armor equippedArmor = null;
    private static CharacterPane characterPane = null;
    private static EquipPane equipPane = null;
    private static InventoryPane inventoryPane = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("Application starting...");
        primaryStage.setTitle("Chapter1");
        primaryStage.setResizable(false);
        primaryStage.show();
        mainCharacter = GenCharacter.setUpCharacter();
        allEquipments = GenItemList.setUpItemList();
        logger.info("Initial character '{}' created. Inventory size={}.", mainCharacter != null ? mainCharacter.getName() : "<none>", allEquipments != null ? allEquipments.size() : 0);
        Pane mainPane = getMainPane();
        mainScene = new Scene(mainPane);
        primaryStage.setScene(mainScene);
        logger.info("JavaFX primary stage initialized.");
    }

    public Pane getMainPane() {
        BorderPane mainPane = new BorderPane();
        characterPane = new CharacterPane();
        equipPane = new EquipPane();
        inventoryPane = new InventoryPane();
        refreshPane();
        mainPane.setCenter(characterPane);
        mainPane.setBottom(inventoryPane);
        mainPane.setLeft(equipPane);
        return mainPane;
    }

    public static void refreshPane() {
        characterPane.drawPane(mainCharacter);
        equipPane.drawPane(equippedWeapon, equippedArmor);
        inventoryPane.drawPane(allEquipments);
        if (logger.isDebugEnabled()) {
            logger.debug("Pane refreshed. Equipped Weapon='{}' Armor='{}' InventorySize={}",
                    equippedWeapon != null ? equippedWeapon.getName() : "-",
                    equippedArmor != null ? equippedArmor.getName() : "-",
                    allEquipments != null ? allEquipments.size() : 0);
        }
    }

    public static BasedCharacter getMainCharacter() {
        return mainCharacter;
    }

    public static void setMainCharacter(BasedCharacter mainCharacter) {
        Launcher.mainCharacter = mainCharacter;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
