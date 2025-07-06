package BinarySearchTree;

import Excepciones.ExceptionIsEmpty;
import Excepciones.ItemDuplicated;
import Excepciones.ItemNotFound;

public interface BinarySearchTree<E> {
    void insert(E valor) throws ItemDuplicated;
    E search(E valor) throws ItemNotFound;
    void delete (E valor) throws ExceptionIsEmpty;
    boolean isEmpty();
}
