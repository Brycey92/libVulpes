package zmaster587.libVulpes.network;

import zmaster587.libVulpes.util.InputSyncHandler;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraft.client.Minecraft;

public class PacketChangeKeyState {

	public static void encode(PacketChangeKeyState pkt, PacketBuffer buf)
	{
		pkt.write(buf);
	}
	
	public static PacketChangeKeyState decode( PacketBuffer buf)
	{
		PacketChangeKeyState pkt = new PacketChangeKeyState();
		 
		pkt.read(buf);
		return pkt;
	}
	
	public static class Handler 
	{
		public static void handle(PacketChangeKeyState msg, Supplier<NetworkEvent.Context> ctx)
		{
			if(ctx.get().getDirection().getReceptionSide().isServer())
				ctx.get().enqueueWork(() -> msg.executeServer(ctx.get().getSender()));
			else
				ctx.get().enqueueWork(() -> msg.executeClient(Minecraft.getInstance().player));
			
			ctx.get().setPacketHandled(true);
			
		}
	}
	
	int key;
	boolean state;
	
	public PacketChangeKeyState(int key, boolean state) {
		this.key = key;
		this.state = state;
	}
	
	public PacketChangeKeyState() {};
	

	public void write(PacketBuffer out) {
		out.writeInt(key);
		out.writeBoolean(state);
	}


	public void readClient(PacketBuffer in) {
		in.readInt();
		in.readBoolean();
	}

	
	public void read(PacketBuffer in) {
		key = in.readInt();
		state = in.readBoolean();
	}

	
	public void executeClient(PlayerEntity thePlayer) {
		
	}

	
	public void executeServer(ServerPlayerEntity player) {
		InputSyncHandler.updateKeyPress(player, key, state);
	}

}
