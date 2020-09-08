package data.scripts.world.systems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.impl.campaign.ids.Terrain;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin.DebrisFieldParams;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin.DebrisFieldSource;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.MagneticFieldTerrainPlugin;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static data.scripts.world.srfpWorldGen.addMarketplace;

public class srfp_Dragon444 {
    public void generate(SectorAPI sector) {

        StarSystemAPI system = sector.createStarSystem("Dragon444");
        system.getLocation().set(3853, 3000);

        system.setBackgroundTextureFilename("graphics/backgrounds/background5.jpg");

        PlanetAPI dr_star = system.initStar("Dragon444", // unique id for this star
                "star_yellow",  // id in planets.json
                660f,          // radius (in pixels at default zoom)
                700,          // corona radius, from star edge
                8, 0.5f, 3f);// solar wind/flares/CR loss

        system.setLightColor(new Color(214, 255, 216)); // light color in entire system, affects all entities

        PlanetAPI dr_star2 = system.addPlanet("Dragon445", dr_star, "Dragon445", "star_blue_giant", 50, 900, 9000, 700);


        PlanetAPI Shultzeburg = system.addPlanet("srfp_Shultzeburg", dr_star, "Shultzeburg", "jungle", 50, 180, 3000, 260);
        Shultzeburg.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "volturn"));
        Shultzeburg.getSpec().setGlowColor(new Color(255, 255, 255, 255));
        Shultzeburg.getSpec().setUseReverseLightForGlow(true);
        Shultzeburg.applySpecChanges();
        Shultzeburg.setCustomDescriptionId("planet_Shultzeburg");

        MarketAPI ShultzeburgMarket = addMarketplace("srfp", Shultzeburg, null
                , Shultzeburg.getName(), 6,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_7, // population
                                Conditions.JUNGLE,
                                Conditions.FARMLAND_RICH,
                                Conditions.HOT,
                                Conditions.HABITABLE,
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
                                Industries.ORBITALSTATION,
                                Industries.FARMING,
                                Industries.PATROLHQ,
                                Industries.WAYSTATION,
                                Industries.MINING,
                                Industries.LIGHTINDUSTRY
                        )),
                0.35f,
                false,
                false);

        // Morrison and magnetic fields ---------------
        PlanetAPI morrison = system.addPlanet("srfp_planet_morrison", dr_star, "morrison", "gas_giant", 300, 360, 6500, 240);
        morrison.getSpec().setPitch(35f);
        morrison.getSpec().setPlanetColor(new Color(200,235,245,255));
        morrison.getSpec().setAtmosphereColor(new Color(210,240,250,250));
        morrison.getSpec().setCloudColor(new Color(220,250,240,200));
        morrison.getSpec().setIconColor(new Color(100,130,120,255));
        morrison.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "aurorae"));
        morrison.getSpec().setGlowColor(new Color(225,45,225,145));
        morrison.getSpec().setUseReverseLightForGlow(true);
        morrison.getSpec().setAtmosphereThickness(0.5f);
        morrison.applySpecChanges();

        system.addRingBand(morrison, "misc", "rings_dust0", 256f, 3, Color.white, 256f, 520, 45f, Terrain.RING, "Morrison Rings");
        system.addRingBand(morrison, "misc", "rings_dust0", 256f, 2, Color.white, 256f, 540, 60, null, null);

        morrison.setCustomDescriptionId("srfp_planet_morrison");

        SectorEntityToken morrison_field1 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                new MagneticFieldTerrainPlugin.MagneticFieldParams(200f, // terrain effect band width
                        460, // terrain effect middle radius
                        morrison, // entity that it's around
                        360f, // visual band start
                        560f, // visual band end
                        new Color(50, 30, 100, 60), // base color
                        1f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
                        new Color(50, 20, 110, 130),
                        new Color(150, 30, 120, 150),
                        new Color(200, 50, 130, 190),
                        new Color(250, 70, 150, 240),
                        new Color(200, 80, 130, 255),
                        new Color(75, 0, 160),
                        new Color(127, 0, 255)
                ));
        morrison_field1.setCircularOrbit(morrison, 0, 0, 100);

        SectorEntityToken morrison_field2 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                new MagneticFieldTerrainPlugin.MagneticFieldParams(120f, // terrain effect band width
                        680, // terrain effect middle radius
                        morrison, // entity that it's around
                        640f, // visual band start
                        760f, // visual band end
                        new Color(50, 30, 100, 30), // base color
                        0.6f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
                        new Color(50, 20, 110, 130),
                        new Color(150, 30, 120, 150),
                        new Color(200, 50, 130, 190),
                        new Color(250, 70, 150, 240),
                        new Color(200, 80, 130, 255),
                        new Color(75, 0, 160),
                        new Color(127, 0, 255)
                ));
        morrison_field2.setCircularOrbit(morrison, 0, 0, 100);

        // Morrison gets 1 orbiting derelict station, one lost station, and ideally plenty of debris
        SectorEntityToken dragonStation1 = system.addCustomEntity("dragon_abandoned_station1",
                "废弃的生态空间站", "station_side06", "neutral");

        dragonStation1.setCircularOrbitPointingDown( system.getEntityById("srfp_planet_morrison"), 45, 350, 50);

        Misc.setAbandonedStationMarket("dragon_abandoned_station1_market", dragonStation1);

        dragonStation1.setCustomDescriptionId("dragon_station1");
        dragonStation1.setInteractionImage("illustrations", "abandoned_station3");


        DebrisFieldParams dr_params = new DebrisFieldParams(
                500f, // field radius - should not go above 1000 for performance reasons
                1f, // density, visual - affects number of debris pieces
                10000000f, // duration in days
                0f); // days the field will keep generating glowing pieces
        dr_params.source = DebrisFieldSource.MIXED;
        dr_params.baseSalvageXP = 250; // base XP for scavenging in field
        SectorEntityToken debrisNextToBelt = Misc.addDebrisField(system, dr_params, StarSystemGenerator.random);
        debrisNextToBelt.setSensorProfile(null);
        debrisNextToBelt.setDiscoverable(null);
        debrisNextToBelt.setCircularOrbit(dr_star, 210f, 1000f, 320f);
        debrisNextToBelt.setId("dr_debris");

        SectorEntityToken dragonleagStation = system.addCustomEntity("dragon_leag", "Dragon444 贸易站", "station_side02", "srfpB");
        dragonleagStation.setCircularOrbitWithSpin(dr_star, 135, 4000f, 300f, 3f, 5f);

        MarketAPI dragonleagMarket = addMarketplace("srfpB", dragonleagStation, null
                , dragonleagStation.getName(), 5,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_5, // population
                                Conditions.FRONTIER
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
                                Industries.MINING
                        )),
                0.3f,
                true,
                true);



        SectorEntityToken pirate1Station = system.addCustomEntity("srfp_pirate1", "Dragon445采矿站", "station_lowtech1", "pirates");
        pirate1Station.setCircularOrbitPointingDown(system.getEntityById("Dragon445"), 90, 3500, 290);
        pirate1Station.setCustomDescriptionId("station_pirate1");

        system.addAsteroidBelt(dr_star2, 150, 3500, 500, 150, 290, Terrain.ASTEROID_BELT,  "Epsilon201 Belt");
        system.addRingBand(dr_star2, "misc", "rings_dust0", 256f, 3, Color.white, 256f, 3450, 130f, null, null);
        system.addRingBand(dr_star2, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 3550, 140f, null, null);

        system.addRingBand(dr_star2, "misc", "rings_dust0", 256f, 0, Color.white, 256f, 2000, 240f);

        MarketAPI pirate1Market = addMarketplace("pirates", pirate1Station, null
                , pirate1Station.getName(), 4,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_4 // population
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
                                Industries.SPACEPORT
                        )),
                0.25f,
                true,
                false);

        // Dragon444 SRFP Relay
        SectorEntityToken epsilon_relay = system.addCustomEntity("dragon_relay", // unique id
                null, // name - if null, defaultName from custom_entities.json will be used
                "comm_relay", // type of object, defined in custom_entities.json
                "srfp"); // faction
        epsilon_relay.setCircularOrbitPointingDown(system.getEntityById("Dragon444"), 90, 3200, 245f);

        system.autogenerateHyperspaceJumpPoints(true, true);

        cleanup(system);
    }

    private void cleanup(StarSystemAPI system) {
        HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin();
        NebulaEditor editor = new NebulaEditor(plugin);
        float minRadius = plugin.getTileSize() * 2f;

        float radius = system.getMaxRadiusInHyperspace();
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius * 0.5f, 0, 360f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f, 0.25f);
    }
}
