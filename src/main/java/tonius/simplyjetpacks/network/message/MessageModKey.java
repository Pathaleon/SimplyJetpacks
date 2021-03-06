package tonius.simplyjetpacks.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import tonius.simplyjetpacks.item.IModeSwitchable;
import tonius.simplyjetpacks.item.IToggleable;
import tonius.simplyjetpacks.setup.SJKey;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageModKey implements IMessage, IMessageHandler<MessageModKey, IMessage> {

    public int keyId;

    public MessageModKey() {
    }

    public MessageModKey(SJKey key) {
        this.keyId = key.ordinal();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.keyId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.keyId);
    }

    @Override
    public IMessage onMessage(MessageModKey msg, MessageContext ctx) {
        EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;

        if (entityPlayer != null) {
            ItemStack armor = entityPlayer.inventory.armorItemInSlot(2);
            if (armor != null) {
                if (msg.keyId == SJKey.TOGGLE.ordinal() && armor.getItem() instanceof IToggleable) {
                    ((IToggleable) armor.getItem()).toggle(armor, entityPlayer);
                } else if (msg.keyId == SJKey.MODE.ordinal() && armor.getItem() instanceof IModeSwitchable) {
                    ((IModeSwitchable) armor.getItem()).switchMode(armor, entityPlayer);
                }
            }
        }
        return null;
    }

}
