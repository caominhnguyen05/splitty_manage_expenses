package client.data_structures;

import commons.Participant;

import java.util.ArrayList;
import java.util.List;

public class Deleteable {

    private List<Participant> participantList;
    private List<Boolean> deleteOrNotBooleans;
    private List<String> partNames;

    /**
     * Constructor for Deleteable object, sets #(participants) booleans to false
     * @param participantList   the list of participants currently in the event
     */
    public Deleteable(List<Participant> participantList) {
        this.participantList = participantList;
        this.deleteOrNotBooleans = new ArrayList<>();
        for (int i = 0; i < participantList.size(); i++) {
            deleteOrNotBooleans.add(false);
        }

        this.partNames = participantList.stream()
                .map(participant -> participant.name)
                .toList();
    }

    /**
     * Returns the boolean associated to the provided participant
     * @param name  the name to check
     * @return  the boolean value
     */
    public boolean getDelete(String name) {
        int index = partNames.indexOf(name);
        return this.deleteOrNotBooleans.get(index);
    }

    /**
     * Sets a certain participant boolean to true or false
     * @param name  the participant to set
     * @param deleteOrNot   the boolean to set it as
     */
    public void setDelete(String name, boolean deleteOrNot) {
        int index = partNames.indexOf(name);
        this.deleteOrNotBooleans.set(index, deleteOrNot);
    }

    /**
     * Getter for participant list
     * @return participantList field
     */
    public List<Participant> getParticipantList() {
        return this.participantList;
    }

    /**
     * Getter for booleans list of every participant
     * @return deleteOrNotBooleans field
     */
    public List<Boolean> getDeleteOrNotBooleans() {
        return this.deleteOrNotBooleans;
    }

    /**
     * Getter for the participant names list
     * @return partNames field
     */
    public List<String> getPartNames() {
        return this.partNames;
    }
}
