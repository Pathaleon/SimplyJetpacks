package tonius.simplyjetpacks.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import tonius.simplyjetpacks.config.SJConfig.ConfigSection;
import tonius.simplyjetpacks.util.StringUtils;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

public class ConfigGuiSJ extends GuiConfig {

    public ConfigGuiSJ(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(parentScreen), "simplyjetpacks", false, false, StringUtils.translate("config.title"));
    }

    private static List<IConfigElement> getConfigElements(GuiScreen parent) {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        String prefix = "simplyjetpacks.config.";

        for (ConfigSection configSection : SJConfig.configSections) {
            list.add(new ConfigElement<ConfigCategory>(SJConfig.config.getCategory(configSection.toLowerCase()).setLanguageKey(prefix + configSection.id)));
        }

        return list;
    }

}
