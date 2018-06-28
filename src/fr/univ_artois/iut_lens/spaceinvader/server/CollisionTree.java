package fr.univ_artois.iut_lens.spaceinvader.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import fr.univ_artois.iut_lens.spaceinvader.server.entities.Entity;
import fr.univ_artois.iut_lens.spaceinvader.util.Rectangle;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class CollisionTree {

	private int MAX_OBJECTS = 4;
	private int MAX_LEVELS = 7;

	private int level;
	private String path;
	private List<Entity> objects;
	private Rectangle bounds;
	private Vector2d center;
	private CollisionTree[] nodes;
	private CollisionTree parent;
	
	private int size = 0;

	public CollisionTree(Rectangle pBounds) {
		this("", 0, pBounds, null);
	}

	/*
	 * Constructor
	 */
	private CollisionTree(String path, int pLevel, Rectangle pBounds, CollisionTree p) {
		this.path = path;
		level = pLevel;
		objects = new ArrayList<>();
		setBounds(pBounds);
		nodes = new CollisionTree[4];
		parent = p;
	}

	private void setBounds(Rectangle b) {
		bounds = b;
		center = bounds.getCenter();
	}

	/*
	 * Clears the quadtree
	 */
	public synchronized void clear() {
		objects.clear();
		for (int i = 0; i < nodes.length; i++)
			nodes[i] = null;
		size = 0;
	}

	/*
	 * Splits the node into 4 subnodes
	 */
	private synchronized void split() {
		double subWidth = bounds.width / 2;
		double subHeight = bounds.height / 2;
		double x = bounds.x;
		double y = bounds.y;

		nodes[0] = new CollisionTree(path + "-0", level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight), this); // top right
		nodes[1] = new CollisionTree(path + "-1", level + 1, new Rectangle(x, y, subWidth, subHeight), this); // top left
		nodes[2] = new CollisionTree(path + "-2", level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight), this); // bottom left
		nodes[3] = new CollisionTree(path + "-3", level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight), this); // bottom right
	}
	
	
	private synchronized void merge() {
		if (nodes[0] != null) {
			for (int i = 0; i < 4; i++) {
				nodes[i].merge();
				objects.addAll(nodes[i].objects);
				nodes[i] = null;
			}
		}
	}

	/*
	 * Determine which node the object belongs to. -1 means object cannot completely
	 * fit within a child node and is part of the parent node
	 */
	private int getIndex(Entity e) {
		Rectangle pRect = e.getBoundingBox();
		// Object can completely fit within the top quadrants
		boolean topQuadrant = (pRect.y < center.y
				&& pRect.y + pRect.height < center.y);

		// Object can completely fit within the bottom quadrants
		boolean bottomQuadrant = (pRect.y > center.y);

		if (pRect.x < center.x && pRect.x + pRect.width < center.x) {
			// Object can completely fit within the left quadrants
			if (topQuadrant)
				return 1;
			else if (bottomQuadrant)
				return 2;
		} else if (pRect.x > center.x) { // Object can completely fit within the right quadrants
			if (topQuadrant)
				return 0;
			else if (bottomQuadrant)
				return 3;
		}

		return -1;
	}

	/*
	 * Insert the object into the quadtree. If the node exceeds the capacity, it
	 * will split and add all objects to their corresponding nodes.
	 */
	public void add(Entity pRect) {
		boolean hasSubNode;
		
		synchronized (this) {
			size++;
			hasSubNode = nodes[0] != null;
		}
		
		if (hasSubNode) {
			int index = getIndex(pRect);

			if (index != -1) {
				nodes[index].add(pRect);
				return;
			}
		}

		synchronized (this) {
			objects.add(pRect);
			splitIfNecessary();
		}
	}
	
	
	
	
	public void addAll(List<Entity> addList) {
		if (addList.isEmpty())
			return;
		size += addList.size();
		
		if (nodes[0] != null) {
			Map<Integer, List<Entity>> addMap = addList.stream().collect(Collectors.groupingBy(e -> getIndex(e)));
			for (int i = 0; i < 4; i++) {
				if (addMap.containsKey(i)) {
					nodes[i].addAll(addMap.get(i));
				}
			}
			
			
			
			if (addMap.containsKey(-1))
				addList = addMap.get(-1);
			else
				addList = new ArrayList<>(0);
			
		}
		
		synchronized (this) {
			objects.addAll(addList);
			splitIfNecessary();
		}
	}
	
	
	
	private void splitIfNecessary() {
		if (nodes[0] == null && objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
			split();
			
			for (int i = 0; i < objects.size();) {
				int index = getIndex(objects.get(i));
				if (index != -1)
					nodes[index].add(objects.remove(i));
				else
					i++;
			}
		}
	}
	
	
	
	
	
	
	public synchronized int size() {
		return size;
	}
	
	
	
	
	public void computeCollision(boolean parallel) {
		boolean hasSubNodes;
		
		synchronized (this) {
			if (size == 0)
				return;
			for (int i = 0; i < objects.size(); i++) {
				Entity e1 = objects.get(i);
				if (!e1.plannedToRemoved())
					computeCollision(e1, i);
			}
			
			hasSubNodes = nodes[0] != null;
		}
		
		if (hasSubNodes) {
			if (parallel) {
				List<Future<?>> futures = new ArrayList<>(4);
				for (int i = 0; i < 4; i++) {
					int I = i;
					futures.add(ForkJoinTask.adapt(() -> nodes[I].computeCollision(true)).fork());
				}
				for (Future<?> f : futures) {
					try {
						f.get();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			else {
				for (int i = 0; i < 4; i++) {
					nodes[i].computeCollision(false);
				}
			}
		}
	}
	
	
	
	private void computeCollision(Entity e1, int i) {
		boolean hasSubNodes;
		
		synchronized (this) {
			if (size == 0)
				return;
			for (int j = i + 1; j < objects.size(); j++) {
				Entity e2 = objects.get(j);
				if (!e2.plannedToRemoved() && e1.collidesWith(e2)) {
					e2.collidedWith(e1);
					e1.collidedWith(e2);
				}
			}
			
			hasSubNodes = nodes[0] != null;
		}
		
		
		if (hasSubNodes) {
			int index = getIndex(e1);
			if (index == -1) {
				nodes[0].computeCollision(e1, -1);
				nodes[1].computeCollision(e1, -1);
				nodes[2].computeCollision(e1, -1);
				nodes[3].computeCollision(e1, -1);
			}
			else
				nodes[index].computeCollision(e1, -1);
		}
	}
	
	
	
	
	
	
	public void draw(GraphicsContext g) {
		g.setStroke(Color.rgb(0, 255, 255, 0.5));
		g.setFill(g.getStroke());
		g.setTextAlign(TextAlignment.LEFT);
		g.setTextBaseline(VPos.TOP);
		if (level == 0) {
			g.strokeRect(bounds.x, bounds.y, bounds.width, bounds.height);
		}

		if (nodes[0] != null) {
			g.strokeLine(bounds.x, center.y, bounds.x + bounds.width, center.y);
			g.strokeLine(center.x, bounds.y, center.x, bounds.y + bounds.height);
			nodes[0].draw(g);
			nodes[1].draw(g);
			nodes[2].draw(g);
			nodes[3].draw(g);
		}
		else {
			g.fillText(size + "", bounds.x + 2, bounds.y + 2);
		}
	}
	
	
	
	
	
	public synchronized void updatePositions() {
		if (size == 0)
			return;
		
		if (nodes[0] != null) {
			nodes[0].updatePositions();
			nodes[1].updatePositions();
			nodes[2].updatePositions();
			nodes[3].updatePositions();
		}
		
		for (int i = 0; i < objects.size();) {
			int index = getIndex(objects.get(i));
			if (parent != null && !bounds.isInside(objects.get(i).getBoundingBox())) {
				parent.objects.add(objects.remove(i));
				size--;
			}
			else if (index != -1 && nodes[index] != null) {
				nodes[index].add(objects.remove(i));
			}
			else
				i++;
		}
		
		if (size < MAX_OBJECTS)
			merge();
	}
	
	
	
	public synchronized void removeAllReally(List<Entity> remList) {
		if (remList.isEmpty())
			return;
		if (size == 0)
			return;

		size = 0;
		
		objects.removeAll(remList);
		
		size += objects.size();

		if (nodes[0] != null) {
			for (int i = 0; i < 4; i++) {
				nodes[i].removeAllReally(remList);
				size += nodes[i].size;
			}
		}
		
		if (size < MAX_OBJECTS)
			merge();
	}
	
	
	public synchronized void removeAll(List<Entity> remList) {
		if (remList.isEmpty())
			return;
		if (size == 0)
			return;
		
		size = 0;
		
		if (nodes[0] != null) {
			Map<Integer, List<Entity>> remMap = remList.stream().collect(Collectors.groupingBy(e -> getIndex(e)));
			for (int i = 0; i < 4; i++) {
				if (remMap.containsKey(i)) {
					nodes[i].removeAll(remMap.get(i));
				}
				size += nodes[i].size;
			}
			
			if (remMap.containsKey(-1))
				remList = remMap.get(-1);
			
		}
		
		objects.removeAll(remList);
		size += objects.size();

		if (size < MAX_OBJECTS)
			merge();
	}
	
	
	public synchronized boolean remove(Entity e) {
		if (size == 0)
			return false;
		int index = getIndex(e);
		boolean rem = false;

		boolean hasSubNode;
		
		hasSubNode = nodes[0] != null;
		
		if (hasSubNode && index != -1) {
			rem = nodes[index].remove(e);
		}
		if (!rem) {
			rem = objects.remove(e);
		}
		
		if (rem)
			size--;

		if (size < MAX_OBJECTS)
			merge();
		
		return rem;
	}
	
	public synchronized boolean removeReally(Entity e) {
		if (size == 0)
			return false;
		return objects.remove(e) || nodes[0].removeReally(e) || nodes[1].removeReally(e) || nodes[2].removeReally(e) || nodes[3].removeReally(e);
	}
	
	public synchronized void forEach(Consumer<? super Entity> action) {
		if (size == 0)
			return;
		for (int i = 0; i < objects.size(); i++)
			action.accept(objects.get(i));
		if (nodes[0] != null) {
			nodes[0].forEach(action);
			nodes[1].forEach(action);
			nodes[2].forEach(action);
			nodes[3].forEach(action);
		}
	}
	
	
	@Override
	public synchronized String toString() {
		String ret = "";
		String tab = tab(level), tab2 = tab(level + 1);
		ret += tab + path + " CollisionNode size=" + size + " (" + bounds.x + "," + bounds.y + ") -> (" + (bounds.x + bounds.width) + "," + (bounds.y + bounds.height) + ")\n";
		for (Entity e : objects) {
			ret += tab2 + "> " + e + "\n";
		}
		if (nodes[0] != null) {
			for (int i = 0; i < 4; i++) {
				ret += nodes[i];
			}
		}
		return ret;
	}
	
	private static String tab(int level) { String t = ""; for (int i = 0; i < level; i++) t += "  "; return t; }
	
	
	
	public synchronized Spliterator<Entity> spliterator() {
		return new CollisionTreeSpliterator(this);
	}
	
	public Stream<Entity> stream() {
		return StreamSupport.stream(spliterator(), false);
	}
	
	public Stream<Entity> parallelStream() {
		return StreamSupport.stream(spliterator(), true);
	}
	
	
	
	private static class CollisionTreeSpliterator implements Spliterator<Entity> {
		
		private final List<Spliterator<Entity>> subSpliterator;
		
		private CollisionTreeSpliterator(CollisionTree n) {
			subSpliterator = new ArrayList<>(5);
			subSpliterator.add(n.objects.spliterator());
			if (n.nodes[0] != null) {
				subSpliterator.add(n.nodes[0].spliterator());
				subSpliterator.add(n.nodes[1].spliterator());
				subSpliterator.add(n.nodes[2].spliterator());
				subSpliterator.add(n.nodes[3].spliterator());
			}
		}
		
		private CollisionTreeSpliterator(List<Spliterator<Entity>> subS) {
			subSpliterator = subS;
		}
		
		
		@Override
		public boolean tryAdvance(Consumer<? super Entity> action) {
			if (subSpliterator.isEmpty())
				return false;
			
			boolean advanced = false;
			do {
				advanced = subSpliterator.get(0).tryAdvance(action);
				if (!advanced)
					subSpliterator.remove(0);
			} while(!advanced && !subSpliterator.isEmpty());
			
			return advanced;
		}
		
		@Override
		public Spliterator<Entity> trySplit() {
			if (subSpliterator.isEmpty())
				return null;
			if (subSpliterator.size() == 1)
				return subSpliterator.get(0).trySplit();
			long currSize = estimateSize();
			long retSize = 0;
			List<Spliterator<Entity>> retSplit = new ArrayList<>(4);
			while (retSize < currSize && subSpliterator.size() > 1) {
				Spliterator<Entity> s = subSpliterator.remove(subSpliterator.size() - 1);
				retSplit.add(s);
				long sSize = s.estimateSize();
				currSize -= sSize;
				retSize += sSize;
			}
			return new CollisionTreeSpliterator(retSplit);
		}
		
		@Override
		public long estimateSize() {
			int count = 0;
			for (Spliterator<Entity> s : subSpliterator) {
				count += s.estimateSize();
			}
			return count;
		}
		
		@Override
		public int characteristics() {
			return SIZED | SUBSIZED;
		}
		
	}
	
	
	
}
