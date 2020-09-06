package data.missions.fsaltest;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets so we can add ships and fighter wings to them.
		// In this scenario, the fleets are attacking each other, but
		// in other scenarios, a fleet may be defending or trying to escape
		api.initFleet(FleetSide.PLAYER, "FSAL", FleetGoal.ATTACK, false, 10);
		api.initFleet(FleetSide.ENEMY, "HSS", FleetGoal.ATTACK, true, 10);

		// Set a small blurb for each fleet that shows up on the mission detail and
		// mission results screens to identify each side.
		api.setFleetTagline(FleetSide.PLAYER, "Anarchist Fleet");
		api.setFleetTagline(FleetSide.ENEMY, "Hegemony Fleet");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("歼灭霸主舰队.");
		
		// Friendly ships
		api.addToFleet(FleetSide.PLAYER, "srfp_labrador_assault", FleetMemberType.SHIP, "Ingolfur",true);
		api.addToFleet(FleetSide.PLAYER, "srfp_labrador_assault", FleetMemberType.SHIP, "Lawrence of Arabia",true);
		api.addToFleet(FleetSide.PLAYER, "srfp_tachanka_partisan", FleetMemberType.SHIP, "Dramatic",true);
		api.addToFleet(FleetSide.PLAYER, "srfp_tachanka_partisan", FleetMemberType.SHIP, "Anarchia Mama",true);
		api.addToFleet(FleetSide.PLAYER, "srfp_tachanka_partisan", FleetMemberType.SHIP, "Princess Noire", true);
		api.addToFleet(FleetSide.PLAYER, "srfp_kobayashimaru_standard", FleetMemberType.SHIP, "Don't Tread on Me", true);
		

		api.addToFleet(FleetSide.ENEMY, "dominator_XIV_Elite", FleetMemberType.SHIP, "HSS Jeanne d'Arc",true);
	

		
		// Set up the map.
		float width = 10000f;
		float height = 10000f;
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);
		
		float minX = -width/2;
		float minY = -height/2;
		
		// Add an asteroid field
		api.addAsteroidField(minX, minY + height / 2, 0, 8000f,
							 20f, 70f, 100);
		
		api.addPlanet(0, 0, 50f, "star_yellow", 250f, true);
		
	}

}
