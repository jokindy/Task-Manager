package taskmanager.managers;

import taskmanager.interfaces.HistoryManager;
import taskmanager.tasks.*;
import taskmanager.utilities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final DoublyLinkedList<Task> history;
    private final HashMap<Integer, Node<Task>> historyMap;

    public InMemoryHistoryManager() {
        history = new DoublyLinkedList<>();
        historyMap = new HashMap<>();
    }

    @Override
    public void addTaskToHistory(Task task) {
        int id = task.getId();
        removeTask(id);
        Node<Task> newNode = history.createNode(task);
        history.addLast(newNode);
        historyMap.put(id, newNode);
    }

    @Override
    public void removeTask(int id) {
        if (historyMap.containsKey(id)) {
            Node<Task> node = historyMap.get(id);
            history.removeNode(node);
            historyMap.remove(id);
        }
    }

    @Override
    public void clearHistory() {
        history.clearList();
    }

    @Override
    public List<Task> getHistory() {
        return history.getList();
    }

    static class DoublyLinkedList<T> {

        Node<T> head = null;
        Node<T> tail = null;

        public Node<T> createNode(T element) {
            Node<T> oldTail = tail;
            return new Node<>(null, element, oldTail);
        }

        public void addLast(Node<T> newNode) {
            if (head == null) {
                head = tail = newNode;
                head.setPrev(null);
            } else {
                tail.setNext(newNode);
                newNode.setPrev(tail);
                tail = newNode;
            }
            tail.setNext(null);
        }

        public void removeNode(Node<T> element) {
            Node<T> oldPrev = element.getPrev();
            Node<T> oldNext = element.getNext();
            if (oldNext != null) {
                oldNext.setPrev(oldPrev);
            } else {
                tail = oldPrev;
            }
            if (oldPrev != null) {
                oldPrev.setNext(oldNext);
            } else {
                head = oldNext;
            }
            if (head != null) {
                head.setPrev(null);
            }
            if (tail != null) {
                tail.setNext(null);
            }
        }

        public List<T> getList() {
            List<T> list = new ArrayList<>();
            if (head != null) {
                Node<T> current = head;
                while (current != null) {
                    list.add(current.getData());
                    current = current.getNext();
                }
            }
            return list;
        }

        public void clearList() {
            Node<T> element = tail;
            while (head != null) {
                removeNode(element);
                element = tail;
            }
        }
    }
}