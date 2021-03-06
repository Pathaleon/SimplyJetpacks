package tonius.simplyjetpacks.item.jetpack;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import tonius.simplyjetpacks.item.ItemJetpack;
import tonius.simplyjetpacks.util.StackUtils;
import tonius.simplyjetpacks.util.StringUtils;
import cofh.api.energy.IEnergyContainerItem;

public class JetpackFluxPlate extends JetpackArmored {

    public int energyPerTickOut;

    public JetpackFluxPlate(int meta, int tier, boolean enchantable, EnumRarity rarity, int energyCapacity, int energyPerTick, double speedVertical, double accelVertical, float speedSideways, double speedVerticalHover, double speedVerticalHoverSlow, boolean emergencyHoverMode, int armorDisplay, double armorAbsorption, int energyPerHit, int energyPerTickOut) {
        super(meta, tier, enchantable, rarity, energyCapacity, energyPerTick, speedVertical, accelVertical, speedSideways, speedVerticalHover, speedVerticalHoverSlow, emergencyHoverMode, armorDisplay, armorAbsorption, energyPerHit);
        this.energyPerTickOut = energyPerTickOut;
    }

    public boolean isChargerOn(ItemStack itemStack) {
        return StackUtils.getNBT(itemStack).getBoolean("FluxPackOn");
    }

    public void toggleCharger(ItemStack itemStack, EntityPlayer player) {
        String msg = "";
        if (this.isChargerOn(itemStack)) {
            msg = StringUtils.translate("chat.fluxpack.charger") + " " + StringUtils.LIGHT_RED + StringUtils.translate("chat.disabled");
            itemStack.stackTagCompound.setBoolean("FluxPackOn", false);
        } else {
            msg = StringUtils.translate("chat.fluxpack.charger") + " " + StringUtils.BRIGHT_GREEN + StringUtils.translate("chat.enabled");
            itemStack.stackTagCompound.setBoolean("FluxPackOn", true);
        }
        player.addChatMessage(new ChatComponentText(msg));
    }

    @Override
    public String getBaseName() {
        return "jetpack." + this.tier;
    }

    @Override
    public boolean hasArmoredVersion() {
        return false;
    }

    @Override
    public void toggle(ItemStack itemStack, EntityPlayer player) {
        if (player.isSneaking()) {
            this.toggleCharger(itemStack, player);
        } else {
            super.toggle(itemStack, player);
        }
    }

    @Override
    public void useJetpack(EntityLivingBase user, ItemStack armor, ItemJetpack item, boolean force) {
        super.useJetpack(user, armor, item, force);
        if (this.isChargerOn(armor)) {
            for (int i = 0; i <= 4; i++) {
                ItemStack currentStack = user.getEquipmentInSlot(i);
                if (currentStack != null && currentStack != armor && currentStack.getItem() instanceof IEnergyContainerItem) {
                    IEnergyContainerItem heldEnergyItem = (IEnergyContainerItem) (currentStack.getItem());
                    int energyToAdd = Math.min(item.extractEnergy(armor, this.energyPerTickOut, true), heldEnergyItem.receiveEnergy(currentStack, this.energyPerTickOut, true));
                    item.extractEnergy(armor, energyToAdd, false);
                    heldEnergyItem.receiveEnergy(currentStack, energyToAdd, false);
                }
            }
        }
    }

    @Override
    public void addShiftInformation(ItemStack itemStack, EntityPlayer player, List list) {
        list.add(StringUtils.getStateText(this.isOn(itemStack)));
        list.add(StringUtils.getHoverModeText(this.isHoverModeOn(itemStack)));
        list.add(StringUtils.getChargerStateText(this.isChargerOn(itemStack)));
        int currentTickEnergy = this.isHoverModeOn(itemStack) ? this.energyPerTickHover : this.energyPerTick;
        list.add(StringUtils.getEnergyUsageText(currentTickEnergy));
        list.add(StringUtils.getChargerRateText(this.energyPerTickOut));
        list.add(StringUtils.getParticlesText(this.getParticleType(itemStack)));
        list.add(StringUtils.BRIGHT_GREEN + StringUtils.translate("tooltip.jetpack.description.1"));
        list.add(StringUtils.BRIGHT_GREEN + StringUtils.translate("tooltip.fluxpack.description.1"));
        list.add(StringUtils.BRIGHT_GREEN + StringUtils.translate("tooltip.jetpack.description.2"));
        list.add(StringUtils.BRIGHT_BLUE + StringUtils.ITALIC + StringUtils.translate("tooltip.jetpackFluxPlate.controls"));
    }

}
