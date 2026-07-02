package bikerboys.screenbackhotkey.client.mixin;

import bikerboys.screenbackhotkey.client.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.components.events.*;
import net.minecraft.client.input.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ContainerEventHandler.class)
public interface ContainerEventHandlerMixin {


    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void onClicked(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {

        /*
        For some reason this injected method gets called multiple times. So I added a 50 Ms delay between each call.
         */

        if (System.nanoTime() - ScreenBackHotkeyClient.msSinceClick <= 50000000) {
            ScreenBackHotkeyClient.msSinceClick = System.nanoTime();
            return;
        } else {

            boolean matchesMouse = ScreenBackHotkeyClient.backHotKey.matchesMouse(event);
            if (matchesMouse) {

                if (Minecraft.getInstance().screen instanceof IScreenBack back) {
                    back.screen_back_hotkey$goBack();
                    System.out.println("mouseclicksucced");
                }
            }
            ScreenBackHotkeyClient.msSinceClick = System.nanoTime();
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void onKeyPressed(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        boolean matchesKeyboard = ScreenBackHotkeyClient.backHotKey.matches(event);

        if (System.nanoTime() - ScreenBackHotkeyClient.msSinceClick <= 50000000) {
            ScreenBackHotkeyClient.msSinceClick = System.nanoTime();
            return;
        } else  {
            if (matchesKeyboard) {
                if (Minecraft.getInstance().screen instanceof IScreenBack back) {
                    back.screen_back_hotkey$goBack();
                }
            }

            ScreenBackHotkeyClient.msSinceClick = System.nanoTime();
        }
    }
}
