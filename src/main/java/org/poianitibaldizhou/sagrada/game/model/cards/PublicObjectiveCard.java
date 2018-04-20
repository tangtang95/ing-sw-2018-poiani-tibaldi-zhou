package org.poianitibaldizhou.sagrada.game.model.cards;

import org.poianitibaldizhou.sagrada.exception.ConstraintTypeException;
import org.poianitibaldizhou.sagrada.game.model.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.NumberConstraint;

import java.util.*;

public abstract class PublicObjectiveCard extends Card implements IScore {

    private final ConstraintType type;
    private Set<IConstraint> constraints;
    private final int cardPoints;

    protected PublicObjectiveCard(String name, String description, int cardPoints) {
        super(name, description);
        type = ConstraintType.NONE;
        this.cardPoints = cardPoints;
    }

    protected PublicObjectiveCard(String name, String description, int cardPoints, Collection<IConstraint> constraints, ConstraintType type) throws ConstraintTypeException {
        super(name, description);

        //Check if Collection<IConstraint> has only one type of class and coincide with ConstraintType
        IConstraint[] array = (IConstraint[]) constraints.toArray();

        for (int i = 1; i < array.length; i++) {
            if(type == ConstraintType.COLOR) {
                if (!(array[i] instanceof ColorConstraint))
                    throw new ConstraintTypeException("Color constraint type error");
            }
            else if(type == ConstraintType.NUMBER) {
                if (!(array[i] instanceof NumberConstraint))
                    throw new ConstraintTypeException("Number constraint type error");
            }
            else
                throw new ConstraintTypeException("Constraint type not available");
        }
        this.type = type;
        this.cardPoints = cardPoints;
        this.constraints = new TreeSet();
        this.constraints.addAll(constraints);
    }


    public List<IConstraint> getConstraint() {
        return new ArrayList(constraints);
    }

    public ConstraintType getType() {
        return type;
    }

    public int getCardPoints() {
        return cardPoints;
    }

    public int containsConstraint(IConstraint other) {
        TreeSet set = (TreeSet) constraints;
        Iterator it = set.iterator();
        while(it.hasNext()){
            if (((IConstraint) it.next()).matches(other)) {
                return other.getIndexValue();
            }
        }
        return -1;
    }
}
