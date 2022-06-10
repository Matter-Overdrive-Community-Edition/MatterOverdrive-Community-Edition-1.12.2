
package matteroverdrive.gui.config;

import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.Map;
import java.util.TreeMap;

public class EnumConfigProperty extends GuiConfigEntries.SelectValueEntry {
	public EnumConfigProperty(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
		super(owningScreen, owningEntryList, configElement, getSelectableValues(configElement));
	}

	private static Map<Object, String> getSelectableValues(IConfigElement configElement) {
		Map<Object, String> selectableValues = new TreeMap<>();

		for (int i = 0; i < configElement.getValidValues().length; i++) {
			selectableValues.put(i, configElement.getValidValues()[i]);
		}

		return selectableValues;
	}

	@Override
	public void updateValueButtonText() {
		super.updateValueButtonText();
		btnValue.displayString = configElement.getValidValues()[Integer.parseInt(getCurrentValue())];
	}
}
