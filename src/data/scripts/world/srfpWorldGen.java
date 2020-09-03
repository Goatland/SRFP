package data.scripts.world;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import data.scripts.world.systems.srfp_Omega810;

import java.util.ArrayList;

public class srfpWorldGen implements SectorGeneratorPlugin {
    //Shorthand function for adding a market, just copy it
    public static MarketAPI addMarketplace(String factionID, SectorEntityToken primaryEntity, ArrayList<SectorEntityToken> connectedEntities, String name,
                                           int size, ArrayList<String> marketConditions, ArrayList<String> submarkets, ArrayList<String> industries, float tarrif,
                                           boolean freePort, boolean withJunkAndChatter) {
        EconomyAPI globalEconomy = Global.getSector().getEconomy();
        String planetID = primaryEntity.getId();
        String marketID = planetID + "_market";

        MarketAPI newMarket = Global.getFactory().createMarket(marketID, name, size);
        newMarket.setFactionId(factionID);
        newMarket.setPrimaryEntity(primaryEntity);
        newMarket.getTariff().modifyFlat("generator", tarrif);

        //Adds submarkets
        if (null != submarkets) {
            for (String market : submarkets) {
                newMarket.addSubmarket(market);
            }
        }

        //Adds market conditions
        for (String condition : marketConditions) {
            newMarket.addCondition(condition);
        }

        //Add market industries
        for (String industry : industries) {
            newMarket.addIndustry(industry);
        }

        //Sets us to a free port, if we should
        //newMarket.setFreePort(freePort);

        //Adds our connected entities, if any
        if (null != connectedEntities) {
            for (SectorEntityToken entity : connectedEntities) {
                newMarket.getConnectedEntities().add(entity);
            }
        }

        globalEconomy.addMarket(newMarket, withJunkAndChatter);
        primaryEntity.setMarket(newMarket);
        primaryEntity.setFaction(factionID);

        if (null != connectedEntities) {
            for (SectorEntityToken entity : connectedEntities) {
                entity.setMarket(newMarket);
                entity.setFaction(factionID);
            }
        }

        //Finally, return the newly-generated market
        return newMarket;
    }

    @Override
    public void generate(SectorAPI sector) {
        //Add faction to bounty system
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("srfp");
        initFactionRelationships(sector);
        //Generate your system
        new srfp_Omega810().generate(sector);
    }
    public static void initFactionRelationships(SectorAPI sector)
    {
        FactionAPI srfp = sector.getFaction("srfp");
        FactionAPI srfpB = sector.getFaction("srfpB");
        FactionAPI hegemony = sector.getFaction(Factions.HEGEMONY);
        FactionAPI tritachyon = sector.getFaction(Factions.TRITACHYON);
        FactionAPI pirates = sector.getFaction(Factions.PIRATES);
        FactionAPI independent = sector.getFaction(Factions.INDEPENDENT);
        FactionAPI kol = sector.getFaction(Factions.KOL);
        FactionAPI church = sector.getFaction(Factions.LUDDIC_CHURCH);
        FactionAPI path = sector.getFaction(Factions.LUDDIC_PATH);
        FactionAPI diktat = sector.getFaction(Factions.DIKTAT);
        FactionAPI league = sector.getFaction(Factions.PERSEAN);
        FactionAPI neutral = sector.getFaction(Factions.NEUTRAL);
        FactionAPI remnants = sector.getFaction(Factions.REMNANTS);


        //FactionAPI sra = sector.getFaction("shadow_industry");
        //FactionAPI birdy = sector.getFaction("blackrock_driveyards");
        //FactionAPI thi = sector.getFaction("tiandong");


        srfp.setRelationship(hegemony.getId(), RepLevel.WELCOMING);
        srfp.setRelationship(tritachyon.getId(), RepLevel.INHOSPITABLE);
        srfp.setRelationship(pirates.getId(), RepLevel.INHOSPITABLE);
        srfp.setRelationship(independent.getId(), RepLevel.FAVORABLE);
        srfp.setRelationship(kol.getId(), RepLevel.HOSTILE);
        srfp.setRelationship(church.getId(), RepLevel.NEUTRAL);
        srfp.setRelationship(path.getId(), RepLevel.VENGEFUL);
        srfp.setRelationship(diktat.getId(), RepLevel.HOSTILE);
        srfp.setRelationship(league.getId(), RepLevel.WELCOMING);
        srfp.setRelationship(remnants.getId(), RepLevel.HOSTILE);

        srfp.setRelationship("blackrock_driveyards", RepLevel.WELCOMING);
        srfp.setRelationship("tiandong", RepLevel.NEUTRAL);
        srfp.setRelationship("diableavionics", RepLevel.HOSTILE);

        srfpB.setRelationship(srfp.getId(), RepLevel.COOPERATIVE);
        srfpB.setRelationship(path.getId(), RepLevel.VENGEFUL);
        srfpB.setRelationship(diktat.getId(), RepLevel.HOSTILE);
        srfpB.setRelationship(remnants.getId(), RepLevel.VENGEFUL);
        srfpB.setRelationship(pirates.getId(), RepLevel.VENGEFUL);
        srfpB.setRelationship(league.getId(), RepLevel.NEUTRAL);
        srfpB.setRelationship(independent.getId(), RepLevel.FAVORABLE);
        srfpB.setRelationship(church.getId(), RepLevel.INHOSPITABLE);
        srfpB.setRelationship(pirates.getId(), RepLevel.NEUTRAL);

        srfpB.setRelationship("blackrock_driveyards", RepLevel.WELCOMING);
        srfpB.setRelationship("tiandong", RepLevel.NEUTRAL);
        srfpB.setRelationship("diableavionics", RepLevel.HOSTILE);
    }
}

