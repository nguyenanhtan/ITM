package algorithms.localsearch;

import java.util.Vector;

public class ConstraintSystem implements IConstraint {

    private Vector<IConstraint> _S;
    private int _violations;

    public ConstraintSystem() {
        _S = new Vector<IConstraint>();
    }

    public int size(){
    	return _S.size();
    }
    public IConstraint getConstraint(int idx){
    	return _S.elementAt(idx);
    }
    @Override
    public int violations() {
        // TODO Auto-generated method stub
        return _violations;
    }

    @Override
    public int getAssignDelta(VarInt x, int val) {
        // TODO Auto-generated method stub
        int d = 0;
        for (int i = 0; i < _S.size(); i++) {
            d = d + _S.elementAt(i).getAssignDelta(x, val);
        }
        return d;
    }

    @Override
    public void propagate(VarInt x, int val) {
        // TODO Auto-generated method stub
        _violations = 0;
        for (int i = 0; i < _S.size(); i++) {
            _S.elementAt(i).propagate(x, val);
            _violations += _S.elementAt(i).violations();
        }
    }

    @Override
    public void initPropagate() {
        // TODO Auto-generated method stub
        _violations = 0;
        for (int i = 0; i < _S.size(); i++) {
            _S.elementAt(i).initPropagate();
            _violations += _S.elementAt(i).violations();
        }
    }

    public void close() {
        initPropagate();
    }

    public void post(IConstraint c) {
        _S.add(c);
    }

    public boolean verify() {
        for (int i = 0; i < _S.size(); i++) {
            if (!_S.elementAt(i).verify()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }
}
