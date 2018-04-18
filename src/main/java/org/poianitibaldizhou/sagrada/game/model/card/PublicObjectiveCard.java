package org.poianitibaldizhou.sagrada.game.model.card;

import org.poianitibaldizhou.sagrada.exception.ConstraintMixedException;
import org.poianitibaldizhou.sagrada.game.model.IConstraint;

import java.util.*;

public abstract class PublicObjectiveCard extends Card implements IScore {

    protected Set<IConstraint> constraints;
    private int scorePoints;

    protected PublicObjectiveCard(String name, String description) {
        super(name, description);
    }

    public PublicObjectiveCard(String name, String description, Collection<IConstraint> constraints) throws ConstraintMixedException {
        super(name, description);

        //Check if Collection<IConstraint> has only one type of class
        IConstraint[] array = (IConstraint[]) constraints.toArray();
        for (int i = 1; i < array.length; i++) {
            if(!array[0].getClass().getSimpleName().equals(array[i].getClass().getSimpleName()))
                throw new ConstraintMixedException();
        }
        this.constraints = new TreeSet();
        this.constraints.addAll(constraints);
    }

    public List<IConstraint> getConstraint() {
        return new ArrayList(constraints);
    }

    public int getCardPoints() {
        return scorePoints;
    }

    public int containsConstraint(IConstraint other){
        TreeSet set = (TreeSet) constraints;
        Iterator it = set.iterator();
        while(it.hasNext()){
            if(((IConstraint)it.next()).matches(other))
                return other.getValue();
        }
        return -1;
    }

}
