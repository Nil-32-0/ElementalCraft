package metafact.elementalcraft.spell;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import metafact.elementalcraft.entity.EntityHelper;
import metafact.elementalcraft.network.message.MessageHandler;
import metafact.elementalcraft.tag.ECTags;

import java.util.function.Supplier;

public record ChangeSpellMessage(int i) {

    public static ChangeSpellMessage decode(FriendlyByteBuf buf) {
        return new ChangeSpellMessage(buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(i);
    }


    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();

            if (player == null) {
                return;
            }
            EntityHelper.handStream(player)
                    .filter(stack -> stack.is(ECTags.Items.SPELL_CAST_TOOLS))
                    .findFirst()
                    .ifPresent(stack -> SpellHelper.setSelected(stack, i));
        });
        ctx.get().setPacketHandled(true);
    }

    public void send() {
        MessageHandler.CHANNEL.sendToServer(this);
    }

}
