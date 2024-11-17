package map.domain;

public class Entity<ID>  {


    private ID id;

    /**
     * returns the id of the entity
     * @return ID the id of the entity
     */
    public ID getId() {
        return id;
    }

    /**
     * sets the id of the entity
     * @param id the id of the entity
     */
    public void setId(ID id) {
        this.id = id;
    }
}