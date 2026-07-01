package bikerboys.screenbackhotkey.client;

import com.mojang.blaze3d.platform.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.client.screen.v1.*;
import net.fabricmc.fabric.impl.client.keymapping.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.*;
import org.lwjgl.glfw.*;

public class ScreenBackHotkeyClient implements ClientModInitializer {

	KeyMapping backHotKey = new KeyMapping("screenbackhotkey.backhotkey", InputConstants.Type.MOUSE ,GLFW.GLFW_MOUSE_BUTTON_4, KeyMapping.Category.MISC);


	@Override
	public void onInitializeClient() {
		KeyMappingRegistryImpl.registerKeyMapping(backHotKey);

		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			ScreenMouseEvents.beforeMouseClick(screen).register((screen1, mouseButtonEvent) -> {
				if (mouseButtonEvent.button() == GLFW.GLFW_MOUSE_BUTTON_4 && screen1 instanceof IScreenBack back) {
					back.screen_back_hotkey$goBack();
				}
			});
		});



	}
}