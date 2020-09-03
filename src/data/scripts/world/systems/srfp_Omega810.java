package data.scripts.world.systems;

import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.HeavyIndustry;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.SalvageSpecialAssigner;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import data.utils.srfp.I18nUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static data.scripts.world.srfpWorldGen.addMarketplace;

public class srfp_Omega810 {
    public void generate(SectorAPI sector) {
        //create a star system
        StarSystemAPI system = sector.createStarSystem("Omega810");
        //set its location
        system.getLocation().set(3500, 300);
        //set background image
        system.setBackgroundTextureFilename("graphics/backgrounds/background3.jpg");

        //the star
        PlanetAPI pg_Star = system.initStar("Omega", "star_yellow", 600f, 350f);
        //background light color
        system.setLightColor(new Color(255, 178, 31));

        //make asteroid belt surround it
        system.addAsteroidBelt(pg_Star, 100, 2200f, 150f, 180, 360, Terrain.ASTEROID_BELT, "");

        //a new planet for people
        PlanetAPI Neoamster = system.addPlanet("srfp_planet_Neoamster", pg_Star, I18nUtil.getStarSystemsString("planet_name_Neoamster"), "terran", 215, 120f, 4500f, 365f);

        //a new market for planet
        MarketAPI NeoamsterMarket = addMarketplace("srfp", Neoamster, null
                , Neoamster.getName(), 6,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_7, // population
                                Conditions.HABITABLE,
                                Conditions.FARMLAND_BOUNTIFUL,
                                Conditions.MILD_CLIMATE,
                                Conditions.RUINS_WIDESPREAD
                        )),
                new ArrayList<>(
                        Arrays.asList(
                                Submarkets.GENERIC_MILITARY,
                                Submarkets.SUBMARKET_BLACK,
                                Submarkets.SUBMARKET_OPEN,
                                Submarkets.SUBMARKET_STORAGE
                        )),
                new ArrayList<>(
                        Arrays.asList(
                                Industries.POPULATION,
                                Industries.MEGAPORT,
                                Industries.STARFORTRESS_MID,
                                Industries.FARMING,
                                Industries.PATROLHQ,
                                Industries.ORBITALWORKS,
                                Industries.WAYSTATION,
                                Industries.TECHMINING,
                                Industries.HEAVYBATTERIES
                        )),
                0.3f,
                false,
                true);
        //make a custom description which is specified in descriptions.csv
        Neoamster.setCustomDescriptionId("srfp_planet_Neoamster");
        //give the orbital works a gamma core
        NeoamsterMarket.getIndustry(Industries.ORBITALWORKS).setAICoreId(Commodities.GAMMA_CORE);
        //and give it a nanoforge
        ((HeavyIndustry) NeoamsterMarket.getIndustry(Industries.ORBITALWORKS)).setNanoforge(new SpecialItemData(Items.CORRUPTED_NANOFORGE, null));

        PlanetAPI Leningrad = system.addPlanet("srfp_planet_Leningrad", Neoamster, I18nUtil.getStarSystemsString("planet_name_Leningrad"), "barren", 90, 60, 1000f, 30);

        MarketAPI LeningradMarket = addMarketplace("srfp", Leningrad, null
                , Leningrad.getName(), 6,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_5,
                                Conditions.ORE_MODERATE,
                                Conditions.RARE_ORE_RICH,
                                Conditions.VOLATILES_PLENTIFUL
                        )),
                new ArrayList<>(
                        Arrays.asList(
                                Submarkets.SUBMARKET_BLACK,
                                Submarkets.SUBMARKET_OPEN,
                                Submarkets.SUBMARKET_STORAGE
                        )),
                new ArrayList<>(
                        Arrays.asList(
                                Industries.POPULATION,
                                Industries.SPACEPORT,
                                Industries.MINING,
                                Industries.HEAVYINDUSTRY,
                                Industries.LIGHTINDUSTRY,
                                Industries.REFINING
                        )),
                0.3f,
                true,
                true);
        Leningrad.setCustomDescriptionId("srfp_planet_Leningrad");
        ((HeavyIndustry) LeningradMarket.getIndustry(Industries.HEAVYINDUSTRY)).setNanoforge(new SpecialItemData(Items.CORRUPTED_NANOFORGE, null));

        // generates hyperspace destinations for in-system jump points
        system.autogenerateHyperspaceJumpPoints(true, true);

        // Debris
        DebrisFieldTerrainPlugin.DebrisFieldParams params = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                150f, // field radius - should not go above 1000 for performance reasons
                1f, // density, visual - affects number of debris pieces
                10000000f, // duration in days
                0f); // days the field will keep generating glowing pieces
        params.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
        params.baseSalvageXP = 500; // base XP for scavenging in field
        SectorEntityToken debris = Misc.addDebrisField(system, params, StarSystemGenerator.random);
        SalvageSpecialAssigner.assignSpecialForDebrisField(debris);

        // makes the debris field always visible on map/sensors and not give any xp or notification on being discovered
        debris.setSensorProfile(null);
        debris.setDiscoverable(null);

        // makes it discoverable and give 200 xp on being found
        // sets the range at which it can be detected (as a sensor contact) to 2000 units
        // commented out.
        debris.setDiscoverable(true);
        debris.setDiscoveryXP(200f);
        debris.setSensorProfile(1f);
        debris.getDetectedRangeMod().modifyFlat("gen", 2000);

        debris.setCircularOrbit(pg_Star, 45 + 10, 1600, 250);
        //Finally cleans up hyperspace
        cleanup(system);
    }

    //Learning from Tart scripts
    //Clean nearby Nebula(nearby system)
    private void cleanup(StarSystemAPI system) {
        HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin();
        NebulaEditor editor = new NebulaEditor(plugin);
        float minRadius = plugin.getTileSize() * 2f;

        float radius = system.getMaxRadiusInHyperspace();
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius * 0.5f, 0, 360f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f, 0.25f);
    }
}
