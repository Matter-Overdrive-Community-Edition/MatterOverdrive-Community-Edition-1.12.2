
package matteroverdrive.data.quest.logic;

import com.google.gson.JsonObject;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestLogicState;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.api.quest.QuestState;
import matteroverdrive.data.quest.QuestBlock;
import matteroverdrive.data.quest.QuestItem;
import matteroverdrive.util.MOJsonHelper;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.MOQuestHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;
import java.util.Random;

public class QuestLogicPlaceBlock extends AbstractQuestLogicBlock {
    String namePattern;
    int minBlockPlace;
    int maxBlockPlace;
    private int radius;

    public QuestLogicPlaceBlock() {
        super();
    }

    public QuestLogicPlaceBlock(int radius, QuestItem blockStack) {
        this(radius, blockStack, 1, 1);
    }

    public QuestLogicPlaceBlock(int radius, QuestItem blockStack, int minBlockPlace, int maxBlockPlace) {
        super(blockStack);
        this.radius = radius;
        this.minBlockPlace = minBlockPlace;
        this.maxBlockPlace = maxBlockPlace;
    }

    public QuestLogicPlaceBlock(int radius, QuestBlock block) {
        this(radius, block, 1, 1);
    }

    public QuestLogicPlaceBlock(int radius, QuestBlock block, int minBlockPlace, int maxBlockPlace) {
        super(block);
        this.radius = radius;
        this.minBlockPlace = minBlockPlace;
        this.maxBlockPlace = maxBlockPlace;
    }

    @Override
    public void loadFromJson(JsonObject jsonObject) {
        super.loadFromJson(jsonObject);
        radius = MOJsonHelper.getInt(jsonObject, "radius", 0);
        minBlockPlace = MOJsonHelper.getInt(jsonObject, "place_count_min");
        maxBlockPlace = MOJsonHelper.getInt(jsonObject, "place_count_max");
        namePattern = MOJsonHelper.getString(jsonObject, "regex", null);
    }

    @Override
    public String modifyInfo(QuestStack questStack, String info) {
        return info;
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex) {
        return getBlockPlaced(questStack) >= getMaxBlockPlace(questStack);
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex) {
        objective = replaceBlockNameInText(objective);
        BlockPos pos = MOQuestHelper.getPosition(questStack);
        if (pos != null) {
            double distance = new Vec3d(Math.floor(entityPlayer.posX), Math.floor(entityPlayer.posY), Math.floor(entityPlayer.posZ)).distanceTo(new Vec3d(pos));
            objective = objective.replace("$distance", Integer.toString((int) Math.max(distance - radius, 0)) + " blocks");
        } else {
            objective = objective.replace("$distance", "0 blocks");
        }
        return objective;
    }

    @Override
    public void initQuestStack(Random random, QuestStack questStack) {
        setMaxBlockPlace(questStack, random(random, minBlockPlace, maxBlockPlace));
    }

    @Override
    public QuestLogicState onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer) {
        if (event instanceof BlockEvent.PlaceEvent) {
            BlockEvent.PlaceEvent placeEvent = (BlockEvent.PlaceEvent) event;
            boolean isTheSameBlockFlag = false;
            if (blockStack != null) {
                isTheSameBlockFlag = !placeEvent.getItemInHand().isEmpty() && areBlockStackTheSame(placeEvent.getItemInHand());
            } else if(block != null) {
                if (areBlocksTheSame(placeEvent.getPlacedBlock())) {
                    if (namePattern != null && !placeEvent.getItemInHand().isEmpty()) {
                        isTheSameBlockFlag = placeEvent.getItemInHand().getDisplayName().matches(namePattern);
                    } else {
                        isTheSameBlockFlag = true;
                    }
                }
            } else {
                MOLog.error("QuestLogicPlaceBlock had neither a blockStack or block, this shouldn't be possible: " + this);
            }

            BlockPos pos = MOQuestHelper.getPosition(questStack);
            if (pos != null && isTheSameBlockFlag) {
                if (!(new Vec3d(placeEvent.getPos()).distanceTo(new Vec3d(pos)) <= radius)) {
                    return null;
                }
            }

            if (isTheSameBlockFlag) {
                setBlockPlaced(questStack, getBlockPlaced(questStack) + 1);
                markComplete(questStack, entityPlayer);
                return new QuestLogicState(QuestState.Type.COMPLETE, true);
            }
        }
        return null;
    }

    @Override
    public void onQuestTaken(QuestStack questStack, EntityPlayer entityPlayer) {

    }

    @Override
    public void onQuestCompleted(QuestStack questStack, EntityPlayer entityPlayer) {

    }

    @Override
    public void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<IQuestReward> rewards) {

    }

    protected void setBlockPlaced(QuestStack questStack, int placed) {
        initTag(questStack);
        getTag(questStack).setShort("Placed", (short) placed);
    }

    protected int getBlockPlaced(QuestStack questStack) {
        if (hasTag(questStack)) {
            return getTag(questStack).getShort("Placed");
        }
        return 0;
    }

    protected void setMaxBlockPlace(QuestStack questStack, int maxBlockPlace) {
        initTag(questStack);
        getTag(questStack).setShort("MaxPlaced", (short) maxBlockPlace);
    }

    protected int getMaxBlockPlace(QuestStack questStack) {
        if (hasTag(questStack)) {
            return getTag(questStack).getShort("MaxPlaced");
        }
        return 0;
    }

    public void setNamePattern(String namePattern) {
        this.namePattern = namePattern;
    }
}
