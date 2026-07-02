package bikerboys.screenbackhotkey.client.mixin;

import bikerboys.screenbackhotkey.client.*;
import static bikerboys.screenbackhotkey.client.ScreenBackHotkeyClient.languageinstance;
import net.fabricmc.loader.api.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.*;
import net.minecraft.client.gui.narration.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.input.*;
import net.minecraft.client.resources.language.*;
import net.minecraft.locale.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.contents.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.*;
import java.util.*;

@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractContainerEventHandler implements IScreenBack {
	@Shadow
	protected abstract <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget);


	@Shadow
	@Final
	private List<Renderable> renderables;

	@Shadow
	public abstract void onClose();

	@Shadow
	public abstract boolean shouldCloseOnEsc();

	@Override
	public void screen_back_hotkey$goBack() {

		if (searchForButtons()) return;
		if (tryFields()) return;
		if (tryDepthSearchForFields()) return;
		if (shouldCloseOnEsc()) {
			onClose();
			return;
		}
	}



	@Unique
    private boolean tryFields() {

		Screen screen = (Screen)(Object)this;

		Field[] declaredFields = screen.getClass().getDeclaredFields();
		for (Field field : declaredFields) {

			if (isSpecialName(field.getName().toLowerCase())) {
				try {
					field.setAccessible(true);
					Screen o = (Screen) field.get(screen);

					Minecraft.getInstance().setScreenAndShow(o);
					return true;

				} catch (IllegalAccessException e) {
					if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
						e.printStackTrace();
						System.out.println("failed to access field " + field.getName());
					}
				}
			}
		}
		return false;
	}

	@Unique
    private boolean tryDepthSearchForFields() {
		Screen screen = (Screen)(Object)this;

		int depth = 7;


		Class<?> currentSuperedClass = null;

		for (int i = 1; i <= depth; i++) {
			if (currentSuperedClass == null) {
				currentSuperedClass = screen.getClass();
			}

			for (Field declaredField : currentSuperedClass.getDeclaredFields()) {
				if (isSpecialName(declaredField.getName().toLowerCase())) {

					try {
						declaredField.setAccessible(true);
						Screen field = (Screen) declaredField.get(screen);
						Minecraft.getInstance().setScreenAndShow(field);
						return true;
					} catch (IllegalAccessException e) {
						if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
							e.printStackTrace();
							System.out.println("failed to access field");
						}
                    }

                }

			}
			currentSuperedClass = currentSuperedClass.getSuperclass();

		}

		return false;
	}

	@Unique
    private boolean searchForButtons() {
		Language defaultInstance = languageinstance;

		for (Renderable renderable : renderables) {
			if (renderable instanceof Button button) {
				if (button.getMessage().getContents() instanceof TranslatableContents translatableContents) {
					String orDefault = defaultInstance.getOrDefault(translatableContents.getKey());
					if (isBackString(orDefault)) {
						button.onPress(new MouseButtonInfo(1, 0));
						return true;
					}
				}
			}

		}

		return false;
	}

	@Unique
    private boolean isBackString(String string) {
		switch (string.toLowerCase()) {
			case "back", "return", "done", "cancel" -> {
				return true;
			}
		}
		return false;
	}



	@Unique
    private boolean isSpecialName(String name) {
        switch (name.toLowerCase()) {
            case "previous", "previousscreen", "lastscreen", "parent", "parentscreen" -> {
                return true;
            }
        }
		return false;
	}

}