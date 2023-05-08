package dev.adventurecraft.awakening.script;

import dev.adventurecraft.awakening.extension.client.gui.ExInGameHud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.texture.TextureManager;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("unused")
public class ScriptUIRect extends UIElement {

    public float width;
    public float height;
    public float red;
    public float green;
    public float blue;
    public float alpha;

    public ScriptUIRect(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
        this(var1, var2, var3, var4, var5, var6, var7, var8, ((ExInGameHud) Minecraft.instance.overlay).getScriptUI());
    }

    public ScriptUIRect(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, ScriptUIContainer var9) {
        this.red = 1.0F;
        this.green = 1.0F;
        this.blue = 1.0F;
        this.alpha = 1.0F;
        this.prevX = this.curX = var1;
        this.prevY = this.curY = var2;
        this.width = var3;
        this.height = var4;
        this.red = var5;
        this.green = var6;
        this.blue = var7;
        this.alpha = var8;
        if (var9 != null) {
            var9.add(this);
        }
    }

    @Override
    public void render(TextRenderer var1, TextureManager var2, float var3) {
        float var4 = this.getXAtTime(var3);
        float var5 = this.getYAtTime(var3);
        Tessellator var6 = Tessellator.INSTANCE;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(this.red, this.green, this.blue, this.alpha);
        var6.start();
        var6.addVertex(var4, var5 + this.height, 0.0D);
        var6.addVertex(var4 + this.width, var5 + this.height, 0.0D);
        var6.addVertex(var4 + this.width, var5, 0.0D);
        var6.addVertex(var4, var5, 0.0D);
        var6.tessellate();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
