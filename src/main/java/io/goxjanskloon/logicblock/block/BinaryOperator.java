package io.goxjanskloon.logicblock.block;
import java.util.Collection;
import java.util.Iterator;
public abstract class BinaryOperator extends Operator{
    public final int requiredInputSize=2;
    public BinaryOperator(){super();}
    public BinaryOperator(boolean initValue,Collection<? extends Outputable> initInputs,Collection<? extends Inputable> initOutputs){super(initValue,initInputs,initOutputs);}
    @Override public int getRequiredInputSize(){return requiredInputSize;}
    @Override public boolean calculate(Collection<? extends Outputable> inputs){
        Iterator<? extends Outputable> i=inputs.iterator();
        return calculate(i.next().getValue(),i.next().getValue());
    }
    public abstract boolean calculate(boolean input1,boolean input2);
}