package data.scripts.world.systems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.impl.campaign.DerelictShipEntityPlugin;
import com.fs.starfarer.api.impl.campaign.econ.impl.HeavyIndustry;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.SalvageSpecialAssigner;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import data.utils.srfp.AddAdmin;
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
        system.getLocation().set(3750, 1050);
        //set background image
        system.setBackgroundTextureFilename("graphics/backgrounds/background3.jpg");

        //the star
        PlanetAPI om_Star = system.initStar("omega", "star_yellow", 600f, 350f);
        //background light color
        system.setLightColor(new Color(255, 178, 31));

        //make asteroid belt surround it
        system.addAsteroidBelt(om_Star, 100, 2200f, 150f, 180, 360, Terrain.ASTEROID_BELT, "");

        // Gate of Omega810
        SectorEntityToken gate = system.addCustomEntity("Omega810_gate", // unique id
                "Omega810之门", // name - if null, defaultName from custom_entities.json will be used
                "inactive_gate", // type of object, defined in custom_entities.json
                null); // faction

        gate.setCircularOrbit(system.getEntityById("omega"), 360*(float)Math.random(), 1500, 150f);

        system.addAsteroidBelt(om_Star, 300, 6000, 500, 290, 310, Terrain.ASTEROID_BELT,  "Omega810 Belt");
        system.addRingBand(om_Star, "misc", "rings_dust0", 256f, 3, Color.white, 256f, 4950, 375f, null, null);
        system.addRingBand(om_Star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 5050, 345f, null, null);


        PlanetAPI Vesuvio = system.addPlanet("srfp_planet_Vesuvio", om_Star, "Vesuvio", "lava", 40, 70, 1100f, 180);

        //Capital of Srfp
        PlanetAPI Neoamster = system.addPlanet("srfp_planet_Neoamster", om_Star, I18nUtil.getStarSystemsString("planet_name_Neoamster"), "terran", 215, 120f, 4000f, 380f);

        Neoamster.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "volturn"));
        Neoamster.getSpec().setGlowColor(new Color(255,255,255,255));
        Neoamster.getSpec().setUseReverseLightForGlow(true);
        Neoamster.applySpecChanges();

        //a moon for Neoamster

        PlanetAPI Haiya = system.addPlanet("srfp_planet_Haiya", Neoamster, "Haiya", "barren-bombarded", 20, 60, 800f, 43);
        Haiya.setCustomDescriptionId("srfp_planet_Haiya");

        //a new market for planet
        MarketAPI NeoamsterMarket = addMarketplace("srfp", Neoamster, null
                , Neoamster.getName(), 8,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_8, // population
                                Conditions.HABITABLE,
                                Conditions.FARMLAND_ADEQUATE,
                                Conditions.MILD_CLIMATE,
                                Conditions.RUINS_WIDESPREAD,
                                Conditions.ORE_ULTRARICH,
                                Conditions.RARE_ORE_ABUNDANT
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
                                Industries.MINING,
                                Industries.HEAVYBATTERIES,
                                Industries.REFINING,
                                "srfp_revolutionaryguard",
                                "srfp_miahq"
                        )),
                0.35f,
                false,
                true);

        //Add the Boss
        NeoamsterMarket = AddAdmin.assignCustomAdmin(
                NeoamsterMarket,
                FullName.Gender.MALE,
                Ranks.FACTION_LEADER,
                Ranks.POST_FACTION_LEADER,
                "Tashkent",
                "Soviet",
                "graphics/srfp/portraits/characters/tashkent.png",
                3,
                3);

        //make a custom description which is specified in descriptions.csv
        Neoamster.setCustomDescriptionId("srfp_planet_Neoamster");
        //give the orbital works a gamma core
        NeoamsterMarket.getIndustry(Industries.ORBITALWORKS).setAICoreId(Commodities.GAMMA_CORE);
        //and give it a nanoforge
        ((HeavyIndustry) NeoamsterMarket.getIndustry(Industries.ORBITALWORKS)).setNanoforge(new SpecialItemData(Items.CORRUPTED_NANOFORGE, null));

        PlanetAPI Georgia = system.addPlanet("srfp_planet_Georgia", om_Star, "Georgia", "toxic_cold", 180, 100, 5800f, 400);
        Georgia.setCustomDescriptionId("srfp_planet_Georgia");
        Georgia.getSpec().setGlowColor(new Color(245, 255, 250));
        Georgia.getSpec().setUseReverseLightForGlow(true);
        Georgia.getSpec().setCloudColor(new Color(220, 220, 200, 150));
        Georgia.applySpecChanges();

        PlanetAPI Jonesburg = system.addPlanet("srfp_planet_Jonesburg", om_Star, I18nUtil.getStarSystemsString("planet_name_Jonesburg"), "barren-desert", 90, 75, 8000f, 800);

        MarketAPI JonesburgMarket = addMarketplace("srfp", Jonesburg, null
                , Jonesburg.getName(), 4,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_4,
                                Conditions.ORE_MODERATE,
                                Conditions.RARE_ORE_RICH,
                                Conditions.VOLATILES_PLENTIFUL,
                                Conditions.RUINS_EXTENSIVE
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
                                Industries.SPACEPORT,
                                Industries.STARFORTRESS,
                                Industries.MINING,
                                Industries.HEAVYINDUSTRY,
                                Industries.LIGHTINDUSTRY,
                                Industries.REFINING,
                                Industries.TECHMINING,
                                Industries.HEAVYBATTERIES,
                                Industries.MILITARYBASE
                        )),
                0.35f,
                true,
                true);
        Jonesburg.setCustomDescriptionId("srfp_planet_Jonesburg");
        JonesburgMarket.getIndustry(Industries.HEAVYINDUSTRY).setAICoreId(Commodities.GAMMA_CORE);
        ((HeavyIndustry) JonesburgMarket.getIndustry(Industries.HEAVYINDUSTRY)).setNanoforge(new SpecialItemData(Items.CORRUPTED_NANOFORGE, null));

        //set Jumppoint
        JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint("omega_jump", "Omega810 Bridge");
        jumpPoint1.setCircularOrbit( system.getEntityById("omega"), 280, 4300, 380f);
        jumpPoint1.setRelatedPlanet(Neoamster);
        system.addEntity(jumpPoint1);

        // generates hyperspace destinations for in-system jump points
        system.autogenerateHyperspaceJumpPoints(true, true);

        // Omega810 Relay
        SectorEntityToken omega_relay = system.addCustomEntity("omega_relay", // unique id
                null, // name - if null, defaultName from custom_entities.json will be used
                "comm_relay", // type of object, defined in custom_entities.json
                "srfp"); // faction
        omega_relay.setCircularOrbitPointingDown(system.getEntityById("omega"), 150, 4500f, 240);

        // Omega810 Nav Buoy
        SectorEntityToken omega_nav = system.addCustomEntity("omega_nav", // unique id
                null, // name - if null, defaultName from custom_entities.json will be used
                "nav_buoy", // type of object, defined in custom_entities.json
                "srfp"); // faction
        omega_nav.setCircularOrbitPointingDown(system.getEntityById("omega"), 75, 6000f, 300);

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

        addDerelict(system, om_Star, "srfp_kanikosen_standard", ShipRecoverySpecial.ShipCondition.GOOD, 2250f, true);

        debris.setCircularOrbit(om_Star, 45 + 10, 1600, 250);

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

    //Learning from Harmful Mechanic's Dassault-Mikoyan Engineering
    private void addDerelict(StarSystemAPI system,
                             SectorEntityToken focus,
                             String variantId,
                             ShipRecoverySpecial.ShipCondition condition,
                             float orbitRadius,
                             boolean recoverable) {
        DerelictShipEntityPlugin.DerelictShipData params = new DerelictShipEntityPlugin.DerelictShipData(new ShipRecoverySpecial.PerShipData(variantId, condition), false);
        SectorEntityToken ship = BaseThemeGenerator.addSalvageEntity(system, Entities.WRECK, Factions.NEUTRAL, params);
        ship.setDiscoverable(true);

        float orbitDays = orbitRadius / (10f + (float) Math.random() * 5f);
        ship.setCircularOrbit(focus, (float) Math.random() * 360f, orbitRadius, orbitDays);

        if (recoverable) {
            SalvageSpecialAssigner.ShipRecoverySpecialCreator creator = new SalvageSpecialAssigner.ShipRecoverySpecialCreator(null, 0, 0, false, null, null);
            Misc.setSalvageSpecial(ship, creator.createSpecial(ship, null));
        }
    }
}