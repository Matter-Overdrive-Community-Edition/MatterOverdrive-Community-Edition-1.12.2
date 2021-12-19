/*
 * This file is part of Matter Overdrive
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */
package matteroverdrive.data.recipes;

import com.google.common.collect.ImmutableList;
import matteroverdrive.tile.TileEntityMicrowave;
import net.minecraft.item.ItemStack;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

/**
 * @author shadowfacts
 */
public class MicrowaveRecipe extends Recipe<TileEntityMicrowave> {

    private ItemStack main;
    private ItemStack output;
    private int energy;
    private int time;

    public MicrowaveRecipe() {
    }

    public ItemStack getMain() {
        return main;
    }

    public ItemStack getOutput() {
        return output;
    }

    public int getEnergy() {
        return energy;
    }

    public int getTime() {
        return time;
    }

    @Override
    public boolean matches(TileEntityMicrowave machine) {
//        ItemStack primary = machine.getStackInSlot(TileEntityMicrowave.INPUT_SLOT_ID);
//
//        return ItemStack.areItemsEqual(primary, this.main);

        return true;
    }

    public ItemStack getOutput(TileEntityMicrowave machine) {
        return output.copy();
    }

    @Override
    public List<ItemStack> getInputs() {
        return ImmutableList.of(main);
    }

    @Override
    public void fromXML(Element element) {
        energy = getIntAttr(element, "energy");
        time = getIntAttr(element, "time");

        NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                switch (node.getNodeName()) {
                    case "primary":
                        main = getStack((Element) node);
                        break;
                    case "output":
                        output = getStack((Element) node);
                        break;
                }
            }
        }
    }
}
