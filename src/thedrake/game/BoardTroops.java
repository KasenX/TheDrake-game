package thedrake.game;

import java.io.PrintWriter;
import java.util.*;

public class BoardTroops implements JSONSerializable {
	private final PlayingSide playingSide;
	private final Map<BoardPos, TroopTile> troopMap;
	private final TilePos leaderPosition;
	private final int guards;
	
	public BoardTroops(PlayingSide playingSide) { 
		this.playingSide = playingSide;
		this.troopMap = Collections.emptyMap();
		this.leaderPosition = TilePos.OFF_BOARD;
		this.guards = 0;
	}
	
	public BoardTroops(
			PlayingSide playingSide,
			Map<BoardPos, TroopTile> troopMap,
			TilePos leaderPosition, 
			int guards) {
		this.playingSide = playingSide;
		this.troopMap = troopMap;
		this.leaderPosition = leaderPosition;
		this.guards = guards;
	}

	public Optional<TroopTile> at(TilePos pos) {
		return Optional.ofNullable(troopMap.get(pos));
	}
	
	public PlayingSide playingSide() {
		return playingSide;
	}
	
	public TilePos leaderPosition() {
		return leaderPosition;
	}

	public int guards() {
		return guards;
	}
	
	public boolean isLeaderPlaced() {
		return leaderPosition != TilePos.OFF_BOARD;
	}
	
	public boolean isPlacingGuards() {
		return leaderPosition != TilePos.OFF_BOARD && guards < 2;
	}	
	
	public Set<BoardPos> troopPositions() {
		Set<BoardPos> troopPositions = new HashSet<>();
		for (Map.Entry<BoardPos, TroopTile> entry : troopMap.entrySet())
			troopPositions.add(entry.getKey());
		return troopPositions;
	}

	public BoardTroops placeTroop(Troop troop, BoardPos target) {
		if (at(target).isPresent())
			throw new IllegalArgumentException();

		Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
		newTroops.put(target, new TroopTile(troop, playingSide, TroopFace.AVERS));

		if (!isLeaderPlaced())
			return new BoardTroops(playingSide, newTroops, target, guards);
		if (isPlacingGuards())
			return new BoardTroops(playingSide, newTroops, leaderPosition, guards + 1);

		return new BoardTroops(playingSide, newTroops, leaderPosition, guards);
	}
	
	public BoardTroops troopStep(BoardPos origin, BoardPos target) {
		if(!isLeaderPlaced()) {
			throw new IllegalStateException(
					"Cannot move troops before the leader is placed.");
		}

		if(isPlacingGuards()) {
			throw new IllegalStateException(
					"Cannot move troops before guards are placed.");
		}

		if(!at(origin).isPresent() || at(target).isPresent())
			throw new IllegalArgumentException();

		Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
		TroopTile tile = newTroops.remove(origin);
		newTroops.put(target, tile.flipped());

		TilePos newLeader = origin.equals(leaderPosition) ? target : leaderPosition;

		return new BoardTroops(playingSide, newTroops, newLeader, guards);
	}
	
	public BoardTroops troopFlip(BoardPos origin) {
		if(!isLeaderPlaced()) {
			throw new IllegalStateException(
					"Cannot move troops before the leader is placed.");			
		}
		
		if(isPlacingGuards()) {
			throw new IllegalStateException(
					"Cannot move troops before guards are placed.");			
		}
		
		if(!at(origin).isPresent())
			throw new IllegalArgumentException();
		
		Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
		TroopTile tile = newTroops.remove(origin);
		newTroops.put(origin, tile.flipped());

		return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
	}
	
	public BoardTroops removeTroop(BoardPos target) {
		if(!isLeaderPlaced()) {
			throw new IllegalStateException(
					"Cannot move troops before the leader is placed.");
		}

		if(isPlacingGuards()) {
			throw new IllegalStateException(
					"Cannot move troops before guards are placed.");
		}

		if(!at(target).isPresent())
			throw new IllegalArgumentException();

		Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
		newTroops.remove(target);

		TilePos newLeader = target.equals(leaderPosition) ? TilePos.OFF_BOARD : leaderPosition;

		return new BoardTroops(playingSide, newTroops, newLeader, guards);
	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.print("{\"side\":");
		playingSide.toJSON(writer);
		writer.print(",\"leaderPosition\":");
		leaderPosition.toJSON(writer);
		writer.printf(",\"guards\":%d", guards);
		writer.printf(",\"troopMap\":{");
		// Get the keys from map and convert it into sorted set
		int count = 0;
		for (BoardPos boardPos : new TreeSet<>(troopMap.keySet())) {
			boardPos.toJSON(writer);
			writer.printf(":");
			troopMap.get(boardPos).toJSON(writer);
			count++;
			if (count < troopMap.size())
				writer.printf(",");
		}
		writer.printf("}}");
	}
}
