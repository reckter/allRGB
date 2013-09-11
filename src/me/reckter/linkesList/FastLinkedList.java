package me.reckter.linkesList;

import me.reckter.Util;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 7/31/13
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class FastLinkedList {
    private linkedListItem firstItem;
    private linkedListItem lastItem;

    private int maxId;
    public FastLinkedList(){
        firstItem = null;
        lastItem = firstItem;
        maxId = -1;
    }

    public linkedListItem add(int value) {
        lastItem = new linkedListItem(++maxId, lastItem);
        lastItem.setValue(value);
        if(maxId == 0) {
            firstItem = lastItem;
        }
        return lastItem;
    }

    public void delete(int id){
        if(id > maxId / 2){
            if(lastItem.delete(id) == lastItem.getId()) {
                lastItem = lastItem.last;
            }
        } else {
            if(firstItem.delete(id) == lastItem.getId()){
                firstItem = firstItem.next;
            }
        }
    }

    public linkedListItem get(int id) throws IndexOutOfBoundsException{
        if(id > maxId / 2){
            return lastItem.get(id);
        } else {
            return firstItem.get(id);
        }
    }

    public void cleanUp() {
        int oldMaxId = maxId;
        linkedListItem tmp = firstItem;
        linkedListItem newFirst = new linkedListItem(0);
        newFirst.setValue(firstItem.getValue());
        linkedListItem newLast = newFirst;
        this.maxId = 0;

        while(tmp.next != null){
            tmp = tmp.next;
            newLast = new linkedListItem(++maxId, newLast);
            newLast.setValue(tmp.getValue());
        }
        firstItem = newFirst;
        lastItem = newLast;
        Util.c_log("cleaned up " + (oldMaxId - maxId) + ". so " + maxId + " are left.");
    }
    public int size() {
        return maxId;
    }
}
