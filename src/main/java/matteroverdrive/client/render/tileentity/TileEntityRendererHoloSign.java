
package matteroverdrive.client.render.tileentity;

import matteroverdrive.Reference;
import matteroverdrive.blocks.BlockHoloSign;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.tile.TileEntityHoloSign;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;

import static matteroverdrive.util.MOBlockHelper.getLeftSide;
import static matteroverdrive.util.MOBlockHelper.getRightSide;

public class TileEntityRendererHoloSign extends TileEntitySpecialRenderer<TileEntityHoloSign> {
    @Override
    public void render(TileEntityHoloSign tile, double x, double y, double z, float ticks, int destoryStage, float a) {
        if (!tile.shouldRender())
            return;

        EnumFacing side = tile.getWorld().getBlockState(tile.getPos()).getValue(MOBlock.PROPERTY_DIRECTION);

        RenderUtils.beginDrawinngBlockScreen(x, y, z, side, Reference.COLOR_HOLO, tile, -0.8375, 0.2f);

        String text = tile.getText();
        if (text != null) {
            for (TextFormatting formatting : TextFormatting.values()) {
                text = text.replaceAll(formatting.toString().replace('\u00a7', '&'), formatting.toString());
            }
            String[] infos = text.split("\n");
            int leftMargin = 10;
            int rightMargin = 10;
            float maxSize = 4f;
            EnumFacing leftSide = getLeftSide(side);
            if (tile.getWorld().getBlockState(tile.getPos().offset(leftSide)).getBlock() instanceof BlockHoloSign) {
                leftMargin = -80;
                maxSize = 8;
            }
            EnumFacing rightSide = getRightSide(side);
            if (tile.getWorld().getBlockState(tile.getPos().offset(rightSide)).getBlock() instanceof BlockHoloSign) {
                rightMargin = -80;
                maxSize = 8;
            }

            if (tile.getConfigs().getBoolean("AutoLineSize", false)) {
                RenderUtils.drawScreenInfoWithLocalAutoSize(infos, Reference.COLOR_HOLO, side, leftMargin, rightMargin, maxSize);
            } else {
                RenderUtils.drawScreenInfoWithGlobalAutoSize(infos, Reference.COLOR_HOLO, side, leftMargin, rightMargin, maxSize);
            }
        }

        RenderUtils.endDrawinngBlockScreen();
    }
}