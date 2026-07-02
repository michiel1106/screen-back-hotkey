package bikerboys.screenbackhotkey.client;

import com.mojang.blaze3d.platform.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.client.screen.v1.*;
import net.fabricmc.fabric.impl.client.keymapping.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.locale.*;
import org.lwjgl.glfw.*;

import java.lang.reflect.*;

public class ScreenBackHotkeyClient implements ClientModInitializer {
	public static long msSinceClick = System.nanoTime();
	public static Language languageinstance = Language.getInstance();

	public static KeyMapping backHotKey = new KeyMapping("screenbackhotkey.backhotkey", InputConstants.Type.MOUSE ,GLFW.GLFW_MOUSE_BUTTON_4, KeyMapping.Category.MISC);


	@Override
	public void onInitializeClient() {
        KeyMappingRegistryImpl.registerKeyMapping(backHotKey);
	}
}