package me.reckter.linkesList;

import me.reckter.Util;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 7/31/13
 * Time: 3:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class linkedListItem {
	private int value;
	private int id;

	public linkedListItem next;
	public linkedListItem last;

	public linkedListItem next100;
	public linkedListItem last100;

	public linkedListItem next10000;
	public linkedListItem last10000;

	public linkedListItem(int id) {
		this.id = id;
		next = null;
		last = null;

		next100 = null;
		last100 = null;

		next10000 = null;
		last10000 = null;
	}

	public linkedListItem(int id, linkedListItem last) {
		this(id);

		this.last = last;
		if (this.last != null) {
			this.last.next = this;

			if (this.last.last100 != null) {
				this.last100 = this.last.last100.next;
				this.last100.next100 = this;

				if (this.last.last10000 != null) {
					this.last10000 = this.last.last10000.next;
					this.last10000.next10000 = this;
				} else if (this.id >= 9999) {
					this.last10000 = this.get(this.id - 9999);
				}
			} else if (this.id >= 99) {
				this.last100 = this.get(this.id - 99);
			}
		}


	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public int delete() {
		if (this.last != null)
			this.last.next = this.next;

		if (this.next != null)
			this.next.last = this.last;


		if (this.last100 != null)
			this.last100.next100 = this.next;

		if (this.next100 != null)
			this.next100.last100 = this.last;

		if (this.last10000 != null)
			this.last10000.next10000 = this.next;

		if (this.next10000 != null)
			this.next10000.last10000 = this.last;
		return this.id;
	}

	public linkedListItem get(int id) throws IndexOutOfBoundsException {
		int[] lastIds = new int[10];
		boolean isLoop = false;

		for (int i = 0; i < lastIds.length; i++) {
			lastIds[i] = -1;
		}
		linkedListItem tmp = this;
		while (tmp.id != id) {
			for (int i = 0; i < lastIds.length; i++) {
				if (tmp.id == lastIds[i]) {
					if (isLoop)
						throw new IndexOutOfBoundsException();
					isLoop = true;
				}
			}
			for (int i = lastIds.length - 1; i > 0; i--) {
				lastIds[i] = lastIds[i - 1];
			}
			lastIds[0] = tmp.id;
			if (id > tmp.id) {
				if (id > tmp.id + 5000 && tmp.next10000 != null && !isLoop) {
					tmp = tmp.next10000;
				} else if (id > tmp.id + 50 && tmp.next100 != null && !isLoop) {
					tmp = tmp.next100;
				} else {
					tmp = tmp.next;
				}
			} else {

				if (id < tmp.id - 5000 && tmp.last10000 != null && !isLoop) {
					tmp = tmp.last10000;
				} else if (id < tmp.id - 50 && tmp.last100 != null && !isLoop) {
					tmp = tmp.last100;
				} else {
					tmp = tmp.last;
				}
			}
			isLoop = false;
			if (tmp == null) {
				Util.c_log("ERROR! tmp = null");
				return null;
			}
		}
		return tmp;
	}

	public int getId() {
		return id;
	}

	public int delete(int id) {
		return this.get(id).delete();
	}
}
