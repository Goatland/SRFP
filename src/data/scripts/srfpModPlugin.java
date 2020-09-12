package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import data.scripts.world.srfpWorldGen;
import exerelin.campaign.SectorManager;
import org.dark.shaders.light.LightData;
import org.dark.shaders.util.ShaderLib;
import org.dark.shaders.util.TextureData;

public class srfpModPlugin extends BaseModPlugin {
    @Override
    public void onApplicationLoad() throws Exception {
        //check if lazylib exists
        boolean hasLazyLib = Global.getSettings().getModManager().isModEnabled("lw_lazylib");
        if (!hasLazyLib) {
            throw new RuntimeException("星际革命:红与黑需要LazyLib!");
        }
        boolean hasMagicLib = Global.getSettings().getModManager().isModEnabled("MagicLib");
        if (!hasMagicLib) {
            throw new RuntimeException("星际革命:红与黑需要MagicLib!" +
                    "\n请前往 http://fractalsoftworks.com/forum/index.php?topic=13718.0下载");
        }
        boolean hasGraphicsLib = Global.getSettings().getModManager().isModEnabled("shaderLib");
        if (hasGraphicsLib) {
            ShaderLib.init();
            LightData.readLightDataCSV("data/lights/srfp_light_data.csv");
            TextureData.readTextureDataCSV("data/lights/srfp_texture_data.csv");
        }
        if (!hasGraphicsLib) {
            throw new RuntimeException("星际革命:红与黑需要GraphicsLib!" +
                    "\n请前往 http://fractalsoftworks.com/forum/index.php?topic=10982下载");
        }
    }
    @Override
    public void onNewGame() {
        //Nex compatibility setting, if there is no nex or corvus mode(Nex), just generate the system
        boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
        if (!haveNexerelin || SectorManager.getCorvusMode()) {
            new srfpWorldGen().generate(Global.getSector());
        }
    }
}
