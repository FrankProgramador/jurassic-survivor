package com.jogo.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.jogo.main.Game;


public class AStar {
	
	public static double lastTime = System.currentTimeMillis();
	public static Comparator<Node> nodeSorter = new Comparator<Node>() {
		
		@Override
		public int compare(Node n0, Node n1) {
			if(n1.fCost < n0.fCost) {
				return + 1;
			}
			if(n1.fCost > n0.fCost) {
				return - 1;
			}
			return 0;
		}
	};
	
	
	public static boolean clear() {
		if(System.currentTimeMillis() - lastTime >= 1000) {
			return true;
		}
		return false;
	}
	
	public static List<Node> findPath(World world, Vector2i start, Vector2i end){
		lastTime = System.currentTimeMillis();
		List<Node> openList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		
		Node current = new Node(start, null, 0, getDistance(start, end));
		
		openList.add(current);
		int count = 0;
		while(openList.size() > 0) {
			
			count++;
			if(count > 70) {
				count = 0;
				break;
			}
			
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			if(current.tile.equals(end)) {
				List<Node> path = new ArrayList<Node>();
				while(current.parent != null) {
					path.add(current);
					current = current.parent;
				}
				openList.clear();
				closedList.clear();
				return path;
			}
			
			openList.remove(current);
			closedList.add(current);
			
			for(int i = 0; i < 9; i++) {
				if(i == 4) continue;
				
				int x = current.tile.x;
				int y = current.tile.y;
				int xi = (i%3) -1;
				int yi = (i/3) -1;

				
				
				Tile tile = World.getTile(x+xi+((y+yi)*World.WIDTH));
				
				
				if(tile == null) continue;
				if(tile instanceof WallTile) continue;
				
				
				
				if(i == 0) {
					Tile test = World.getTile(x+xi+1+((y+yi)*World.WIDTH));
					Tile test2 = World.getTile(x+xi+((y+yi+1)*World.WIDTH)); 
					if(test instanceof WallTile || test2 instanceof WallTile || test == null || test2 == null) {
						continue;
					}
				
				}else if(i == 2) {
					Tile test = World.getTile(x+xi-1+((y+yi)*World.WIDTH));
					Tile test2 = World.getTile(x+xi+((y+yi+1)*World.WIDTH)); 
					if(test instanceof WallTile || test2 instanceof WallTile || test == null || test2 == null) {
						continue;
					}
				
				}else if(i == 6) {
					Tile test = World.getTile(x+xi+((y+yi-1)*World.WIDTH));
					Tile test2 = World.getTile(x+xi+1+((y+yi)*World.WIDTH)); 
					if(test instanceof WallTile || test2 instanceof WallTile || test == null || test2 == null) {
						continue;
					}
				
				}else if(i == 8) {
					Tile test = World.getTile(x+xi+((y+yi-1)*World.WIDTH));
					Tile test2 = World.getTile(x+xi-1+((y+yi)*World.WIDTH)); 
					if(test instanceof WallTile || test2 instanceof WallTile || test == null || test2 == null) {
						continue;
					}
				
				}
				
				

				
				Vector2i a = new Vector2i(x + xi, y + yi);
				double gCost = current.gCost + getDistance(current.tile, a);
				double hCost = getDistance(a, end);
				
				Node node = new Node(a, current, gCost, hCost);
				
				if(vecInList(closedList, a) && gCost >= current.gCost) continue;
				
				if(!vecInList(openList, a)) {
					openList.add(node);
				}else if(gCost < current.gCost) {
					openList.remove(current);
					openList.add(node);
				}
				
			}
		}
		
		closedList.clear();
		return null;
	}
	
	
	private static boolean vecInList(List<Node> list, Vector2i vector) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).tile.equals(vector)) {
				return true;
			}
		}
		return false;
	}
	

	private static double getDistance(Vector2i tile, Vector2i goal) {
		double dx = tile.x - goal.x;
		double dy = tile.y - goal.y;
		
		return Math.sqrt(dx*dx + dy*dy);
		
	}

}
