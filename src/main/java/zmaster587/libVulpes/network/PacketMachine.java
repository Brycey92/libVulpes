package zmaster587.libVulpes.network;

import java.util.function.Supplier;

import zmaster587.libVulpes.util.INetworkMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketMachine {

	INetworkMachine machine;

	CompoundNBT nbt;

	byte packetId;

	public PacketMachine() {
		nbt = new CompoundNBT();
	};

	public PacketMachine(INetworkMachine machine, byte packetId) {
		this();
		this.machine = machine;
		this.packetId = packetId;
	}

	
	public static void encode(PacketMachine pkt, PacketBuffer buf)
	{
		pkt.write(buf);
	}
	
	public static PacketMachine decode( PacketBuffer buf)
	{
		PacketMachine pkt = new PacketMachine();
		 
		pkt.read(buf);
		return pkt;
	}
	
	public static class Handler 
	{
		public static void handle(PacketMachine msg, Supplier<NetworkEvent.Context> ctx)
		{
			if(ctx.get().getDirection().getReceptionSide().isServer())
				ctx.get().enqueueWork(() -> msg.executeServer(ctx.get().getSender()));
			else
				ctx.get().enqueueWork(() -> msg.executeClient(Minecraft.getInstance().player));
			
			ctx.get().setPacketHandled(true);
			
		}
	}
	
	public void write(PacketBuffer outline) {
		// dimension
		BasePacket.writeWorld(outline, ((TileEntity)machine).getWorld());
		outline.writeInt(((TileEntity)machine).getWorld().func_230315_m_().func_241513_m_());
		outline.writeInt(((TileEntity)machine).getPos().getX());
		outline.writeInt(((TileEntity)machine).getPos().getY());
		outline.writeInt(((TileEntity)machine).getPos().getZ());

		outline.writeByte(packetId);

		machine.writeDataToNetwork(outline, packetId);
	}

	@OnlyIn(value=Dist.CLIENT)
	public void readClient(PacketBuffer in) {
		//DEBUG:
		BasePacket.readWorld(in);
		World world = Minecraft.getInstance().world;
		int x = in.readInt();
		int y = in.readInt();
		int z = in.readInt();
		packetId = in.readByte();

		TileEntity ent = world.getTileEntity(new BlockPos(x, y, z));

		if(ent != null && ent instanceof INetworkMachine) {
			machine = (INetworkMachine)ent;
			machine.readDataFromNetwork(in, packetId, nbt);
		}
		else {
			//Error
		}
	}

	
	public void read(PacketBuffer in) {
		World world = BasePacket.readWorld(in);

		int x = in.readInt();
		int y = in.readInt();
		int z = in.readInt();
		packetId = in.readByte();

		BlockPos pos = new BlockPos(x,y,z);
		Chunk chunk = world.getChunkAt(pos);
		
		if(chunk != null && world.isBlockLoaded(pos)) {
			TileEntity ent = world.getTileEntity(pos);

			if(ent != null && ent instanceof INetworkMachine) {
				machine = (INetworkMachine)ent;
				machine.readDataFromNetwork(in, packetId, nbt);
			}
			else {
				//Error
			}
		}
	}

	public void executeClient(PlayerEntity player) {
		//Machine can be null if not all chunks are loaded
		if(machine != null)
			machine.useNetworkData(player, Dist.CLIENT, packetId, nbt);
	}

	public void executeServer(ServerPlayerEntity player) {
		if(machine != null)
			machine.useNetworkData(player, Dist.DEDICATED_SERVER, packetId, nbt);
	}

	public void execute(PlayerEntity player, Dist side) {
		if(machine != null)
			machine.useNetworkData(player, side, packetId, nbt);
	}

}
