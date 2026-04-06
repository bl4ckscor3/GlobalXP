package bl4ckscor3.mod.globalxp;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import bl4ckscor3.mod.globalxp.compat.GetTheOneProbe;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(GlobalXP.MODID)
@EventBusSubscriber
public class NeoEntrypoint implements Platform {
	private final Map<ResourceKey<? extends Registry<?>>, Map<String, DeferredRegister<?>>> registers = new HashMap<>();
	private final IEventBus modBus;

	public NeoEntrypoint(ModContainer modContainer, IEventBus modBus) {
		this.modBus = modBus;
		GlobalXP.initialize(this);
		modContainer.registerConfig(ModConfig.Type.CLIENT, Configuration.CLIENT_SPEC);
		modContainer.registerConfig(ModConfig.Type.SERVER, Configuration.SERVER_SPEC);
	}

	@SubscribeEvent
	public static void onInterModEnqueue(InterModEnqueueEvent event) {
		if (ModList.get().isLoaded("theoneprobe"))
			InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetTheOneProbe::new);
	}

	@SubscribeEvent
	public static void onCreativeModeTabBuildContents(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS || event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS)
			event.accept(GlobalXP.XP_BLOCK_ITEM.get());
	}

	@Override
	public void onXpChange(Player player, int amount) {
		NeoForge.EVENT_BUS.post(new PlayerXpEvent.XpChange(player, amount));
	}

	@Override
	public void onLevelChange(Player player, int experienceLevel) {
		NeoForge.EVENT_BUS.post(new PlayerXpEvent.LevelChange(player, player.experienceLevel));
	}

	@Override
	public <T extends BlockEntity> BlockEntityType<T> createBlockEntity(BlockEntityFactory<T> factory, Block validBlock) {
		return new BlockEntityType<>(factory::create, validBlock);
	}

	@Override
	public <R, T extends R> void register(ResourceKey<? extends Registry<R>> registry, Supplier<T> entry, Identifier id) {
		@SuppressWarnings("unchecked")
		DeferredRegister<R> register = (DeferredRegister<R>) registers.computeIfAbsent(
			registry,
			_ -> new HashMap<>()
		).computeIfAbsent(
			id.toString(),
			_ -> {
				DeferredRegister<R> r = DeferredRegister.create(registry, id.getNamespace());

				r.register(modBus);
				return r;
			}
		);
		register.register(id.getPath(), entry);
	}
}
