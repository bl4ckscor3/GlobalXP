package bl4ckscor3.mod.globalxp.theoneprobe;

import net.minecraftforge.fml.common.Loader;

public class MainCompatHandler
{
    public static void registerTOP()
    {
        if(Loader.isModLoaded("theoneprobe"))
            TOPCompatibility.register();
    }
}
