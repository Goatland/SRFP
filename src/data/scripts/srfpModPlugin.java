package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import data.scripts.world.srfpWorldGen;
import exerelin.campaign.SectorManager;

public class srfpModPlugin extends BaseModPlugin {
    @Override
    public void onApplicationLoad() throws Exception {
        //check if lazylib exists
        boolean hasLazyLib = Global.getSettings().getModManager().isModEnabled("lw_lazylib");
        if (!hasLazyLib) {
            throw new RuntimeException("SRFP requires LazyLib!");
        }
        boolean hasMagicLib = Global.getSettings().getModManager().isModEnabled("MagicLib");
        if (!hasMagicLib) {
            throw new RuntimeException("SRFP requires MagicLib! Where is the magic?" +
                    "\nGet it at http://fractalsoftworks.com/forum/index.php?topic=13718.0");
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
