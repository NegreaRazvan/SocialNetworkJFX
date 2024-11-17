package map.domain;

public class Tuple <E>{
    private E e1;
    private E e2;

    public Tuple(E e1, E e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    /**
     * @return the first component of the tuple
     */
    public E first() {
        return e1;
    }

    /**
     * @return the second component of the tuple
     */
    public E second() {
        return e2;
    }
}
