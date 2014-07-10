package utils;
import java.util.*;

public class ConnectedComponents {

	/**
	 * @param args
	 */
	
	int[] ccIndex;
	boolean[] visited;
	int nbCC;
	Vector[] A;
	public ConnectedComponents(){
		
	}
	
	private void DFS(int v){
		ccIndex[v] = nbCC;
		visited[v] = true;
		for(int i = 0; i < A[v].size(); i++){
			int u = (Integer)A[v].get(i);
			if(!visited[u]){
				DFS(u);
			}
		}
	}
	public Vector<Vector<Integer>> computeConnectedComponents(Vector[] A){
		this.A = A;
		visited = new boolean[A.length];
		ccIndex = new int[A.length];
		nbCC = 0;
		for(int v = 0; v < A.length; v++){
			visited[v] = false;
		}
		
		for(int v = 0; v < A.length; v++){
			if(!visited[v]){
				nbCC++;
				DFS(v);
			}
		}
		Vector<Vector<Integer>> AllCC = new Vector<Vector<Integer>>();
		for(int c = 1; c <= nbCC; c++){
			Vector<Integer> CC = new Vector<Integer>();
			for(int v = 0; v < A.length; v++)
				if(ccIndex[v] == c){
					CC.add(v);
				}
			AllCC.add(CC);
		}
		return AllCC;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
