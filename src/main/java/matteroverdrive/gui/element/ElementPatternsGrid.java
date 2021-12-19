
package matteroverdrive.gui.element;

import matteroverdrive.data.matter_network.ItemPatternMapping;
import matteroverdrive.gui.MOGuiBase;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;

public class ElementPatternsGrid extends ElementGrid {
    String filter = "";

    public ElementPatternsGrid(MOGuiBase guiBase, int x, int y, int width, int height) {
        super(guiBase, x, y, width, height, width);
        setMargins(0, 0, 4, 0);
    }

    public void setPattern(ItemPatternMapping patternMapping) {
        Iterator<MOElementBase> elementBaseIterator = elements.iterator();
        boolean patternSet = false;
        while (elementBaseIterator.hasNext()) {
            MOElementBase elementBase = elementBaseIterator.next();
            if (elementBase instanceof ElementItemPattern) {
                ItemPatternMapping elementPatternMapping = ((ElementItemPattern) elementBase).getPatternMapping();
                if (elementPatternMapping.getDatabaseId().equals(patternMapping.getDatabaseId()) && elementPatternMapping.getStorageId() == patternMapping.getStorageId() && elementPatternMapping.getPatternId() == patternMapping.getPatternId()) {
                    if (patternMapping.getItemPattern() == null) {
                        elementBaseIterator.remove();
                    } else {
                        ((ElementItemPattern) elementBase).setPatternMapping(patternMapping);
                        patternSet = true;
                        break;
                    }

                }
            }
        }

        if (!patternSet && patternMapping.getItemPattern() != null) {
            elements.add(new ElementMonitorItemPattern(gui, patternMapping, gui));
        }
    }

    public void removePatterns(BlockPos database) {
        Iterator<MOElementBase> elementBaseIterator = elements.iterator();
        while (elementBaseIterator.hasNext()) {
            MOElementBase elementBase = elementBaseIterator.next();
            if (elementBase instanceof ElementItemPattern) {
                if (((ElementItemPattern) elementBase).getPatternMapping().getDatabaseId().equals(database)) {
                    elementBaseIterator.remove();
                }
            }
        }
    }

    public void removePatterns(BlockPos database, int storageId) {
        Iterator<MOElementBase> elementBaseIterator = elements.iterator();
        while (elementBaseIterator.hasNext()) {
            MOElementBase elementBase = elementBaseIterator.next();
            if (elementBase instanceof ElementItemPattern) {
                if (((ElementItemPattern) elementBase).getPatternMapping().getDatabaseId().equals(database) && ((ElementItemPattern) elementBase).getPatternMapping().getStorageId() == storageId) {
                    elementBaseIterator.remove();
                }
            }
        }
    }

    public void clearPattern() {
        elements.clear();
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public boolean shouldBeDisplayed(MOElementBase element) {
        return element.getName() != null && element.getName().toLowerCase().contains(filter.toLowerCase());
    }
}
