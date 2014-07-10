package algorithms.localsearch;

public class VarInt {

	/**
	 * @param args
	 */
	int _id;
	int _value;
	int _min;// minimal value of the domain
	int _max;// maximal value of the domain
	boolean[] _forbiden;
	int _sz_forbiden;
	public int getValue(){ return _value;}
	public void setValue(int v){ _value = v;}
	public void setID(int id){
		_id = id;
	}
	public int getID(){
		return _id;
	}
	public VarInt(int id, int min, int max){
		_id = id;
		_min = min;
		_max = max;
		_sz_forbiden = _max-_min+1;
		_forbiden = new boolean[_sz_forbiden];
		for(int i = 0; i < _sz_forbiden; i++)
			_forbiden[i] = false;
		_value = _min;
	}
	public void disableValue(int v){
		if(v < _min || v > _max){
			System.out.println("VarIntTT.disableValue -> exception, value " + v + " is out of bound " + _min + ".." + _max);
			assert(false);
		}
		_forbiden[v-_min] = true;
	}
	public void enableValue(int v){
		if(v < _min || v > _max){
			System.out.println("VarIntTT.enableValue -> exception, value " + v + " is out of bound " + _min + ".." + _max);
			assert(false);
		}
		_forbiden[v-_min] = false;
	}
	int getMinValue(){ return _min;}
	int getMaxValue(){ return _max;}
	boolean isValue(int v){ 
		if(v < _min || v > _max){
			System.out.println("VarIntTT.isValue -> exception, value " + v + " is out of bound " + _min + ".." + _max);
			assert(false);
		}
		return _forbiden[v-_min] == false;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
