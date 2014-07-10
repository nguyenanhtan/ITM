package algorithms.localsearch;

public class NotEqual implements IConstraint {
	private VarInt _x;
	private VarInt _y;
	private int _violations;
	
	public NotEqual(VarInt x, VarInt y){
		_x = x;
		_y = y;
	}
	
	@Override
	public int violations() {
		// TODO Auto-generated method stub
		return _violations;
	}

	@Override
	public int getAssignDelta(VarInt x, int val) {
		// TODO Auto-generated method stub
		int nv;
		if(x == _x){
			if(_y.getValue() == val) nv = 1; else nv = 0;
			return nv - _violations;
		}else if(x == _y){
			if(_x.getValue() == val) nv = 1; else nv = 0;
			return nv - _violations;
		}else{
			return 0;
		}
	}

	@Override
	public void propagate(VarInt x, int val) {
		// TODO Auto-generated method stub
		if(x == _x){
			if(_y.getValue() == val) _violations = 1; else _violations = 0;
		}else if(x == _y){
			if(_x.getValue() == val) _violations = 1; else _violations = 0;
		}else{
			//do nothing
		}
	}

	public void initPropagate(){
		if(_x.getValue() == _y.getValue())
			_violations = 1;
		else
			_violations = 0;
		
		//System.out.println("initPropagation x = " + _x.getValue() + " y = " + _y.getValue() + " violations = " + _violations);
	}
	
	public boolean verify(){
		int v;
		if(_x.getValue() == _y.getValue()) v = 1; else v = 0;
		//System.out.println("NotEqual.verify, x[" + _x.getID() + "] = " + _x.getValue() + " y[" + _y.getID() + "] = " + _y.getValue() + " violations = " + _violations);
		if(v != _violations) return false;
		return true;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
