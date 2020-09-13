package data.scripts.campaign.econ.impl;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.combat.MutableStat;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;

import java.awt.*;

public class srfp_miahq extends BaseIndustry {

    public static float SENSOR_BONUS = 800f;

    @Override
    public boolean isHidden() {
        return !market.getFactionId().equals("srfp");
    }

    @Override
    public boolean isFunctional() {
        return super.isFunctional() && market.getFactionId().equals("srfp");
    }

    @Override
    public void apply() {
        super.apply(true);

        int size = market.getSize();

        demand(Commodities.CREW, size - 5);
        demand(Commodities.SUPPLIES, size - 4);
        demand(Commodities.MARINES, size - 4);
        demand(Commodities.FUEL, size - 6);
        demand(Commodities.SHIPS, size - 7);
        demand(Commodities.ORGANS, size - 7);
        demand(Commodities.HAND_WEAPONS, size - 7);

        Pair<String, Integer> deficit = getMaxDeficit(
                Commodities.CREW,
                Commodities.SUPPLIES,
                Commodities.MARINES,
                Commodities.HEAVY_MACHINERY,
                Commodities.FUEL,
                Commodities.SHIPS,
                Commodities.ORGANS,
                Commodities.HAND_WEAPONS
        );

        modifyStabilityWithBaseMod();

        MemoryAPI memory = market.getMemoryWithoutUpdate();
        Misc.setFlagWithReason(memory, MemFlags.MARKET_PATROL, getModId(), true, -1);
        Misc.setFlagWithReason(memory, MemFlags.MARKET_MILITARY, getModId(), true, -1);

        if (!isFunctional()) {
            supply.clear();
        }
    }

    @Override
    public void unapply() {
        super.unapply();

        MemoryAPI memory = market.getMemoryWithoutUpdate();
        Misc.setFlagWithReason(memory, MemFlags.MARKET_PATROL, getModId(), false, -1);
        Misc.setFlagWithReason(memory, MemFlags.MARKET_MILITARY, getModId(), false, -1);

        unmodifyStabilityWithBaseMod();
    }

    protected boolean hasPostDemandSection(boolean hasDemand, IndustryTooltipMode mode) {
        return mode != IndustryTooltipMode.NORMAL || isFunctional();
    }


    @Override
    protected void addPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {
        Color h = Misc.getHighlightColor();
        if (mode != IndustryTooltipMode.NORMAL || isFunctional()) {
            addStabilityPostDemandSection(tooltip, hasDemand, mode);
            tooltip.addPara("本星系友军舰队传感距离增加: %s", 10f, h, "800f");
        }
    }

    @Override
    protected int getBaseStabilityMod() {
        return 5;
    }

    @Override
    public void advance(float amount) {
        if (market.getContainingLocation() == null) return;

        String id = getModId();
        for (CampaignFleetAPI fleet : market.getContainingLocation().getFleets()) {
            if (fleet.isInHyperspaceTransition()) continue;

            if (fleet.getFaction() == market.getFaction() || fleet.getFaction().isPlayerFaction()) {
                String desc = "情报中心";
                float bonus = SENSOR_BONUS;
                MutableStat.StatMod curr = fleet.getStats().getSensorRangeMod().getFlatBonus(id);
                if (curr == null || curr.value <= bonus) {
                    fleet.getStats().addTemporaryModFlat(0.1f, id,
                            desc, bonus,
                            fleet.getStats().getSensorRangeMod());
                }
            }
        }
    }


    public boolean isAvailableToBuild() {
        return false;
    }

    public boolean showWhenUnavailable() {
        return false;
    }

}
