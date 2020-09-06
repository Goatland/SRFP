package data.utils.srfp;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.Skills;

public class AddAdmin {
    public static MarketAPI assignCustomAdmin (MarketAPI market, FullName.Gender gender, String rankid, String postid, String first, String last, String portrait, int fleetskill, int planetaryskill) {
        if (market != null) {
            PersonAPI person = Global.getFactory().createPerson();
            person.setFaction(market.getFactionId());
            person.setGender(gender);
            person.setRankId(rankid);
            person.setPostId(postid);
            person.getName().setFirst(first);
            person.getName().setLast(last);
            person.setPortraitSprite(portrait);
            person.getStats().setSkillLevel(Skills.FLEET_LOGISTICS, fleetskill);
            person.getStats().setSkillLevel(Skills.PLANETARY_OPERATIONS, planetaryskill);

            market.setAdmin(person);
            market.getCommDirectory().addPerson(person, 0);
            market.addPerson(person);
        }
        return (market);
    }
}
