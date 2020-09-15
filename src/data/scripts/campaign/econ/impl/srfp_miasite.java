package data.scripts.campaign.econ.impl;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.combat.MutableStat;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.impl.campaign.procgen.SalvageEntityGenDataSpec.DropData;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.SalvageEntity;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lazywizard.lazylib.campaign.MessageUtils.showMessage;


public class srfp_miasite extends BaseIndustry {

    public static float SENSOR_BONUS = 400f;

    @Override
    public boolean isFunctional() {
        if (market.getFactionId().equals("srfp")) {return true;}
        if (!Global.getSector().getFaction("srfp").isAtWorst("player", RepLevel.COOPERATIVE)) {
            return false;
        }
        return "srfp".equals(Misc.getCommissionFactionId());
    }

    @Override
    public boolean isHidden() {
        if (market.getFactionId().equals("srfp")) {return false;}
        if (!Global.getSector().getFaction("srfp").isAtWorst("player", RepLevel.COOPERATIVE)) {
            return true;
        }
        return !"srfp".equals(Misc.getCommissionFactionId());
    }

    @Override
    public void apply() {
        super.apply(true);

        int size = market.getSize();

        demand(Commodities.CREW, size - 6);
        demand(Commodities.SUPPLIES, size - 5);
        demand(Commodities.MARINES, size - 5);
        demand(Commodities.FUEL, size - 6);
        demand(Commodities.SHIPS, size - 7);
        demand(Commodities.ORGANS, size - 7);

        supply(Commodities.HAND_WEAPONS, size - 6);
        supply(Commodities.DRUGS, size - 5);

        Pair<String, Integer> deficit = getMaxDeficit(
                Commodities.CREW,
                Commodities.SUPPLIES,
                Commodities.MARINES,
                Commodities.FUEL,
                Commodities.SHIPS,
                Commodities.ORGANS
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

    @Override
    public String getCurrentImage() {
        return super.getCurrentImage();
    }

    protected boolean hasPostDemandSection(boolean hasDemand, IndustryTooltipMode mode) {
        return mode != IndustryTooltipMode.NORMAL || isFunctional();
    }

    @Override
    protected void addPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {
        Color h = Misc.getHighlightColor();
        float opad = 10f;
        String str = "中等";
        if (mode != IndustryTooltipMode.NORMAL || isFunctional()) {
            addStabilityPostDemandSection(tooltip, hasDemand, mode);
            tooltip.addPara("本星系友军舰队传感距离增加: %s", 10f, h, "400f");
        }
        if (market.getSize() > 7){str = "很高";}
        if (market.getSize() < 5){str = "很低";}
        tooltip.addPara("因为殖民地市场大小为"+market.getSize()+",将有 %s 的几率缴获稀有走私品",opad,h,str);
    }

    @Override
    protected int getBaseStabilityMod() {
        return 2;
    }

    @Override
    public CargoAPI generateCargoForGatheringPoint(Random random) {
        if (!isFunctional()) return null;

        List<DropData> dropRandom = new ArrayList<>();
        List<DropData> dropValue = new ArrayList<>();

        DropData d = new DropData();
        d.chances = 1;
        d.group = "ai_cores3";
        dropRandom.add(d);

        d = new DropData();
        d.chances = 1;
        d.group = "rare_tech_low";
        dropRandom.add(d);

        CargoAPI result = SalvageEntity.generateSalvage(random, 1f, 1f, (market.getSize()), 1f, dropValue, dropRandom);

        if (result.isEmpty()){
            showMessage(market.getName() + "没有的内务委员会查获任何稀有走私品!");
        }
        if (result.getCommodityQuantity("alpha_core") != 0){
            showMessage(market.getName() + "的内务委员会查获了"+ (int)result.getCommodityQuantity("alpha_core") +"个阿尔法AI核心");
        }
        if (result.getCommodityQuantity("beta_core") != 0){
            showMessage(market.getName() + "的内务委员会查获了"+ (int)result.getCommodityQuantity("beta_core") +"个贝塔AI核心");
        }
        if (result.getCommodityQuantity("gamma_core") != 0){
            showMessage(market.getName() + "的内务委员会查获了"+ (int)result.getCommodityQuantity("gamma_core") +"个伽马AI核心");
        }
        if (result.getQuantity(CargoAPI.CargoItemType.SPECIAL, Items.CORRUPTED_NANOFORGE) != 0){
            showMessage(market.getName() + "的内务委员会查获了"+ (int)result.getQuantity(CargoAPI.CargoItemType.SPECIAL, Items.CORRUPTED_NANOFORGE) +"个受损的纳米锻炉");
        }
        if (result.getQuantity(CargoAPI.CargoItemType.SPECIAL, Items.PRISTINE_NANOFORGE) != 0){
            showMessage(market.getName() + "的内务委员会查获了"+ (int)result.getQuantity(CargoAPI.CargoItemType.SPECIAL, Items.PRISTINE_NANOFORGE) +"个受损的纳米锻炉");
        }
        if (result.getQuantity(CargoAPI.CargoItemType.SPECIAL, Items.SYNCHROTRON) != 0){
            showMessage(market.getName() + "的内务委员会查获了"+ (int)result.getQuantity(CargoAPI.CargoItemType.SPECIAL, Items.SYNCHROTRON) +"个受损的纳米锻炉");
        }
        return result;
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