package data.scripts.campaign.econ.impl;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.combat.MutableStat;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;

import java.awt.*;


public class srfp_miasite extends BaseIndustry {

    public static float SENSOR_BONUS = 400f;

    @Override
    public boolean isFunctional() {
        if (!Global.getSector().getFaction("srfp").isAtWorst("player", RepLevel.COOPERATIVE)) {
            return false;
        }
        if (!"srfp".equals(Misc.getCommissionFactionId())){
            return false;
        }
        boolean fun = false;
        for (Industry ind : market.getIndustries()) {
            if (ind == this) continue;
            if (!ind.isFunctional()) {
                fun = true;
                break;
            }
        }
        return fun;
    }

    @Override
    public void apply() {
        super.apply(true);

        int size = market.getSize();

        demand(Commodities.CREW, size - 6);
        demand(Commodities.SUPPLIES, size - 5);
        demand(Commodities.MARINES, size - 5);
        demand(Commodities.FUEL, size - 7);
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
            tooltip.addPara("本星系友军舰队传感距离增加: %s", 10f, h, "400f");
        }
    }

    @Override
    protected int getBaseStabilityMod() {
        return 2;
    }

    @Override
    public void advance(float amount) {
        if (market.getContainingLocation() == null) return;

        String id = getModId();
        for (CampaignFleetAPI fleet : market.getContainingLocation().getFleets()) {
            if (fleet.isInHyperspaceTransition()) continue;

            if (fleet.getFaction() == market.getFaction() || fleet.getFaction().isPlayerFaction()) {
                String desc = "内务部情报站";
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


    @Override
    public boolean isAvailableToBuild() {
        if (!Global.getSector().getFaction("srfp").isAtWorst("player", RepLevel.COOPERATIVE)) {
            return false;
        }
        if (!"srfp".equals(Misc.getCommissionFactionId())){
            return false;
        }
        boolean canBuild = false;
        for (Industry ind : market.getIndustries()) {
            if (ind == this) continue;
            if (!ind.isFunctional()) {
                canBuild = true;
                break;
            }
        }
        return canBuild;
    }

    @Override
    public String getUnavailableReason() {
        String str = "";
        if (!Global.getSector().getFaction("srfp").isAtWorst("player", RepLevel.COOPERATIVE)) {
            str = "需要和" + " " + Global.getSector().getFaction("srfp").getDisplayName() + " " + "达成合作关系";
        }
            str = str + "\n需要和"+" "+Global.getSector().getFaction("srfp").getDisplayName()+" "+"签订雇佣协议";
        return str;
    }

    public boolean showWhenUnavailable() { return true; }
}