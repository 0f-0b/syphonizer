package ud2.syphonizer.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;

import jsyphon.JSyphonServer;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
  @Shadow
  @Final
  private RenderTarget mainRenderTarget;
  @Shadow
  @Final
  private Window window;
  private JSyphonServer syphon;

  @Inject(method = "run", at = @At("HEAD"))
  private void handleStartRun(CallbackInfo ci) {
    this.syphon = new JSyphonServer();
    this.syphon.initWithName("Minecraft " + SharedConstants.getCurrentVersion().getName());
  }

  @Inject(method = "run", at = @At("RETURN"))
  private void handleEndRun(CallbackInfo ci) {
    this.syphon.stop();
    this.syphon = null;
  }

  @Inject(method = "runTick", at = @At(value = "INVOKE", target = "com/mojang/blaze3d/pipeline/RenderTarget.blitToScreen(II)V", shift = Shift.AFTER))
  public void handleRunTick(CallbackInfo ci) {
    var width = this.window.getWidth();
    var height = this.window.getHeight();
    this.syphon.bindToDrawFrameOfSize(width, height);
    this.mainRenderTarget.blitToScreen(width, height);
    this.syphon.unbindAndPublish();
  }
}
