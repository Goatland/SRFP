package data.scripts.world.systems;

import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.HeavyIndustry;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import data.utils.srfp.I18nUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static data.scripts.world.srfpWorldGen.addMarketplace;

public class srfp_Epsilon201 {
    public void generate(SectorAPI sector) {
        //create a star system
        StarSystemAPI system = sector.createStarSystem("Epsilon201");
        //set its location
        system.getLocation().set(4133, 2500);
        //set background image
        system.setBackgroundTextureFilename("graphics/backgrounds/background5.jpg");

        //the star
        PlanetAPI ep_Star = system.initStar("epsilon201", "star_red_dwarf",350f,150,3f,0.2f,0.1f);

        system.addAsteroidBelt(ep_Star, 50, 3500, 500, 150, 170, Terrain.ASTEROID_BELT,  "Epsilon201 Belt");
        system.addRingBand(ep_Star, "misc", "rings_dust0", 256f, 3, Color.white, 256f, 3450, 130f, null, null);
        system.addRingBand(ep_Star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 3550, 140f, null, null);

        //a new planet for people
        PlanetAPI Aume = system.addPlanet("srfp_planet_Aume", ep_Star, I18nUtil.getStarSystemsString("planet_name_Aume"), "terran", 105, 105f, 2500f, 228f);

        //a new market for planet
        MarketAPI AumeMarket = addMarketplace("srfpB", Aume, null
                , Aume.getName(), 6,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_6, // population
                                Conditions.HABITABLE,
                                Conditions.FARMLAND_RICH,
                                Conditions.MILD_CLIMATE,
                                Conditions.RUINS_WIDESPREAD,
                                Conditions.ORE_MODERATE
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
                                Industries.STARFORTRESS,
                                Industries.FARMING,
                                Industries.HIGHCOMMAND,
                                Industries.ORBITALWORKS,
                                Industries.WAYSTATION,
                                Industries.MINING,
                                Industries.LIGHTINDUSTRY,
                                Industries.HEAVYBATTERIES
                        )),
                0.25f,
                true,
                true);
        //make a custom description which is specified in descriptions.csv
        Aume.setCustomDescriptionId("srfp_planet_Aume");
        //give the orbital works a gamma core
        AumeMarket.getIndustry(Industries.ORBITALWORKS).setAICoreId(Commodities.GAMMA_CORE);
        //and give it a nanoforge
        ((HeavyIndustry) AumeMarket.getIndustry(Industries.ORBITALWORKS)).setNanoforge(new SpecialItemData(Items.CORRUPTED_NANOFORGE, null));

        PlanetAPI Makhnovtchina = system.addPlanet("srfp_planet_Makhnovtchina", ep_Star, I18nUtil.getStarSystemsString("planet_name_Makhnovtchina"), "gas_giant", 90, 200, 6500f, 700);
        Makhnovtchina.setCustomDescriptionId("srfp_planet_Makhnovtchina");

        // Add fixed conditions to Makhnovtchina.
        Misc.initConditionMarket(Makhnovtchina);
        Makhnovtchina.getMarket().addCondition(Conditions.EXTREME_WEATHER);
        Makhnovtchina.getMarket().addCondition(Conditions.HIGH_GRAVITY);
        Makhnovtchina.getMarket().addCondition(Conditions.TOXIC_ATMOSPHERE);
        Makhnovtchina.getMarket().getFirstCondition(Conditions.EXTREME_WEATHER).setSurveyed(true);

        PlanetAPI Stirner = system.addPlanet("srfp_planet_Stirner", Makhnovtchina, I18nUtil.getStarSystemsString("planet_name_Stirner"), "cryovolcanic", 90, 70, 1000f, 120);
        Stirner.setCustomDescriptionId("srfp_planet_Stirner");

        //a new market for planet
        MarketAPI StirnerMarket = addMarketplace("srfpB", Stirner, null
                , Stirner.getName(), 5,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_5, // population
                                Conditions.VERY_COLD,
                                Conditions.VOLATILES_PLENTIFUL,
                                Conditions.RARE_ORE_ABUNDANT,
                                Conditions.ORE_MODERATE
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
                                Industries.STARFORTRESS,
                                Industries.PATROLHQ,
                                Industries.ORBITALWORKS,
                                Industries.WAYSTATION,
                                Industries.MINING,
                                Industries.REFINING
                        )),
                0.25f,
                true,
                true);

        StirnerMarket.getIndustry(Industries.ORBITALWORKS).setAICoreId(Commodities.GAMMA_CORE);
        //and give it a nanoforge
        ((HeavyIndustry) StirnerMarket.getIndustry(Industries.ORBITALWORKS)).setNanoforge(new SpecialItemData(Items.CORRUPTED_NANOFORGE, null));

        //give the orbital works a gamma core
        StirnerMarket.getIndustry(Industries.ORBITALWORKS).setAICoreId(Commodities.GAMMA_CORE);
        //and give it a nanoforge
        ((HeavyIndustry) StirnerMarket.getIndustry(Industries.ORBITALWORKS)).setNanoforge(new SpecialItemData(Items.CORRUPTED_NANOFORGE, null));

        // Makhnov Space Stations
        SectorEntityToken makhnovindStation = system.addCustomEntity("makhnov_ind", "Makhnov综合商贸空间站", "station_lowtech1", "independent");
        makhnovindStation.setCircularOrbitPointingDown(system.getEntityById("srfp_planet_Makhnovtchina"), 90, 400, 32);

        MarketAPI MahknovMarket = addMarketplace("independent", makhnovindStation, null
                , makhnovindStation.getName(), 4,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_4, // population
                                Conditions.VERY_COLD,
                                Conditions.VICE_DEMAND
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
                                Industries.STARFORTRESS,
                                Industries.PATROLHQ,
                                Industries.WAYSTATION,
                                Industries.REFINING,
                                Industries.FUELPROD
                        )),
                0.3f,
                true,
                true);

        makhnovindStation.setCustomDescriptionId("station_makhnovind");

        // Epsilon201 Relay
        SectorEntityToken epsilon_relay = system.addCustomEntity("epsilon_relay", // unique id
                null, // name - if null, defaultName from custom_entities.json will be used
                "comm_relay", // type of object, defined in custom_entities.json
                "srfpB"); // faction
        epsilon_relay.setCircularOrbitPointingDown(system.getEntityById("epsilon201"), 120, 2500f, 228f);

        // Epsilon201 Nav Buoy
        SectorEntityToken epsilon_nav = system.addCustomEntity("epsilon_nav", // unique id
                null, // name - if null, defaultName from custom_entities.json will be used
                "nav_buoy", // type of object, defined in custom_entities.json
                "srfpB"); // faction
        epsilon_nav.setCircularOrbitPointingDown(system.getEntityById("epsilon201"), 75, 5000f, 300);


        // generates hyperspace destinations for in-system jump points
        system.autogenerateHyperspaceJumpPoints(true, true);

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