package algorithms.localsearch;

public class AtMost implements IConstraint {
	private VarInt[] _x;
	private int _ub;
	private int _violations;
	private int[] _occ;// _occ[v] is the number of occurrences of the value
						// v+_min in _x;
	private int _szOcc;// size of
	private int _min;
	private int _max;

	public AtMost(VarInt[] x, int ub) {
		_x = x;
		_ub = ub;
		_min = 100000;
		_max = -100000;
		for (int i = 0; i < x.length; i++) {
			if (_x[i].getMinValue() < _min)
				_min = _x[i].getMinValue();
			if (_x[i].getMaxValue() > _max)
				_max = _x[i].getMaxValue();
		}
		_szOcc = _max - _min + 1;
		_occ = new int[_szOcc];
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
		int oldV = x.getValue();
		if (x.getValue() == val)
			return 0;
		if (_occ[oldV - _min] > _ub)
			d--;
		if (_occ[val - _min] >= _ub)
			d++;
		// System.out.println("AtMost::getAssignDelta(x[" + x.getID() + "]," +
		// val + ") = " + d);
		return d;
	}

	@Override
	public void propagate(VarInt x, int val) {
		// TODO Auto-generated method stub
		System.out.println("AtMost::propagate(x[" + x.getID() + "] = "
				+ x.getValue() + "," + val + ")");
		int oldV = x.getValue();
		if (oldV == val)
			return;
		_occ[oldV - _min]--;
		if (_occ[oldV - _min] >= _ub)
			_violations--;
		_occ[val - _min]++;
		if (_occ[val - _min] > _ub)
			_violations++;
	}

	@Override
	public void initPropagate() {
		// TODO Auto-generated method stub
		_violations = 0;
		System.out.println("AtMmost::initPropagate, _min = " + _min
				+ " _max = " + _max + " _szOcc = " + _szOcc);
		for (int i = 0; i < _szOcc; i++)
			_occ[i] = 0;
		for (int i = 0; i < _x.length; i++) {
			int v = _x[i].getValue();
			System.out.println("AtMmost::initPropagate, v = " + v);
			_occ[v - _min]++;
		}
		for (int i = 0; i < _szOcc; i++)
			if (_occ[i] > _ub)
				_violations += _occ[i] - _ub;
	}

	@Override
	public boolean verify() {
		System.out.println("AtMost::verify");
		// TODO Auto-generated method stub
		int[] occ = new int[_szOcc];
		for (int i = 0; i < _szOcc; i++)
			occ[i] = 0;
		for (int i = 0; i < _x.length; i++) {
			int v = _x[i].getValue();
			occ[v - _min]++;
		}
		int vio = 0;
		for (int i = 0; i < _szOcc; i++) {
			if (occ[i] != _occ[i]) {
				System.out.println("AtMost::verify --> failed, _occ[" + i
						+ "] = " + _occ[i] + " while recomputing occ = "
						+ occ[i]);
				return false;
			}
			if (occ[i] > _ub)
				vio += occ[i] - _ub;
		}
		if (vio != _violations) {
			System.out.println("AtMost::verify --> failed, _violations = "
					+ _violations + " while recomputing vio = " + vio);
			return false;
		}
		return true;
	}

	public void print() {
		int[] occ = new int[_szOcc];
		for (int i = 0; i < _szOcc; i++)
			occ[i] = 0;
		for (int i = 0; i < _x.length; i++) {
			int v = _x[i].getValue();
			occ[v - _min]++;
		}

		for (int i = 0; i < _x.length; i++)
			System.out.println("AtMost::print _x[" + i + "] = "
					+ _x[i].getValue());
		for (int v = 0; v < _szOcc; v++) {
			int vv = v + _min;
			System.out.println("AtMost::print _occ[" + vv + "] = " + _occ[v]
					+ " = occ[" + vv + "] = " + occ[v]);
		}
		System.out.println("AtMost::print, violations = " + violations());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
