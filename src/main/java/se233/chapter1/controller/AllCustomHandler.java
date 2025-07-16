package se233.chapter1.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane; // Fixed typo: Stackpane → StackPane

import se233.chapter1.Launcher;
import se233.chapter1.model.character.BasedCharacter;
import se233.chapter1.model.item.Armor;
import se233.chapter1.model.item.BasedEquipment;
import se233.chapter1.model.item.Weapon;

import java.util.ArrayList;

public class AllCustomHandler {
    public static class UnequipAllHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            BasedCharacter character = Launcher.getMainCharacter();
            ArrayList<BasedEquipment> allEquipments = Launcher.getAllEquipments();

            // Add equipped weapon back to inventory if it exists
            if (Launcher.getEquippedWeapon() != null) {
                allEquipments.add(Launcher.getEquippedWeapon());
                Launcher.setEquippedWeapon(null);
            }

            // Add equipped armor back to inventory if it exists
            if (Launcher.getEquippedArmor() != null) {
                allEquipments.add(Launcher.getEquippedArmor());
                Launcher.setEquippedArmor(null);
            }

            // Update character stats by re-equipping with null
            if (character != null) {
                character.equipWeapon(null);
                character.equipArmor(null);
                Launcher.setMainCharacter(character);
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
                    Launcher.setEquippedWeapon(null);
                }

                // Add equipped armor back to inventory if it exists
                if (Launcher.getEquippedArmor() != null) {
                    allEquipments.add(Launcher.getEquippedArmor());
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
            Launcher.refreshPane();
        }

        public static void onDragDetected(MouseEvent event, BasedEquipment equipment, ImageView imgView) {
            Dragboard db = imgView.startDragAndDrop(TransferMode.ANY);
            db.setDragView(imgView.getImage());
            ClipboardContent content = new ClipboardContent();
            content.put(BasedEquipment.DATA_FORMAT, equipment);
            db.setContent(content);
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
                    }

                    // Finally, equip the new weapon
                    try {
                        Launcher.setEquippedWeapon(weaponToEquip);
                        character.equipWeapon(weaponToEquip);
                    } catch (IllegalArgumentException e) {
                        // This should not happen since we already checked, but just in case
                        allEquipments.add(weaponToEquip); // add the weapon back to inventory
                        event.setDropCompleted(false);
                        return;
                    }
                } else {
                    Armor armorToEquip = (Armor) retrievedEquipment;
                    
                    // Check if the character can equip this armor
                    if (character != null && !character.canEquipArmor(armorToEquip)) {
                        // Character cannot equip this armor - do nothing and show the rejection
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
                    }

                    // Finally, equip the new armor
                    try {
                        Launcher.setEquippedArmor(armorToEquip);
                        character.equipArmor(armorToEquip);
                    } catch (IllegalArgumentException e) {
                        // This should not happen since we already checked, but just in case
                        allEquipments.add(armorToEquip); // add the armor back to inventory
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

                dragCompleted = true;
            }

            event.setDropCompleted(dragCompleted);
        }

        public static void onEquipDone(DragEvent event) {
            // Simplified since we handle everything in onDragDropped
            // Just refresh the pane if needed
            if (event.isDropCompleted()) {
                Launcher.refreshPane();
            }
        }
    }
}
