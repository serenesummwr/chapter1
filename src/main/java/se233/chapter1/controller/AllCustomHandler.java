package se233.chapter1.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import se233.chapter1.Launcher;
import se233.chapter1.model.character.BasedCharacter;
import se233.chapter1.model.item.Armor;
import se233.chapter1.model.item.BasedEquipment;
import se233.chapter1.model.item.Weapon;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AllCustomHandler {
    private static final Logger logger = LogManager.getLogger(AllCustomHandler.class);
    public static class UnequipAllHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            BasedCharacter character = Launcher.getMainCharacter();
            ArrayList<BasedEquipment> allEquipments = Launcher.getAllEquipments();

            // Add equipped weapon back to inventory if it exists
            if (Launcher.getEquippedWeapon() != null) {
                allEquipments.add(Launcher.getEquippedWeapon());
                logger.info("Unequipped weapon '{}' from character '{}'.", Launcher.getEquippedWeapon().getName(), character != null ? character.getName() : "<no character>");
                Launcher.setEquippedWeapon(null);
            }

            // Add equipped armor back to inventory if it exists
            if (Launcher.getEquippedArmor() != null) {
                allEquipments.add(Launcher.getEquippedArmor());
                logger.info("Unequipped armor '{}' from character '{}'.", Launcher.getEquippedArmor().getName(), character != null ? character.getName() : "<no character>");
                Launcher.setEquippedArmor(null);
            }

            // Update character stats by re-equipping with null
            if (character != null) {
                character.equipWeapon(null);
                character.equipArmor(null);
                Launcher.setMainCharacter(character);
                logger.info("Character '{}' stats reset to base after unequip. Power={}, Def={}, Res={}", character.getName(), character.getPower(), character.getDefense(), character.getResistance());
            }

            Launcher.setAllEquipments(allEquipments);
            Launcher.refreshPane();
        }
    }

    public static class GenCharacterHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            // Unequip all items from the current character if any are equipped
            if (Launcher.getEquippedWeapon() != null || Launcher.getEquippedArmor() != null) {
                BasedCharacter character = Launcher.getMainCharacter();
                ArrayList<BasedEquipment> allEquipments = Launcher.getAllEquipments();

                // Add equipped weapon back to inventory if it exists
                if (Launcher.getEquippedWeapon() != null) {
                    allEquipments.add(Launcher.getEquippedWeapon());
                    logger.info("(Gen) Unequipped weapon '{}' from character '{}'.", Launcher.getEquippedWeapon().getName(), character != null ? character.getName() : "<no character>");
                    Launcher.setEquippedWeapon(null);
                }

                // Add equipped armor back to inventory if it exists
                if (Launcher.getEquippedArmor() != null) {
                    allEquipments.add(Launcher.getEquippedArmor());
                    logger.info("(Gen) Unequipped armor '{}' from character '{}'.", Launcher.getEquippedArmor().getName(), character != null ? character.getName() : "<no character>");
                    Launcher.setEquippedArmor(null);
                }

                // Update character stats by re-equipping with null
                if (character != null) {
                    character.equipWeapon(null);
                    character.equipArmor(null);
                    Launcher.setMainCharacter(character);
                }

                Launcher.setAllEquipments(allEquipments);
            }

            // Generate new character
            Launcher.setMainCharacter(GenCharacter.setUpCharacter());
            logger.info("Generated new character '{}'.", Launcher.getMainCharacter().getName());
            Launcher.refreshPane();
        }

        public static void onDragDetected(MouseEvent event, BasedEquipment equipment, ImageView imgView) {
            Dragboard db = imgView.startDragAndDrop(TransferMode.ANY);
            db.setDragView(imgView.getImage());
            ClipboardContent content = new ClipboardContent();
            content.put(BasedEquipment.DATA_FORMAT, equipment);
            db.setContent(content);
            logger.debug("Drag detected for equipment '{}' (type={}).", equipment.getName(), equipment.getClass().getSimpleName());
            event.consume();
        }

        public static void onDragOver(DragEvent event, String type) {
            Dragboard dragboard = event.getDragboard();
            BasedEquipment retrievedEquipment = (BasedEquipment) dragboard.getContent(BasedEquipment.DATA_FORMAT);
            BasedCharacter character = Launcher.getMainCharacter();
            
            if (dragboard.hasContent(BasedEquipment.DATA_FORMAT)
                    && retrievedEquipment.getClass().getSimpleName().equals(type)) {
                
                // Check if the character can actually equip this equipment
                boolean canEquip = false;
                if (character != null) {
                    if (type.equals("Weapon")) {
                        canEquip = character.canEquipWeapon((Weapon) retrievedEquipment);
                    } else if (type.equals("Armor")) {
                        canEquip = character.canEquipArmor((Armor) retrievedEquipment);
                    }
                }
                
                if (canEquip) {
                    event.acceptTransferModes(TransferMode.MOVE);
                } else {
                    logger.debug("Drag over rejected: character '{}' cannot equip {} '{}'.", character != null ? character.getName() : "<no character>", type, retrievedEquipment.getName());
                }
            }
        }

        public static void onDragDropped(DragEvent event, Label lbl, StackPane imgGroup) {
            boolean dragCompleted = false;
            Dragboard dragboard = event.getDragboard();
            ArrayList<BasedEquipment> allEquipments = Launcher.getAllEquipments();
            BasedCharacter character = Launcher.getMainCharacter();

            if (dragboard.hasContent(BasedEquipment.DATA_FORMAT)) {
                BasedEquipment retrievedEquipment = (BasedEquipment) dragboard.getContent(BasedEquipment.DATA_FORMAT);

                if (retrievedEquipment.getClass().getSimpleName().equals("Weapon")) {
                    Weapon weaponToEquip = (Weapon) retrievedEquipment;

                    // Check if the character can equip this weapon
                    if (character != null && !character.canEquipWeapon(weaponToEquip)) {
                        // Character cannot equip this weapon - do nothing and show the rejection
                        logger.warn("Equip attempt failed: character '{}' (type={}) cannot equip weapon '{}' (damageType={}).", character != null ? character.getName() : "<no character>", character != null ? character.getType() : "?", weaponToEquip.getName(), weaponToEquip.getDamageType());
                        event.setDropCompleted(false);
                        return;
                    }
                    
                    // First, remove the new weapon from inventory immediately
                    allEquipments.removeIf(equipment ->
                            equipment.getName().equals(retrievedEquipment.getName()) &&
                                    equipment.getClass().equals(retrievedEquipment.getClass()));

                    // Then, if there was a previously equipped weapon, add it back to inventory
                    if (Launcher.getEquippedWeapon() != null) {
                        Weapon oldWeapon = Launcher.getEquippedWeapon();
                        allEquipments.add(oldWeapon);
                        logger.info("Swapping weapon: '{}' -> '{}' for character '{}'.", oldWeapon.getName(), weaponToEquip.getName(), character != null ? character.getName() : "<no character>");
                    } else {
                        logger.info("Equipping new weapon '{}' for character '{}'.", weaponToEquip.getName(), character != null ? character.getName() : "<no character>");
                    }

                    // Finally, equip the new weapon
                    try {
                        Launcher.setEquippedWeapon(weaponToEquip);
                        if (character != null) character.equipWeapon(weaponToEquip);
                    } catch (IllegalArgumentException e) {
                        // This should not happen since we already checked, but just in case
                        allEquipments.add(weaponToEquip); // add the weapon back to inventory
                        logger.error("Unexpected equip failure for weapon '{}' on character '{}': {}", weaponToEquip.getName(), character != null ? character.getName() : "<no character>", e.getMessage());
                        event.setDropCompleted(false);
                        return;
                    }
                } else {
                    Armor armorToEquip = (Armor) retrievedEquipment;

                    // Check if the character can equip this armor
                    if (character != null && !character.canEquipArmor(armorToEquip)) {
                        // Character cannot equip this armor - do nothing and show the rejection
                        logger.warn("Equip attempt failed: character '{}' cannot equip armor '{}'.", character != null ? character.getName() : "<no character>", armorToEquip.getName());
                        event.setDropCompleted(false);
                        return;
                    }
                    
                    // First, remove the new armor from inventory immediately
                    allEquipments.removeIf(equipment ->
                            equipment.getName().equals(retrievedEquipment.getName()) &&
                                    equipment.getClass().equals(retrievedEquipment.getClass()));

                    // Then, if there was a previously equipped armor, add it back to inventory
                    if (Launcher.getEquippedArmor() != null) {
                        Armor oldArmor = Launcher.getEquippedArmor();
                        allEquipments.add(oldArmor);
                        logger.info("Swapping armor: '{}' -> '{}' for character '{}'.", oldArmor.getName(), armorToEquip.getName(), character != null ? character.getName() : "<no character>");
                    } else {
                        logger.info("Equipping new armor '{}' for character '{}'.", armorToEquip.getName(), character != null ? character.getName() : "<no character>");
                    }

                    // Finally, equip the new armor
                    try {
                        Launcher.setEquippedArmor(armorToEquip);
                        if (character != null) character.equipArmor(armorToEquip);
                    } catch (IllegalArgumentException e) {
                        // This should not happen since we already checked, but just in case
                        allEquipments.add(armorToEquip); // add the armor back to inventory
                        logger.error("Unexpected equip failure for armor '{}' on character '{}': {}", armorToEquip.getName(), character != null ? character.getName() : "<no character>", e.getMessage());
                        event.setDropCompleted(false);
                        return;
                    }
                }

                Launcher.setMainCharacter(character);
                Launcher.setAllEquipments(allEquipments);
                Launcher.refreshPane();

                ImageView imgView = new ImageView();
                if (imgGroup.getChildren().size() != 1) {
                    imgGroup.getChildren().remove(1); // remove previous image
                }

                lbl.setText(retrievedEquipment.getClass().getSimpleName() + ":\n" +
                        retrievedEquipment.getName());

                imgView.setImage(new Image(Launcher.class.getResource(
                        retrievedEquipment.getImagepath()).toString()));
                imgGroup.getChildren().add(imgView);

                if (character != null) {
                    logger.info("Equip complete: Character='{}' Weapon='{}' Armor='{}' => Power={}, Def={}, Res={}",
                            character.getName(),
                            Launcher.getEquippedWeapon() != null ? Launcher.getEquippedWeapon().getName() : "-",
                            Launcher.getEquippedArmor() != null ? Launcher.getEquippedArmor().getName() : "-",
                            character.getPower(), character.getDefense(), character.getResistance());
                } else {
                    logger.warn("Equip finished but no active character.");
                }

                dragCompleted = true;
            }

            event.setDropCompleted(dragCompleted);
        }

        public static void onEquipDone(DragEvent event) {
            // Always log on mouse release after a drag gesture
            BasedCharacter character = Launcher.getMainCharacter();
            String weaponName = Launcher.getEquippedWeapon() != null ? Launcher.getEquippedWeapon().getName() : "-";
            String armorName = Launcher.getEquippedArmor() != null ? Launcher.getEquippedArmor().getName() : "-";
            boolean success = event.isDropCompleted();
            if (character != null) {
                logger.info("Mouse released (drop {}): Character='{}' Equipped Weapon='{}' Armor='{}' => Power={}, Def={}, Res={}",
                        success ? "SUCCESS" : "FAIL",
                        character.getName(), weaponName, armorName,
                        character.getPower(), character.getDefense(), character.getResistance());
            } else {
                logger.info("Mouse released (drop {}) with no active character. Equipped Weapon='{}' Armor='{}'", success ? "SUCCESS" : "FAIL", weaponName, armorName);
            }
            if (success) {
                Launcher.refreshPane();
            }
        }
    }
}
